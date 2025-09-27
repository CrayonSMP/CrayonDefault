package com.crayonsmp.paper;

import com.crayonsmp.paper.commands.TwitchCommand;
import com.crayonsmp.paper.utils.TwitchAPI;
import com.crayonsmp.paper.utils.config.ConfigUtil;
import com.crayonsmp.paper.utils.config.SConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;

public final class Main extends JavaPlugin {

    public static SConfig twitchcConfig;

    public static TwitchAPI twitchAPI;

    public static HashMap<String, String> streamers = new HashMap<>();

    @Override
    public void onEnable() {
        twitchcConfig = ConfigUtil.getConfig("twitch-config", this);

        if (!twitchcConfig.getFile().exists()) {
            twitchcConfig.setDefault("twitch.client_id", "client_id");
            twitchcConfig.setDefault("twitch.client_secret", "client_secret");

            // First String Player UUID second string Twitch Username.
            HashMap<String, String> streamers = new HashMap<>();
            streamers.put("abaa4e3b-34af-4122-a4db-35df3be54dd5", "tamashiimon");

            twitchcConfig.setDefault("streamers", streamers);

            twitchcConfig.save();
        }

        ConfigurationSection section = twitchcConfig.getConfigurationSection("streamers");

        if (section != null) {
            for (String key : section.getKeys(false)) {
                streamers.put(key, section.getString(key));
            }
        }

        twitchAPI = new TwitchAPI();

        Objects.requireNonNull(getCommand("twitch")).setExecutor(new TwitchCommand());

        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
