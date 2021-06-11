package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.CurrencyClass;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GenPayCommand extends Command
{

	public GenPayCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
                         @NotNull List<String> aliases) {
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(@NotNull CommandSender sender, String command, String @NotNull [] args) {
		// Check if Player has permission
		if (!sender.hasPermission(Permissions.genPayPerm)) {
			sender.sendMessage(Permissions.useError);
			return false;
		}
		
		// cancel if there's less than 4 sub-commands
		if (args.length < 3) {
			sender.sendMessage(String.format("%s» %sUsage: /pay {Player} {Amount} {Currency}", ChatColor.GOLD, ChatColor.GRAY));
			return false;
		}
		
		// Get command arguments of target player's username, the amount to be given,
		// and currency type to be paid in.
		String targetStr = args[0];
		String amountStr = args[1];
		String currencyStr = args[2];
		
		// Get sender in player format
		Player playerSender = Bukkit.getPlayer(targetStr);
		
		if (playerSender == null)
			return false;
		
		// Get sender in CustomPlayer format
		CustomPlayer customPlayerSender = CustomPlayer.get(playerSender.getUniqueId());
		
		// Get Amount
		int amount;
		
		// Try convert amount String to Integer
		try {
			amount = Integer.parseInt(amountStr);
		}
		catch (NumberFormatException exception) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Unknown amount. Use digit numbers"));
			return false;
		}
		
		// Get Currency
		Currency currency;
		
		if (currencyStr.equalsIgnoreCase("Points"))
			currency = Currency.POINTS;
		else if (currencyStr.equalsIgnoreCase("Coins"))
			currency = Currency.COINS;
		else
		{
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Unknown Currency Type. Usage: Points/Coins"));
			return false;
		}
		
		// Generate money to player
		CurrencyClass.genPay(customPlayerSender, amount, currency);
		
		return true;
	}

}
