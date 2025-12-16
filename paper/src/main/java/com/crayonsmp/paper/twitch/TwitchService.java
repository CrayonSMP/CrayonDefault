package com.crayonsmp.paper.twitch;

import com.crayonsmp.api.ICrayonDefault;
import com.crayonsmp.api.events.StreamerNowLiveEvent;
import com.crayonsmp.api.twitch.ITwitchService;
import com.crayonsmp.api.twitch.ITwitchServiceProvider;
import com.crayonsmp.paper.command.TwitchCommand;
import com.crayonsmp.api.config.ConfigurationUtil;
import com.crayonsmp.api.config.Configuration;
import com.google.common.util.concurrent.ListenableFutureTask;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class TwitchService implements ITwitchService {
    private Configuration twitchConfig;
    private ITwitchServiceProvider twitchServiceProvider;
    private Map<String, String> streamers;

    @Override
    public void init(ICrayonDefault instance) {
        JavaPlugin plugin = (JavaPlugin) instance;
        twitchConfig = ConfigurationUtil.getConfig("twitch-config", plugin);
        twitchServiceProvider = new TwitchServiceProvider(instance);
        streamers = new HashMap<>();
        if (!twitchConfig.getFile().exists()) {
            twitchConfig.setDefault("twitch.client_id", "client_id");
            twitchConfig.setDefault("twitch.client_secret", "client_secret");
            twitchConfig.setDefault("streamers", streamers);
            twitchConfig.save();
        }
        ConfigurationSection twitchConfigSection = twitchConfig.getConfigurationSection("streamers");
        if (twitchConfigSection != null) {
            for (String key : twitchConfigSection.getKeys(false)) {
                streamers.put(key, twitchConfigSection.getString(key));
            }
        }
        Objects.requireNonNull(plugin.getCommand("twitch")).setExecutor(new TwitchCommand());
        startScheduler(instance);
    }

    private void startScheduler(ICrayonDefault instance) {
        Map<String, String> liveStreamers = new HashMap<>();
        Bukkit.getScheduler().runTaskTimerAsynchronously((JavaPlugin) instance, () -> {
            streamers.forEach((key, value) -> {
                if (Bukkit.getPlayer(key) == null) {
                    return;
                }
                if (!twitchServiceProvider.isStreamerExists(value)) {
                    streamers.remove(key);
                    return;
                }
                if (!twitchServiceProvider.getStreamer(value).isLive()) {
                    if (liveStreamers.containsKey(key)) {
                        liveStreamers.remove(key);
                    }
                    return;
                }

                if (liveStreamers.containsKey(key)) {
                    return;
                }
                liveStreamers.put(key, value);
                StreamerNowLiveEvent event = new StreamerNowLiveEvent(Bukkit.getPlayer(key), twitchServiceProvider.getStreamer(value));
                Bukkit.getPluginManager().callEvent(event);
            });
        }, 0L, 20 * 20L);
    }

    @Override
    public Configuration getTwitchConfig() {
        return twitchConfig;
    }

    @Override
    public ITwitchServiceProvider getTwitchServiceProvider() {
        return twitchServiceProvider;
    }

    @Override
    public Map<String, String> getStreamers() {
        return streamers;
    }
}
