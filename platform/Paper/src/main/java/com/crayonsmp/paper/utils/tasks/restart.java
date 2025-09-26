package com.crayonsmp.paper.utils.tasks;

import com.crayonsmp.paper.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class restart implements Runnable {
    @Override
    public void run() {
        new BukkitRunnable() {
            int counter = 30;

            @Override
            public void run() {
                if (counter > 0) {
                    if (counter == 30) {
                        Bukkit.broadcastMessage(ChatUtil.format("<#b2b2b2>Server restart in: <#ff0040>" + counter + " seconds!"));
                    } else if (counter == 20){
                        Bukkit.broadcastMessage(ChatUtil.format("<#b2b2b2>Server restart in: <#ff0040>" + counter + " seconds!"));
                    } else if (counter <= 10){
                        Bukkit.broadcastMessage(ChatUtil.format("<#b2b2b2>Server restart in: <#ff0040>" + counter + " seconds!"));
                    }
                    counter--;
                } else {
                    Bukkit.broadcastMessage(ChatUtil.format("<#b2b2b2>Server restart now!"));

                    Bukkit.getServer().restart();
                    this.cancel();
                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("CrayonCore"), 0L, 20L);
    }
}
