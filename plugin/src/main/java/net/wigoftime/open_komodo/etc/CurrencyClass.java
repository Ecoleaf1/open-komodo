package net.wigoftime.open_komodo.etc;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.chat.MessageFormat;
import net.wigoftime.open_komodo.config.WorldInventoryConfig;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Pet;
import net.wigoftime.open_komodo.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public abstract class CurrencyClass 
{
	private static String notEnoughPoints = String.format("%s%sSorry%s, but you do not have enough points", ChatColor.RED, ChatColor.BOLD, ChatColor.GRAY);
	private static String notEnoughCoins = String.format("%s%sSorry%s, but you do not have enough coins", ChatColor.RED, ChatColor.BOLD, ChatColor.GRAY);
	private static @NotNull String balanceMessage = "&6»&7 Your account balance:\n    - &6$C&7 coins\n    - &6$T&7 points";
	
	public static boolean buy(@NotNull CustomPlayer playerCustomPlayer, int amount, @NotNull Currency currency, Pet pet) {
		if (!takeOutFromBalance(playerCustomPlayer, amount, currency))
		return false;
		
		playerCustomPlayer.addPet(pet);
		return true;
	}
	
	public static boolean buy(@NotNull CustomPlayer playerCustomPlayer, int amount, @NotNull Currency currency, @NotNull CustomItem customItem) {
		if (!takeOutFromBalance(playerCustomPlayer, amount, currency)) return false;
		
		switch (customItem.getType()) {
		case PROP:
			ItemStack boughtItem =  new ItemStack(customItem.getItem());
			
			ItemMeta meta = customItem.getItem().getItemMeta().clone();
			if (boughtItem.getType() == Material.STICK) {
				int bagID;
				if (SQLManager.isEnabled())
				bagID = SQLManager.createBagInventory(playerCustomPlayer.getUniqueId());
				else bagID = WorldInventoryConfig.createBagInventory(playerCustomPlayer.getPlayer());
				meta.setCustomModelData(bagID);
				boughtItem.setItemMeta(meta);
			}
			else {
				meta.setCustomModelData(meta.getCustomModelData());
				boughtItem.setItemMeta(meta);
			}
			playerCustomPlayer.getPlayer().getInventory().addItem(boughtItem);
			break;
		case TAG:
			playerCustomPlayer.addItem(customItem);
			break;
		case HAT:
			playerCustomPlayer.addItem(customItem);
			break;
		case PHONE:
			playerCustomPlayer.addItem(customItem);
			playerCustomPlayer.setActivePhone(customItem);
			break;
		}
		
		ServerScoreBoard.add(playerCustomPlayer);
		InventoryManagement.saveInventory(playerCustomPlayer, playerCustomPlayer.getPlayer().getWorld());
		return true;
	}
	
	public static boolean takeOutFromBalance(@NotNull CustomPlayer playerCustomPlayer, int amount, @NotNull Currency currency) {
		int balance = playerCustomPlayer.getCurrency(currency);
		int cost = amount;
		
		int remaining = balance - cost;
		
		if (remaining < 0) {
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
	
	public static void genPay(@NotNull CustomPlayer playerCustomPlayer, int amount , @NotNull Currency currency) {
		int remaining = playerCustomPlayer.getCurrency(currency);
		
		playerCustomPlayer.setCurrency(amount + remaining, currency);
		
		Bukkit.getScheduler().runTask(Main.getPlugin(), new Runnable() {
			public void run() {
				ServerScoreBoard.add(playerCustomPlayer);
			}
		});
	}
	
	public static void pay(@NotNull CustomPlayer givercustomPlayer, @NotNull CustomPlayer receiverCustomPlayer, int amount, @NotNull Currency currency) {
		if (amount < 1) {
			givercustomPlayer.getPlayer().sendMessage(String.format("%s» %sSorry, but please choose a higher number", ChatColor.GOLD, ChatColor.GRAY));
			return;
		}
		
		int remaining = receiverCustomPlayer.getCurrency(currency);
		
		Bukkit.getScheduler().runTask(Main.getPlugin(), new Runnable() {
			public void run() {
				if (!takeOutFromBalance(givercustomPlayer, amount, currency))
				return;
				
				receiverCustomPlayer.setCurrency(remaining + amount, currency);
				
				String senderMessage = String.format("%s» %sSucessfully transfered %d %s to %s",
						ChatColor.GOLD, ChatColor.GRAY, amount, currency == Currency.POINTS ? "points" : "coins",
						receiverCustomPlayer.getPlayer().getDisplayName());
				
				givercustomPlayer.getPlayer().sendMessage(senderMessage);
				
				String receiverMessage = String.format("%s» %sYou have just got paid %d %s from %s!", 
					ChatColor.GOLD, ChatColor.GRAY, amount, currency == Currency.POINTS ? "points" : "coins",
					givercustomPlayer.getPlayer().getDisplayName());
				receiverMessage = MessageFormat.format(receiverMessage, givercustomPlayer, receiverCustomPlayer, null);
				receiverMessage = receiverMessage.replaceFirst("$C", amount+"");
				receiverMessage = receiverMessage.replaceFirst("$T", currency == Currency.POINTS ? "Points" : "Coins");
				
				receiverCustomPlayer.getPlayer().sendMessage(receiverMessage);
				ServerScoreBoard.add(receiverCustomPlayer);
			}
		});
	}
	
	public static void displayBalance(@NotNull CustomPlayer customPlayer) {
		String message = balanceMessage;
		
		message = ChatColor.translateAlternateColorCodes('&', message);
		message = message.replace("$T", customPlayer.getCurrency(Currency.POINTS)+"");
		message = message.replace("$C", customPlayer.getCurrency(Currency.COINS)+"");
		
		if (customPlayer.getPlayer().getCustomName() == null)
		message = message.replace("$P", customPlayer.getPlayer().getDisplayName());
		else message = message.replace("$S", customPlayer.getPlayer().getCustomName());
		
		message = message.replace("$P", customPlayer.getPlayer().getDisplayName());
		customPlayer.getPlayer().sendMessage(message);
	}
	
}
