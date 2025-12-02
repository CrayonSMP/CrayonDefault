package com.crayonsmp.paper.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class Waystone implements ConfigurationSerializable {
    String name;
    Location location;
    List<String> players;
    String creator;

    public static Waystone deserialize(Map<String, Object> args) {
        String name = (String) args.get("name");
        Location location = (Location) args.get("location");
        List<String> players = (List<String>) args.get("players");
        String creator = (String) args.get("creator");

        return new Waystone(name, location, players, creator);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("location", location);
        map.put("players", players);
        map.put("creator", creator);
        return map;
    }
}
