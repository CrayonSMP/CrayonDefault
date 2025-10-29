package com.crayonsmp.paper.services;

import com.crayonsmp.paper.Main;
import com.crayonsmp.paper.object.ArtifactRecipe;
import com.crayonsmp.paper.object.gui.ArtifactCrafterInventory;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class ArtifactService {
    public List<ArtifactRecipe> recipes = new ArrayList<>();
    public List<ArtifactCrafterInventory> inventories = new ArrayList<>();

    public void readRecipes() {
        Objects.requireNonNull(Main.artefactConfig.getList("recipes")).forEach(recipe -> {
            if (recipe instanceof ArtifactRecipe) {
                recipes.add((ArtifactRecipe) recipe);
            }
        });
    }

    public void openCrafterGUI(Player player) {
        ArtifactCrafterInventory CrafterInventory = ArtifactCrafterInventory.builder().build();

        CrafterInventory.setOwner(player);

        Inventory inventory = Bukkit.createInventory(player, 27, "InventoryCrafter");
        CrafterInventory.setInventory(inventory);

        inventories.add(CrafterInventory);
    }

    public boolean isInventoryCrafterInventory(Inventory inventory) {
        for (ArtifactCrafterInventory artifactCrafterInventory : inventories) {
            if (artifactCrafterInventory.getInventory() == inventory) {
                return true;
            }
        }
        return false;
    }

    //Als CraftEngine ids
    public boolean isArtifactRecipe(String[] ingredients) {
        for (ArtifactRecipe recipe : recipes) {
            if (recipe.getIngredients() == ingredients) {
                return true;
            }
        }
        return false;
    }

    public ArtifactRecipe getArtifactRecipe(String[] ingredients) {
        for (ArtifactRecipe recipe : recipes) {
            if (recipe.getIngredients() == ingredients) {
                return recipe;
            }
        }
        return null;
    }
}
