package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.etc.connectfour.ConnectFourSession;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class ConnectFourCommand extends Command {

	public ConnectFourCommand(String name, String description, String usageMessage, List<String> aliases) {
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		if (!(sender instanceof CommandSender)) return true;
		
		if (args.length < 1) {
			sender.sendMessage(String.format("%s» %s%s", ChatColor.GOLD, ChatColor.GRAY, this.usageMessage));
			return true;
		}
		
		if (args[0].equalsIgnoreCase("help")) {
			help(sender);
			return true;
		} 
		
		CustomPlayer playerSender = CustomPlayer.get(((Player) sender).getUniqueId());
		
		if (args[0].equalsIgnoreCase("request")) {
			request(playerSender, args);
			return true;
		}
		
		if (args[0].equalsIgnoreCase("accept")) {
			ConnectFourSession.acceptRequest(playerSender);
			return true;
		}
		
		return true;
	}
	
	private void help(CommandSender sender) {
		sender.sendMessage(String.format("%s» %sHow to play connect4:", ChatColor.GOLD, ChatColor.GRAY));
		sender.sendMessage(String.format("%sConnect a pattern of 4 tokens to win, while preventing your opponent from winning", ChatColor.GRAY));
		sender.sendMessage("");
		sender.sendMessage(String.format("%sCommands:", ChatColor.GRAY));
		sender.sendMessage(String.format("%s/connect4 request {Player} : Request a player to play connect4", ChatColor.GRAY));
		sender.sendMessage(String.format("%s/connect4 accept : Accept a connect4 match request", ChatColor.GRAY));
	}
	
	private void request(CustomPlayer sender, String[] args) {
		if (args.length < 2) return;
		
		final Player playerBukkit = Bukkit.getPlayer(args[1]);
		if (playerBukkit == null) {
			sender.getPlayer().sendMessage(String.format("%s» %sPlayer not found", ChatColor.GOLD, ChatColor.DARK_RED));
			return;
		}
		
		CustomPlayer player2 = CustomPlayer.get(playerBukkit.getUniqueId());
		
		if (sender == player2) {
			sender.getPlayer().sendMessage(String.format("%s» %sSorry, but you can not play against with yourself", ChatColor.GOLD, ChatColor.DARK_RED));
			return;
		}
		
		ConnectFourSession.requestToPlay(sender, player2);;
		return;
	}
}
