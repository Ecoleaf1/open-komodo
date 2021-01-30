package net.wigoftime.open_komodo.events;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.EventExecutor;

import net.wigoftime.open_komodo.chat.NormalMessage;
import net.wigoftime.open_komodo.etc.Moderation;
import net.wigoftime.open_komodo.etc.PetsManager;
import net.wigoftime.open_komodo.etc.RankSystem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.etc.Filter;

public class AsyncPlayerChat implements EventExecutor
{
	@Override
	public void execute(Listener listener, Event event) throws EventException {
		AsyncPlayerChatEvent chatEvent = (AsyncPlayerChatEvent) event;
		
		CustomPlayer chatterCustomPlayer = CustomPlayer.get(chatEvent.getPlayer().getUniqueId());
		chatEvent.setCancelled(true);
		
		if (chatterCustomPlayer == null)
			return;
		
		chatterCustomPlayer.setAfk(false);
		
		String message = chatEvent.getMessage();
		
		
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
