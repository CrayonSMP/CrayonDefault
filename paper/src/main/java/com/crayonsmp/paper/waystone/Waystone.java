package com.crayonsmp.paper.waystone;

import com.crayonsmp.api.waystone.IWaystone;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SerializableAs("Waystone")
public class Waystone implements IWaystone {
    private final String uid;
    private final String name;
    private final Location location;
    private List<String> players;
    private final String creator;

    public Waystone(String uid, String name, Location location, List<String> players, String creator) {
        this.uid = uid;
        this.name = name;
        this.location = location;
        this.players = players;
        this.creator = creator;
    }

    public static IWaystone deserialize(Map<String, Object> args) {
        String uid = (String) args.get("uid");
        String name = (String) args.get("name");
        Location location = (Location) args.get("location");
        List<String> players = (List<String>) args.get("players");
        String creator = (String) args.get("creator");
        return new Waystone(uid, name, location, players, creator);
    }

    @Override
    public String uid() {
        return this.uid;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Location location() {
        return this.location;
    }

    @Override
    public List<String> players() {
        return this.players;
    }

    @Override
    public String creator() {
        return this.creator;
    }

    @Override
    public void setPlayers(List<String> players) {
        this.players = players;
    }
}
