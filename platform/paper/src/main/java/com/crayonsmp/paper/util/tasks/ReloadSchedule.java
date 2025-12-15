package com.crayonsmp.paper.util.tasks;

import com.crayonsmp.paper.CrayonDefault;
import com.crayonsmp.api.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class ReloadSchedule implements Runnable {
    @Override
    public void run() {
        new BukkitRunnable() {
            int counter = 30;

            @Override
            public void run() {
                switch (counter) {
                    case 30, 20, 10, 5, 4, 3, 2, 1 -> {
                        Bukkit.broadcast(ChatUtil.miniMessage("<#b2b2b2>Server reload sequence in: <#ff0040>" + counter + " second" + (counter != 1 ? "s" : "") + "!"));
                    }
                    case 0 -> {
                        Bukkit.broadcast(ChatUtil.miniMessage("<#b2b2b2>Initiating reload sequence now!"));
                        Bukkit.broadcast(ChatUtil.miniMessage("<#b2b2b2>This may take a few seconds..."));
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "meg reload");
                        Bukkit.getScheduler().runTaskLater(CrayonDefault.getInstance(), () -> Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "mm reload"), 20L * 2);
                        Bukkit.getScheduler().runTaskLater(CrayonDefault.getInstance(), () -> {
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "ce reload all");
                            Bukkit.broadcast(ChatUtil.miniMessage("<#b2b2b2>Plugin reload sequence completed!"));
                        }, 20L * 5);

                        this.cancel();
                    }
                }
                if (counter == 0) return;
                counter--;
            }
        }.runTask(CrayonDefault.getInstance());
    }
}