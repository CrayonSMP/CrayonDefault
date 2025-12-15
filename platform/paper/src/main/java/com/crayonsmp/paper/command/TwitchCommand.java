package com.crayonsmp.paper.command;

import com.crayonsmp.api.twitch.IStreamer;
import com.crayonsmp.paper.CrayonDefault;
import com.crayonsmp.api.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/*
twitch <register | info | unregister> <twitchname>
 */
public class TwitchCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            ChatUtil.sendMessage(sender, "&cYou must be a player!");
            return false;
        }

        if (args.length == 0) {
            ChatUtil.sendMessage(sender, "&cThe Twitch command requires arguments!");
            return false;
        }

        var api = CrayonDefault.twitchService.twitchAPI;

        switch(args[0].toLowerCase()) {
            case "register" -> {
                if (args.length != 2) {
                    ChatUtil.sendMessage(player, "&cPlease use /twitch register <twitchname>");
                    return true;
                }
                if (!api.isStreamerExists(args[1])) {
                    ChatUtil.sendMessage(sender, "&cThe Twitch username " + args[1] + " does not exist.");
                    return true;
                }
                HashMap<String, String> streamers = CrayonDefault.twitchService.streamers;
                if (streamers.get(player.getUniqueId().toString()) != null) {
                    ChatUtil.sendMessage(sender, "&cYou have already registered a Twitch username!");
                    return false;
                }
                streamers.put(player.getUniqueId().toString(), args[1]);
                CrayonDefault.twitchService.twitchConfig.set("streamers", streamers);
                CrayonDefault.twitchService.twitchConfig.save();
                ChatUtil.sendMessage(sender, "&aSuccessfully registered the Twitch username " + args[1] + "!");
                return true;
            }
            case "unregister" -> {
                if (args.length != 1) {
                    ChatUtil.sendMessage(player, "&cPlease use /twitch unregister");
                    return false;
                }
                HashMap<String, String> streamers = CrayonDefault.twitchService.streamers;
                if (streamers.get(player.getUniqueId().toString()) == null) {
                    ChatUtil.sendMessage(sender, "&cYou have not registered a Twitch username!");
                    return false;
                }
                streamers.remove(player.getUniqueId().toString());
                CrayonDefault.twitchService.twitchConfig.set("streamers", streamers);
                CrayonDefault.twitchService.twitchConfig.save();
                ChatUtil.sendMessage(sender, "&aSuccessfully unregistered your Twitch username!");
                return true;
            }
            case "info" -> {
                if (args.length != 2) {
                    ChatUtil.sendMessage(player, "&cPlease use /twitch info <twitchname>");
                    return false;
                }
                if (!api.isStreamerExists(args[1])) {
                    ChatUtil.sendMessage(sender, "&cThe Twitch username " + args[1] + " does not exist.");
                    return false;
                }
                IStreamer streamer = api.getStreamer(args[1]);
                ChatUtil.sendMessage(sender, "&aTwitch Username: " + streamer.getLoginName());
                ChatUtil.sendMessage(sender, "&aIs Live: " + streamer.isLive());
                ChatUtil.sendMessage(sender, "&aTitle: " + streamer.getTitle());
                ChatUtil.sendMessage(sender, "&aGame: " + streamer.getGameName());
                return true;
            }
            default -> {
                ChatUtil.sendMessage(sender, "&cPlease use /twitch <register | info | unregister> (twitchname)");
                return false;
            }
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1) {
            return List.of("register", "info", "unregister");
        }
        return Collections.emptyList();
    }
}
