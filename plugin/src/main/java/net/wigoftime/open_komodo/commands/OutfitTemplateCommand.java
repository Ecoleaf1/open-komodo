package net.wigoftime.open_komodo.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OutfitTemplateCommand extends Command {
	
	public OutfitTemplateCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
                                 @NotNull List<String> aliases) {
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(@NotNull CommandSender sender, String commandLabel, String[] args) {
		TextComponent text = new TextComponent((String.format("%s»%s Ready to get into your school uniforms? Then click here!", ChatColor.GOLD, ChatColor.GRAY)));
		ClickEvent clickEvent = new ClickEvent(Action.OPEN_URL, "https://www.planetminecraft.com/collection/94624/open-komodo/");
		text.setClickEvent(clickEvent);
		
		sender.spigot().sendMessage(text);
		return true;
	}

}
