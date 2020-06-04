package net.wigoftime.open_komodo.gui;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtinjector.NBTInjector;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.PetsManager;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.etc.ServerScoreBoard;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.Pet;

abstract public class GUIManager {
	
	public static void setUp() {
		
	}
	
	public static void setGuiToPlayer(Player player, int gui) {
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
	
	/*
	 * getEntityGui checks if there's a guiID on entities, as well as if it does, open the gui.
	 * Returning false means it doesn't exist.
	 */
	public static boolean getEntityGui(Entity entity, Player interactor)
	{
		if (entity instanceof Player)
			return false;
		
		if (entity instanceof CraftEntity)
			return false;
		
		entity = NBTInjector.patchEntity(entity);
		NBTCompound comp = NBTInjector.getNbtData(entity);
		
		int guiID = comp.getInteger("GuiID");
		
		if (guiID == 0)
			return false;
		
		setGuiToPlayer(interactor, guiID);
		
		return true;
	}
	
	public static void invItemClicked(InventoryClickEvent e) 
	{
		
		if (e.getCurrentItem() == null)
			return;
		
		ItemStack is = e.getCurrentItem();
		
		InventoryView inventoryView = e.getView();
		String inventoryName = inventoryView.getTitle();
		Player player = (Player) e.getWhoClicked();
		
		if (inventoryName.equalsIgnoreCase(PhoneGui.titleName)) {
			e.setCancelled(true);
			PhoneGui.clicked(player, e.getCurrentItem());
			return;
		}
		
		if (inventoryName.equals(Warps.guiName)) {
			e.setCancelled(true);
			Warps.clicked(player, e.getCurrentItem());
		}
		
		if (inventoryName.equals(PropShop.title))
		{
			e.setCancelled(true);
			
			NBTItem nbti = new NBTItem(is);
			int id = nbti.getInteger("CustomModelData");
			
			if (id < 1)
				return;
			
			CustomItem cs = CustomItem.getCustomItem(id);
			
			if (cs == null)
				return;
			
			if (cs.getPointPrice() < 0 && cs.getCoinPrice() < 0)
				return;
			
			if (cs.getPointPrice() < 0)
				return;
			
			BuyConfirm.create(player, cs, Currency.POINTS);
		}
		
		if (inventoryName.equalsIgnoreCase(TagMenu.title)) 
		{
			e.setCancelled(true);
			
			if (e.getCurrentItem().getType() == Material.NAME_TAG) 
			{
				// Change nametag
				PlayerConfig.changeCurrentTag(player, e.getCurrentItem().getItemMeta().getDisplayName());
				
				// Refresh scoreboard
				ServerScoreBoard.add(player);
				
				player.closeInventory();
				return;
			}
			
			if (e.getCurrentItem().getType() == Material.WHITE_WOOL) 
			{
				PlayerConfig.changeCurrentTag(player, "");
				
				// Refresh scoreboard
				ServerScoreBoard.add(player);
				
				player.closeInventory();
				return;
			}
			
			if (e.getCurrentItem().getItemMeta().getDisplayName().equals(TagMenu.shop))
			{
				TagShop.open(player);
			}
			
			if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Next")) 
			{
				TagMenu.open((Player) player, true); 
			}
			
			if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Back")) 
			{
				TagMenu.open((Player) player, false); 
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
				player.closeInventory();
			
			return;
		}
		
		if (inventoryName.equals(PetsGui.title))
		{
			e.setCancelled(true);
			
			ItemMeta meta = is.getItemMeta();
			int id = meta.getCustomModelData();
			
			Pet pet = Pet.getPet(id);
			
			if (PlayerConfig.containsPet(player, pet))
			{
				PetsManager.create(player, pet);
				return;
			}
			
			BuyConfirm.create(player, pet, Currency.POINTS);
		}
		
		if (inventoryName.equals(PetControl.title))
		{
			
			ItemMeta meta = is.getItemMeta();
			PrintConsole.test("test #002" + meta.getDisplayName());
			
			if (meta.getCustomModelData() == PetControl.cancelID)
				PetsManager.removePet(player);
			
			if (meta.getCustomModelData() == PetControl.changeNameID)
				PetsManager.setAwaitingNameInput(player);
			
			if (meta.getCustomModelData() == PetControl.mountID)
				PetsManager.mount(player);
			
			
			player.closeInventory();
			e.setCancelled(true);
			return;
		}
		
		if (inventoryName.equals(FriendsList.title)) {
			e.setCancelled(true);
			
			if (e.getCurrentItem().getType() == FriendsList.nextButtonMaterial && e.getCurrentItem().getItemMeta().getDisplayName().equals(FriendsList.nextButtonLabel)) {
				FriendsList.open(player, (byte) 1);
				return;
			}
			
			if (e.getCurrentItem().getType() == FriendsList.backButtonMaterial && e.getCurrentItem().getItemMeta().getDisplayName().equals(FriendsList.backButtonLabel)) {
				FriendsList.open(player, (byte) 2);
				return;
			}
		}
		
		if (inventoryName.equalsIgnoreCase("Tag Shop")) {
			e.setCancelled(true);
			
			if (e.getCurrentItem().getType() == Material.NAME_TAG) {
				ItemMeta im = e.getCurrentItem().getItemMeta();
				int id = im.getCustomModelData();
				
				BuyConfirm.create(player, CustomItem.getCustomItem(id), Currency.POINTS);
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
			
			if (e.getCurrentItem().getItemMeta().getCustomModelData() != 1)
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
			
			if (e.getCursor().getItemMeta().getCustomModelData() != 1)
				return;
			
			return;
		}
	}
	
}
