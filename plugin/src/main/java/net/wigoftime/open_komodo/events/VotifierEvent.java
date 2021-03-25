package net.wigoftime.open_komodo.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

import com.vexsoftware.votifier.model.Vote;

import github.scarsz.discordsrv.DiscordSRV;
import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.chat.NormalMessage;
import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.CurrencyClass;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class VotifierEvent implements EventExecutor {
	public void execute(Listener listener, Event event) throws EventException {
		com.vexsoftware.votifier.model.VotifierEvent voteEvent = (com.vexsoftware.votifier.model.VotifierEvent) event;
		Vote vote = voteEvent.getVote();
		
		Player player = Bukkit.getPlayer(vote.getUsername());
		if (player == null)
			return;
		
		CustomPlayer playerCustomPlayer = CustomPlayer.get(player.getUniqueId());
		if (playerCustomPlayer == null)
			return;
		
		String votedMsg = String.format("%s%s has voted for the server on %s and earned additional points!", ChatColor.GOLD, vote.getUsername(), vote.getServiceName());
		for (Player onlinePlayer : Bukkit.getOnlinePlayers())
			onlinePlayer.sendMessage(votedMsg);
		
		int amount;
		if (playerCustomPlayer.getRank() != null)
			if (playerCustomPlayer.getRank().getSalery(Currency.POINTS) > 0)
				amount = playerCustomPlayer.getRank().getSalery(Currency.POINTS) * 5;
			else
				amount = 15 * 5;
		else
			amount = 15 * 5;
		
		CurrencyClass.genPay(playerCustomPlayer, amount, Currency.POINTS);
		playerCustomPlayer.getPlayer().sendMessage(String.format("%s» %sThanks for voting! You earned %d points!", ChatColor.GOLD, ChatColor.GRAY, amount));
		
		sendToDiscord(votedMsg);
	}
	
	private void sendToDiscord(String message) {
		if (Main.getDiscordSRV() == null)
			return;
		
		// Send message to Discord
		((DiscordSRV) Main.getDiscordSRV()).processChatMessage(null, message, null, false);
	}
}
