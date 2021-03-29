package net.wigoftime.open_komodo.etc;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.objects.CustomItem;

public class FurnitureMangement {
	public static void createFurniture(ItemStack furnitureItem, PlayerInteractEvent interactEvent) {
		Location placementLocation = interactEvent.getClickedBlock().getLocation().add(0, 1, 0);
		
		ArmorStand stand = interactEvent.getPlayer().getWorld().spawn(placementLocation, ArmorStand.class);
		stand.setVisible(false);
		stand.setInvulnerable(true);
		stand.setGravity(false);
		stand.getEquipment().setItemInMainHand(furnitureItem);
		
		interactEvent.getPlayer().sendMessage(String.format("%s» %sFurniture placed", ChatColor.GOLD, ChatColor.GRAY));
	}
	
	public static void rotateFurniture(PlayerInteractEntityEvent interactEvent) {
		Entity armorStand = interactEvent.getRightClicked();
		
		armorStand.setRotation(armorStand.getLocation().getYaw() + 90, armorStand.getLocation().getPitch() + 90);
		interactEvent.getPlayer().sendMessage(String.format("%s» %sFurniture rotated", ChatColor.GOLD, ChatColor.GRAY));
	}
	
	public static void deleteFurniture(EntityDamageByEntityEvent damageEvent) {
		damageEvent.getEntity().remove();
		damageEvent.getDamager().sendMessage(String.format("%s» %sFurniture deleted", ChatColor.GOLD, ChatColor.DARK_RED));
	}
	
	public static boolean isValid (ItemStack item) {
		if (item.getType() != Material.INK_SAC) return false;
		if (!item.getItemMeta().hasCustomModelData()) return false;
		
		return true;
	}
	
	public static boolean isValid (Entity entity) {
		if (!(entity instanceof ArmorStand)) return false;
		
		ItemStack itemInHand = ((ArmorStand)entity).getEquipment().getItemInMainHand();
		
		return itemInHand.getType() == Material.INK_SAC && itemInHand.getItemMeta().hasCustomModelData() ?
				true : false;
	}
}
