package net.wigoftime.open_komodo.objects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.World;
import org.bukkit.permissions.Permission;

import net.wigoftime.open_komodo.etc.Currency;

public class Rank 
{
	private final int id;
	private final double xpPrice;
	private final int pointsSalery;
	private final String name;
	private final String prefix;
	
	private final List<Permission> permissions;
	private HashMap<String, List<Permission>> worldPermissions = new HashMap<String, List<Permission>>();
	
	private static Set<Rank> ranks = new HashSet<Rank>();
	
	public Rank(int id, double xpPrice, int pointsSalery, String name, String prefix, List<Permission> permissions, HashMap<String, List<Permission>> worldPermissions)
	{
		this.id = id;
		this.xpPrice = xpPrice;
		this.pointsSalery = pointsSalery;
		this.name = name;
		this.prefix = prefix;
		
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
		if (currency == Currency.POINTS)
			return pointsSalery;
		else
			return 0;
	}
	
	// Static methods
	
	// Get rank from name
	public static Rank getRank(String rank)
	{	
		for (Rank r : ranks)
		{
			if (r.getName().equalsIgnoreCase(rank))
				return r;
		}
		
		return null;
	}
	
	public static Set<Rank> getRanks() 
	{
		return ranks;
	}
}
