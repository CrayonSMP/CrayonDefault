package com.crayonsmp.paper.command;

import com.crayonsmp.api.util.ChatUtil;
import com.crayonsmp.paper.CrayonDefault;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class ArtifactCommand implements CommandExecutor{

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatUtil.miniMessage("<red>You must be a player!"));
            return true;
        }
        CrayonDefault.getInstance().getArtifactService().openCrafterGUI(player);
        return false;
    }
}
