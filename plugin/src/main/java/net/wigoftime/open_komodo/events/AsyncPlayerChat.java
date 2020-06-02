package net.wigoftime.open_komodo.events;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.wigoftime.open_komodo.chat.NormalMessageJson;
import net.wigoftime.open_komodo.etc.Moderation;
import net.wigoftime.open_komodo.etc.PetsManager;
import net.wigoftime.open_komodo.etc.RankSystem;
import net.wigoftime.open_komodo.etc.Filter;

abstract public class AsyncPlayerChat 
{

	public static void function(AsyncPlayerChatEvent e)
	{
		Player player = e.getPlayer();
		String message = e.getMessage();
		
		e.setCancelled(true);
		
		if (PetsManager.awaitingNameInput(player))
		{
			PetsManager.changeName(player, message);
			return;
		}
		
		if (Moderation.isMuted(player))
			return;
		
		if (!Filter.checkMessage(player, message))
			return;
		
		// Send message
		if (message.charAt(0) == '!')
			NormalMessageJson.shout(player, message);
		else
			NormalMessageJson.sendMessage(player, message);
		
		// Add XP
		RankSystem.lettersToXP(player, message);
	}
	
}
