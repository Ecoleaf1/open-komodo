package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MailCommand extends Command {

    public MailCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String string, @NotNull String @NotNull [] arguments) {
        if (!(commandSender instanceof Player)) return true;

        CustomPlayer playerCustom = CustomPlayer.get(((Player) commandSender).getPlayer().getUniqueId());
        if (playerCustom == null) return true;

        if (arguments.length > 0) {
            if (arguments[0].equalsIgnoreCase("clear")) {
                playerCustom.getPersonalMailSystem().clearMail();
                return true;
            }

            if (arguments[0].equalsIgnoreCase("send")) {
                if (arguments.length < 2) {
                    return true;
                }

                StringBuilder stringBuilder = new StringBuilder();

                OfflinePlayer recipientOffline = Bukkit.getOfflinePlayer(arguments[1]);
                if (recipientOffline.getName() == null) {
                    playerCustom.getPlayer().sendMessage(String.format("%sSorry, player not found.", ChatColor.DARK_RED));
                    return true;
                }

                for (int index = 2; index < arguments.length; index++)
                    stringBuilder.append(arguments[index]+" ");

                String message = stringBuilder.toString();
                playerCustom.getPersonalMailSystem().sendMail(recipientOffline.getUniqueId(), message);
                return true;
            }
            if (arguments[0].equalsIgnoreCase("page")) {
                if (arguments.length < 2) return true;

                try {
                    byte page = Byte.parseByte(arguments[1]);

                    playerCustom.getPersonalMailSystem().displayMail((byte) (page - 1));
                } catch (NumberFormatException exception) {
                    playerCustom.getPlayer().sendMessage(String.format("%sSorry, but the page needs to be a number.", ChatColor.DARK_RED));
                }
            }
            return true;
        }

        playerCustom.getPersonalMailSystem().displayMail((byte) 0);

        return true;
    }
}
