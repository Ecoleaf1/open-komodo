package net.wigoftime.open_komodo.etc;

import java.io.File;

import org.bukkit.ChatColor;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Home;

abstract public class HomeSystem {
	private static final File playerConfigFolder = new File(Main.dataFolderPath+"/Players/");
	
	public static final String noHomes = ChatColor.translateAlternateColorCodes('&', "&4You have no homes!");
	public static final String invaildHouse = ChatColor.translateAlternateColorCodes('&', "&cYou don't have a house by that name!");
	private static final String aboveLimit = ChatColor.translateAlternateColorCodes('&', "&cSorry, but you are above your home limit!");
	private static final String homeCreated = ChatColor.translateAlternateColorCodes('&', "&b&lHome Created!");
	
	private static final String homeDeleted = ChatColor.translateAlternateColorCodes('&', "&cHome deleted.");
	
	public static void createHome(CustomPlayer playerCustomPlayer, String name) {
		Home home = new Home(name, playerCustomPlayer.getPlayer().getLocation());
		
		playerCustomPlayer.addHome(home);
	}
	
}
