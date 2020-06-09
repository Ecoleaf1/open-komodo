package net.wigoftime.open_komodo.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.config.WorldInventoryConfig;
import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.InventoryManagement;
import net.wigoftime.open_komodo.etc.PetsManager;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.etc.ServerScoreBoard;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Pet;

abstract public class GUIManager {
	
	public static void setUp() {
		
	}
	
	public static void setGuiToPlayer(CustomPlayer player, int gui) {
		if (gui == 1)
			PrintConsole.print("Not added in yet #0002.");
			//HatMenu.open(player);
		
		if (gui == -1)
			PropShop.open(player);
	}
	
	public static int getGuiID(String gui)
	{
		gui = gui.toLowerCase();
		int id;
		switch(gui)
		{
			case "hatmenu":
				id = 1;
			break;
			
			default:
				id = 0;
			break;
		}
		
		return id;
	}
	
	public static void invItemClicked(InventoryClickEvent e) 
	{
		// If couldn't get item, return
		if (e.getCurrentItem() == null)
			return;
		
		ItemStack is = e.getCurrentItem();
		
		InventoryView inventoryView = e.getView();
		String inventoryName = inventoryView.getTitle();
		CustomPlayer player = CustomPlayer.get(e.getWhoClicked().getUniqueId());
		
		// If in phone gui
		if (inventoryName.equalsIgnoreCase(PhoneGui.titleName)) {
			e.setCancelled(true);
			PhoneGui.clicked(player, e.getCurrentItem());
			return;
		}
		
		if (is.getType() == Material.STICK)
			if (InventoryManagement.currentOpen.containsKey(player.getUniqueId()))
			{
				int bagID = InventoryManagement.currentOpen.get(player.getUniqueId());
				
				if (is.getItemMeta().getCustomModelData() == bagID)
				{
					e.setCancelled(true);
					return;
				}
			}
		
		// If in warps gui
		if (inventoryName.equals(Warps.guiName)) {
			e.setCancelled(true);
			Warps.clicked(player.getPlayer(), e.getCurrentItem());
		}
		
		if (inventoryName.equals(PropShop.title))
		{
			e.setCancelled(true);
			
			int id;
			if (is.hasItemMeta())
				id = is.getItemMeta().getCustomModelData();
			else
				id = 0;
			
			PrintConsole.test("ID: " + id);
			
			if (id < 1)
				return;
			
			CustomItem cs = CustomItem.getCustomItem(id);
			
			if (cs == null)
				return;
			
			if (cs.getPointPrice() < 0 && cs.getCoinPrice() < 0)
				return;
			
			if (cs.getPointPrice() < 0)
				return;
			
			BuyConfirm.create(player.getPlayer(), cs, Currency.POINTS);
		}
		
		if (inventoryName.equalsIgnoreCase(TagMenu.title)) 
		{
			e.setCancelled(true);
			
			if (e.getCurrentItem().getType() == Material.NAME_TAG) 
			{
				// Change nametag
				PlayerConfig.changeCurrentTag(player.getPlayer(), e.getCurrentItem().getItemMeta().getDisplayName());
				
				// Refresh scoreboard
				ServerScoreBoard.add(player.getPlayer());
				
				player.getPlayer().closeInventory();
				return;
			}
			
			if (e.getCurrentItem().getType() == Material.WHITE_WOOL) 
			{
				PlayerConfig.changeCurrentTag(player.getPlayer(), "");
				
				// Refresh scoreboard
				ServerScoreBoard.add(player.getPlayer());
				
				player.getPlayer().closeInventory();
				return;
			}
			
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(TagMenu.shop))
			{
				TagShop.open(player);
			}
			
			if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Next")) 
			{
				TagMenu.open(player, true); 
			}
			
			if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Back")) 
			{
				TagMenu.open(player, false); 
			}
			return;
			
		}

		if (inventoryName.equalsIgnoreCase("Hat-Menu"))
		{
			HatMenu.clicked((Player) e.getWhoClicked(), e.getCurrentItem());
			e.setCancelled(true);
		}
		
		if (inventoryName.equalsIgnoreCase(BuyConfirm.title)) 
		{
			e.setCancelled(true);
			
			ItemStack confirmItem = e.getCurrentItem();
			
			if (confirmItem.getType() == Material.LIME_WOOL)
				BuyConfirm.buy(e.getClickedInventory());
			if (confirmItem.getType() == Material.RED_WOOL)
				player.getPlayer().closeInventory();
			
			return;
		}
		
		if (inventoryName.equals(PetsGui.title))
		{
			e.setCancelled(true);
			
			ItemMeta meta = is.getItemMeta();
			int id = meta.getCustomModelData();
			
			Pet pet = Pet.getPet(id);
			
			if (PlayerConfig.containsPet(player.getPlayer(), pet))
			{
				PetsManager.create(player.getPlayer(), pet);
				return;
			}
			
			BuyConfirm.create(player, pet, Currency.POINTS);
		}
		
		if (inventoryName.equals(PetControl.title))
		{
			
			ItemMeta meta = is.getItemMeta();
			
			if (meta.getCustomModelData() == PetControl.cancelID)
				PetsManager.removePet(player.getPlayer());
			
			if (meta.getCustomModelData() == PetControl.changeNameID)
				PetsManager.setAwaitingNameInput(player.getPlayer());
			
			if (meta.getCustomModelData() == PetControl.mountID)
				PetsManager.mount(player.getPlayer());
			
			
			player.getPlayer().closeInventory();
			e.setCancelled(true);
			return;
		}
		
		if (inventoryName.equals(FriendsList.title)) {
			e.setCancelled(true);
			
			if (e.getCurrentItem().getType() == FriendsList.nextButtonMaterial && e.getCurrentItem().getItemMeta().getDisplayName().equals(FriendsList.nextButtonLabel)) {
				FriendsList.open(player.getPlayer(), (byte) 1);
				return;
			}
			
			if (e.getCurrentItem().getType() == FriendsList.backButtonMaterial && e.getCurrentItem().getItemMeta().getDisplayName().equals(FriendsList.backButtonLabel)) {
				FriendsList.open(player.getPlayer(), (byte) 2);
				return;
			}
		}
		
		if (inventoryName.equalsIgnoreCase("Tag Shop")) {
			e.setCancelled(true);
			
			if (e.getCurrentItem().getType() == Material.NAME_TAG) {
				ItemMeta im = e.getCurrentItem().getItemMeta();
				int id = im.getCustomModelData();
				
				BuyConfirm.create(player.getPlayer(), CustomItem.getCustomItem(id), Currency.POINTS);
			}
			
			if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Next"))
				TagShop.open(player);
			
			if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Back"))
				TagShop.open(player);
			
			return;
		}
		
		if (e.getCurrentItem().getType() == Material.STONE_HOE) {
			e.setCancelled(true);
			return;
		}

		if (e.getCurrentItem().getType() == Material.INK_SAC) 
		{	
			if (!e.getCurrentItem().getItemMeta().hasCustomModelData())
				return;
			
			if (e.getSlot() == 39)
				e.setCancelled(true);
			
			if (e.getCurrentItem().getItemMeta().getCustomModelData() != 1 && e.getCurrentItem().getItemMeta().getCustomModelData() != 56)
				return;
			
			e.setCancelled(true);
			return;
		}
		
		if (e.getCurrentItem().getType() == Material.FLINT) {
			e.setCancelled(true);
			return;
		}
		
		if (e.getCursor().getType() == Material.STONE_HOE) {
			e.setCancelled(true);
			return;
		}
		
		if (e.getCursor().getType() == Material.FLINT) {
			e.setCancelled(true);
			return;
		}
		
		if (e.getCursor().getType() == Material.INK_SAC) 
		{
			if (e.getCursor().getItemMeta() == null)
				return;
			
			if (!e.getCursor().getItemMeta().hasCustomModelData())
				return;
			
			if (e.getCursor().getItemMeta().getCustomModelData() != 1 && e.getCursor().getItemMeta().getCustomModelData() != 56)
				return;
			
			return;
		}
	}
	
	public static void inventoryClosed(InventoryCloseEvent e)
	{	
		CustomPlayer player = CustomPlayer.get(e.getPlayer().getUniqueId());
		
		InventoryManagement.saveInventory(player, player.getPlayer().getWorld());
	}
	
}
