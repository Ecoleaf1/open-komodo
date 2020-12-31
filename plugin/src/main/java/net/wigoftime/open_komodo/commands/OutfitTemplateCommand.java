package net.wigoftime.open_komodo.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class OutfitTemplateCommand extends Command {
	
	public OutfitTemplateCommand(String name, String description, String usageMessage,
			List<String> aliases) {
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		TextComponent text = new TextComponent((String.format("%s»%s Ready to get into your school uniforms? Then click here!", ChatColor.GOLD, ChatColor.GRAY)));
		ClickEvent clickEvent = new ClickEvent(Action.OPEN_URL, "https://www.planetminecraft.com/collection/94624/open-komodo/");
		text.setClickEvent(clickEvent);
		
		sender.spigot().sendMessage(text);
		return true;
	}

}
