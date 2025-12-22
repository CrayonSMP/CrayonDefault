package com.crayonsmp.paper;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.crayonsmp.api.ICrayonDefault;
import com.crayonsmp.api.provider.CrayonDefaultProvider;
import com.crayonsmp.api.artifact.IArtifactService;
import com.crayonsmp.api.twitch.ITwitchService;
import com.crayonsmp.api.waystone.IWaystoneService;
import com.crayonsmp.paper.command.ReloadCommand;
import com.crayonsmp.paper.command.RestartCommand;
import com.crayonsmp.paper.listener.ItemListener;
import com.crayonsmp.paper.artifact.ArtifactRecipe;
import com.crayonsmp.paper.waystone.Waystone;
import com.crayonsmp.paper.artifact.ArtifactService;
import com.crayonsmp.paper.twitch.TwitchService;
import com.crayonsmp.paper.scheduler.ReloadSchedule;
import com.crayonsmp.paper.scheduler.RestartSchedule;
import com.crayonsmp.paper.waystone.WaystoneService;
import io.github.projectunified.unidialog.paper.PaperDialogManager;
import it.sauronsoftware.cron4j.Scheduler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class CrayonDefault extends JavaPlugin implements ICrayonDefault {
    @Getter
    private static CrayonDefault instance;
    private IArtifactService artifactService;
    private IWaystoneService waystoneService;
    private ITwitchService twitchService;
    @Getter
    private ProtocolManager protocolManager;
    @Getter
    private PaperDialogManager dialogManager;

    public void onLoad() {
        instance = this;
        CrayonDefaultProvider.register(instance);
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        ConfigurationSerialization.registerClass(ArtifactRecipe.class, "ArtifactRecipe");
        ConfigurationSerialization.registerClass(Waystone.class, "Waystone");
        dialogManager = new PaperDialogManager(this);
        artifactService = new ArtifactService();
        artifactService.init(this);
        waystoneService = new WaystoneService();
        waystoneService.init(this);
        twitchService = new TwitchService();
        twitchService.init(this);
        getServer().getPluginManager().registerEvents(new ItemListener(), this);
        scheduleDailyTasks();
        dialogManager.register();

        Objects.requireNonNull(getServer().getPluginCommand("reload")).setExecutor(new ReloadCommand());
        Objects.requireNonNull(getServer().getPluginCommand("restart")).setExecutor(new RestartCommand());
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
        Scheduler restart = new Scheduler();
        restart.schedule("0 4 * * *", new RestartSchedule());
        restart.start();

    }

    @Override
    public IArtifactService getArtifactService() {
        return artifactService;
    }

    @Override
    public IWaystoneService getWaystoneService() {
        return waystoneService;
    }

    @Override
    public ITwitchService getTwitchService() {
        return twitchService;
    }
}
