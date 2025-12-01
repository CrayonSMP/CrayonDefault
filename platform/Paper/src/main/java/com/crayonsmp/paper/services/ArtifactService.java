package com.crayonsmp.paper.services;

import com.crayonsmp.paper.Main;
import com.crayonsmp.paper.commands.ArtifactCommand;
import com.crayonsmp.paper.listener.gui.ArtifactCrafterListener;
import com.crayonsmp.paper.object.ArtifactRecipe;
import com.crayonsmp.paper.object.gui.ArtifactCrafterInventory;
import com.crayonsmp.paper.utils.config.ConfigUtil;
import com.crayonsmp.paper.utils.config.SConfig;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

@Setter
@Getter
public class ArtifactService {
    public List<ArtifactRecipe> recipeList = new ArrayList<>();
    public List<ArtifactCrafterInventory> inventorieList = new ArrayList<>();
    public SConfig config;

    public void init(Main instance) {
        config = ConfigUtil.getConfig("artefact-config", instance);

        initConfig();
        readRecipes();

        Objects.requireNonNull(instance.getCommand("artifactcrafter")).setExecutor(new ArtifactCommand());
        instance.getServer().getPluginManager().registerEvents(new ArtifactCrafterListener(), instance);
    }

    public void initConfig(){
        if (!config.getFile().exists()) {
            config.setDefault("CrafterBlock", "default:palm_log");
            config.setDefault("InventoryTitle", "Inventory Crafter");

            List<ArtifactRecipe> recipes = new ArrayList<>();
            ArtifactRecipe recipe1 = new ArtifactRecipe("resloult1", new String[]{"artifact1", "artifact2", "artifact3"});
            recipes.add(recipe1);
            config.setDefault("recipes", recipes);
        }
    }

    public void readRecipes() {
        Objects.requireNonNull(config.getList("recipes")).forEach(recipe -> {

            if (recipe instanceof ArtifactRecipe) {
                recipeList.add((ArtifactRecipe) recipe);
            }
        });
    }

    public void openCrafterGUI(Player player) {
        ArtifactCrafterInventory CrafterInventory = ArtifactCrafterInventory.builder().build();

        CrafterInventory.setOwner(player);

        Inventory inventory = Bukkit.createInventory(player, 27, config.getString("InventoryTitle"));
        CrafterInventory.setInventory(inventory);

        inventorieList.add(CrafterInventory);

        player.openInventory(inventory);
    }

    public boolean isInventoryCrafterInventory(Inventory inventory) {
        for (ArtifactCrafterInventory artifactCrafterInventory : inventorieList) {
            if (artifactCrafterInventory.getInventory() == inventory) {
                return true;
            }
        }
        return false;
    }

    public boolean isArtifactRecipe(String[] ingredients) {

        for (ArtifactRecipe recipe : recipeList) {
            if (Arrays.equals(recipe.getIngredients(), ingredients)) {
                return true;
            }
        }
        return false;
    }

    public ArtifactRecipe getArtifactRecipe(String[] ingredients) {

        for (ArtifactRecipe recipe : recipeList) {
            if (Arrays.equals(recipe.getIngredients(), ingredients)) {
                return recipe;
            }
        }
        return null;
    }
}
