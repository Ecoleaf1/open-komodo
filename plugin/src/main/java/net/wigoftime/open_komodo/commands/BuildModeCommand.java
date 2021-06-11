package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.etc.InventoryManagement;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BuildModeCommand extends Command
{
	public BuildModeCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
                            @NotNull List<String> aliases) {
		
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) 
	{
		if (!(sender instanceof Player))
		{
			sender.sendMessage(ChatColor.DARK_RED + "Only a player can use this command");
			return false;
		}
		
		if (!sender.hasPermission(Permissions.buildModePerm))
		{
			sender.sendMessage(ChatColor.DARK_RED + "You don't have permission to go to build mode.");
			return false;
		}
		
		CustomPlayer player = CustomPlayer.get(((Player) sender).getUniqueId());
		
		toggleBuild(player);
		return true;
	}
	
	public static void toggleBuild(@NotNull CustomPlayer player)
	{
		if (player.isBuilding())
		{
			player.getPlayer().setGameMode(GameMode.SURVIVAL);
			player.setBuilding(false);
			InventoryManagement.loadInventory(player, player.getPlayer().getWorld());
			
			player.getPlayer().setCanPickupItems(false);
		}
		else
		{
			player.setBuilding(true);
			
			InventoryManagement.saveInventory(player, player.getPlayer().getWorld());
			player.getPlayer().getInventory().clear();
			
			player.getPlayer().setGameMode(GameMode.CREATIVE);
			
			player.getPlayer().setCanPickupItems(true);
		}
		
		return;
	}

}
