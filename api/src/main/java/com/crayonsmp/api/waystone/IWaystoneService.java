package com.crayonsmp.api.waystone;

import com.crayonsmp.api.ICrayonDefault;
import com.crayonsmp.api.config.Configuration;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public interface IWaystoneService {
    void init(ICrayonDefault instance);
    void initConfig();
    void readWaystones();
    void saveWaystones();
    void addWaystone(String uid, String name, Location location, List<String> players, String creator);
    void removeWaystone(String uid);
    IWaystone getWaystone(Location location);
    IWaystone getWaystone(String uid);
    void addPlayerToWaystone(String waystoneUid, String playerUUID);
    void openWaystoneGUI(Player player, String currentWaystoneUID);
    void openWaystoneGUI(Player player, Location currentLocation, String TitleString);

    Configuration getConfig();
    List<IWaystone> getWaystones();
}
