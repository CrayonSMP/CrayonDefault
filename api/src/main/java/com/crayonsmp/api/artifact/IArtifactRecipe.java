package com.crayonsmp.api.artifact;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public interface IArtifactRecipe extends ConfigurationSerializable {
    String getResult();
    String[] getIngredients();

    @Override
    @NotNull
    default Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("result", getResult());
        map.put("ingredients", getIngredients());
        return map;
    }
}
