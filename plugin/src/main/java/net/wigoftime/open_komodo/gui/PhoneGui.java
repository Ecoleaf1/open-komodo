package net.wigoftime.open_komodo.gui;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.wigoftime.open_komodo.objects.CustomPlayer;

public class PhoneGui extends CustomGUI {
	
// Variables
	
	public static final String titleName = ChatColor.translateAlternateColorCodes('&', "&e&lEPhone");
	
//	All the icon variables.

	private static ItemStack props;
	
	private static ItemStack pets;
	
	private static ItemStack hats;
	
	private static ItemStack tags;
	
	private static ItemStack bin;
	
	private static ItemStack warps;
	
	private static ItemStack phoneShop;
	
	private static ItemStack particlesMenu;
	
	private static ItemStack settings;
	
// Borders and panels ItemStacks
	
	private static ItemStack leftBorder;
	
	private static ItemStack rightBorder;
	
	private static ItemStack backgroundPannel;
	
//	Social media section background setup
	
	private static ItemStack website;

	private static ItemStack discord;
	
	// Sky background
	private static ItemStack sky;
	
	private static ArrayList<ItemStack> icons = new ArrayList<ItemStack>(5);
	
	
//	Functions
	
	public static void setUp() {
		
		props = new ItemStack(Material.INK_SAC); {
			ItemMeta meta = props.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&d&lProps"));
			meta.setCustomModelData(-14);
			props.setItemMeta(meta);
		}
		
		hats = new ItemStack(Material.INK_SAC); {
			ItemMeta meta = hats.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&lHats"));
			meta.setLore(Arrays.asList(String.format("%sVIP", ChatColor.YELLOW)));
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
		
		phoneShop = new ItemStack(Material.INK_SAC); {
			ItemMeta meta = phoneShop.getItemMeta();
			meta.setDisplayName(String.format("%s%sPhone Shop", ChatColor.WHITE, ChatColor.BOLD));
			meta.setLore(Arrays.asList(String.format("%sVIP+", ChatColor.YELLOW)));
			meta.setCustomModelData(-18);
			phoneShop.setItemMeta(meta);
		}
		
		particlesMenu = new ItemStack(Material.INK_SAC); {
			ItemMeta meta = particlesMenu.getItemMeta();
			meta.setDisplayName(String.format("%s%sParticles", ChatColor.LIGHT_PURPLE, ChatColor.BOLD));
			meta.setLore(Arrays.asList(String.format("%sMVP", ChatColor.AQUA)));
			meta.setCustomModelData(-19);
			particlesMenu.setItemMeta(meta);
		}
		
		pets = new ItemStack(Material.INK_SAC); {
			ItemMeta meta = pets.getItemMeta();
			meta.setDisplayName(String.format("%s%sPets", ChatColor.YELLOW, ChatColor.BOLD));
			meta.setLore(Arrays.asList(String.format("%sMVP", ChatColor.AQUA)));
			meta.setCustomModelData(-20);
			pets.setItemMeta(meta);
		}
		
		settings = new ItemStack(Material.INK_SAC); {
			ItemMeta meta = settings.getItemMeta();
			meta.setDisplayName(String.format("%s%sSettings", ChatColor.GRAY, ChatColor.BOLD));
			meta.setCustomModelData(-21);
			settings.setItemMeta(meta);
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
		
		leftBorder = new ItemStack(Material.INK_SAC);{
			ItemMeta meta = leftBorder.getItemMeta();
			meta.setDisplayName(" ");
			meta.setCustomModelData(-10);
			leftBorder.setItemMeta(meta);
		}
		
		rightBorder = new ItemStack(Material.INK_SAC); {
			ItemMeta meta = rightBorder.getItemMeta();
			meta.setDisplayName(" ");
			meta.setCustomModelData(-10);
			rightBorder.setItemMeta(meta);
		}
		
		backgroundPannel = new ItemStack(Material.INK_SAC); {
			ItemMeta meta = backgroundPannel.getItemMeta();
			meta.setDisplayName(" ");
			meta.setCustomModelData(-9);
			backgroundPannel.setItemMeta(meta);
			
		}
		
//		Social media section background setup
		
			// Bottom of the sky
		sky = new ItemStack(Material.INK_SAC); {
			ItemMeta meta = sky.getItemMeta();
			meta.setDisplayName(" ");
			meta.setCustomModelData(-15);
			sky.setItemMeta(meta);
		}
		
		// List of icons that will be displayed.
		
		icons.add(props);
		icons.add(hats);
		icons.add(tags);
		icons.add(warps);
		icons.add(phoneShop);
		icons.add(particlesMenu);
		icons.add(pets);
		icons.add(settings);
	}
	
	public void clicked(InventoryClickEvent clickEvent) {
		clickEvent.setCancelled(true);
		ItemStack clickedItem = clickEvent.getCurrentItem();
		
		if (!clickedItem.getItemMeta().hasCustomModelData())
			return;
		
		// Get ID
		int clickedItemID = clickedItem.getItemMeta().getCustomModelData();
		
		// If clicked on TagMenu Icon
		if (clickedItemID == tags.getItemMeta().getCustomModelData()) {
			CustomGUI gui = new TagMenu(opener);
			gui.open();
			return;
		}
		
		// If clicked on Hats Icon
		if (clickedItemID == hats.getItemMeta().getCustomModelData()) {
			HatMenu gui = new HatMenu(opener);
			gui.open();
			return;
		}
		
		// If clicked on Props Icon
		if (clickedItemID == props.getItemMeta().getCustomModelData()) {
			CustomGUI gui = new PropShop(opener);
			gui.open();
			//PropShop.open(opener);
			return;
		}
		
		// If clicked on warps Icon
		if (clickedItemID == warps.getItemMeta().getCustomModelData()) {
			Warps gui = new Warps(opener);
			gui.open();
			return;
		}
		
		// If clicked on phone shop Icon
		if (clickedItemID == phoneShop.getItemMeta().getCustomModelData()) {
			PhoneSwitcher.open(opener);
			return;
		}
		
		// If clicked on Particles Menu
		if (clickedItemID == particlesMenu.getItemMeta().getCustomModelData()) {
			CustomGUI gui = new ParticlesGUI(opener);
			gui.open();
			return;
		}
		
		if (clickedItem.equals(pets)) {
			CustomGUI gui = new PetsGui(opener);
			gui.open();
			return;
		}
		
		if (clickedItem.equals(settings)) {
			CustomGUI gui = new SettingsGui(opener);
			gui.open();
			return;
		}
		
		// If clicked on Bin Icon
		if (clickedItemID == bin.getItemMeta().getCustomModelData()) {
			BinGUI gui = new BinGUI(opener);
			gui.open();
			return;
		}
		
		// If clicked on Website Icon
		if (clickedItemID == website.getItemMeta().getCustomModelData()) {
			opener.getPlayer().closeInventory();
			opener.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bWebsite link: https://www.wigoftime.net"));
			return;
		}
		
		// If clicked on Discord Icon
		if (clickedItemID == discord.getItemMeta().getCustomModelData()) {
			opener.getPlayer().closeInventory();
			opener.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Discord link: https://discord.gg/nUJM7dT"));
			return;
		}
	}
	
	public PhoneGui(CustomPlayer player) {
		super(player, null, Bukkit.createInventory(null, 54, titleName));
		
		// Get Background
		gui.setContents(getBackground(gui.getSize()));
		
		// Get Icons
		ItemStack[] icons = getIcons(gui.getSize());
		for (byte index = 0; index < icons.length; index++)
			if (icons[index] != null)
				gui.setItem(index, icons[index]);
		
		return;
	}
	
	private static ItemStack[] getIcons(int size) {
		ItemStack[] content = new ItemStack[size];
		
		int slotIndex = 11;
		
		// Display icons
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
			
			content[slotIndex] = i;
			slotIndex++;
		}
		
		// Display side Icons
		content[8] = website;
		content[17] =  discord;
		content[39] = bin; 
		
		return content;
	}
	
	private static ItemStack[] getBackground(int size) {
		ItemStack[] contents = new ItemStack[size];
		// Setup the border and the backgroundPannels
		
		contents[0] = leftBorder;
		
		contents[1] = backgroundPannel;
		contents[2] = backgroundPannel;
		contents[3] = backgroundPannel;
		contents[1] = backgroundPannel;
		contents[4] = backgroundPannel;
		contents[5] = backgroundPannel;
	
		contents[6] = rightBorder;
		contents[9] = leftBorder;
		
		contents[10] = backgroundPannel;
		contents[11] = backgroundPannel;
		contents[12] = backgroundPannel;
		contents[13] = backgroundPannel;
		contents[14] = backgroundPannel;
		
		contents[15] = rightBorder;
		contents[18] = leftBorder;
		
		contents[19] = backgroundPannel;
		contents[20] = backgroundPannel;
		contents[21] = backgroundPannel;
		contents[22] = backgroundPannel;
		contents[23] = backgroundPannel;
		
		contents[24] = rightBorder;
		contents[27] = leftBorder;
		
		contents[28] = backgroundPannel;
		contents[29] = backgroundPannel;
		contents[30] = backgroundPannel;
		contents[31] = backgroundPannel;
		contents[32] = backgroundPannel;
		
		contents[33] = rightBorder;
		contents[36] = leftBorder;
		
		contents[37] = backgroundPannel;
		contents[38] = backgroundPannel;
		contents[39] = backgroundPannel;
		contents[40] = backgroundPannel;
		contents[41] = backgroundPannel;
		
		contents[42] = rightBorder;
		contents[45] = leftBorder;
		
		contents[46] = backgroundPannel;
		contents[47] = backgroundPannel;
		contents[48] = backgroundPannel;
		contents[49] = backgroundPannel;
		contents[50] = backgroundPannel;
		
		contents[51] = rightBorder;
		
		// Social media background
		
		contents[7] = sky;
		contents[8] = sky;
		contents[16] = sky;
		contents[17] = sky;
		contents[25] = sky;
		contents[26] = sky;
		contents[34] = sky;
		contents[35] = sky;
		contents[43] = sky;
		contents[44] = sky;
		contents[52] = sky;
		contents[53] = sky;
		
		return contents;
	}
	
}
