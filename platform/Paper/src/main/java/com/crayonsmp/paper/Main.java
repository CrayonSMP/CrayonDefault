package com.crayonsmp.paper;

import com.crayonsmp.paper.utils.tasks.reload;
import com.crayonsmp.paper.utils.tasks.restart;
import it.sauronsoftware.cron4j.Scheduler;
import net.momirealms.craftengine.core.plugin.CraftEngine;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main instance;

    @Override
    public void onEnable() {
        instance = this;
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
