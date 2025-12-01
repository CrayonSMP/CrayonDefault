package com.crayonsmp.paper;

import com.crayonsmp.paper.listener.ItemListener;
import com.crayonsmp.paper.object.ArtifactRecipe;
import com.crayonsmp.paper.object.Waystone;
import com.crayonsmp.paper.services.ArtifactService;
import com.crayonsmp.paper.services.TwitchService;
import com.crayonsmp.paper.services.WaystoneService;
import com.crayonsmp.paper.utils.tasks.reload;
import com.crayonsmp.paper.utils.tasks.restart;
import it.sauronsoftware.cron4j.Scheduler;
import lombok.Getter;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    @Getter
    public static Main instance;
    public static ArtifactService artifactService;
    public static WaystoneService waystoneService;
    public static TwitchService twitchService;

    @Override
    public void onEnable() {
        instance = this;

        ConfigurationSerialization.registerClass(ArtifactRecipe.class, "ArtifactRecipe");
        ConfigurationSerialization.registerClass(Waystone.class, "Waystone");

        artifactService = new ArtifactService();
        artifactService.init(this);

        waystoneService = new WaystoneService();
        waystoneService.init(this);

        twitchService = new TwitchService();
        twitchService.init(this);

        getServer().getPluginManager().registerEvents(new ItemListener(), this);

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
}
