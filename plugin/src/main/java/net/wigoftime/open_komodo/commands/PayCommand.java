package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.CurrencyClass;
import net.wigoftime.open_komodo.etc.ServerScoreBoard;

public class PayCommand extends Command
{
	public PayCommand(String name, String description, String usageMessage,
			List<String> aliases) 
	{
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) 
	{
		// Don't Continue if isn't player
		if (!(sender instanceof Player))
			return false;
		
		// Get sender in player format
		Player player = (Player) sender;
		
		// cancel if there's less than 4 sub-commands
		if (args.length < 3)
		{
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eUsage: /pay {Player} {Amount} {Currency}"));
			return false;
		}
		
		// Get sub-command targetting a player
		String targetStr = args[0];
		
		// Get sub-command targetting amount
		String amountStr = args[1];
		
		// Get Sub-command targetting Currency Type
		String currencyStr = args[2];
		
		// Get Target
		Player target = Bukkit.getPlayer(targetStr);
		
		// If target doesn't exist, cancel
		if (target == null)
			return false;
		
		// Get Amount
		int amount;
		
		// Try convert amount String to Integer
		try
		{
			amount = Integer.parseInt(amountStr);
		}
		catch (NumberFormatException ee)
		{
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Unknown amount. Use digit numbers"));
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
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Unknown Currency Type. Usage: Points/Coins"));
			return false;
		}
		
		// Pay player
		CurrencyClass.pay(player, target, amount, currency);
		
		// Update Sender's info side bored
		ServerScoreBoard.add(player);
		
		if (target != player)
			ServerScoreBoard.add(target);
		
		return true;
	}
}
