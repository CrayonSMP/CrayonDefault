package com.crayonsmp.paper.artifact;

import com.crayonsmp.api.artifact.IArtifactRecipe;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.List;
import java.util.Map;

@SerializableAs("ArtifactRecipe")
public class ArtifactRecipe implements IArtifactRecipe {
    private final String result;
    private final String[] ingredients;

    public ArtifactRecipe(String result, String[] ingredients) {
        this.result = result;
        this.ingredients = ingredients;
    }

    public static ArtifactRecipe deserialize(Map<String, Object> args) {
        String result = (String) args.get("result");
        List<String> ingList = (List<String>) args.get("ingredients");
        String[] ingredients = ingList.toArray(new String[0]);
        return new ArtifactRecipe(result, ingredients);
    }

    @Override
    public String getResult() {
        return result;
    }

    @Override
    public String[] getIngredients() {
        return ingredients;
    }
}