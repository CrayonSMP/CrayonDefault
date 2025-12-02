package com.crayonsmp.paper.listeners;

import com.crayonsmp.paper.CrayonDefault;
import net.momirealms.craftengine.bukkit.api.event.FurniturePlaceEvent;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;

public class WaystoneListener implements Listener {
    @EventHandler
    public void onWaystonePlace(FurniturePlaceEvent event){
        if (event.furniture().id().equals(Key.of(Objects.requireNonNull(CrayonDefault.waystoneService.config.getString("waystone-id"))))){
            //TODO:Logic
        }
    }
}
