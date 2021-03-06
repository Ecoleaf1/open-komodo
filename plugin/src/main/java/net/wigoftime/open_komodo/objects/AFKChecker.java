package net.wigoftime.open_komodo.objects;

import java.time.Instant;

public class AFKChecker implements Runnable {

	public void run() {
		for (CustomPlayer playerCustomPlayer : CustomPlayer.getOnlinePlayers())
		{
			if (playerCustomPlayer.isAfk())
				continue;
			
			if (playerCustomPlayer.getLastActiveTime().plusSeconds(480).isAfter(Instant.now()))
				continue;
				
			playerCustomPlayer.setAfk(true);
		}
		
	}
	
}
