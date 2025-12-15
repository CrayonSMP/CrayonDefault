package com.crayonsmp.api.waystone;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IWaystone extends ConfigurationSerializable {
    String uid();
    String name();
    Location location();
    List<String> players();
    String creator();
    void setPlayers(List<String> players);

    @Override
    @NotNull
    default Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", uid());
        map.put("name", name());
        map.put("location", location());
        map.put("players", players());
        map.put("creator", creator());
        return map;
    }
}
