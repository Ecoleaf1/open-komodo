package net.wigoftime.open_komodo.commands;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.etc.Moderation;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.sql.SQLManager;

public class BanCommand extends Command
{
	private final String playerNotFound = ChatColor.translateAlternateColorCodes('&', "Player not found");

	public BanCommand(String name, String description, String usageMessage,
			List<String> aliases) 
	{
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String command, String[] args) 
	{
		if (!sender.hasPermission(Permissions.banPerm))
		{
			sender.sendMessage(ChatColor.DARK_RED + "You are not permitted to use that command.");
			return false;
		}
		
		if (args.length < 2)
		{
			sender.sendMessage(this.usageMessage);
			return false;
		}
		
		OfflinePlayer target = Bukkit.getPlayer(args[0]);
		if (target == null)
		{
			target = Bukkit.getOfflinePlayer(args[0]);
			
			if (target == null)
			{
				sender.sendMessage(playerNotFound);
			}
		}
		
		//if (PlayerConfig.getPlayerConfig(target) != null)
		//{
		List<Permission> perms; 
		if (SQLManager.isEnabled())
		perms = SQLManager.getGlobalPermissions(target.getUniqueId());
		else
		perms = PlayerConfig.getGlobalPermissions(target.getUniqueId());
		
		for (Permission permission : perms)
		{
			if (permission.getName().equals(Permissions.abuseMonitorPerm.getName()))
			{
				for (Player p : Bukkit.getOnlinePlayers())
				{
					if (p.hasPermission(Permissions.abuseMonitorPerm))
						p.sendMessage(String.format("%s tried to mute another mod %s", sender.getName(), target.getName()));
				}
				
				return false;
			}
		}
		//}
		
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
		// After loop
		
		// Create reference variables to referense on potentially different threads
		final UUID refUUID = target.getUniqueId();
		final Instant refInstant = instant;
		
		if (args.length > 2)
		{
			StringBuilder sb2 = new StringBuilder();
			for (int i = 2; i < args.length; i++)
			{
				sb2.append(args[i]+" ");
			}
			
			Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
				public void run() {
					Moderation.ban(refUUID, Date.from(refInstant), sb2.toString());
				}
			});
			Moderation.ban(target.getUniqueId(), Date.from(instant), sb2.toString());
		}
		else
			Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
				public void run() {
					Moderation.ban(refUUID, Date.from(refInstant), null);
				}
			});
		
		return true;
	}

}
