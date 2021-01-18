package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Rank;

public class DisplayXPCommand extends Command {

	public DisplayXPCommand(String name, String description, String usageMessage, List<String> aliases) {
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!(sender instanceof Player))
			return true;
		
		CustomPlayer player = CustomPlayer.get(((Player) sender).getUniqueId());
		Rank nextRank;
		
		if (player.getRank() == null) {
			Rank rank = Rank.getRank(1);
			
			if (rank == null) {
				soloDisplay(player);
				return true;
			}
			
			nextRank = rank;
		} else
			nextRank = Rank.getRank(player.getRank().getID() + 1);
		
		if (nextRank == null) {
			soloDisplay(player);
			return true;
		}
		
		if (nextRank.getXPPrice() <= -1) {
			soloDisplay(player);
			return true;
		}
		
		double currentXP = player.getXP();
		
		nextRankDisplay(player, currentXP, nextRank);
		return true;
	}
	
	private void soloDisplay(CustomPlayer playerCustomPlayer) {
		double currentXP = playerCustomPlayer.getXP();
		playerCustomPlayer.getPlayer().sendMessage(String.format("%s» %sYou currently have %s%f XP!", ChatColor.GOLD, ChatColor.GRAY, ChatColor.GOLD, currentXP, ChatColor.GRAY));
	}
	
	private void nextRankDisplay (CustomPlayer playerCustomPlayer, double currentXP, Rank nextRank) {
		double nextRankXP = nextRank.getXPPrice();
		double xpNeeded = nextRank.getXPPrice() / currentXP;
		
		double percentageNeeded = calculatePercentage(currentXP, nextRankXP);
		
		playerCustomPlayer.getPlayer().sendMessage(String.format("%s» %sCurrent XP: %s%f%s / %s%f%s (%s%f%%)", ChatColor.GOLD, ChatColor.GRAY, 
				ChatColor.GOLD, currentXP, ChatColor.GRAY, 
				ChatColor.GOLD, nextRankXP, ChatColor.GRAY, 
				ChatColor.GOLD, percentageNeeded, ChatColor.GRAY));
	}
	
    public double calculatePercentage(double obtained, double total) {
        return obtained * 100 / total;
    }
}
