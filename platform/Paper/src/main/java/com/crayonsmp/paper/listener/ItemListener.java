package com.crayonsmp.paper.listener;

import io.papermc.paper.datacomponent.item.Equippable;
import net.kyori.adventure.key.Key;
import net.momirealms.craftengine.core.attribute.AttributeModifier;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;

import java.util.Objects;

public class ItemListener implements Listener {

    private static final NamespacedKey OLD_MODEL_KEY = NamespacedKey.fromString("minecraft:copper_helmet");
    private static final NamespacedKey NEW_MODEL_KEY = NamespacedKey.fromString("minecraft:copper_helmet_new");

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();
        item = updateArmorModel(item);
        event.getItem().setItemStack(item);
    }

    @EventHandler
    public void onInventoryMove(InventoryMoveItemEvent event) {
        ItemStack item = event.getItem();
        item = updateArmorModel(item);
        event.setItem(item);
    }

    @EventHandler
    public void onInventoryDrag(InventoryMoveItemEvent event) {
        ItemStack item = event.getItem();
        item = updateArmorModel(item);
        event.setItem(item);
    }

    @EventHandler
    public void onInventoryClick(InventoryMoveItemEvent event) {
        ItemStack item = event.getItem();
        item = updateArmorModel(item);
        event.setItem(item);
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof LivingEntity livingEntity)) {
            return;
        }

        EntityEquipment equipment = livingEntity.getEquipment();

        if (equipment == null) {
            return;
        }

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemInSlot = equipment.getItem(slot);
            ItemStack updatedItem = updateArmorModel(itemInSlot);
            equipment.setItem(slot, updatedItem);
        }
    }



    private ItemStack updateArmorModel(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        if (meta == null) return item;

        NamespacedKey currentModel = meta.getItemModel();

        if (item.getType() == Material.COPPER_HELMET) {

            if (currentModel == null) {

                meta.setItemModel(NEW_MODEL_KEY);
                EquippableComponent equippableComponent = meta.getEquippable();
                equippableComponent.setModel(null);
                meta.setEquippable(equippableComponent);
                item.setItemMeta(meta);
                return item;
            }

            else if (Objects.equals(currentModel, OLD_MODEL_KEY)) {
                meta.setItemModel(NEW_MODEL_KEY);
                meta.getEquippable().setModel(null);
                item.setItemMeta(meta);
                return item;
            }
        }
        return item;
    }
}