package net.wigoftime.open_komodo.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import net.wigoftime.open_komodo.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ResourceCommand extends Command {

	public ResourceCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		player.setResourcePack(Main.resourcePackLink);
		
		TextComponent text = new TextComponent("If you are having trouble loading the resourcepack, feel free to download it maunally by clicking here");
		text.setColor(ChatColor.GRAY);
		text.setClickEvent(new ClickEvent(Action.OPEN_URL, Main.resourcePackLink));
		
		player.spigot().sendMessage(text);
		return false;
	}

}
