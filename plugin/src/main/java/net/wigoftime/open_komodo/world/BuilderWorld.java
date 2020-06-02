package net.wigoftime.open_komodo.world;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

public abstract class BuilderWorld 
{
	private static World world;
	
	public static void setup()
	{
		world = Bukkit.getWorld("BuilderWorld");
		
		if (world == null)
			createWorld();
		
		world = Bukkit.getWorld("BuilderWorld");
	}
	
	public static void joinWorld(Player player)
	{
		Location location = new Location(world, 0, 10, 0);
		player.teleport(location);
		
		player.setGameMode(GameMode.CREATIVE);
	}
	
	private static void createWorld()
	{
		WorldCreator cr = new WorldCreator("BuilderWorld");
		cr.type(WorldType.FLAT);
		
		world = Bukkit.createWorld(cr);
		
		world.setGameRule(GameRule.DO_FIRE_TICK, false);
		world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
		world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
	}
	
	public static World getWorld()
	{
		return world;
	}
}
