package com.crayonsmp.paper.scheduler;

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
                if (counter > 0) {
                    if (counter == 30 || counter == 20 || counter == 10 || counter <= 5) {
                        Bukkit.broadcast(ChatUtil.miniMessage(
                                "<#b2b2b2>Server restart in: <#ff0040>" + counter + " second" + (counter != 1 ? "s" : "") + "!"
                        ));
                    }
                    counter--;
                } else {
                    this.cancel();
                    Bukkit.broadcast(ChatUtil.miniMessage("<#b2b2b2>Server restarts now!"));

                    Bukkit.getScheduler().runTaskLater(CrayonDefault.getInstance(), () -> {
                        Bukkit.getServer().restart();
                    }, 20L);
                }
            }
        }.runTaskTimer(CrayonDefault.getInstance(), 0L, 20L);
    }
}
