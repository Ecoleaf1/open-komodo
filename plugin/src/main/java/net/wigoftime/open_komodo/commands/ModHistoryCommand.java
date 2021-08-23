package net.wigoftime.open_komodo.commands;

import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.objects.ModHistorySingle;
import net.wigoftime.open_komodo.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ModHistoryCommand extends Command {
    public ModHistoryCommand() {
        super("modhistory", "Look at history of player of being punished", "/modhistory {Username}", Arrays.asList(""));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!sender.hasPermission(Permissions.modHistory)) return true;
        if (args.length < 1) {
            sender.sendMessage(ChatColor.GRAY + usageMessage);
            return true;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        SQLManager.getModHistory(offlinePlayer.getUniqueId()).stream().forEach(historySingle -> {
            sender.sendMessage(ChatColor.GOLD + "» "+ ChatColor.GRAY+ "Causer: " + (historySingle.causerPlayer == null ? "Unknown" : Bukkit.getOfflinePlayer(historySingle.causerPlayer).getName())
                    + " Type: " + "DATE: "+ Date.from(Instant.ofEpochSecond(historySingle.dateTime)).toString() + " " + historySingle.type + " Context:");
            sender.sendMessage(ChatColor.GRAY + " "+ historySingle.context);
        });

        sender.sendMessage(ChatColor.GRAY + "» list ended");
        return true;
    }
}
