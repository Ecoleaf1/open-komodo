package net.wigoftime.open_komodo.etc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.chat.MessageFormat;
import net.wigoftime.open_komodo.config.WorldInventoryConfig;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.ItemType;
import net.wigoftime.open_komodo.objects.Pet;
import net.wigoftime.open_komodo.sql.SQLManager;

public abstract class CurrencyClass 
{
	private static String notEnoughPoints = String.format("%s%sSorry%s, but you do not have enough points", ChatColor.RED, ChatColor.BOLD, ChatColor.GRAY);
	//private static String notEnoughPoints = ChatColor.translateAlternateColorCodes('&', "&c&lSorry, but you do not have enough points");
	private static String notEnoughCoins = String.format("%s%sSorry%s, but you do not have enough coins", ChatColor.RED, ChatColor.BOLD, ChatColor.GRAY);
	//private static String notEnoughCoins = ChatColor.translateAlternateColorCodes('&', "&c&lSorry, but you do not have enough coins");
	private static String balanceMessage = "&6»&7 Your account balance:\n    - &6$C&7 coins\n    - &6$T&7 points";
	
	// When buying bet
	public static boolean buy(CustomPlayer playerCustomPlayer, int amount, Currency currency, Pet pet)
	{
		// Try take money out, if doesn't work, skip
		if (!buy(playerCustomPlayer, amount, currency))
			return false;
		
		playerCustomPlayer.addPet(pet);
		return true;
	}
	
	public static boolean buy(CustomPlayer playerCustomPlayer, int amount, Currency currency, CustomItem customItem)
	{
		// Try take money out, if doesn't work, skip
		if (!buy(playerCustomPlayer, amount, currency))
			return false;
		
		// Get ItemStack
		ItemStack is =  new ItemStack(customItem.getItem().getType());
		
		// Get ItemType
		ItemType type = customItem.getType();
		
		// If item is prop
		if (type == ItemType.PROP) {
			ItemMeta meta = customItem.getItem().getItemMeta().clone();
			if (is.getType() == Material.STICK) {
				PrintConsole.test("is stick");
				int bagID;
				if (SQLManager.isEnabled())
					bagID = SQLManager.createBagInventory(playerCustomPlayer.getUniqueId(), playerCustomPlayer.getPlayer().getWorld().getName());
				else
					bagID = WorldInventoryConfig.createBagInventory(playerCustomPlayer.getPlayer());
				
				PrintConsole.test("bagID: "+ bagID);
				meta.setCustomModelData(bagID);
				is.setItemMeta(meta);
			}
			else
			{
				meta.setCustomModelData(meta.getCustomModelData());
				is.setItemMeta(meta);
			}
			
			playerCustomPlayer.getPlayer().getInventory().addItem(is);
		}
		
		// If item is tag
		if (type == ItemType.TAG)
			playerCustomPlayer.addItem(customItem);
		
		// if item is Hat 
		if (type == ItemType.HAT)
			playerCustomPlayer.addItem(customItem);
		
		if (type == ItemType.PHONE) {
			playerCustomPlayer.addItem(customItem);
			playerCustomPlayer.setActivePhone(customItem);
		}
		
		ServerScoreBoard.add(playerCustomPlayer);
		
		// Save player's inventory in case server crashes or something unexpected happened
		InventoryManagement.saveInventory(playerCustomPlayer, playerCustomPlayer.getPlayer().getWorld());
		return true;
	}
	
	public static boolean buy(CustomPlayer playerCustomPlayer, int amount, Currency currency)
	{
		int balance = playerCustomPlayer.getCurrency(currency);
		int cost = amount;
		
		int remaining = balance - cost;
		
		if (remaining < 0)
		{
			if (currency == Currency.POINTS)
				playerCustomPlayer.getPlayer().sendMessage(notEnoughPoints);
			
			if (currency == Currency.COINS)
				playerCustomPlayer.getPlayer().sendMessage(notEnoughCoins);
			
			return false;
		}
		
		playerCustomPlayer.setCurrency(remaining, currency);
		ServerScoreBoard.add(playerCustomPlayer);
		return true;
	}
	
	public static void genPay(CustomPlayer playerCustomPlayer, int amount ,Currency currency) {
		int remaining = playerCustomPlayer.getCurrency(currency);
		
		playerCustomPlayer.setCurrency(amount + remaining, currency);
		
		Bukkit.getScheduler().runTask(Main.getPlugin(), new Runnable() {
			public void run() {
				ServerScoreBoard.add(playerCustomPlayer);
			}
		});
	}
	
	public static void pay(CustomPlayer givercustomPlayer, CustomPlayer receiverCustomPlayer, int amount, Currency currency) {
		if (amount < 1) {
			givercustomPlayer.getPlayer().sendMessage(String.format("%s» %sSorry, but please choose a higher number", ChatColor.GOLD, ChatColor.GRAY));
			return;
		}
		
		Bukkit.getScheduler().runTask(Main.getPlugin(), new Runnable() {
			public void run() {
				// Take money, if not enough, cancel payment
				if (!buy(givercustomPlayer, amount, currency))
					return;
				
				// Get receiver's remaining currency
				int remaining = receiverCustomPlayer.getCurrency(currency);
				// Add money into receiver's account
				receiverCustomPlayer.setCurrency(remaining + amount, currency);
				
				// Send message to giver that it went successful
				String msg = String.format("%s» %sSucessfully transfered %d %s to %s",
						ChatColor.GOLD, ChatColor.GRAY, amount, currency == Currency.POINTS ? "points" : "coins",
								receiverCustomPlayer.getPlayer().getDisplayName());
				//msg = msg.replaceFirst("$C", amount+"");
				//msg = msg.replaceFirst("$T", currency == Currency.POINTS ? "Points" : "Coins");
				//msg = MessageFormat.format(msg, givercustomPlayer, receiverCustomPlayer, null);
				
				// Send message
				givercustomPlayer.getPlayer().sendMessage(msg);
				
				// Send message to receiver to notify
				String msg2 = String.format("%s» %sYou have just got paid %d %s from %s!", 
						ChatColor.GOLD, ChatColor.GRAY, currency == Currency.POINTS ? "points" : "coins",
							givercustomPlayer.getPlayer().getDisplayName());
				msg2 = MessageFormat.format(msg2, givercustomPlayer, receiverCustomPlayer, null);
				msg2 = msg2.replaceFirst("$C", amount+"");
				msg2 = msg2.replaceFirst("$T", currency == Currency.POINTS ? "Points" : "Coins");
				
				// Send message
				receiverCustomPlayer.getPlayer().sendMessage(msg2);
				
				ServerScoreBoard.add(receiverCustomPlayer);
			}
		});
	}
	
	public static void displayBalance(CustomPlayer customPlayer) {
		
		String message = balanceMessage;
		
		message = ChatColor.translateAlternateColorCodes('&', message);
		message = message.replace("$T", customPlayer.getCurrency(Currency.POINTS)+"");
		message = message.replace("$C", customPlayer.getCurrency(Currency.COINS)+"");
		
		if (customPlayer.getPlayer().getCustomName() == null)
			message = message.replace("$P", customPlayer.getPlayer().getDisplayName());
		else
			message = message.replace("$S", customPlayer.getPlayer().getCustomName());
		message = message.replace("$P", customPlayer.getPlayer().getDisplayName());
		
		customPlayer.getPlayer().sendMessage(message);
		
	}
	
}
