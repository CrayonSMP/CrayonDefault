package com.crayonsmp.paper.listener.gui;

import com.crayonsmp.paper.Main;
import com.crayonsmp.paper.object.ArtifactRecipe;
import com.crayonsmp.paper.services.ArtifactService;
import net.momirealms.craftengine.bukkit.api.CraftEngineItems;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
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
                if (event.isShiftClick() || event.getClick().isKeyboardClick() || event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
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

        String i1S = "";
        String i2S = "";
        String i3S = "";

        if (CraftEngineItems.getCustomItemId(i1) != null) {
            CraftEngineItems.getCustomItemId(i1);
        } else i1S = i1.getType().toString();
        if (CraftEngineItems.getCustomItemId(i2) != null) {
            CraftEngineItems.getCustomItemId(i2);
        } else i2S = i1.getType().toString();
        if (CraftEngineItems.getCustomItemId(i3) != null) {
            CraftEngineItems.getCustomItemId(i3);
        } else i3S = i1.getType().toString();

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

    public void removeIngredients(Inventory inventory){
        inventory.setItem(2, null);
        inventory.setItem(10, null);
        inventory.setItem(18, null);
    }
}
