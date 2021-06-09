package net.wigoftime.open_komodo.actions;

import net.wigoftime.open_komodo.config.Config;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.objects.CustomPlayer;

import java.util.ArrayList;
import java.util.List;

abstract public class Rules {
	private static final List<String> rules = Config.getRules();
	public static void display(CommandSender sender) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%s» %sRules%s:\n", ChatColor.GOLD, ChatColor.GRAY, ChatColor.DARK_GRAY));

		for (String ruleIndex : rules)
			sb.append(String.format("    %s» %s%s\n", ChatColor.GOLD, ChatColor.GRAY, ruleIndex));

		sender.sendMessage(sb.toString());
		
		if (!(sender instanceof Player)) return;
		
		CustomPlayer playerCustom = CustomPlayer.get(((Player)sender).getUniqueId());
		if (playerCustom == null) return;
		
		if (playerCustom.isInTutorial()) playerCustom.getTutorial().trigger(Rules.class);
	}
}
