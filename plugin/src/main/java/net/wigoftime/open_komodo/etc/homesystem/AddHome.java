package net.wigoftime.open_komodo.etc.homesystem;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Home;
import net.wigoftime.open_komodo.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddHome {
	public static void execute(@NotNull CustomPlayer player, Home home) {
		if (player.isInTutorial())
		if (!player.getTutorial().validState(AddHome.class)) return;
		
		List<Home> homes = player.getHomes();
		
		if (player.getHomes().size() >= player.getHomeLimit()) {
			player.getPlayer().sendMessage(String.format("%s%sHEY! %sSorry, but you maxed out your home limit.", ChatColor.RED, ChatColor.BOLD, ChatColor.DARK_RED));
			return;
		}
		
		homes.add(home);
		
		if (player.isInTutorial()) {
			player.getTutorial().trigger(AddHome.class);
			return;
		}
		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				if (SQLManager.isEnabled())
				SQLManager.setHomes(player.getUniqueId(), homes);
				else
				PlayerConfig.setHomes(player.getUniqueId(), homes);
			}
		});
	}
}
