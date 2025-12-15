package com.crayonsmp.paper.artifact;

import com.crayonsmp.paper.CrayonDefault;
import com.crayonsmp.paper.command.ArtifactCommand;
import com.crayonsmp.paper.listener.ArtifactCrafterListener;
import com.crayonsmp.paper.util.config.ConfigurationUtil;
import com.crayonsmp.paper.util.config.Configuration;
import com.crayonsmp.api.util.ChatUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Setter
@Getter
public class ArtifactService {
    public List<ArtifactRecipe> recipeList = new ArrayList<>();
    public List<ArtifactCrafterInventory> inventorieList = new ArrayList<>();
    public Configuration config;

    public void init(CrayonDefault instance) {
        config = ConfigurationUtil.getConfig("artefact-config", instance);

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
            ArtifactRecipe recipe1 = new ArtifactRecipe("resoult1", new String[]{"artifact1", "artifact2", "artifact3"});
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

        Inventory inventory = Bukkit.createInventory(player, 27, ChatUtil.miniMessage(config.getString("InventoryTitle")));
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

    public void removeCrafterInventory(Player player) {

        if (isInventoryCrafterInventory(player.getOpenInventory().getTopInventory())) {
            Inventory inventory = player.getOpenInventory().getTopInventory();

            ItemStack i1 = inventory.getItem(2);
            ItemStack i2 = inventory.getItem(10);
            ItemStack i3 = inventory.getItem(18);

            List<ItemStack> itemsToGive = new ArrayList<>();
            if (i1 != null) itemsToGive.add(i1);
            if (i2 != null) itemsToGive.add(i2);
            if (i3 != null) itemsToGive.add(i3);

            inventory.clear();
            inventorieList.removeIf(inventory1 -> inventory1.getInventory() == inventory);

            World world = player.getWorld();
            Location dropLocation = player.getLocation();

            for (ItemStack item : itemsToGive) {
                if (item == null || item.getAmount() <= 0) continue;

                HashMap<Integer, ItemStack> remainingItems = player.getInventory().addItem(item);

                if (!remainingItems.isEmpty()) {
                    for (ItemStack remainingItem : remainingItems.values()) {
                        world.dropItemNaturally(dropLocation, remainingItem);
                    }
                }
            }
        }
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
