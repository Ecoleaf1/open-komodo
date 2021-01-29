package net.wigoftime.open_komodo;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
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
import net.wigoftime.open_komodo.etc.ServerScoreBoard;
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
		
		if (command.equalsIgnoreCase("/help") || command.equalsIgnoreCase("/minecraft:help"))
		{
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bHelp:&3\nFor info about ranks, type in /rank help\nFor info about points, type in /money help\n"
					+ "/msg (Player) (message) to message someone!\n/tpa (player) to send a teleport request!\n/home help for setting homes!"));
			e.setCancelled(true);
			return;
		}
	}
	
}
