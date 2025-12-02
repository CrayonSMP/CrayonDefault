package com.crayonsmp.paper.listeners.gui;

import com.crayonsmp.paper.CrayonDefault;
import com.crayonsmp.paper.objects.ArtifactRecipe;
import com.crayonsmp.paper.services.ArtifactService;
import net.momirealms.craftengine.bukkit.api.CraftEngineItems;
import net.momirealms.craftengine.bukkit.api.event.CustomBlockInteractEvent;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ArtifactCrafterListener implements Listener {
    ArtifactService artifactService = CrayonDefault.artifactService;

    private static final List<Integer> Allowed_Slots = Arrays.asList(2, 10, 16, 18);
    private static final int RESULT_SLOT = 16;

    @EventHandler
    public void onCustomBlockInteract(CustomBlockInteractEvent event){
        if (event.customBlock().id().equals(Key.of(Objects.requireNonNull(CrayonDefault.artifactService.config.getString("CrafterBlock"))))) {
            if (event.player().isSneaking()) return;
            Player player = event.player();
            CrayonDefault.artifactService.openCrafterGUI(player);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Inventory topInventory = event.getView().getTopInventory();

        if (event.getClickedInventory() == null) {
            return;
        }

        if (artifactService.isInventoryCrafterInventory(topInventory)) {
            if (!event.getClickedInventory().equals(topInventory)) {
                if (event.isShiftClick()) {
                    event.setCancelled(true);
                }
                return;
            }

            int slot = event.getSlot();

            if (!Allowed_Slots.contains(slot) && slot != RESULT_SLOT) {
                event.setCancelled(true);
                return;
            }

            if (slot == RESULT_SLOT) {
                if (!event.isLeftClick() || !event.isRightClick() && event.getCursor().getType() != Material.AIR) {
                    event.setCancelled(true);
                    return;
                }

                if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {

                    ItemStack itemToEnchant = event.getCurrentItem();

                    List<Enchantment> applicableEnchants = new ArrayList<>();

                    for (Enchantment enchantment : Enchantment.values()) {
                        if (enchantment.equals(Enchantment.MENDING)) {
                            continue;
                        }

                        if (enchantment.canEnchantItem(itemToEnchant)) {
                            applicableEnchants.add(enchantment);
                        }
                    }

                    if (!applicableEnchants.isEmpty()) {
                        Random random = new Random();
                        Enchantment chosenEnchant = applicableEnchants.get(random.nextInt(applicableEnchants.size()));

                        int maxLevel = chosenEnchant.getMaxLevel();
                        int newLevel = maxLevel + 1;

                        event.getCurrentItem().addUnsafeEnchantment(chosenEnchant, newLevel);
                    }

                    Bukkit.getScheduler().runTaskLater(CrayonDefault.getPlugin(CrayonDefault.class), () -> {
                        removeIngredients(topInventory);
                        updateInventory(topInventory);
                    }, 1L);
                }
            }

            if (Allowed_Slots.contains(slot)) {

                Bukkit.getScheduler().runTaskLater(CrayonDefault.getPlugin(CrayonDefault.class), () -> {
                    updateInventory(topInventory);
                }, 1L);

            }
        }
    }

    public void updateInventory(Inventory inventory){
        ItemStack i1 = inventory.getItem(2);
        ItemStack i2 = inventory.getItem(10);
        ItemStack i3 = inventory.getItem(18);

        String i1S = getStringFromItem(i1, CraftEngineItems.getCustomItemId(i1));
        String i2S = getStringFromItem(i2, CraftEngineItems.getCustomItemId(i2));
        String i3S = getStringFromItem(i3, CraftEngineItems.getCustomItemId(i3));

        String[] ingredients = {i1S, i2S, i3S};

        if (artifactService.isArtifactRecipe(ingredients)){
            ArtifactRecipe recipe = artifactService.getArtifactRecipe(ingredients);
            ItemStack resoult;
            if (CraftEngineItems.byId(Key.from(recipe.getResoult())) != null){
                resoult = CraftEngineItems.byId(Key.from(recipe.getResoult())).buildItemStack();
            } else {
                resoult = new ItemStack(Objects.requireNonNull(Material.matchMaterial(recipe.getResoult())));
            }

            inventory.setItem(16, resoult);
        }
        else inventory.setItem(16, null);
    }

    private String getStringFromItem(ItemStack i1, Key customItemId) {
        String i1S;
        if (i1 != null) {
            String customId = String.valueOf(CraftEngineItems.getCustomItemId(i1));
            if (customItemId != null) {
                i1S = customId;
            } else {
                i1S = i1.getType().toString();
            }
        } else {
            i1S = "AIR";
        }
        return i1S;
    }

    public void removeIngredients(Inventory inventory){
        inventory.setItem(2, null);
        inventory.setItem(10, null);
        inventory.setItem(18, null);
    }
}
