package com.crayonsmp.paper.listener;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import io.papermc.paper.event.player.PlayerInventorySlotChangeEvent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;

import java.util.Objects;

public class ItemListener implements Listener {
    private static final NamespacedKey OLD_MODEL_KEY = NamespacedKey.fromString("minecraft:copper_helmet");
    private static final NamespacedKey NEW_MODEL_KEY = NamespacedKey.fromString("minecraft:copper_helmet_new");

    private boolean applyFixes(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;

        boolean changed = false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        if (meta.hasEnchant(Enchantment.MENDING)) {
            meta.removeEnchant(Enchantment.MENDING);
            changed = true;
        }

        if (item.getType() == Material.COPPER_HELMET) {
            NamespacedKey currentModel = meta.getItemModel();
            if (currentModel == null || Objects.equals(currentModel, OLD_MODEL_KEY)) {
                meta.setItemModel(NEW_MODEL_KEY);

                EquippableComponent equippable = meta.getEquippable();
                equippable.setModel(null);
                meta.setEquippable(equippable);

                changed = true;
            }
        }

        if (changed) {
            item.setItemMeta(meta);
        }
        return changed;
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemPickup(EntityPickupItemEvent event) {
        applyFixes(event.getItem().getItemStack());
    }

    @EventHandler
    public void onInventoryClick(org.bukkit.event.inventory.InventoryClickEvent event) {
        boolean changed = applyFixes(event.getCurrentItem());
        if (applyFixes(event.getCursor())) changed = true;
    }

    @EventHandler(ignoreCancelled = true)
    public void onHopperMove(InventoryMoveItemEvent event) {
        applyFixes(event.getItem());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (ItemStack item : event.getPlayer().getInventory().getContents()) {
            applyFixes(item);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onMobSpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof LivingEntity living) {
            EntityEquipment eq = living.getEquipment();
            if (eq == null) return;
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                applyFixes(eq.getItem(slot));
            }
        }
    }
}