package net.wigoftime.open_komodo.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.wigoftime.open_komodo.objects.CustomPlayer;

public class PhoneGui 
{
	
// Variables
	
	public static final String titleName = ChatColor.translateAlternateColorCodes('&', "&e&lEPhone");
	
//	All the icon variables.

	private static ItemStack props;
	
	private static ItemStack pets;
	
	private static ItemStack hats;
	
	private static ItemStack tags;
	
	private static ItemStack bin;
	
	private static ItemStack warps;
	
// Borders and panels ItemStacks
	
	private static ItemStack leftBorder;
	
	private static ItemStack rightBorder;
	
	private static ItemStack backgroundPannel;
	
//	Social media section background setup
	
	private static ItemStack website;

	private static ItemStack discord;
	
	// Sky background
	private static ItemStack sky;
	
	private static ArrayList<ItemStack> icons = new ArrayList<ItemStack>(12);
	
	
//	Functions
	
	public static void setUp() {
		
		props = new ItemStack(Material.INK_SAC); 
		{
			ItemMeta meta = props.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&d&lProps"));
			meta.setCustomModelData(-14);
			props.setItemMeta(meta);
		}
		
		pets = new ItemStack(Material.BONE); {
			ItemMeta meta = pets.getItemMeta();
			meta.setDisplayName(" ");
			pets.setItemMeta(meta);
		}
		
		hats = new ItemStack(Material.INK_SAC); {
			ItemMeta meta = hats.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lHats"));
			meta.setCustomModelData(-13);
			hats.setItemMeta(meta);
		}
		
		tags = new ItemStack(Material.INK_SAC); {
			ItemMeta meta = tags.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lTags"));
			meta.setCustomModelData(-12);
			tags.setItemMeta(meta);
		}
		
		bin = new ItemStack(Material.INK_SAC); 
		{
			ItemMeta meta = bin.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7&lBin"));
			meta.setCustomModelData(-11);
			bin.setItemMeta(meta);
		}
		
		warps = new ItemStack(Material.INK_SAC); {
			ItemMeta meta = warps.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lWarps"));
			meta.setCustomModelData(-8);
			warps.setItemMeta(meta);
		}
		
		website = new ItemStack(Material.INK_SAC); {
			ItemMeta meta = warps.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a&lWebsite"));
			meta.setCustomModelData(-17);
			website.setItemMeta(meta);
		}
		
		discord = new ItemStack(Material.INK_SAC); {
			ItemMeta meta = warps.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f&lDiscord"));
			meta.setCustomModelData(-16);
			discord.setItemMeta(meta);
		}
		
	// Borders and panels ItemStacks
		
		leftBorder = new ItemStack(Material.INK_SAC);
		{
			ItemMeta meta = leftBorder.getItemMeta();
			meta.setDisplayName(" ");
			meta.setCustomModelData(-10);
			leftBorder.setItemMeta(meta);
		}
		
		rightBorder = new ItemStack(Material.INK_SAC); 
		{
			ItemMeta meta = rightBorder.getItemMeta();
			meta.setDisplayName(" ");
			meta.setCustomModelData(-10);
			rightBorder.setItemMeta(meta);
		}
		
		backgroundPannel = new ItemStack(Material.INK_SAC); 
		{
			ItemMeta meta = backgroundPannel.getItemMeta();
			meta.setDisplayName(" ");
			meta.setCustomModelData(-9);
			backgroundPannel.setItemMeta(meta);
			
		}
		
//		Social media section background setup
		
			// Bottom of the sky
		sky = new ItemStack(Material.INK_SAC); 
		{
			ItemMeta meta = sky.getItemMeta();
			meta.setDisplayName(" ");
			meta.setCustomModelData(-15);
			sky.setItemMeta(meta);
		}
		
		// List of icons that will be displayed.
		
		icons.add(props);
		//icons.add(particles);
		//icons.add(pets);
		icons.add(hats);
		//icons.add(clothes);
		//icons.add(help);
		icons.add(tags);
		//icons.add(music);
		//icons.add(updates);
		//icons.add(friends);
		icons.add(warps);
	}
	
	protected static void clicked(CustomPlayer player, ItemStack item) 
	{
		
		if (!item.getItemMeta().hasCustomModelData())
			return;
		
		if (item.getItemMeta().getCustomModelData() == tags.getItemMeta().getCustomModelData()) 
		{
			TagMenu.open(player, true);
			return;
		}
		
		if (item.getItemMeta().getCustomModelData() == hats.getItemMeta().getCustomModelData()) 
		{
			HatMenu.open(player);
			return;
		}
		
		if (item.getItemMeta().getCustomModelData() == props.getItemMeta().getCustomModelData()) 
		{
			PropShop.open(player);
			return;
		}
		
		if (item.getItemMeta().getCustomModelData() == warps.getItemMeta().getCustomModelData()) 
		{
			Warps.open(player);
			return;
		}
		
		if (item.getItemMeta().getCustomModelData() == bin.getItemMeta().getCustomModelData()) 
		{
			BinGUI.open(player);
			return;
		}
		
		if (item.getItemMeta().getCustomModelData() == website.getItemMeta().getCustomModelData()) 
		{
			player.getPlayer().closeInventory();
			player.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bWebsite link: https://www.wigoftime.net"));
			return;
		}
		
		if (item.getItemMeta().getCustomModelData() == discord.getItemMeta().getCustomModelData()) 
		{
			player.getPlayer().closeInventory();
			player.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Discord link: https://discord.gg/nUJM7dT"));
			return;
		}
	}
	
	public static void open(CustomPlayer player) 
	{
		if (player.isBuilding())
		{
			player.getPlayer().sendMessage(CustomPlayer.buildingError);
			return;
		}
		
		// The Gui
		Inventory gui = Bukkit.createInventory(null, 54, titleName); 
		{
			// Setup the border and the backgroundPannels
			
			gui.setItem(0,leftBorder);
			
			gui.setItem(1,backgroundPannel);
			gui.setItem(2,backgroundPannel);
			gui.setItem(3,backgroundPannel);
			gui.setItem(1,backgroundPannel);
			gui.setItem(4,backgroundPannel);
			gui.setItem(5,backgroundPannel);
		
			gui.setItem(6, rightBorder);
			gui.setItem(9, leftBorder);
			
			gui.setItem(10, backgroundPannel);
			gui.setItem(11, backgroundPannel);
			gui.setItem(12, backgroundPannel);
			gui.setItem(13, backgroundPannel);
			gui.setItem(14, backgroundPannel);
			
			gui.setItem(15, rightBorder);
			gui.setItem(18, leftBorder);
			
			gui.setItem(19, backgroundPannel);
			gui.setItem(20, backgroundPannel);
			gui.setItem(21, backgroundPannel);
			gui.setItem(22, backgroundPannel);
			gui.setItem(23, backgroundPannel);
			
			gui.setItem(24, rightBorder);
			gui.setItem(27, leftBorder);
			
			gui.setItem(28, backgroundPannel);
			gui.setItem(29, backgroundPannel);
			gui.setItem(30, backgroundPannel);
			gui.setItem(31, backgroundPannel);
			gui.setItem(32, backgroundPannel);
			
			gui.setItem(33, rightBorder);
			gui.setItem(36, leftBorder);
			
			gui.setItem(37, backgroundPannel);
			gui.setItem(38, backgroundPannel);
			gui.setItem(39, backgroundPannel);
			gui.setItem(40, backgroundPannel);
			gui.setItem(41, backgroundPannel);
			
			gui.setItem(42, rightBorder);
			gui.setItem(45, leftBorder);
			
			gui.setItem(46, backgroundPannel);
			gui.setItem(47, backgroundPannel);
			gui.setItem(48, backgroundPannel);
			gui.setItem(49, backgroundPannel);
			gui.setItem(50, backgroundPannel);
			
			gui.setItem(51, rightBorder);
			
			// Social media background
			
			gui.setItem(7, sky);
			gui.setItem(8, website);
			gui.setItem(16, sky);
			gui.setItem(17, discord);
			gui.setItem(25, sky);
			gui.setItem(26, sky);
			gui.setItem(34, sky);
			gui.setItem(35, sky);
			gui.setItem(43, sky);
			gui.setItem(44, sky);
			gui.setItem(52, sky);
			gui.setItem(53, sky);
			
			// Display those icons
			
			int slotIndex = 11;
			for (ItemStack i : icons) {
			if (slotIndex == 14)
					slotIndex = 20;
				else if (slotIndex == 23)
					slotIndex = 29;
				else if (slotIndex == 32)
					slotIndex = 38;
				else if (slotIndex == 39)
					slotIndex = 40;
				else if (slotIndex > 40)
					break;
				
				gui.setItem(slotIndex, i);
				slotIndex++;
			}
			
			gui.setItem(39, bin); 
		};

		player.getPlayer().openInventory(gui);
	}
	
}
