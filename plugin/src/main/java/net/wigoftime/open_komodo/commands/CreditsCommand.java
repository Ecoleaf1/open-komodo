package net.wigoftime.open_komodo.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CreditsCommand extends Command {

	public CreditsCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage,
			@NotNull List<String> aliases) {
		super(name, description, usageMessage, aliases);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(@NotNull CommandSender sender, String commandLabel, String[] args) {
		sender.sendMessage(String.format("%s» %sServer icon: SleepyLoser (Discord: SleepyLoser#6078)", ChatColor.GOLD, ChatColor.GRAY));
		sender.sendMessage("");
		sender.sendMessage(String.format("%s» %sProps: Jadesweetgamer (Discord: Jade/Yui#1428), Jazzycow11 (Discord: Aramis#2706)", ChatColor.GOLD, ChatColor.GRAY));
		sender.sendMessage("");
		sender.sendMessage(String.format("%s» %sOriginal map creator: Eivisxp", ChatColor.GOLD, ChatColor.GRAY));
		sender.sendMessage(String.format("%s» %sBuilders: Jadesweetgamer (Discord: Jade/Yui#1428), KrispyKrunch (Discord: KRiSPY#2019), JustAluri (Discord: YeetusAFetus#2533)", ChatColor.GOLD, ChatColor.GRAY));
		sender.sendMessage("");
		sender.sendMessage(String.format("%s» %sProgrammer: Ecoleaf (Discord: Ecoleaf#9713)", ChatColor.GOLD, ChatColor.GRAY));
		sender.sendMessage("");
		sender.sendMessage(ChatColor.GOLD+"»" + ChatColor.GRAY +" Skins:");
		sender.sendMessage(ChatColor.GOLD+"»" + ChatColor.GRAY +" 707: https://www.planetminecraft.com/skin/707---mystic-messenger-3818625/");
		sender.sendMessage(ChatColor.GOLD+"»" + ChatColor.GRAY +" Selene: https://www.planetminecraft.com/skin/100-remakes-entry-clockmaker/");
		sender.sendMessage(ChatColor.GOLD+"»" + ChatColor.GRAY +" Hugo: https://www.planetminecraft.com/skin/badass-guy-with-a-coat/");
		sender.sendMessage(ChatColor.GOLD+"»" + ChatColor.GRAY +" Mall Guide: https://www.planetminecraft.com/skin/-5237543/");
		sender.sendMessage(ChatColor.GOLD+"»" + ChatColor.GRAY +" Chao Xing: https://www.planetminecraft.com/skin/the-fire-inside-you-oc/");
		sender.sendMessage(ChatColor.GOLD+"»" + ChatColor.GRAY +" Ayaka: https://www.planetminecraft.com/skin/you-ll-bring-honour-to-us-all/");
		sender.sendMessage(ChatColor.GOLD+"»" + ChatColor.GRAY +" Ravenor: https://www.planetminecraft.com/skin/reginald---god-of-gentlemen/");
		sender.sendMessage(ChatColor.GOLD+"»" + ChatColor.GRAY +" School Guide: https://www.planetminecraft.com/skin/cute-school-boy-o/");
		sender.sendMessage("");
		sender.sendMessage(String.format("%s» %sAnd you, The community even just checking by helps! Thank you!", ChatColor.GOLD, ChatColor.GRAY));
		return true;
	}
	
}
