package net.wigoftime.open_komodo.filecreation;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.chat.Emote;
import net.wigoftime.open_komodo.config.ConfigType;
import net.wigoftime.open_komodo.config.ItemConfig;
import net.wigoftime.open_komodo.config.PetConfig;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.config.RankConfig;
import net.wigoftime.open_komodo.etc.Filter;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.etc.RankSystem;
import net.wigoftime.open_komodo.gui.BuyOptionsGUI;
import net.wigoftime.open_komodo.gui.PhoneGui;
import net.wigoftime.open_komodo.gui.PropShop;
import net.wigoftime.open_komodo.gui.Warps;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.ItemType;

abstract public class CheckFiles 
{
	
	private static final String dataFolderPath = Main.dataFolderPath;
	
	public static void checkFiles() {
		
		File folder = new File(dataFolderPath);
		
		// If the plugin folder doesn't exist, create the folder
		if (!folder.exists()) {
			folder.mkdir();
		}

		FileCreation.create(ConfigType.NORMAL);
		
		FileCreation.create(ConfigType.ITEM);
		ItemConfig.setup();
		
		ItemStack is = new ItemStack(Material.STICK);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Backpack");
		is.setItemMeta(meta);
		new CustomItem(is, 999, meta.getDisplayName(), null, true, 1050, null, ItemType.PROP);
		
		FileCreation.create(ConfigType.RANK);
		RankConfig.setup();
		
		PrintConsole.test("c4");
		FileCreation.create(ConfigType.EMOTE);
		
		FileCreation.create(ConfigType.PET);
		PetConfig.setup();
		
		FileCreation.create(ConfigType.WHITELIST_DICT);
		Filter.setup();
		
		FileCreation.create(ConfigType.PROPSHOP_DEFAULT);
		PropShop.setup();
		
		/*
		PrintConsole.test("c5");
		TagConfig.create(); */
		
		PrintConsole.test("c6");
		PlayerConfig.createFolder();
		
		PrintConsole.test("c7");
		Emote.setup();
		//LegacyItemConfig.setUp();
		
		PrintConsole.test("c8");
		BuyOptionsGUI.setup();
		
		PrintConsole.test("c11");
		PhoneGui.setUp();
		
		PrintConsole.test("c12");
		Warps.setup();
		
		RankSystem.setup();
	}
	
}
