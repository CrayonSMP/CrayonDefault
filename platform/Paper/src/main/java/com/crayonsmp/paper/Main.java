package com.crayonsmp.paper;

import com.crayonsmp.paper.commands.TwitchCommand;
import com.crayonsmp.paper.listener.ItemListener;
import com.crayonsmp.paper.utils.TwitchAPI;
import com.crayonsmp.paper.utils.config.ConfigUtil;
import com.crayonsmp.paper.utils.config.SConfig;
import net.momirealms.craftengine.bukkit.api.CraftEngineItems;
import net.momirealms.craftengine.bukkit.item.BukkitCustomItem;
import net.momirealms.craftengine.core.item.CustomItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import com.crayonsmp.paper.utils.tasks.reload;
import com.crayonsmp.paper.utils.tasks.restart;
import it.sauronsoftware.cron4j.Scheduler;
import net.momirealms.craftengine.core.plugin.CraftEngine;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;

public final class Main extends JavaPlugin {
    public static Main instance;
  
    public static SConfig twitchcConfig;

    public static TwitchAPI twitchAPI;

    public static HashMap<String, String> streamers = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
      
        twitchcConfig = ConfigUtil.getConfig("twitch-config", this);

        getServer().getPluginManager().registerEvents(new ItemListener(), this);

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


        scheduleDailyTasks();
    }

    @Override
    public void onDisable() {

    }

    private void scheduleDailyTasks() {
        Scheduler reload = new Scheduler();
        reload.schedule("0 7,13,17,20,23 * * *", new reload());
        reload.start();

        Scheduler restart = new Scheduler();
        restart.schedule("0 3 * * *", new restart());
        restart.start();
    }

    public static Main getInstance() {
        return instance;
    }
}
