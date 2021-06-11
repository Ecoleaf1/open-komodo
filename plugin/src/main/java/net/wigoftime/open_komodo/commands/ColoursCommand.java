package net.wigoftime.open_komodo.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ColoursCommand extends Command {
    public ColoursCommand() {
        super("colours", "Display a basic colour code template", "/color", Arrays.asList("color"));
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.DARK_RED+"Dark Red - &4");
        commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.RED+"Red - &c");
        commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.GOLD+"Gold - &6");
        commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.YELLOW+"Yellow - &e");
        commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.DARK_GREEN+"Dark Green - &2");
        commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.GREEN+"Green - &a");
        commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.AQUA+"Aqua - &b");
        commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.DARK_AQUA+"Dark Aqua - &3");
        commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.DARK_BLUE+"Dark Blue - &1");
        commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.BLUE+"Dark Blue - &9");
        commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.LIGHT_PURPLE+"Light Purple - &d");
        commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.DARK_PURPLE+"Dark Purple - &5");
        commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.WHITE+"White - &f");
        commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.GRAY+"Gray - &7");
        commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.DARK_GRAY+"Dark Gray - &8");
        commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.BLACK+"Black - &0");
        commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.GRAY+ "" + ChatColor.BOLD +"Bold - &l");
        commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.GRAY+ "" + ChatColor.GRAY +"Underline - &n");
        commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.GRAY+ "" + ChatColor.STRIKETHROUGH +"Strikethrough - &m");
        commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.GRAY+ "" + ChatColor.ITALIC +"Italic - &o");
        commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.GRAY+ "" + ChatColor.WHITE +"Reset - &r");
        return true;
    }
}
