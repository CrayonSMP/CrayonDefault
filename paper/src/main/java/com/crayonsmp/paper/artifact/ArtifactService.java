package com.crayonsmp.paper.artifact;

import com.crayonsmp.api.ICrayonDefault;
import com.crayonsmp.api.artifact.IArtifactService;
import com.crayonsmp.paper.command.ArtifactCommand;
import com.crayonsmp.paper.listener.ArtifactCrafterListener;
import com.crayonsmp.api.config.ConfigurationUtil;
import com.crayonsmp.api.config.Configuration;
import com.crayonsmp.api.util.ChatUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

@Setter
@Getter
public class ArtifactService implements IArtifactService {
    private List<ArtifactRecipe> recipeList;
    private List<ArtifactCrafterInventory> inventoryList;
    private Configuration config;

    @Override
    public void init(ICrayonDefault instance) {
        JavaPlugin plugin = (JavaPlugin) instance;
        recipeList = new ArrayList<>();
        inventoryList = new ArrayList<>();
        config = ConfigurationUtil.getConfig("artifact-config", plugin);
        initConfig();
        readRecipes();
        Objects.requireNonNull(plugin.getCommand("artifactcrafter")).setExecutor(new ArtifactCommand());
        plugin.getServer().getPluginManager().registerEvents(new ArtifactCrafterListener(this), plugin);
    }

    @Override
    public void initConfig() {
        if (!config.getFile().exists()) {
            config.setDefault("CrafterBlock", "default:palm_log");
            config.setDefault("InventoryTitle", "Inventory Crafter");
            List<ArtifactRecipe> recipes = new ArrayList<>();
            ArtifactRecipe recipe1 = new ArtifactRecipe("result1", new String[]{"artifact1", "artifact2", "artifact3"});
            recipes.add(recipe1);
            config.setDefault("recipes", recipes);
        }
    }

    @Override
    public void readRecipes() {
        Objects.requireNonNull(config.getList("recipes")).forEach(recipe -> {
            if (recipe instanceof ArtifactRecipe) {
                recipeList.add((ArtifactRecipe) recipe);
            }
        });
    }

    @Override
    public void openCrafterGUI(Player player) {
        ArtifactCrafterInventory CrafterInventory = ArtifactCrafterInventory.builder().build();
        CrafterInventory.setOwner(player);
        Inventory inventory = Bukkit.createInventory(player, 27, ChatUtil.miniMessage(config.getString("InventoryTitle")));
        CrafterInventory.setInventory(inventory);
        inventoryList.add(CrafterInventory);
        player.openInventory(inventory);
    }

    @Override
    public void removeCrafterInventory(Player player) {
        if (!isCrafterInventory(player.getOpenInventory().getTopInventory())) {
            return;
        }
        Inventory inventory = player.getOpenInventory().getTopInventory();
        ItemStack i1 = inventory.getItem(3);
        ItemStack i2 = inventory.getItem(11);
        ItemStack i3 = inventory.getItem(19);
        List<ItemStack> itemsToGive = new ArrayList<>();
        if (i1 != null) itemsToGive.add(i1);
        if (i2 != null) itemsToGive.add(i2);
        if (i3 != null) itemsToGive.add(i3);
        inventory.clear();
        inventoryList.removeIf(inventory1 -> inventory1.getInventory() == inventory);
        World world = player.getWorld();
        Location dropLocation = player.getLocation();
        for (ItemStack item : itemsToGive) {
            if (item == null || item.getAmount() <= 0) {
                continue;
            }
            HashMap<Integer, ItemStack> remainingItems = player.getInventory().addItem(item);
            if (remainingItems.isEmpty()) {
                continue;
            }
            for (ItemStack remainingItem : remainingItems.values()) {
                world.dropItemNaturally(dropLocation, remainingItem);
            }
        }
    }

    @Override
    public boolean isCrafterInventory(Inventory inventory) {
        for (ArtifactCrafterInventory artifactCrafterInventory : inventoryList) {
            if (artifactCrafterInventory.getInventory() == inventory) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isArtifactRecipe(String[] ingredients) {
        for (ArtifactRecipe recipe : recipeList) {
            if (Arrays.equals(recipe.getIngredients(), ingredients)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ArtifactRecipe getArtifactRecipe(String[] ingredients) {
        for (ArtifactRecipe recipe : recipeList) {
            if (Arrays.equals(recipe.getIngredients(), ingredients)) {
                return recipe;
            }
        }
        return null;
    }
}
