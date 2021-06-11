package net.wigoftime.open_komodo.etc;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

abstract public class FurnitureMangement {
	public static void createFurniture(ItemStack furnitureItem, @NotNull PlayerInteractEvent interactEvent) {
		// Disabled it due to armorstand editor plugin having issues
		if (true) return;

		Location placementLocation = interactEvent.getClickedBlock().getLocation().add(0, 1, 0);
		
		ArmorStand stand = interactEvent.getPlayer().getWorld().spawn(placementLocation, ArmorStand.class);
		stand.setVisible(false);
		stand.setInvulnerable(true);
		stand.setGravity(false);
		stand.getEquipment().setItemInMainHand(furnitureItem);
		
		interactEvent.getPlayer().sendMessage(String.format("%s» %sFurniture placed", ChatColor.GOLD, ChatColor.GRAY));
	}
	
	public static void rotateFurniture(@NotNull PlayerInteractEntityEvent interactEvent) {
		// Disabled it due to armorstand editor plugin having issues
		if (true) return;

		Entity armorStand = interactEvent.getRightClicked();
		
		armorStand.setRotation(armorStand.getLocation().getYaw() + 90, armorStand.getLocation().getPitch() + 90);
		interactEvent.getPlayer().sendMessage(String.format("%s» %sFurniture rotated", ChatColor.GOLD, ChatColor.GRAY));
	}
	
	public static void deleteFurniture(@NotNull EntityDamageByEntityEvent damageEvent) {
		// Disabled it due to armorstand editor plugin having issues
		if (true) return;

		damageEvent.getEntity().remove();
		damageEvent.getDamager().sendMessage(String.format("%s» %sFurniture deleted", ChatColor.GOLD, ChatColor.DARK_RED));
	}
	
	public static boolean isValid (@NotNull ItemStack item) {
		// Disabled it due to armorstand editor plugin having issues
		if (true) return false;

		if (item.getType() != Material.INK_SAC) return false;
		if (!item.getItemMeta().hasCustomModelData()) return false;
		
		return true;
	}
	
	public static boolean isValid (Entity entity) {
		// Disabled it due to armorstand editor plugin having issues
		if (true) return false;

		if (!(entity instanceof ArmorStand)) return false;
		
		ItemStack itemInHand = ((ArmorStand)entity).getEquipment().getItemInMainHand();
		
		return itemInHand.getType() == Material.INK_SAC && itemInHand.getItemMeta().hasCustomModelData() ?
		true : false;
	}
}
