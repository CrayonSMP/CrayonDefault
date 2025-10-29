package com.crayonsmp.paper.object.gui;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

@Getter
@Setter
@Builder
public class ArtifactCrafterInventory {
    Player owner;
    Inventory inventory;

    String resoult;
}
