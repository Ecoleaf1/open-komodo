package net.wigoftime.open_komodo.etc;

import org.bukkit.Bukkit;

import net.wigoftime.open_komodo.Main;

abstract public class PrintConsole 
{
	
	// Used to enable the test print function, which is used to problem solve and fix issues.
	private static final boolean debugMode = true;
	
	private static final String name = Main.name;
	
	public static void print(String text) {
		Bukkit.getLogger().info("["+name+"] "+text);
	}
	
	public static void test(String text)
	{
		if (debugMode)
			Bukkit.getLogger().info("["+name+"] "+text);
	}
}
