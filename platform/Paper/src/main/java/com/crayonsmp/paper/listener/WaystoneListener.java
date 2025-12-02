package com.crayonsmp.paper.listener;

import com.crayonsmp.paper.Main;
import com.crayonsmp.paper.services.WaystoneService;
import net.momirealms.craftengine.bukkit.api.event.CustomBlockPlaceEvent;
import net.momirealms.craftengine.bukkit.api.event.FurniturePlaceEvent;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;

public class WaystoneListener implements Listener {
    @EventHandler
    public void onWaystonePlace(FurniturePlaceEvent event){
        if (event.furniture().id().equals(Key.of(Objects.requireNonNull(Main.waystoneService.config.getString("waystone-id"))))){
            //TODO:Logic
        }
    }
}
