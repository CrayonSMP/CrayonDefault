package com.crayonsmp.api.events;

import com.crayonsmp.api.twitch.IStreamer;
import com.crayonsmp.api.waystone.IWaystone;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WaystoneTeleportEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    public final boolean isFromWaystone;
    private final Location fromLocation;
    private final IWaystone fromWaystone;
    private final IWaystone toWaystone;

    public WaystoneTeleportEvent(Player player, boolean isFromWaystone, Location fromLocation, IWaystone fromWaystone, IWaystone toWaystone) {
        this.player = player;
        this.isFromWaystone = isFromWaystone;
        this.fromLocation = fromLocation;
        this.fromWaystone = fromWaystone;
        this.toWaystone = toWaystone;
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

    public Location getFromLocation() {
        return fromLocation;
    }

    public IWaystone getFromWaystone() {
        return fromWaystone;
    }

    public IWaystone getToWaystone() {
        return toWaystone;
    }
}
