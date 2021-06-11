package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DivorceCommand extends Command {
    public DivorceCommand() {
        super("divorce", "Get a divorce", "/divorce {Username}", new ArrayList<String>(0));
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String @NotNull [] arguments) {
        if (!(commandSender instanceof Player)) return true;
        CustomPlayer playerCustom = CustomPlayer.get(((Player)commandSender).getUniqueId());
        if (playerCustom == null) return true;

        if (playerCustom.getPartners().size() < 1) {
            commandSender.sendMessage(ChatColor.GOLD + "» " + ChatColor.DARK_RED + "You are not married to anyone.");
            return true;
        }

        if (playerCustom.getPartners().size() < 2) {
            playerCustom.divorce(playerCustom.getPartners().get(0).getName());
            return true;
        }

        if (arguments.length < 1) {
            commandSender.sendMessage("Specify one of your partners");
            return true;
        }

        playerCustom.divorce(arguments[1]);
        return true;
    }
}
