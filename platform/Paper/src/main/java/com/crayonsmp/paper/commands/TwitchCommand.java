package com.crayonsmp.paper.commands;

import com.crayonsmp.objects.Streamer;
import com.crayonsmp.paper.services.TwitchService;
import com.crayonsmp.utils.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

/*
twitch <register | info | unregister> <twitchname>
 */
public class TwitchCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            ChatUtil.sendMessage(sender, "&cYou must be a player!");
            return true;
        }

        var api = TwitchService.twitchAPI;

        if (args[0].equalsIgnoreCase("register")) {
            if (args.length != 2) {
                ChatUtil.sendMessage(player, "&cPlease use /twitch register <twitchname>");
                return true;
            }

            if (!api.isStreamerExists(args[1])) {
                ChatUtil.sendMessage( sender,"&cThe Twitch username " + args[1] + " does not exist.");
                return true;
            }

            HashMap<String, String> streamers = TwitchService.streamers;

            if (streamers.get(player.getUniqueId().toString()) != null){
                ChatUtil.sendMessage( sender,"&cYou have already registered a Twitch username!");
                return true;
            }

            streamers.put(player.getUniqueId().toString(), args[1]);
            TwitchService.twitchConfig.set("streamers", streamers);
            TwitchService.twitchConfig.save();

            ChatUtil.sendMessage( sender,"&aSuccessfully registered the Twitch username " + args[1] + "!");
        } else if (args[0].equalsIgnoreCase("info")) {
            if (args.length != 2) {
                ChatUtil.sendMessage(player, "&cPlease use /twitch info <twitchname>");
                return true;
            }

            if (!api.isStreamerExists(args[1])) {
                ChatUtil.sendMessage( sender,"&cThe Twitch username " + args[1] + " does not exist.");
                return true;
            }

            Streamer streamer = api.getStreamer(args[1]);

            ChatUtil.sendMessage( sender,"&aTwitch Username: " + streamer.getLoginName());
            ChatUtil.sendMessage( sender,"&aIs Live: " + streamer.isLive());
            ChatUtil.sendMessage( sender,"&aTitle: " + streamer.getTitle());
            ChatUtil.sendMessage( sender,"&aGame: " + streamer.getGameName());

        } else if (args[0].equalsIgnoreCase("unregister")) {
            if (args.length != 1) {
                ChatUtil.sendMessage(player, "&cPlease use /twitch unregister");
                return true;
            }

            HashMap<String, String> streamers = TwitchService.streamers;

            if (streamers.get(player.getUniqueId().toString()) == null){
                ChatUtil.sendMessage( sender,"&cYou have not registered a Twitch username!");
                return true;
            }

            streamers.remove(player.getUniqueId().toString());
            TwitchService.twitchConfig.set("streamers", streamers);
            TwitchService.twitchConfig.save();

            ChatUtil.sendMessage( sender,"&aSuccessfully unregistered your Twitch username!");
        } else {
            ChatUtil.sendMessage( sender,"&cPlease use /twitch <register | info | unregister> (twitchname)");
            return true;
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length == 1) {
            return List.of("register", "info", "unregister");
        }
        return List.of();
    }
}
