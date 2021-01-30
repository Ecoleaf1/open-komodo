/*		************************************
 * 		Use Cases:
 * 		Reset AFK timer when player uses a command,
 * 		Overwrite /help with information about the server
 * 		************************************
 */

package net.wigoftime.open_komodo.events;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.EventExecutor;

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

public class CommandPreprocess implements EventExecutor {

	@Override
	public void execute(Listener listener, Event event) throws EventException {
		PlayerCommandPreprocessEvent commandEvent = (PlayerCommandPreprocessEvent) event;
		Player player = commandEvent.getPlayer();
		
		// One possibility would be if the player has not loaded in yet.
		// To prevent errors from occurring, players who has not loaded in
		// can not proceed.
		CustomPlayer playerCustomPlayer = CustomPlayer.get(player.getUniqueId());
		if (playerCustomPlayer == null) {
			commandEvent.setCancelled(true);
			return;
		}
		
		playerCustomPlayer.setAfk(false);
		
		String[] commandArguments = commandEvent.getMessage().split(" ");
		String command = commandArguments[0];
		
		if (command.equalsIgnoreCase("/help") || command.equalsIgnoreCase("/minecraft:help")) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bHelp:&3\nFor info about ranks, type in /rank help\nFor info about points, type in /money help\n"
					+ "/msg (Player) (message) to message someone!\n/tpa (player) to send a teleport request!\n/home help for setting homes!"));
			commandEvent.setCancelled(true);
			return;
		}
	}
	
}
