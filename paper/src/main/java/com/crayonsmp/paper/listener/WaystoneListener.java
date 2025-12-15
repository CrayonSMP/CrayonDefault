package com.crayonsmp.paper.listener;

import com.crayonsmp.api.waystone.IWaystone;
import com.crayonsmp.api.waystone.IWaystoneService;
import com.crayonsmp.paper.CrayonDefault;
import io.github.projectunified.unidialog.core.dialog.Dialog;
import io.github.projectunified.unidialog.core.opener.DialogOpener;
import io.github.projectunified.unidialog.paper.action.PaperDialogActionBuilder;
import net.momirealms.craftengine.bukkit.api.event.FurnitureBreakEvent;
import net.momirealms.craftengine.bukkit.api.event.FurnitureInteractEvent;
import net.momirealms.craftengine.bukkit.api.event.FurniturePlaceEvent;
import net.momirealms.craftengine.core.util.Key;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class WaystoneListener implements Listener {
    private final IWaystoneService waystoneService;

    public WaystoneListener(IWaystoneService waystoneService) {
        this.waystoneService = waystoneService;
    }

    @EventHandler
    public void onWaystonePlace(FurniturePlaceEvent event) {
        if (event.furniture().id().equals(Key.of(Objects.requireNonNull(this.waystoneService.getConfig().getString("waystone-id"))))) {
            Player player = event.player();
            String actionId = "setwaystonename";
            DialogOpener dialogOpener = CrayonDefault.getInstance().getDialogManager().createConfirmationDialog().title("Create Waystone").canCloseWithEscape(true).afterAction(Dialog.AfterAction.CLOSE).input("name", (builder) -> builder.textInput().label("Enter waystone name:")).yesAction((builder) -> ((PaperDialogActionBuilder)builder.label("Confirm")).dynamicCustom(actionId)).noAction((builder) -> ((PaperDialogActionBuilder)builder.label("Cancel")).dynamicCustom(actionId)).opener();
            CrayonDefault.getInstance().getDialogManager().registerCustomAction(actionId, (uuid, map) -> {
                String name = map.get("name").isEmpty() ? "Unnamed Waystone" : map.get("name");
                List<String> players = List.of(player.getUniqueId().toString());
                Location location = event.location();
                String creator = player.getName();
                UUID uid = event.furniture().uuid();
                this.waystoneService.addWaystone(uid.toString(), name, location, players, creator);
                IWaystone waystone = this.waystoneService.getWaystone(uid.toString());
                event.furniture().baseEntity().addScoreboardTag(waystone.uid());
            });
            dialogOpener.open(player.getUniqueId());
        }
    }

    @EventHandler
    public void onWaystoneInteract(FurnitureInteractEvent event) {
        if (event.furniture().id().equals(Key.of(Objects.requireNonNull(this.waystoneService.getConfig().getString("waystone-id"))))) {
            Player player = event.player();
            String furnitureUuid = event.furniture().uuid().toString();

            for(IWaystone waystone : this.waystoneService.getWaystones()) {
                if (waystone.uid().equals(furnitureUuid)) {
                    if (waystone.players().contains(player.getUniqueId().toString())) {
                        this.waystoneService.openWaystoneGUI(player, waystone.uid());
                        return;
                    }

                    this.waystoneService.addPlayerToWaystone(waystone.uid(), player.getUniqueId().toString());
                    player.sendMessage("§aWaystone aktiviert.");
                    break;
                }
            }
        }

    }

    @EventHandler
    public void onWaystoneBreak(FurnitureBreakEvent event) {
        if (event.furniture().id().equals(Key.of(Objects.requireNonNull(this.waystoneService.getConfig().getString("waystone-id"))))) {
            this.waystoneService.getWaystones().forEach((waystone) -> {
                if (waystone.uid().equals(event.furniture().uuid().toString())) {
                    this.waystoneService.removeWaystone(waystone.uid());
                }
            });
        }
    }
}
