package com.crayonsmp.paper.waystone;

import com.crayonsmp.api.ICrayonDefault;
import com.crayonsmp.api.events.StreamerNowLiveEvent;
import com.crayonsmp.api.events.WaaystoneGUICloseEvent;
import com.crayonsmp.api.events.WaystoneTeleportEvent;
import com.crayonsmp.api.util.ChatUtil;
import com.crayonsmp.api.waystone.IWaystone;
import com.crayonsmp.api.waystone.IWaystoneService;
import com.crayonsmp.paper.CrayonDefault;
import com.crayonsmp.paper.listener.WaystoneListener;
import com.crayonsmp.api.config.ConfigurationUtil;
import com.crayonsmp.api.config.Configuration;
import io.github.projectunified.unidialog.core.dialog.Dialog;
import io.github.projectunified.unidialog.paper.PaperDialogManager;
import io.github.projectunified.unidialog.paper.dialog.PaperMultiActionDialog;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

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
            this.config.setDefault("base-xp-cost", 0.01);
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
        Waystone currentWaystone = (Waystone) getWaystone(currentWaystoneUID);
        UUID playerUUID = player.getUniqueId();

        this.waystones.forEach((waystone) -> {
            if (waystone.players().contains(playerUUID.toString())) {
                playerWaystones.add(waystone);
            }
        });

        if (playerWaystones.isEmpty()) {
            player.sendActionBar(ChatUtil.miniMessage("<red>You dont have any unlocked waystone."));
            return;
        }

        PaperMultiActionDialog paperDialog = ((PaperMultiActionDialog) dialogManager.createMultiActionDialog()
                .title("Waystone: " + currentWaystone.name())
                .canCloseWithEscape(true)
                .afterAction(Dialog.AfterAction.CLOSE)
                .body((builder) -> builder.text().text("Travel to a Waystone.")));

        List<String> registeredActions = new ArrayList<>();

        playerWaystones.forEach((waystone) -> {
            String waystoneUID = waystone.uid();
            int lvlCost = this.levelCostsToTeleport(currentWaystone, waystone);
            if (waystoneUID != null) {
                String waystoneName = waystone.name();
                Component buttonLabel;

                if (waystoneName != null && !waystoneName.isEmpty()) {
                    String levelArrow;
                    levelArrow = "%shift_raw_6%<white><shadow:#1b1c1b00><font:minecraft:gui>%image_raw_artifacs:level_1%</font></shadow>%shift_raw_-4%";
                    if (lvlCost >= 11) levelArrow = "%shift_raw_6%<white><shadow:#1b1c1b00><font:minecraft:gui>%image_raw_artifacs:level_2%</font></shadow>%shift_raw_-4%";
                    if (lvlCost >= 21) levelArrow = "%shift_raw_6%<white><shadow:#1b1c1b00><font:minecraft:gui>%image_raw_artifacs:level_3%</font></shadow>%shift_raw_-4%";

                    String lvlCostToTeleport = "<#c8ff8f>" + this.levelCostsToTeleport(currentWaystone, waystone);
                    if (lvlCost == 0) {
                        lvlCostToTeleport = "";
                        levelArrow = "";
                    }
                    buttonLabel = ChatUtil.miniMessage(PlaceholderAPI.setPlaceholders(player, waystoneName + levelArrow + lvlCostToTeleport));
                } else {
                    buttonLabel = Component.text("Unknow Waystone [" + waystoneUID + "]");
                }

                String actionId = "ws_tp_" + playerUUID + "_" + waystoneUID;
                registeredActions.add(actionId);

                paperDialog.action((builder) -> builder.label(buttonLabel).dynamicCustom(actionId));

                dialogManager.registerCustomAction(actionId, (clickedUuid, map) -> {
                    Player clickingPlayer = org.bukkit.Bukkit.getPlayer(clickedUuid);
                    if (clickingPlayer != null) {
                        this.teleportToWaystone(clickingPlayer, currentWaystoneUID, waystoneUID);
                    }

                    registeredActions.forEach(dialogManager::unregisterCustomAction);
                });
            }
        });

        paperDialog.exitAction((closedUuid) -> {
            Player closingPlayer = player;

            if (closingPlayer != null) {
                WaaystoneGUICloseEvent event = new WaaystoneGUICloseEvent(player);
                Bukkit.getPluginManager().callEvent(event);
            }
        });

        paperDialog.opener().open(playerUUID);
    }

    @Override
    public void openWaystoneGUI(Player player, Location currentLocation, String TitleString) {
        List<IWaystone> playerWaystones = new ArrayList<>();
        PaperDialogManager dialogManager = CrayonDefault.getInstance().getDialogManager();
        UUID playerUUID = player.getUniqueId();

        this.waystones.forEach((waystone) -> {
            if (waystone.players().contains(playerUUID.toString())) {
                playerWaystones.add(waystone);
            }
        });

        if (playerWaystones.isEmpty()) {
            player.sendActionBar(ChatUtil.miniMessage("<red>You dont have any unlocked waystone."));
            return;
        }

        PaperMultiActionDialog paperDialog = ((PaperMultiActionDialog) dialogManager.createMultiActionDialog()
                .title(TitleString)
                .canCloseWithEscape(true)
                .afterAction(Dialog.AfterAction.CLOSE)
                .body((builder) -> builder.text().text("Travel to a Waystone.")));

        List<String> registeredActions = new ArrayList<>();

        playerWaystones.forEach((waystone) -> {
            String waystoneUID = waystone.uid();
            int lvlCost = this.levelCostsToTeleport(currentLocation, waystone);
            if (waystoneUID != null) {
                String waystoneName = waystone.name();
                Component buttonLabel;

                if (waystoneName != null && !waystoneName.isEmpty()) {
                    String levelArrow;
                    levelArrow = "%shift_raw_6%<white><shadow:#1b1c1b00><font:minecraft:gui>%image_raw_artifacs:level_1%</font></shadow>%shift_raw_-4%";
                    if (lvlCost >= 11) levelArrow = "%shift_raw_6%<white><shadow:#1b1c1b00><font:minecraft:gui>%image_raw_artifacs:level_2%</font></shadow>%shift_raw_-4%";
                    if (lvlCost >= 21) levelArrow = "%shift_raw_6%<white><shadow:#1b1c1b00><font:minecraft:gui>%image_raw_artifacs:level_3%</font></shadow>%shift_raw_-4%";

                    String lvlCostToTeleport = "<#c8ff8f>" + this.levelCostsToTeleport(currentLocation, waystone);
                    if (lvlCost == 0) {
                        lvlCostToTeleport = "";
                        levelArrow = "";
                    }
                    buttonLabel = ChatUtil.miniMessage(PlaceholderAPI.setPlaceholders(player, waystoneName + levelArrow + lvlCostToTeleport));
                } else {
                    buttonLabel = Component.text("Unknow Waystone [" + waystoneUID + "]");
                }

                String actionId = "ws_tp_" + playerUUID + "_" + waystoneUID;
                registeredActions.add(actionId);

                paperDialog.action((builder) -> builder.label(buttonLabel).dynamicCustom(actionId));

                dialogManager.registerCustomAction(actionId, (clickedUuid, map) -> {
                    Player clickingPlayer = org.bukkit.Bukkit.getPlayer(clickedUuid);
                    if (clickingPlayer != null) {
                        this.teleportToWaystone(clickingPlayer, currentLocation, waystoneUID);
                    }

                    registeredActions.forEach(dialogManager::unregisterCustomAction);
                });
            }
        });

        paperDialog.opener().open(playerUUID);
    }

    public void teleportToWaystone(Player player, String oldUID, String newUID) {
        IWaystone newWaystone = this.getWaystone(newUID);
        IWaystone oldWaystone = this.getWaystone(oldUID);
        if (newWaystone == null) {
            player.sendActionBar(ChatUtil.miniMessage("<red>Error: Waystone not found."));
            return;
        }
        Location safeLocation = this.findSafeLocation(newWaystone.location());
        if (safeLocation == null) {
            player.sendActionBar(ChatUtil.miniMessage("<red>Teleportation ignored! The Waystone-Location ist blocked."));
            return;
        }
        int lvlCost = this.levelCostsToTeleport(oldWaystone, newWaystone);
        if (lvlCost > player.getLevel()) {
            player.sendActionBar(ChatUtil.miniMessage("<red>You dont have enough levels"));
            return;
        }
        WaystoneTeleportEvent event = new WaystoneTeleportEvent(player, true, null, oldWaystone, newWaystone);
        Bukkit.getPluginManager().callEvent(event);
        player.giveExpLevels(-lvlCost);
        player.teleport(safeLocation);

        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
        player.sendActionBar(ChatUtil.miniMessage("<white>You travel to: " + newWaystone.name()));
    }

    public void teleportToWaystone(Player player, Location oldLoc, String newUID) {
        IWaystone newWaystone = this.getWaystone(newUID);
        if (newWaystone == null) {
            player.sendActionBar(ChatUtil.miniMessage("<red>Error: Waystone not found."));
            return;
        }
        Location safeLocation = this.findSafeLocation(newWaystone.location());
        if (safeLocation == null) {
            player.sendActionBar(ChatUtil.miniMessage("<red>Teleportation ignored! The Waystone-Location ist blocked."));
            return;
        }
        int lvlCost = this.levelCostsToTeleport(oldLoc, newWaystone);
        if (lvlCost > player.getLevel()) {
            player.sendActionBar(ChatUtil.miniMessage("<red>You dont have enough levels"));
            return;
        }
        WaystoneTeleportEvent event = new WaystoneTeleportEvent(player, false, oldLoc, null, newWaystone);
        Bukkit.getPluginManager().callEvent(event);
        player.giveExpLevels(-lvlCost);
        player.teleport(safeLocation);

        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
        player.sendActionBar(ChatUtil.miniMessage("<white>You travel to: " + newWaystone.name()));
    }

    private Location findSafeLocation(Location baseLocation) {
        Location location = baseLocation.clone();
        List<Location> checkedLocations = new ArrayList<>();
        for (int x = -2; x <= 2; ++x) {
            for (int y = -2; y <= 2; ++y) {
                for (int z = -2; z <= 2; ++z) {
                    if (x == 0 && y == 0 || y == 1 && z == 0) continue;
                    Location checkLocation = location.clone().add(x, y, z);
                    if (this.isSafe(checkLocation)) {
                        checkedLocations.add(checkLocation);
                    }
                }
            }
        }

        if (checkedLocations.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int randomIndex = random.nextInt(0, checkedLocations.size());
        Location targetLoc = checkedLocations.get(randomIndex).add(0.5F, 0.0F, 0.5F);
        targetLoc.setYaw(baseLocation.getYaw());
        targetLoc.setPitch(baseLocation.getPitch());
        return targetLoc;
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
            return 900;
        }
        double distance = fromLoc.distance(toLoc);
        double rawCost = distance * config.getDouble("base-xp-cost");
        return (int) Math.ceil(rawCost);
    }

    public int expCostsToTeleport(Location from, IWaystone to) {
        Location fromLoc = from;
        Location toLoc = to.location();
        if (!fromLoc.getWorld().equals(toLoc.getWorld())) {
            return 900;
        }
        double distance = fromLoc.distance(toLoc);
        double rawCost = distance * config.getDouble("base-xp-cost");
        return (int) Math.ceil(rawCost);
    }

    public int levelCostsToTeleport(IWaystone from, IWaystone to) {
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
                return currentLevel;
            }
            remainingXpCost -= xpToNextLevel;
            ++currentLevel;
        }
    }

    public int levelCostsToTeleport(Location from, IWaystone to) {
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
                return currentLevel;
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
