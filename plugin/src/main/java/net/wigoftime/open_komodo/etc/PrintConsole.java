package net.wigoftime.open_komodo.etc;

import net.wigoftime.open_komodo.Main;
import org.bukkit.Bukkit;

abstract public class PrintConsole 
{
	
	// Used to enable the test print function, which is used to problem solve and fix issues.
	private static boolean debugMode = false;
	
	private static final String name = Main.name;
	
	public static void print(String text) {
		Bukkit.getLogger().info("["+name+"] "+text);
	}
	
	public static void test(String text)
	{
		if (debugMode)
			Bukkit.getLogger().info("["+name+"] "+text);
	}

	public static void setDebugMode(boolean isDebugOn) {
		debugMode = isDebugOn;
	}
}
