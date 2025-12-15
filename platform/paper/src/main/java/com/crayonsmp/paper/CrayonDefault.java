package com.crayonsmp.paper;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.crayonsmp.api.ICrayonDefault;
import com.crayonsmp.api.provider.CrayonDefaultProvider;
import com.crayonsmp.api.artifact.IArtifactService;
import com.crayonsmp.api.util.IProtocolManager;
import com.crayonsmp.api.twitch.ITwitchService;
import com.crayonsmp.api.waystone.IWaystoneService;
import com.crayonsmp.paper.listener.ItemListener;
import com.crayonsmp.paper.artifact.ArtifactRecipe;
import com.crayonsmp.paper.waystone.Waystone;
import com.crayonsmp.paper.artifact.ArtifactService;
import com.crayonsmp.paper.twitch.TwitchService;
import com.crayonsmp.paper.waystone.WaystoneService;
import com.crayonsmp.paper.util.tasks.ReloadSchedule;
import com.crayonsmp.paper.util.tasks.RestartSchedule;
import it.sauronsoftware.cron4j.Scheduler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class CrayonDefault extends JavaPlugin implements ICrayonDefault {
    @Getter
    private static CrayonDefault instance;
    private ArtifactService artifactService;
    private WaystoneService waystoneService;
    private TwitchService twitchService;
    private ProtocolManager protocolManager;

    public void onLoad() {
        instance = this;
        CrayonDefaultProvider.register(instance);
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
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
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);
        this.protocolManager = null;
        this.twitchService = null;
        this.waystoneService = null;
        this.artifactService = null;

    }

    private void scheduleDailyTasks() {
        Scheduler reload = new Scheduler();
        reload.schedule("0 7,13,15,17,20,23 * * *", new ReloadSchedule());
        reload.start();
        Scheduler restart = new Scheduler();
        restart.schedule("0 7,15,23 * * *", new RestartSchedule());
        restart.start();

    }

    @Override
    public IArtifactService getArtifactService() {
        return this.artifactService;
    }

    @Override
    public IWaystoneService getWaystoneService() {
        return this.waystoneService;
    }

    @Override
    public ITwitchService getTwitchService() {
        return this.twitchService;
    }

    @Override
    public IProtocolManager getProtocolManager() {
        return this.protocolManager;
    }
}
