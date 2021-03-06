package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Rank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DisplayXPCommand extends Command {

	public DisplayXPCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!(sender instanceof Player))
			return true;
		
		CustomPlayer player = CustomPlayer.get(((Player) sender).getUniqueId());
		Rank nextRank;
		
		if (player == null) return true;
		
		if (player.isInTutorial())
		if (!player.getTutorial().validState(DisplayXPCommand.class)) return true;
		
		if (player.getRank() == null) {
			Rank rank = Rank.getRank(1);
			
			if (rank == null) {
				soloDisplay(player);
				
				if (player.isInTutorial()) player.getTutorial().trigger(DisplayXPCommand.class);
				return true;
			}
			
			nextRank = rank;
		} else
			nextRank = Rank.getRank(player.getRank().getID() + 1);
		
		if (nextRank == null) {
			soloDisplay(player);
			
			if (player.isInTutorial()) player.getTutorial().trigger(DisplayXPCommand.class);
			return true;
		}
		
		if (nextRank.getXPPrice() <= -1) {
			soloDisplay(player);
			
			if (player.isInTutorial()) player.getTutorial().trigger(DisplayXPCommand.class);
			return true;
		}
		
		double currentXP = player.getXP();
		
		nextRankDisplay(player, currentXP, nextRank);
		
		if (player.isInTutorial()) player.getTutorial().trigger(DisplayXPCommand.class);
		return true;
	}
	
	private void soloDisplay(@NotNull CustomPlayer playerCustomPlayer) {
		double currentXP = playerCustomPlayer.getXP();
		playerCustomPlayer.getPlayer().sendMessage(String.format("%s» %sYou currently have %s%f XP!", ChatColor.GOLD, ChatColor.GRAY, ChatColor.GOLD, currentXP, ChatColor.GRAY));
	}
	
	private void nextRankDisplay (@NotNull CustomPlayer playerCustomPlayer, double currentXP, @NotNull Rank nextRank) {
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
