package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MarryCommand extends Command {
    public MarryCommand() {
        super("marry", "Give a marriage proposal", "/marry {Username}", new ArrayList<String>(0));
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String string, @NotNull String @NotNull [] arguments) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.DARK_RED+ "Only players can use this command");
            return true;
        }

        if (arguments.length < 1) {
            commandSender.sendMessage(ChatColor.GOLD + "» "+ChatColor.GRAY + usageMessage);
            return true;
        }

        CustomPlayer senderCustom = CustomPlayer.get(((Player) commandSender).getUniqueId());
        if (senderCustom == null) return true;

        for (CustomPlayer index : CustomPlayer.getOnlinePlayers()) {
            if (!index.getPlayer().getDisplayName().equalsIgnoreCase(arguments[0])) continue;
            senderCustom.requestMarry(index);
        }
        return true;
    }
}
