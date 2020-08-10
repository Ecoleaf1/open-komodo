package net.wigoftime.open_komodo.events;

import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.wigoftime.open_komodo.chat.NormalMessage;
import net.wigoftime.open_komodo.etc.Moderation;
import net.wigoftime.open_komodo.etc.PetsManager;
import net.wigoftime.open_komodo.etc.RankSystem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.etc.Filter;

abstract public class AsyncPlayerChat 
{

	public static void function(AsyncPlayerChatEvent e)
	{
		CustomPlayer chatterCustomPlayer = CustomPlayer.get(e.getPlayer().getUniqueId());
		e.setCancelled(true);
		
		if (chatterCustomPlayer == null)
			return;
		
		chatterCustomPlayer.setAfk(false);
		
		String message = e.getMessage();
		
		
		if (PetsManager.awaitingNameInput(chatterCustomPlayer.getPlayer())) {
			PetsManager.changeName(chatterCustomPlayer.getPlayer(), message);
			return;
		}
		
		
		if (Moderation.isMuted(chatterCustomPlayer))
			return;
		
		if (!Filter.checkMessage(chatterCustomPlayer.getPlayer(), message))
			return;
		
		// Send message
		if (message.charAt(0) == '!')
			NormalMessage.shout(chatterCustomPlayer.getPlayer(), message);
		else
			NormalMessage.sendMessage(chatterCustomPlayer, message);
		
		// Add XP
		RankSystem.lettersToXP(chatterCustomPlayer.getPlayer(), message);
	}
	
}
