package net.wigoftime.open_komodo.etc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.Main;

abstract public class UpdateLog 
{
	private static final String title = ChatColor.translateAlternateColorCodes('&', "&e&lUpdate Log");
	
	private static final File folder = new File(Main.dataFolderPath+"/updateLogCheckup");

	public static void open(Player player)
	{	
		// Add in text to put in book
		List<String> text = new ArrayList<String>();
		
		// Add page texts for Book Gui
		text.add("Update 0.2 log, what's new?");
		
		
		// Get book and info about book
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta meta = (BookMeta) book.getItemMeta();
		
		// Set page texts, the author, etc
		meta.setPages(text);
		meta.setAuthor("");
		meta.setTitle("test");
		
		// Save Changes
		book.setItemMeta(meta);
		
		// Display the text to player
		player.openBook(book);
	}
	
	// When player joins
	public static void onJoin(Player player)
	{
		// If player check folder doesn't exist, create it
		if (!folder.exists())
			folder.mkdir();
		
		// Get UUID from player
		UUID uuid = player.getUniqueId();
		
		// Get file from user to see if they saw the update log already
		File touch = new File(folder.getAbsolutePath()+"/"+uuid.toString());
		
		// If file exists, return
		if (touch.exists())
			return;
		
		// Try create the file to tell that they read it
		try
		{
			touch.createNewFile();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// Show the player the update log
		open(player);
	}
}
