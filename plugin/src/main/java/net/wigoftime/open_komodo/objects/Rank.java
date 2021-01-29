package net.wigoftime.open_komodo.objects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import net.wigoftime.open_komodo.config.RankConfig;
import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.PrintConsole;

public class Rank 
{
	private final int id;
	private final double xpPrice;
	private final int pointsSalery;
	private final int coinsSalery;
	private final String name;
	private final String prefix;
	
	private final List<Permission> permissions;
	private HashMap<String, List<Permission>> worldPermissions = new HashMap<String, List<Permission>>();
	
	private static Set<Rank> ranks = new HashSet<Rank>();
	
	public Rank(int id, double xpPrice, int pointsSalery, int coinsSalery, String name, String prefix, List<Permission> permissions, HashMap<String, List<Permission>> worldPermissions)
	{
		this.id = id;
		this.xpPrice = xpPrice;
		this.pointsSalery = pointsSalery;
		this.coinsSalery = coinsSalery;
		this.name = name;
		this.prefix = ChatColor.translateAlternateColorCodes('&', prefix);
		
		this.permissions = permissions;
		this.worldPermissions = worldPermissions;
		
		ranks.add(this);
	}
	
	public int getID()
	{
		return id;
	}
	
	public double getXPPrice()
	{
		return xpPrice;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getPrefix()
	{
		return prefix;
	}
	
	public List<Permission> getWorldPermissions(World world)
	{
		String name = world.getName();
		
		if (!worldPermissions.containsKey(name))
			return null;
		
		return worldPermissions.get(name);
	}
	
	public List<Permission> getPermissions()
	{
		return permissions;
	}
	
	public int getSalery(Currency currency)
	{
		switch (currency) {
		case POINTS:
			return pointsSalery;
		case COINS:
			return coinsSalery;
		default:
			return 0;
		}
	}
	
	// Static methods
	
	// Get rank from ID
	public static Rank getRank(int id)
	{	
		for (Rank r : ranks) {
			if (r.getID() == id)
				return r;
		}
		
		return null;
	}
	
	// Get rank from ID
	public static Rank getRank(String rankName)
	{	
		for (Rank r : ranks) {
			if (r.getName().equalsIgnoreCase(rankName))
				return r;
		}
		
		return null;
	}
	
	public static Set<Rank> getRanks() 
	{
		return ranks;
	}
	
	public static void reload()
	{
		ranks.clear();
		RankConfig.setup();
		
		for (Player player : Bukkit.getOnlinePlayers())
		{
			Permissions.setUp(CustomPlayer.get(player.getUniqueId()));
		}
	}
}
