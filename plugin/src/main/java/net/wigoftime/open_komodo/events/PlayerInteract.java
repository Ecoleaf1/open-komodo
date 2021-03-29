package net.wigoftime.open_komodo.events;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.EventExecutor;

import net.wigoftime.open_komodo.etc.CurrencyClass;
import net.wigoftime.open_komodo.etc.FurnitureMangement;
import net.wigoftime.open_komodo.etc.InventoryManagement;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.gui.CustomGUI;
import net.wigoftime.open_komodo.gui.PhoneGui;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.ItemType;

public class PlayerInteract implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		PlayerInteractEvent playerInteractEvent = (PlayerInteractEvent) event;
		
		if (playerInteractEvent.getAction() == Action.PHYSICAL)
		{
			if (playerInteractEvent.getClickedBlock().getType() == Material.FARMLAND)
				playerInteractEvent.setCancelled(true);
			
			return;
		}
		
		// Get player in CustomPlayer format
		CustomPlayer player = CustomPlayer.get(playerInteractEvent.getPlayer().getUniqueId());
		
		// If customplayer format of it doesn't exist, cancel
		// Can happen if the server hasn't loaded all of the player information when they join
		if (player == null)
		{
			playerInteractEvent.setCancelled(true);
			return;
		}
		
		// Get Action
		Action action = playerInteractEvent.getAction();
		// Get Item
		ItemStack item = playerInteractEvent.getItem();
			
		// If not left and right click on block, cancel it.
		// This is to prevent players quickly moving their cursor out right
		// when they left click on the fire to put it out.
		if (action != Action.LEFT_CLICK_BLOCK && action != Action.RIGHT_CLICK_BLOCK)
			playerInteractEvent.setCancelled(true);
		
			// If clicked on fire
			if (player.getPlayer().getTargetBlock((Set<Material>) null, 20).getType() == Material.FIRE)
			{
				// If player does not have permission, cancel and return
				if (!player.getPlayer().hasPermission(Permissions.changePerm))
				{
					player.getPlayer().sendMessage(Permissions.getChangeError());
					
					playerInteractEvent.setCancelled(true);
				}
				
				return;
			}
		
		// If right clicked on block
		if (action == Action.RIGHT_CLICK_BLOCK)
		{
			// Get block
			Block block = playerInteractEvent.getClickedBlock();
			// Get Material
			Material material = block.getType();
			
			if (material == Material.OAK_TRAPDOOR || material == Material.SPRUCE_TRAPDOOR || material == Material.JUNGLE_TRAPDOOR || 
					material == Material.DARK_OAK_TRAPDOOR || material == Material.BIRCH_TRAPDOOR || material == Material.ACACIA_TRAPDOOR || 
					material == Material.DISPENSER || material == Material.DROPPER) {	
				playerInteractEvent.setCancelled(true);
				return;
			}
			
			if (material == Material.FLOWER_POT || material.name().startsWith("POTTED"))
			{
				player.getPlayer().sendMessage(Permissions.getChangeError());
				playerInteractEvent.setCancelled(true);
				return;
			}
			
			if (material.name().endsWith("SHULKER_BOX"))
			{
				playerInteractEvent.setCancelled(true);
				return;
			}
			
			// If Enderchest
			if (block.getType() == Material.ENDER_CHEST)
			{
				// Allow open if player has permission
				if (player.getPlayer().hasPermission(Permissions.changePerm))
					return;
				
				if (player.isBuilding())
					return;
				
				// Other wise, cancel it
				playerInteractEvent.setCancelled(true);
				player.getPlayer().sendMessage(Permissions.useError);
				return;
			}
		}
		
		if (item == null)
			return;
		
		if (action == Action.RIGHT_CLICK_BLOCK)
		if (player.getPlayer().hasPermission(Permissions.changePerm))
			if (player.isBuilding()) {
				if (FurnitureMangement.isValid(item)) {
					playerInteractEvent.setCancelled(true);
					FurnitureMangement.createFurniture(item, playerInteractEvent);
				}
				
				return;
			}
		
		if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
		{
			if (item.getType() == Material.INK_SAC)
			{
				ItemMeta meta = item.getItemMeta();
				if (meta.hasCustomModelData())
				{
					CustomItem cs = CustomItem.getCustomItem(meta.getCustomModelData());
					
					if (cs == null)
						return;
					
					if (cs.isEquipable())
					{
						player.getPlayer().getInventory().setHelmet(item);
						item.setAmount(0);
					}
				}
			}
		}
		
		// Checks if Item could have an ID
		if (item.getItemMeta().hasCustomModelData())
		{
			// If ID is 1 (IPlay Phone)
			if (item.getType() == Material.INK_SAC && !player.hasActiveGui())
			{
				// And if the item is an ink sac, open IPlay
				CustomItem customItem = CustomItem.getCustomItem(item.getItemMeta().getCustomModelData());
				
				if (customItem == null)
					return;
				
				if (customItem.getType() == ItemType.PHONE) {	
					playerInteractEvent.setCancelled(true);
					CustomGUI gui = new PhoneGui(player);
					gui.open();
					//PhoneGui.open(player);
					return;
				}
				
				if (item.getItemMeta().getCustomModelData() == 56)
				{
					playerInteractEvent.setCancelled(true);
					CurrencyClass.displayBalance(player);
					return;
				}
			}
			
			if (item.getType() == Material.STICK)
			{
				InventoryManagement.openBagInventory(player, item.getItemMeta().getCustomModelData());
			}
		}
	}

}
