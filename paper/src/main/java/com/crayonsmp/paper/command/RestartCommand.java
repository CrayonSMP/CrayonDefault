package com.crayonsmp.paper.command;

import com.crayonsmp.paper.scheduler.RestartSchedule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RestartCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if (commandSender.hasPermission("crayonsmp.reload")) {
            RestartSchedule restartSchedule = new RestartSchedule();
            restartSchedule.run();
        }
        return false;
    }
}
