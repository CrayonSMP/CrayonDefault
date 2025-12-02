package com.crayonsmp.paper.commands;

import com.crayonsmp.paper.CrayonDefault;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class ArtifactCommand implements CommandExecutor{

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String [] args) {
        if (!(sender instanceof Player player)){
            sender.sendMessage("You must be a player!");
            return true;
        }
        CrayonDefault.artifactService.openCrafterGUI(player);
        return false;
    }
}
