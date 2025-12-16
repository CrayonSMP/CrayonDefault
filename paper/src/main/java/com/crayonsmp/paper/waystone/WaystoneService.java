package com.crayonsmp.paper.waystone;

import com.crayonsmp.api.ICrayonDefault;
import com.crayonsmp.api.util.ChatUtil;
import com.crayonsmp.api.waystone.IWaystone;
import com.crayonsmp.api.waystone.IWaystoneService;
import com.crayonsmp.paper.CrayonDefault;
import com.crayonsmp.paper.listener.WaystoneListener;
import com.crayonsmp.api.config.ConfigurationUtil;
import com.crayonsmp.api.config.Configuration;
import io.github.projectunified.unidialog.core.dialog.Dialog;
import io.github.projectunified.unidialog.core.opener.DialogOpener;
import io.github.projectunified.unidialog.paper.PaperDialogManager;
import io.github.projectunified.unidialog.paper.dialog.PaperMultiActionDialog;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WaystoneService implements IWaystoneService {
    private Configuration config;
    private List<IWaystone> waystones;

    @Override
    public void init(ICrayonDefault instance) {
        JavaPlugin plugin = (JavaPlugin) instance;
        this.config = ConfigurationUtil.getConfig("waystone-config", plugin);
        this.waystones = new ArrayList<>();
        this.initConfig();
        this.readWaystones();
        plugin.getServer().getPluginManager().registerEvents(new WaystoneListener(this), plugin);
    }

    @Override
    public void initConfig() {
        if (!this.config.getFile().exists()) {
            this.config.setDefault("waystone-id", "default:bench");
            this.config.save();
        }

    }

    @Override
    public void readWaystones() {
        this.waystones.clear();
        if (this.config.getFile().exists()) {
            List<IWaystone> list = (List<IWaystone>) this.config.getList("waystones");
            if (list != null) {
                this.waystones.addAll(list);
            }
        }

    }

    @Override
    public void saveWaystones() {
        this.config.set("waystones", this.waystones);
        this.config.save();
    }

    @Override
    public void addWaystone(String uid, String name, Location location, List<String> players, String creator) {
        Waystone waystone = new Waystone(uid, name, location, players, creator);
        this.waystones.add(waystone);
        this.saveWaystones();
    }

    @Override
    public void removeWaystone(String uid) {
        waystones.removeIf(waystone -> waystone.uid().equalsIgnoreCase(uid));
        this.saveWaystones();
    }

    @Override
    public IWaystone getWaystone(Location location) {
        return this.waystones.stream().filter((waystone) -> waystone.location().equals(location)).findFirst().orElse(null);
    }

    @Override
    public IWaystone getWaystone(String uid) {
        return this.waystones.stream().filter((waystone) -> waystone.uid().equals(uid)).findFirst().orElse(null);
    }

    @Override
    public void addPlayerToWaystone(String waystoneUid, String playerUUID) {
        Optional<IWaystone> waystoneOptional = this.waystones.stream().filter((w) -> w.uid().equals(waystoneUid)).findFirst();
        waystoneOptional.ifPresent((waystone) -> {
            List<String> mutablePlayers = new ArrayList<>(waystone.players());
            if (!mutablePlayers.contains(playerUUID)) {
                mutablePlayers.add(playerUUID);
                waystone.setPlayers(mutablePlayers);
            }
        });
    }

    @Override
    public void openWaystoneGUI(Player player, String currentWaystoneUID) {
        List<IWaystone> playerWaystones = new ArrayList<>();
        PaperDialogManager dialogManager = CrayonDefault.getInstance().getDialogManager();
        UUID playerUUID = player.getUniqueId();
        this.waystones.forEach((waystone) -> {
            if (waystone.players().contains(playerUUID.toString())) {
                playerWaystones.add(waystone);
            }
        });
        if (playerWaystones.isEmpty()) {
            player.sendMessage(ChatUtil.miniMessage("<red>Du hast noch keine Waystones aktiviert."));
            return;
        }
        PaperMultiActionDialog paperDialog = ((PaperMultiActionDialog) dialogManager.createMultiActionDialog().title("Waystones")).canCloseWithEscape(true).afterAction(Dialog.AfterAction.CLOSE).body((builder) -> builder.text().text("Teleport you to a Waystone."));
        playerWaystones.forEach((waystone) -> {
            String waystoneName = waystone.name();
            String waystoneUID = waystone.uid();
            if (waystoneUID != null) {
                Component buttonLabel;
                if (waystoneName != null && !waystoneName.isEmpty()) {
                    buttonLabel = ChatUtil.miniMessage(waystoneName + " -> " + this.levelCostsToTeleport(this.getWaystone(currentWaystoneUID), waystone));
                } else {
                    buttonLabel = Component.text("Unbekannter Waystone [" + waystoneUID + "]");
                }
                String actionId = "waystone-activate-" + waystoneUID;
                paperDialog.action((builder) -> builder.label(buttonLabel).dynamicCustom(actionId));
                dialogManager.registerCustomAction(actionId, (uuid, map) -> this.teleportToWaystone(player, currentWaystoneUID, waystone.uid()));
            }
        });
        paperDialog.canCloseWithEscape(true);
        String closeid = "close";
        paperDialog.action((builder) -> builder.label(Component.text("Close")).dynamicCustom(closeid));
        dialogManager.registerCustomAction(closeid, (uuid, map) -> {
        });
        DialogOpener dialogOpener = paperDialog.opener();
        dialogOpener.open(playerUUID);
    }

    public void teleportToWaystone(Player player, String oldUID, String newUID) {
        IWaystone newWaystone = this.getWaystone(newUID);
        IWaystone oldWaystone = this.getWaystone(oldUID);
        if (newWaystone == null) {
            player.sendMessage(ChatUtil.miniMessage("§cError: Waystone not found."));
            return;
        }
        Location safeLocation = this.findSafeLocation(newWaystone.location());
        if (safeLocation == null) {
            player.sendMessage(ChatUtil.miniMessage("§cTeleportation ignored! The Waystone-Location ist blocked."));
            return;
        }
        int expCost = this.expCostsToTeleport(oldWaystone, newWaystone);
        if (expCost > player.getTotalExperience()) {
            player.sendMessage(ChatUtil.miniMessage("§cYou dont have enough xp"));
            return;
        }
        player.giveExp(-expCost);
        player.teleport(safeLocation);
        String var10001 = this.levelCostsToTeleport(oldWaystone, newWaystone);
        player.sendMessage(ChatUtil.miniMessage("Your teleported for " + var10001));
    }

    private Location findSafeLocation(Location baseLocation) {
        Location location = baseLocation.clone();
        for (int x = -2; x <= 2; ++x) {
            for (int y = -2; y <= 2; ++y) {
                for (int z = -2; z <= 2; ++z) {
                    Location checkLocation = location.clone().add(x, y, z);
                    if (this.isSafe(checkLocation)) {
                        Location targetLoc = checkLocation.add(0.5F, 0.0F, 0.5F);
                        targetLoc.setYaw(baseLocation.getYaw());
                        targetLoc.setPitch(baseLocation.getPitch());
                        return targetLoc;
                    }
                }
            }
        }

        return null;
    }

    private boolean isSafe(Location loc) {
        if (loc.getWorld() == null) {
            return false;
        }
        Block feetBlock = loc.getBlock();
        Block headBlock = feetBlock.getRelative(0, 1, 0);
        Block groundBlock = feetBlock.getRelative(0, -1, 0);
        boolean feetIsClear = feetBlock.isPassable();
        boolean headIsClear = headBlock.isPassable();
        boolean groundIsSolid = groundBlock.getType().isSolid();
        return groundIsSolid && feetIsClear && headIsClear;
    }

    public int expCostsToTeleport(IWaystone from, IWaystone to) {
        Location fromLoc = from.location();
        Location toLoc = to.location();
        if (!fromLoc.getWorld().equals(toLoc.getWorld())) {
            return 5000;
        }
        double distance = fromLoc.distance(toLoc);
        double rawCost = distance * (double) 0.5F;
        return (int) Math.ceil(rawCost);
    }

    public String levelCostsToTeleport(IWaystone from, IWaystone to) {
        int totalXpCost = this.expCostsToTeleport(from, to);
        int currentLevel = 0;
        int remainingXpCost = totalXpCost;
        while (true) {
            int xpToNextLevel;
            if (currentLevel <= 15) {
                xpToNextLevel = 2 * currentLevel + 7;
            } else if (currentLevel <= 30) {
                xpToNextLevel = 5 * currentLevel - 38;
            } else {
                xpToNextLevel = 9 * currentLevel - 158;
            }
            if (remainingXpCost < xpToNextLevel) {
                String levelPart = currentLevel > 0 ? currentLevel + " Level" : "";
                String xpPart = remainingXpCost <= 0 && currentLevel != 0 ? "" : remainingXpCost + " EXP";
                String andSeparator = currentLevel > 0 && remainingXpCost > 0 ? " und " : "";
                String result = levelPart + andSeparator + xpPart;
                return result.trim();
            }
            remainingXpCost -= xpToNextLevel;
            ++currentLevel;
        }
    }

    @Override
    public Configuration getConfig() {
        return config;
    }

    @Override
    public List<IWaystone> getWaystones() {
        return waystones;
    }
}
