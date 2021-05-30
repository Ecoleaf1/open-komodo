package net.wigoftime.open_komodo.etc.homesystem;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Home;

public class HomeSystem {
	private static final File playerConfigFolder = new File(Main.dataFolderPath+"/Players/");
	
	public static final String noHomes = ChatColor.translateAlternateColorCodes('&', "&4You have no homes!");
	public static final String invaildHouse = ChatColor.translateAlternateColorCodes('&', "&cYou don't have a house by that name!");
	private static final String aboveLimit = ChatColor.translateAlternateColorCodes('&', "&cSorry, but you are above your home limit!");
	private static final String homeCreated = ChatColor.translateAlternateColorCodes('&', "&b&lHome Created!");
	
	private static final String homeDeleted = ChatColor.translateAlternateColorCodes('&', "&cHome deleted.");

	private final CustomPlayer playerCustom;

	private int homeLimit = 1;
	private List<Home> tutorialHomes;
	private List<Home> homes;

	public HomeSystem(CustomPlayer playerCustom, List<Home> homes) {
		this.playerCustom = playerCustom;
		this.homes = homes;
	}

	public static void createHome(CustomPlayer playerCustom, String name) {
		Home home = new Home(name, playerCustom.getPlayer().getLocation());

		AddHome.execute(playerCustom, home);
		playerCustom.addHome(home);
	}

	public void setHomeLimit(int limit) {
		homeLimit = limit;
	}

	public int getHomeLimit()
	{
		return homeLimit;
	}

	public void addHome(Home home) {
		AddHome.execute(playerCustom, home);
	}

	public Home getHome(String homeName) {
		if (playerCustom.isInTutorial()) {
			for (Home home : tutorialHomes)
				if (home.name.equalsIgnoreCase(homeName))
					return home;
		}
		else for (Home home : homes) {
			if (home.name.equalsIgnoreCase(homeName))
				return home;
		}

		return null;
	}

	public List<Home> getHomes() {
		return playerCustom.isInTutorial() ? tutorialHomes :  homes;
	}

	public void teleportHome(String homeName) {
		TeleportHome.teleport(playerCustom, homeName);
	}

	public void deleteHome(String homeName) {
		for (Home home : homes) {
			if (home.name.equalsIgnoreCase(homeName)) {
				homes.remove(home);

				Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
					public void run() {
						if (SQLManager.isEnabled())
							SQLManager.setHomes(playerCustom.getUniqueId(), homes);
						else
							PlayerConfig.setHomes(playerCustom.getUniqueId(), homes);
					}
				});

				playerCustom.getPlayer().sendMessage(net.md_5.bungee.api.ChatColor.GRAY + "Home deleted!");
				return;
			}
		}

		playerCustom.getPlayer().sendMessage(HomeSystem.invaildHouse);
		return;
	}

	public void setupTutorialHomes() {
		tutorialHomes = new LinkedList<Home>();
	}

	public void clearTutorialHomes() {
		tutorialHomes = null;
	}
}
