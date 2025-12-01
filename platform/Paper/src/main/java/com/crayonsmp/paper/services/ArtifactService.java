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
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class ArtifactService {
    public List<ArtifactRecipe> recipes = new ArrayList<>();
    public List<ArtifactCrafterInventory> inventories = new ArrayList<>();
    public static SConfig config;

    public void init(Main instance) {
        config = ConfigUtil.getConfig("artefact-config", instance);

        initConfig();
        readRecipes();

        Objects.requireNonNull(instance.getCommand("artifactcrafter")).setExecutor(new ArtifactCommand());
        instance.getServer().getPluginManager().registerEvents(new ArtifactCrafterListener(), instance);
    }

    public void initConfig(){
        if (!config.getFile().exists()) {
            List<ArtifactRecipe> recipes = new ArrayList<>();

            ArtifactRecipe recipe1 = new ArtifactRecipe("resloult1", new String[]{"artifact1", "artifact2", "artifact3"});

            recipes.add(recipe1);
            config.setDefault("recipes", recipes);
        }
    }

    public void readRecipes() {
        Objects.requireNonNull(config.getList("recipes")).forEach(recipe -> {
            Bukkit.getLogger().info("Reading recipe");
            Bukkit.getLogger().info(recipe.toString());
            if (recipe instanceof ArtifactRecipe) {
                recipes.add((ArtifactRecipe) recipe);
                Bukkit.getLogger().info(recipe.toString());
            }
        });
    }

    public void openCrafterGUI(Player player) {
        ArtifactCrafterInventory CrafterInventory = ArtifactCrafterInventory.builder().build();

        CrafterInventory.setOwner(player);

        Inventory inventory = Bukkit.createInventory(player, 27, "InventoryCrafter");
        CrafterInventory.setInventory(inventory);

        inventories.add(CrafterInventory);

        player.openInventory(inventory);
    }

    public boolean isInventoryCrafterInventory(Inventory inventory) {
        for (ArtifactCrafterInventory artifactCrafterInventory : inventories) {
            if (artifactCrafterInventory.getInventory() == inventory) {
                return true;
            }
        }
        return false;
    }

    public boolean isArtifactRecipe(String[] ingredients) {
        Bukkit.getLogger().info("Checking Input: " + Arrays.toString(ingredients));

        for (ArtifactRecipe recipe : recipes) {
            if (Arrays.equals(recipe.getIngredients(), ingredients)) {
                Bukkit.getLogger().info("Match found: " + recipe);
                return true;
            }
        }
        return false;
    }

    public ArtifactRecipe getArtifactRecipe(String[] ingredients) {
        Bukkit.getLogger().info("Getting Recipe for: " + Arrays.toString(ingredients));

        for (ArtifactRecipe recipe : recipes) {
            if (Arrays.equals(recipe.getIngredients(), ingredients)) {
                return recipe;
            }
        }
        return null;
    }
}
