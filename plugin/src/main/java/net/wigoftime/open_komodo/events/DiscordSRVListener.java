package net.wigoftime.open_komodo.events;

import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePostProcessEvent;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class DiscordSRVListener {
    @Subscribe
    public void discordMessageProcessed(DiscordGuildMessagePostProcessEvent event) {
        // Example of modifying a Discord -> Minecraft message
    	
    	event.setCancelled(true);
    	
    	for (CustomPlayer player : CustomPlayer.getOnlinePlayers())
    		if (player.getSettings().isDiscordChatEnabled()) player.getPlayer().sendMessage(event.getProcessedMessage());
    }
}
