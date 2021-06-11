package net.wigoftime.open_komodo.world;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BuilderWorld 
{
	private static @Nullable World world;
	
	public static void setup()
	{
		world = Bukkit.getWorld("BuilderWorld");
		
		if (world == null)
			createWorld();
		
		world = Bukkit.getWorld("BuilderWorld");
	}
	
	public static void joinWorld(@NotNull Player player)
	{
		Location location = new Location(world, 359, 68, -225);
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
	
	public static @Nullable World getWorld()
	{
		return world;
	}
}
