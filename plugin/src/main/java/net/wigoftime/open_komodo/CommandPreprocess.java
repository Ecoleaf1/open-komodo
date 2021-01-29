package net.wigoftime.open_komodo;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.comphenix.protocol.PacketType.Protocol;
import com.mojang.authlib.GameProfile;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R1.BlockPosition;
import net.minecraft.server.v1_16_R1.EntityPlayer;
import net.minecraft.server.v1_16_R1.EntityPose;
import net.minecraft.server.v1_16_R1.MinecraftServer;
import net.minecraft.server.v1_16_R1.PlayerInteractManager;
import net.minecraft.server.v1_16_R1.WorldServer;
import net.wigoftime.open_komodo.objects.CustomPlayer;

abstract public class CommandPreprocess 
{
	
	public static void commands(PlayerCommandPreprocessEvent e) {	
		// Get Player
		Player player = e.getPlayer();
		
		CustomPlayer playerCustomPlayer = CustomPlayer.get(player.getUniqueId());
		if (playerCustomPlayer == null) {
			e.setCancelled(true);
			return;
		}
		
		playerCustomPlayer.setAfk(false);
		
		// The command arguments, first string is the actual command
		String[] args = e.getMessage().split(" ");
		
		// Get the command
		String command = args[0];
		
		if (command.equalsIgnoreCase("/memory")) {
			Long freeMemory = Runtime.getRuntime().freeMemory();
			Long totalMemory = Runtime.getRuntime().totalMemory();
			
			freeMemory = freeMemory / 1000;
			freeMemory = freeMemory / 1000;
			
			totalMemory = totalMemory / 1000;
			totalMemory = totalMemory / 1000;
			
			player.sendMessage(String.format("Free Memory: %d/%d", freeMemory, totalMemory));
			return;
		}
		
		if (command.equalsIgnoreCase("/test")) {
			MinecraftServer mcServer = MinecraftServer.getServer();
			Iterator<WorldServer> worldInterator = mcServer.getWorlds().iterator();
			WorldServer theWorld = worldInterator.next();
			PlayerInteractManager manager = new PlayerInteractManager(theWorld);
			EntityPlayer player2 = new EntityPlayer(MinecraftServer.getServer(),theWorld, new GameProfile(player.getUniqueId(), "Test"), manager);
			
			Location loc = player.getLocation();
			//player2.teleportAndSync(loc.getX(), loc.getY(), loc.getZ());
			player2.teleportTo(theWorld, new BlockPosition(loc.getX(), loc.getY(), loc.getZ()));
		}
		
		if (command.equalsIgnoreCase("/help") || command.equalsIgnoreCase("/minecraft:help"))
		{
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bHelp:&3\nFor info about ranks, type in /rank help\nFor info about points, type in /money help\n"
					+ "/msg (Player) (message) to message someone!\n/tpa (player) to send a teleport request!\n/home help for setting homes!"));
			e.setCancelled(true);
			return;
		}
	}
	
}
