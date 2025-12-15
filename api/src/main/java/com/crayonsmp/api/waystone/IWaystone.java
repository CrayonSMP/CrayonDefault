package com.crayonsmp.api.waystone;

import org.bukkit.Location;

import java.util.List;

public interface IWaystone {
    String name();
    Location location();
    List<String> players();
    String creator();
}
