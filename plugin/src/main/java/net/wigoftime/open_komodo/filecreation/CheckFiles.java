package net.wigoftime.open_komodo.filecreation;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.chat.Emote;
import net.wigoftime.open_komodo.config.*;
import net.wigoftime.open_komodo.etc.Filter;
import net.wigoftime.open_komodo.etc.systems.RankSystem;
import net.wigoftime.open_komodo.gui.*;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomParticle;
import net.wigoftime.open_komodo.objects.ItemType;
import net.wigoftime.open_komodo.sql.SQLCard;
import net.wigoftime.open_komodo.sql.SQLManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;

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
		SQLCard.setup();
		
		FileCreation.create(ConfigType.ITEM);
		ItemConfig.setup();
		
		ItemStack is = new ItemStack(Material.STICK);
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Backpack");
		is.setItemMeta(meta);
		new CustomItem(is, 999, meta.getDisplayName(), null, true, 1050, null, ItemType.PROP);
		
		FileCreation.create(ConfigType.RANK);
		RankConfig.setup();
		
		FileCreation.create(ConfigType.EMOTE);
		Emote.setup();
		
		FileCreation.create(ConfigType.PET);
		PetConfig.setup();
		
		PlayerSettingsConfig.setup();
		
		FileCreation.create(ConfigType.WHITELIST_DICT);
		Filter.setup();
		
		FileCreation.create(ConfigType.PROPSHOP_DEFAULT);
		FileCreation.create(ConfigType.HATSHOP_DEFAULT);
		
		PlayerConfig.setupFolder();
		
		PhoneGui.setUp();
		Warps.setup();
		TagMenu.setup();
		TagShop.setup();
		RankSystem.setup();
		CustomParticle.setup();
		ParticlesGUI.setup();
		
		if (SQLManager.isEnabled())
			SQLManager.setup();
	}
	
}
