package net.wigoftime.open_komodo.etc.homesystem;

import java.util.List;

import net.wigoftime.open_komodo.etc.HomeSystem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Home;

public class TeleportHome {
	public static void teleport(CustomPlayer player, String homeName) {
		Home home = player.getHome(homeName);
		if (home == null) {
			player.getPlayer().sendMessage(HomeSystem.invaildHouse);
			return;
		}
		
		player.getPlayer().teleport(home.location);
		if (player.isInTutorial()) player.getTutorial().trigger(TeleportHome.class);
	}
}
