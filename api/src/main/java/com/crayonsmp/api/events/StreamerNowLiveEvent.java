package com.crayonsmp.api.events;

import com.crayonsmp.api.twitch.IStreamer;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

@Getter
public class StreamerNowLiveEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final IStreamer streamer;

    public StreamerNowLiveEvent(Player player, IStreamer streamer) {
        this.player = player;
        this.streamer = streamer;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
