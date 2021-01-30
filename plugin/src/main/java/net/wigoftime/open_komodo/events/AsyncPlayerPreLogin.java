package net.wigoftime.open_komodo.events;

import java.util.Date;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.plugin.EventExecutor;

import net.wigoftime.open_komodo.etc.Moderation;
import net.wigoftime.open_komodo.sql.SQLManager;

public class AsyncPlayerPreLogin implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		AsyncPlayerPreLoginEvent preLoginEvent = (AsyncPlayerPreLoginEvent) event;
		
		UUID uuid = preLoginEvent.getUniqueId();
		
		if (SQLManager.isEnabled())
		if (!SQLManager.containsModerationPlayer(preLoginEvent.getUniqueId()))
			SQLManager.createModerationPlayer(preLoginEvent.getUniqueId());
		
		boolean isBanned = Moderation.isBanned(uuid);
		
		if (isBanned) {
			String reason = Moderation.getBanReason(uuid);
			Date date = Moderation.getBanDate(uuid);
			
			if (reason == null)
				preLoginEvent.disallow(Result.KICK_BANNED, String.format("%s> %sYou have been banned\n  Due Date: %s", ChatColor.GOLD, ChatColor.DARK_RED, date.toString()));
			else
				preLoginEvent.disallow(Result.KICK_BANNED, String.format("%s> %sYou have been banned\n  Due Date: %s\n  Reason: %s", ChatColor.GOLD, ChatColor.DARK_RED, date.toString(), reason));
			return;
		}
	}

}
