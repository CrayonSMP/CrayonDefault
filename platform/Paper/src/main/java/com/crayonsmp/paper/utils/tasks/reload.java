package com.crayonsmp.paper.utils.tasks;

import com.crayonsmp.paper.Main;
import com.crayonsmp.paper.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class reload implements Runnable{
    @Override
    public void run() {
        new BukkitRunnable() {
            int counter = 30;

            @Override
            public void run() {
                if (counter > 0) {
                    if (counter == 30) {
                        Bukkit.broadcastMessage(ChatUtil.format("<#b2b2b2>Server reload sequence in: <#ff0040>" + counter + " seconds!"));
                    } else if (counter == 20) {
                        Bukkit.broadcastMessage(ChatUtil.format("<#b2b2b2>Server reload sequence in: <#ff0040>" + counter + " seconds!"));
                    } else if (counter <= 10) {
                        Bukkit.broadcastMessage(ChatUtil.format("<#b2b2b2>Server reload sequence in: <#ff0040>" + counter + " seconds!"));
                    }
                    counter--;
                } else {
                    Bukkit.broadcastMessage(ChatUtil.format("<#b2b2b2>Initiating reload sequence now!"));
                    Bukkit.broadcastMessage(ChatUtil.format("<#b2b2b2>This may take a few seconds..."));

                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "meg reload");

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "mm reload");
                        }
                    }.runTaskLater(Main.getInstance(), 20L * 2);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "ce reload all");
                            Bukkit.broadcastMessage(ChatUtil.format("<#b2b2b2>Plugin reload sequence completed!"));
                        }
                    }.runTaskLater(Main.getInstance(), 20L * 5);

                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 20L);
    }
}
