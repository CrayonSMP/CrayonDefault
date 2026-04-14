package com.crayonsmp.api.events;

import com.crayonsmp.api.waystone.IWaystone;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WaaystoneGUICloseEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;

    public WaaystoneGUICloseEvent(Player player) {
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return player;
    }
}
