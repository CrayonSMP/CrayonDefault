package com.crayonsmp.paper;

import com.crayonsmp.paper.listeners.ItemListener;
import com.crayonsmp.paper.objects.ArtifactRecipe;
import com.crayonsmp.paper.objects.Waystone;
import com.crayonsmp.paper.services.ArtifactService;
import com.crayonsmp.paper.services.TwitchService;
import com.crayonsmp.paper.services.WaystoneService;
import com.crayonsmp.paper.utils.tasks.RestartSchedule;
import it.sauronsoftware.cron4j.Scheduler;
import lombok.Getter;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

public final class CrayonDefault extends JavaPlugin {
    @Getter
    public static CrayonDefault instance;
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

        //TODO: Finish Waystone Service.
        /*
        waystoneService = new WaystoneService();
        waystoneService.init(this);
         */

        twitchService = new TwitchService();
        twitchService.init(this);

        getServer().getPluginManager().registerEvents(new ItemListener(), this);

        scheduleDailyTasks();
    }

    @Override
    public void onDisable() {

    }

    private void scheduleDailyTasks() {
        Scheduler restart = new Scheduler();
        restart.schedule("0 7,15,23 * * *", new RestartSchedule());
        restart.start();
    }
}
