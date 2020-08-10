package net.wigoftime.open_komodo.commands;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class MuteCommand extends Command {
	final String playerNotFound = String.format("%s» %sPlayer not found", ChatColor.GOLD, ChatColor.DARK_RED);
	
	public MuteCommand(String name, String description, String usageMessage,
			List<String> aliases) 
	{
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) 
	{
		if (!sender.hasPermission(Permissions.mutePerm)) {
			sender.sendMessage(String.format("%s» %sYou are not permitted to use that command", ChatColor.GOLD, ChatColor.DARK_RED));
			return true;
		}
		
		if (args.length < 2)
		{
			sender.sendMessage(String.format("%s» %sUsage: %s", ChatColor.GOLD, ChatColor.GRAY, this.getUsage()));
			return true;
		}
		
		// Get player
		Player player = Bukkit.getPlayerExact(args[0]);
		
		// If player is null, don't continue
		if (player == null)
		{
			sender.sendMessage(playerNotFound);
			return true;
		}
		
		if (player.hasPermission(Permissions.mutePerm))
		{
			for (Player p : Bukkit.getOnlinePlayers())
			{
				if (p.hasPermission(Permissions.abuseMonitorPerm))
					p.sendMessage(String.format("%s tried to mute another mod %s", sender.getName(), player.getDisplayName()));
			}
			
			return true;
		}
		
		Instant instant = Instant.now();
		
		// 1 is minutes,
		// 2 is hours, 
		// 3 is days,
		// 4 is weeks,
		// 5 is months,
		// 6 is years,
		byte amountType = 3;
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < args[1].length(); i++)
		{
			// Get character
			char c = args[1].charAt(i);
			
			PrintConsole.test("-1");
			// If character is a number
			if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9')
			{
				sb.append(c);
			}
			
			if (c == 'n')
				amountType = 1;
			else if (c == 'h')
				amountType = 2;
			else if (c == 'd')
				amountType = 3;
			else if (c == 'w')
				amountType = 4;
			else if (c == 'm')
				amountType = 5;
			else if (c == 'y')
				amountType = 6;
			
			PrintConsole.test("0");
			if (i + 1 >= args[1].length())
			{
				Integer num = Integer.parseInt(sb.toString());
				
				if (amountType == 1)
					instant = instant.plus(Duration.ofMinutes(num));
				
				if (amountType == 2)
					instant = instant.plus(Duration.ofHours(num));
				
				if (amountType == 3)
					instant = instant.plus(Duration.ofDays(num));
					
				if (amountType == 4)
				{
					// Days to weeks
					num = num * 7;
					
					// Add
					instant = instant.plus(Duration.ofDays(num));
				}
				
				if (amountType == 5)
				{
					// Days to months
					num = num * 29;
					
					instant = instant.plus(Duration.ofDays(num));
				}
				
				if (amountType == 6)
				{
					// Days to years
					num = num * 365;
					instant = instant.plus(Duration.ofDays(num));
				}
				
				break;
			}
			
		}
		
		CustomPlayer customPlayer = CustomPlayer.get(player.getUniqueId());
		// Set mute date
		customPlayer.setMuteDate(Date.from(instant));
		
		if (args.length < 3)
			return true;
		
		StringBuilder sb2 = new StringBuilder();
		
		for (int i = 2; i < args.length; i++)
		{
			sb2.append(args[i] + " ");
		}
		
		customPlayer.setMuteReason(sb2.toString());
		return true;
	}

}
