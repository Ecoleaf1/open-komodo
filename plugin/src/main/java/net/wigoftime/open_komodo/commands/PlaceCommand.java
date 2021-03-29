package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.wigoftime.open_komodo.gui.CustomGUI;
import net.wigoftime.open_komodo.gui.FurnitureMenu;
import net.wigoftime.open_komodo.gui.HatMenu;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class PlaceCommand extends Command {

	public PlaceCommand(String name, String description, String usageMessage, List<String> aliases) {
		super(name, description, usageMessage, aliases);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		Player player = (Player) sender;
		
		CustomGUI gui = new FurnitureMenu(CustomPlayer.get(player.getUniqueId()), null, "default");
		gui.open();
		return true;
	}
}
