package com.crayonsmp.paper.command;

import com.crayonsmp.api.config.Configuration;
import com.crayonsmp.api.twitch.IStreamer;
import com.crayonsmp.api.twitch.ITwitchService;
import com.crayonsmp.api.twitch.ITwitchServiceProvider;
import com.crayonsmp.paper.CrayonDefault;
import com.crayonsmp.api.util.ChatUtil;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/*
twitch <register | info | unregister> <twitchname>
 */
public class TwitchCommand implements CommandExecutor, TabCompleter {

    @SneakyThrows
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatUtil.miniMessage("<red>You must be a player!"));
            return false;
        }
        if (args.length == 0) {
            player.sendMessage(ChatUtil.miniMessage("<red>The Twitch command requires arguments!"));
            return false;
        }
        ITwitchService twitchService = CrayonDefault.getInstance().getTwitchService();
        ITwitchServiceProvider twitchAPI = twitchService.getTwitchServiceProvider();
        Configuration twitchConfig = twitchService.getTwitchConfig();
        switch (args[0].toLowerCase()) {
            case "register" -> {
                if (args.length != 2) {
                    player.sendMessage(ChatUtil.miniMessage("<red>Please use /twitch register <twitchname>"));
                    return true;
                }
                if (!twitchAPI.isStreamerExists(args[1])) {
                    player.sendMessage(ChatUtil.miniMessage("<red>The Twitch username " + args[1] + " does not exist."));
                    return true;
                }
                Map<String, String> streamers = twitchService.getStreamers();
                if (streamers.get(player.getUniqueId().toString()) != null) {
                    player.sendMessage(ChatUtil.miniMessage("<red>You have already registered a Twitch username!"));
                    return false;
                }
                streamers.put(player.getUniqueId().toString(), args[1]);
                twitchConfig.set("streamers", streamers);
                twitchConfig.save();
                player.sendMessage(ChatUtil.miniMessage("<green>Successfully registered the Twitch username " + args[1] + "!"));
                return true;
            }
            case "unregister" -> {
                if (args.length != 1) {
                    player.sendMessage(ChatUtil.miniMessage("<red>Please use /twitch unregister"));
                    return false;
                }
                Map<String, String> streamers = twitchService.getStreamers();
                if (streamers.get(player.getUniqueId().toString()) == null) {
                    player.sendMessage(ChatUtil.miniMessage("<red>You have not registered a Twitch username!"));
                    return false;
                }
                streamers.remove(player.getUniqueId().toString());
                twitchConfig.set("streamers", streamers);
                twitchConfig.save();
                player.sendMessage(ChatUtil.miniMessage("<green>Successfully unregistered your Twitch username!"));
                return true;
            }
            case "info" -> {
                if (args.length != 2) {
                    player.sendMessage(ChatUtil.miniMessage("<red>Please use /twitch info <twitchname>"));
                    return false;
                }
                if (!twitchAPI.isStreamerExists(args[1])) {
                    player.sendMessage(ChatUtil.miniMessage("<red>The Twitch username " + args[1] + " does not exist."));
                    return false;
                }
                IStreamer streamer = twitchAPI.getStreamer(args[1]);
                player.sendMessage(ChatUtil.miniMessage("<green>Twitch Username: " + streamer.getLoginName()));
                player.sendMessage(ChatUtil.miniMessage("<green>Is Live: " + streamer.isLive()));
                player.sendMessage(ChatUtil.miniMessage("<green>Title: " + streamer.getTitle()));
                player.sendMessage(ChatUtil.miniMessage("<green>Game: " + streamer.getGameName()));
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
