package com.crayonsmp.paper.command;

import com.crayonsmp.paper.CrayonDefault;
import com.crayonsmp.paper.scheduler.ReloadSchedule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender.hasPermission("crayonsmp.reload")) {
            ReloadSchedule reloadSchedule = new ReloadSchedule();
            reloadSchedule.run();
        }
        return false;
    }
}
