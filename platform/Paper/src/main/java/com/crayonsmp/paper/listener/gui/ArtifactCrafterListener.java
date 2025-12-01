package com.crayonsmp.paper.listener.gui;

import com.crayonsmp.paper.Main;
import com.crayonsmp.paper.object.ArtifactRecipe;
import com.crayonsmp.paper.services.ArtifactService;
import net.momirealms.craftengine.bukkit.api.CraftEngineItems;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ArtifactCrafterListener implements Listener {
    ArtifactService artifactService = Main.artifactService;

    private static final List<Integer> INGREDIENT_SLOTS = Arrays.asList(2, 10, 18);
    private static final int RESULT_SLOT = 16;

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

            if (!INGREDIENT_SLOTS.contains(slot) && slot != RESULT_SLOT) {
                event.setCancelled(true);
                return;
            }

            if (slot == RESULT_SLOT) {
                if (!event.isLeftClick() || !event.isRightClick() || event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
                    event.setCancelled(true);
                    return;
                }

                if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {

                    Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
                        removeIngredients(topInventory);
                        updateInventory(topInventory);
                    }, 1L);
                }
            }

            if (INGREDIENT_SLOTS.contains(slot)) {

                Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), () -> {
                    updateInventory(topInventory);
                }, 1L);

            }
        }
    }

    public void updateInventory(Inventory inventory){
        ItemStack i1 = inventory.getItem(2);
        ItemStack i2 = inventory.getItem(10);
        ItemStack i3 = inventory.getItem(18);

        if (i1 == null || i2 == null || i3 == null) return;
        String i1S = getStringFromItem(i1, CraftEngineItems.getCustomItemId(i1));
        String i2S = getStringFromItem(i2, CraftEngineItems.getCustomItemId(i2));
        String i3S = getStringFromItem(i3, CraftEngineItems.getCustomItemId(i3));

        String[] ingredients = {i1S, i2S, i3S};

        Bukkit.getLogger().info("Checking if recipe exists");

        Bukkit.getLogger().info(Arrays.toString(ingredients));

        if (artifactService.isArtifactRecipe(ingredients)){
            ArtifactRecipe recipe = artifactService.getArtifactRecipe(ingredients);
            ItemStack resoult;
            Bukkit.getLogger().info(recipe.getResoult());
            if (CraftEngineItems.byId(Key.from(recipe.getResoult())) != null){
                resoult = CraftEngineItems.byId(Key.from(recipe.getResoult())).buildItemStack();
            } else {
                resoult = new ItemStack(Objects.requireNonNull(Material.matchMaterial(recipe.getResoult())));
            }

            Bukkit.getLogger().info(resoult.getType().toString());

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
