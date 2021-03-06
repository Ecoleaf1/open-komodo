package net.wigoftime.open_komodo.events;

import net.wigoftime.open_komodo.etc.CurrencyClass;
import net.wigoftime.open_komodo.etc.FurnitureMangement;
import net.wigoftime.open_komodo.etc.InventoryManagement;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.gui.CustomGUI;
import net.wigoftime.open_komodo.gui.PhoneGui;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.ItemType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class PlayerInteract implements EventExecutor {
	
	@Override
	public void execute(Listener listener, Event event) throws EventException {
		PlayerInteractEvent playerInteractEvent = (PlayerInteractEvent) event;
		CustomPlayer player = CustomPlayer.get(playerInteractEvent.getPlayer().getUniqueId());
		
		// If customplayer format of it doesn't exist, cancel
		// Can happen if the server hasn't loaded all of the player information when they join
		if (player == null) {
			playerInteractEvent.setCancelled(true);
			return;
		}
		
		if (player.getPlayer().getTargetBlock((Set<Material>) null, 20).getType() == Material.FIRE) {
			if (!player.getPlayer().hasPermission(Permissions.changePerm)){
				player.getPlayer().sendMessage(Permissions.getChangeError());
				playerInteractEvent.setCancelled(true);
			}
			
			return;
		}
		
		ItemStack item = playerInteractEvent.getItem();
		actionManage(playerInteractEvent, player, item);
	}
	
	private static boolean specialItem(@NotNull ItemStack itemStack, @NotNull CustomPlayer playerCustom, @NotNull PlayerInteractEvent event) {
		switch(itemStack.getType()) {
		case INK_SAC:
			if (playerCustom.hasActiveGui()) return false;
			
			CustomItem itemCustom = CustomItem.getCustomItem(itemStack.getItemMeta().getCustomModelData());
			if (itemCustom == null) return false;
			
			if (itemCustom.getType() == ItemType.PHONE) {
				CustomGUI gui = new PhoneGui(playerCustom);
				gui.open();
			} else if (itemCustom.getItem().getItemMeta().getCustomModelData() == 56) {
				CurrencyClass.displayBalance(playerCustom);
			} else if (isItemEquipable(itemStack)) {
				playerCustom.getPlayer().getInventory().setItemInMainHand(null);
				playerCustom.setHat(itemStack);
			}
			event.setCancelled(true);
			return true;
		case STICK:
			InventoryManagement.openBagInventory(playerCustom, itemStack.getItemMeta().getCustomModelData());
			event.setCancelled(true);
			return true;
		default:
			return false;
		}
	}
	
	private void actionManage(@NotNull PlayerInteractEvent event, @NotNull CustomPlayer playerCustom, @Nullable ItemStack itemStack) {
		switch(event.getAction()) {
		case PHYSICAL:
			if (event.getClickedBlock().getType() == Material.FARMLAND) event.setCancelled(true);
			break;
		case RIGHT_CLICK_BLOCK:
			Block clickedBlock = event.getClickedBlock();
			
			
			if (playerCustom.getPlayer().hasPermission(Permissions.changePerm))
			if (!playerCustom.isBuilding()) {
			if (clickedBlock.getType() == Material.OAK_TRAPDOOR || clickedBlock.getType() == Material.SPRUCE_TRAPDOOR || clickedBlock.getType() == Material.JUNGLE_TRAPDOOR || 
					clickedBlock.getType() == Material.DARK_OAK_TRAPDOOR || clickedBlock.getType() == Material.BIRCH_TRAPDOOR || clickedBlock.getType() == Material.ACACIA_TRAPDOOR || 
					clickedBlock.getType() == Material.DISPENSER || clickedBlock.getType() == Material.DROPPER) {	
				event.setCancelled(true);
				break;
			} else if (clickedBlock.getType() == Material.FLOWER_POT || clickedBlock.getType().name().startsWith("POTTED")) {
				playerCustom.getPlayer().sendMessage(Permissions.getChangeError());
				event.setCancelled(true);
				break;
			} else if (clickedBlock.getType().name().endsWith("SHULKER_BOX")) {
				event.setCancelled(true);
				break;
			} else if (clickedBlock.getType() == Material.ENDER_CHEST) {
				event.setCancelled(true);
				playerCustom.getPlayer().sendMessage(Permissions.useError);
				break;
			}
			} else {
				if (itemStack == null) break;

				if (!FurnitureMangement.isValid(itemStack)) break;

				FurnitureMangement.createFurniture(itemStack, event);
				event.setCancelled(true);
				break;
			}

			if (itemStack == null) return;
			if (!(itemStack.hasItemMeta())) return;
			if (!(itemStack.getItemMeta().hasCustomModelData())) return;
			
			if (specialItem(itemStack, playerCustom, event)) {
				event.setCancelled(true);
				break;
			}
			break;
		case RIGHT_CLICK_AIR:
			if (!specialItem(itemStack, playerCustom, event)) break;
			event.setCancelled(true);
			break;
		}
		
	}
	
	
	private static boolean isItemEquipable(@NotNull ItemStack itemStack) {
		if (itemStack.getType() != Material.INK_SAC) return false;
		
		ItemMeta meta = itemStack.getItemMeta();
		
		if (!(meta.hasCustomModelData())) return false;
		
		CustomItem itemCustom = CustomItem.getCustomItem(meta.getCustomModelData());
		if (itemCustom == null) return false;
				
		return itemCustom.isEquipable() ? true : false;
	}
}
