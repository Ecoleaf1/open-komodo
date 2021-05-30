/*		************************************
 *		uses chat for:
 *		Pet Name Obtaining, 
 *		Muting, 
 *		Filter, 
 *		sending text to Custom Chat, 
 *		Giving Players Pending XP
 *		************************************
 */

package net.wigoftime.open_komodo.events;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.EventExecutor;

import net.wigoftime.open_komodo.chat.NormalMessage;
import net.wigoftime.open_komodo.chat.ShoutMessage;
import net.wigoftime.open_komodo.etc.PetsManager;
import net.wigoftime.open_komodo.etc.systems.RankSystem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.etc.Filter;

public class AsyncPlayerChat implements EventExecutor {
	@Override
	public void execute(Listener listener, Event event) throws EventException {
		AsyncPlayerChatEvent chatEvent = (AsyncPlayerChatEvent) event;
		CustomPlayer playerCustom = CustomPlayer.get(chatEvent.getPlayer().getUniqueId());
		chatEvent.setCancelled(true);
		
		// One possibility would be if the player has not loaded in yet.
		// To prevent errors from occurring, players who has not loaded in
		// can not proceed.
		if (playerCustom == null) return;
		
		playerCustom.setAfk(false);
		String message = chatEvent.getMessage();
		
		if (PetsManager.awaitingNameInput(playerCustom.getPlayer())) {
			PetsManager.changeName(playerCustom.getPlayer(), message);
			return;
		}
		
		if (playerCustom.isMuted()) return;
		if (!Filter.checkMessage(playerCustom.getPlayer(), message)) return;
		
		if (message.charAt(0) == '!') ShoutMessage.shout(playerCustom, message);
		else NormalMessage.sendMessage(playerCustom, message);
		
		RankSystem.lettersToXP(playerCustom.getPlayer(), message);
	}
	
}
