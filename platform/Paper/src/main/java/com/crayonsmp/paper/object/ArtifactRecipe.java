package com.crayonsmp.paper.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
public class ArtifactRecipe implements ConfigurationSerializable {
    //CraftEngine IDS
    String resoult;

    String[] ingredients;

    @Override
    public @NotNull Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("resoult", resoult);
        map.put("ingredients", ingredients);
        return map;
    }
}
