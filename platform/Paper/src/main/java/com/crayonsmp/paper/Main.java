package com.crayonsmp.paper;

import com.crayonsmp.paper.commands.TwitchCommand;
import com.crayonsmp.paper.listener.ItemListener;
import com.crayonsmp.paper.object.ArtifactRecipe;
import com.crayonsmp.paper.services.ArtifactService;
import com.crayonsmp.paper.utils.TwitchAPI;
import com.crayonsmp.paper.utils.config.ConfigUtil;
import com.crayonsmp.paper.utils.config.SConfig;
import org.bukkit.configuration.ConfigurationSection;
import com.crayonsmp.paper.utils.tasks.reload;
import com.crayonsmp.paper.utils.tasks.restart;
import it.sauronsoftware.cron4j.Scheduler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public final class Main extends JavaPlugin {
    public static Main instance;
  
    public static SConfig twitchConfig;
    public static SConfig artefactConfig;

    public static TwitchAPI twitchAPI;

    public static ArtifactService artifactService;

    public static HashMap<String, String> streamers = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
      
        twitchConfig = ConfigUtil.getConfig("twitch-config", this);
        artefactConfig = ConfigUtil.getConfig("artefact-config", this);

        getServer().getPluginManager().registerEvents(new ItemListener(), this);

        if (!twitchConfig.getFile().exists()) {
            twitchConfig.setDefault("twitch.client_id", "client_id");
            twitchConfig.setDefault("twitch.client_secret", "client_secret");

            // First String Player UUID second string Twitch Username.
            HashMap<String, String> streamers = new HashMap<>();
            streamers.put("abaa4e3b-34af-4122-a4db-35df3be54dd5", "tamashiimon");

            twitchConfig.setDefault("streamers", streamers);

            twitchConfig.save();
        }

        if (!artefactConfig.getFile().exists()) {
            List<ArtifactRecipe> recipes = new ArrayList<>();

            ArtifactRecipe recipe1 = new ArtifactRecipe("resloult1", new String[]{"artifact1", "artifact2", "artifact3"});

            recipes.add(recipe1);
            artefactConfig.setDefault("recipes", recipes);
        }

        ConfigurationSection twitchConfigSection = twitchConfig.getConfigurationSection("streamers");

        if (twitchConfigSection != null) {
            for (String key : twitchConfigSection.getKeys(false)) {
                streamers.put(key, twitchConfigSection.getString(key));
            }
        }

        twitchAPI = new TwitchAPI();
        artifactService = new ArtifactService();
        artifactService.readRecipes();

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
