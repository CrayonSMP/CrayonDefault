package com.crayonsmp.paper.commands;

import com.crayonsmp.paper.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class ArtifactCommand implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String [] strings) {
        if (!(commandSender instanceof Player player)){
            commandSender.sendMessage("You must be a player!");
            return true;
        }

        Bukkit.getLogger().log(Level.INFO, "Artifact command has been sent!");

        Main.artifactService.openCrafterGUI(player);
        return false;
    }
}
