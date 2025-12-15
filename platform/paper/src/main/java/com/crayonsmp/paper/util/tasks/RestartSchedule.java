package com.crayonsmp.paper.util.tasks;

import com.crayonsmp.paper.CrayonDefault;
import com.crayonsmp.api.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class RestartSchedule implements Runnable {
    @Override
    public void run() {
        new BukkitRunnable() {
            int counter = 30;
            @Override
            public void run() {
                switch (counter) {
                    case 30, 20, 10, 5, 4, 3, 2, 1 -> {
                        Bukkit.broadcast(ChatUtil.miniMessage("<#b2b2b2>Server restart in: <#ff0040>" + counter + " second" + (counter != 1 ? "s" : "") + "!"));
                    }
                    case 0 -> {
                        this.cancel();
                        Bukkit.broadcast(ChatUtil.miniMessage("<#b2b2b2>Server restarts now!"));
                        Bukkit.getScheduler().runTaskLater(CrayonDefault.getInstance(), () -> {
                            Bukkit.getServer().restart();
                        }, 20L);
                    }
                }
                if (counter == 0) return;
                counter--;
            }
        }.runTask(CrayonDefault.getInstance());
    }
}
