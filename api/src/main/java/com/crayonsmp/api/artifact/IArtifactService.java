package com.crayonsmp.api.artifact;

import com.crayonsmp.api.ICrayonDefault;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface IArtifactService {
    void init(ICrayonDefault instance);
    void initConfig();
    void readRecipes();
    void openCrafterGUI(Player player);
    void removeCrafterInventory(Player player);
    boolean isCrafterInventory(Inventory inventory);
    boolean isArtifactRecipe(String[] ingredients);
    IArtifactRecipe getArtifactRecipe(String[] ingredients);
}
