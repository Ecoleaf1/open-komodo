package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.CurrencyClass;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PayCommand extends Command
{
	public PayCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
                      @NotNull List<String> aliases)
	{
		super(name, description, usageMessage, aliases);
	}
	
	private static final String usageCommand = String.format("%s» %sUsage: /pay {Player} {Amount} {Currency}", ChatColor.GOLD, ChatColor.GRAY);

	@Override
	public boolean execute(CommandSender sender, String command, String @NotNull [] args)
	{
		// Don't Continue if isn't player
		if (!(sender instanceof Player))
			return false;
		
		// Get sender in player format
		Player senderPlayer = (Player) sender;
		
		// cancel if there's less than 4 sub-commands
		if (args.length < 3) {
			senderPlayer.sendMessage(usageCommand);
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
		try {
			amount = Integer.parseInt(amountStr);
		} catch (NumberFormatException ee) {
			senderPlayer.sendMessage(String.format("%s» %sUnknown amount. Use digit numbers", ChatColor.GOLD, ChatColor.DARK_RED));
			return false;
		}
		
		// Get Currency
		Currency currency;
		
		if (currencyStr.equalsIgnoreCase("Points"))
			currency = Currency.POINTS;
		else if (currencyStr.equalsIgnoreCase("Coins"))
			currency = Currency.COINS;
		else {
			senderPlayer.sendMessage(String.format("%s» %sUnknown currency type. Use Points or Coins", ChatColor.GOLD, ChatColor.DARK_RED));
			return false;
		}
		
		// Get Sender in customPlayer
		CustomPlayer senderCustomPlayer = CustomPlayer.get(senderPlayer.getUniqueId());
		
		// Pay player
		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				CurrencyClass.pay(senderCustomPlayer, CustomPlayer.get(target.getUniqueId()), amount, currency);
			}
		});
		
		/*
		// Update Sender's info side bored
		ServerScoreBoard.add(senderCustomPlayer);
		
		if (target != senderCustomPlayer.getPlayer())
			ServerScoreBoard.add(senderCustomPlayer); */
		
		return true;
	}
}
