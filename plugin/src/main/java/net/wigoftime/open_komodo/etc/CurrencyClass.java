package net.wigoftime.open_komodo.etc;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.wigoftime.open_komodo.chat.MessageFormat;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.config.WorldInventoryConfig;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.ItemType;
import net.wigoftime.open_komodo.objects.Pet;

public abstract class CurrencyClass 
{
	
	private static String notEnoughPoints = ChatColor.translateAlternateColorCodes('&', "&c&lSorry, but you do not have enough points");
	private static String notEnoughCoins = ChatColor.translateAlternateColorCodes('&', "&c&lSorry, but you do not have enough coins");
	private static String balanceMessage = "Your account balance:\n    - &6$C&r coins\n    - &6$T&r points";
	
	public static int getPoints(Player player) {
		
		File config = PlayerConfig.getPlayerConfig(player);
		YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(config);
		
		final int points = configYaml.getConfigurationSection("Currency").getInt("Points");
		
		return points;
	}
	
	public static int getCoins(Player player) {
		File config = PlayerConfig.getPlayerConfig(player);
		YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(config);
		
		final int coins = configYaml.getConfigurationSection("Currency").getInt("Coins");
		
		return coins;
	}
	
	// When buying bet
	public static boolean buy(Player player, int amount, Currency currency, Pet pet)
	{
		// Try take money out, if doesn't work, skip
		if (!buy(player, amount, currency))
			return false;
			
		PlayerConfig.addPet(player, pet);
		return true;
	}
	
	public static boolean buy(Player player, int amount, Currency currency, CustomItem customItem)
	{
		// Try take money out, if doesn't work, skip
		if (!buy(player, amount, currency))
			return false;
		
		// Get ItemStack
		ItemStack is =  new ItemStack(customItem.getItem());
		
		// Get ItemType
		ItemType type = customItem.getType();
		
		// If item is prop
		if (type == ItemType.PROP)
		{
			ItemMeta meta = is.getItemMeta();
			if (is.getType() == Material.STICK)
			{
				meta.setCustomModelData(WorldInventoryConfig.getInventoryIndex(player));
				is.setItemMeta(meta);
			}
			
			player.getInventory().addItem(is);
		}
		
		// If item is tag
		if (type == ItemType.TAG)
			PlayerConfig.addItem(player, customItem);
		
		// if item is Hat 
		if (type == ItemType.HAT)
			PlayerConfig.addItem(player, customItem);
		
		ServerScoreBoard.add(player);
		
		// Save player's inventory in case server crashes or something unexpected happened
		InventoryManagement.saveInventory(player, player.getWorld());
		return true;
	}
	
	private static boolean buy(Player player, int amount, Currency currency)
	{
		int balance = PlayerConfig.getCurrency(player, currency);
		int cost = amount;
		
		int remaining = balance - cost;
		
		if (remaining < 0)
		{
			if (currency == Currency.POINTS)
				player.sendMessage(notEnoughPoints);
			
			if (currency == Currency.COINS)
			player.sendMessage(notEnoughCoins);
			
			return false;
		}
		
		PlayerConfig.setCurrency(player, remaining, currency);
		ServerScoreBoard.add(player);
		return true;
	}
	
	public static void genPay(Player player, int amount ,Currency currency)
	{
		int remaining = PlayerConfig.getCurrency(player, currency);
		
		PlayerConfig.setCurrency(player, amount + remaining, currency);
		ServerScoreBoard.add(player);
	}
	
	public static void pay(Player giver, Player receiver, int amount, Currency currency)
	{
		// Take money, if not enough, cancel payment
		if (!buy(giver, amount, currency))
			return;
		
		// Get receiver's remaining currency
		int remaining = PlayerConfig.getCurrency(receiver, currency);
		// Add money into receiver's account
		PlayerConfig.setCurrency(receiver, remaining + amount, currency);
		
		// Send message to giver that it went successful
		String msg = ChatColor.translateAlternateColorCodes('&', "&e&lSucessfully transfered $C $T to $D&e&l!");
		msg = msg.replaceFirst("$C", amount+"");
		msg = msg.replaceFirst("$T", currency == Currency.POINTS ? "Points" : "Coins");
		msg = MessageFormat.format(msg, giver, receiver, null);
		
		// Send message
		giver.sendMessage(msg);
		
		// Send message to receiver to notify
		String msg2 = ChatColor.translateAlternateColorCodes('&', "&e&l$D&e&l gave u $C $T!");
		msg2 = MessageFormat.format(msg2, giver, receiver, null);
		msg2 = msg2.replaceFirst("$C", amount+"");
		msg2 = msg2.replaceFirst("$T", currency == Currency.POINTS ? "Points" : "Coins");
		
		// Send message
		giver.sendMessage(msg2);
	}
	
	/*
	public static boolean buyWithPoints(Player player, int amount) {
		File config = PlayerConfig.getPlayerConfig(player);
		YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(config);
		
		int points = configYaml.getConfigurationSection("Currency").getInt("Points");
		
		// If User don't have enough
		if (amount > points) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', notEnoughPoints));
			return false;
		}
		
		points = points - amount;
		configYaml.getConfigurationSection("Currency").set("Points", points);
			
		// Try save the charge amount to the player's file.
		try {
			configYaml.save(config);
			return true;
		} catch(IOException e) {
			PrintConsole.print("ERROR: Can't charge user's points; can't save the charge amount.");
			
			return false;
		}
		
	}
	
	public static boolean buyWithCoins(Player player, int amount) {
		File config = PlayerConfig.getPlayerConfig(player);
		YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(config);
		
		int coins = configYaml.getConfigurationSection("Currency").getInt("Coins");
		
		// If User don't have enough
		if (amount > coins) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', notEnoughCoins));
			return false;
		}
		
			coins = coins - amount;
			configYaml.getConfigurationSection("Currency").set("Coins", coins);
			// Try save the charge amount to the player's file.
			try {
				configYaml.save(config);
			} catch(IOException e) {
				PrintConsole.print("ERROR: Can't charge user's coins; can't save the charge amount.");
				
				return false;
			}
			
			return true;
		
	} */
	
	public static void displayBalance(Player player) {
		
		String message = balanceMessage;
		
		message = ChatColor.translateAlternateColorCodes('&', message);
		message = message.replace("$T", getPoints(player)+"");
		message = message.replace("$C", getCoins(player)+"");
		
		if (player.getCustomName() == null)
			message = message.replace("$P", player.getDisplayName());
		else
			message = message.replace("$S", player.getCustomName());
		message = message.replace("$P", player.getDisplayName());
		
		player.sendMessage(message);
		
	}
	
}
