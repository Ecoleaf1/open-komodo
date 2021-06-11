package net.wigoftime.open_komodo.actions;

import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.PrintConsole;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class BugReporter 
{
	public static final int id = 1;
	
	private static final String errorMsg = ChatColor.translateAlternateColorCodes('&', "&4Cannot report when holding an item in ur hand");
	
	private static final String name = ChatColor.translateAlternateColorCodes('&', "&lReport Book");
	private static final File folder = new File(Main.dataFolderPath+"/reports");
	
	// Give player the report book to report things on
	public static void open(@NotNull Player player)
	{
		PlayerInventory inventory = player.getInventory();
		
		// Create book, and get info about it
		ItemStack reportBook = new ItemStack(Material.WRITABLE_BOOK);
		ItemMeta meta = reportBook.getItemMeta();
		
		// Change the display name
		meta.setDisplayName(name);
		// Change the book ID to make the report book unique
		meta.setCustomModelData(id);
		
		// Save Changes
		reportBook.setItemMeta(meta);
		
		// Get item in hand
		ItemStack item = inventory.getItemInMainHand();
		
		// Add report book to player's hand
		if (item.getType() == Material.AIR)
			player.getInventory().setItemInMainHand(reportBook);
		else
			player.sendMessage(errorMsg);
	}
	
	public static void complete(@NotNull Player player, @NotNull BookMeta meta)
	{
		if (!folder.exists())
			folder.mkdir();
		
		File report = new File(folder.toString()+"/"+meta.getTitle()+" "+meta.getAuthor()+".txt");
		
		
		try
		{
			
			if (!report.exists())
				report.createNewFile();
			
			FileWriter fw = new FileWriter(report);
			BufferedWriter writer = new BufferedWriter(fw);
			
			List<String> text = meta.getPages();
			
			for (String s : text)
			{
				writer.write(s);
			}
			
			String endOfText = "\nWrote by: "+meta.getAuthor();
			writer.write(endOfText);
			
			writer.close();
			fw.close();
			
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e&lReport sent! Thank you for your feedback!"));
		}
		catch (IOException e)
		{
			PrintConsole.test("IOExeception for reporting! Here's Stacktrace:");
			e.printStackTrace();
		}
	}
	
}
