package com.crayonsmp.paper.services;

import com.crayonsmp.paper.CrayonDefault;
import com.crayonsmp.paper.commands.TwitchCommand;
import com.crayonsmp.paper.utils.TwitchAPI;
import com.crayonsmp.paper.utils.config.ConfigUtil;
import com.crayonsmp.paper.utils.config.SConfig;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Objects;

public class TwitchService {
    public TwitchAPI twitchAPI;
    public SConfig twitchConfig;
    public HashMap<String, String> streamers = new HashMap<>();

    public void init(CrayonDefault instance) {
        twitchConfig = ConfigUtil.getConfig("twitch-config", instance);
        twitchAPI = new TwitchAPI();

        if (!twitchConfig.getFile().exists()) {
            twitchConfig.setDefault("twitch.client_id", "client_id");
            twitchConfig.setDefault("twitch.client_secret", "client_secret");

            HashMap<String, String> streamers = new HashMap<>();
            streamers.put("abaa4e3b-34af-4122-a4db-35df3be54dd5", "tamashiimon");

            twitchConfig.setDefault("streamers", streamers);

            twitchConfig.save();
        }

        ConfigurationSection twitchConfigSection = twitchConfig.getConfigurationSection("streamers");

        if (twitchConfigSection != null) {
            for (String key : twitchConfigSection.getKeys(false)) {
                streamers.put(key, twitchConfigSection.getString(key));
            }
        }

        Objects.requireNonNull(instance.getCommand("twitch")).setExecutor(new TwitchCommand());
    }
}
