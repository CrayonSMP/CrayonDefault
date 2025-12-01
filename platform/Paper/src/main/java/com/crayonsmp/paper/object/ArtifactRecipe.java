package com.crayonsmp.paper.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SerializableAs("ArtifactRecipe")
@Setter
@Getter
@AllArgsConstructor
public class ArtifactRecipe implements ConfigurationSerializable {
    String resoult;
    String[] ingredients;

    public static ArtifactRecipe deserialize(Map<String, Object> args) {
        String resoult = (String) args.get("resoult");

        List<String> ingList = (List<String>) args.get("ingredients");
        String[] ingredients = ingList.toArray(new String[0]);

        return new ArtifactRecipe(resoult, ingredients);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("resoult", resoult);
        map.put("ingredients", ingredients);
        return map;
    }
}