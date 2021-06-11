package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.gui.CustomGUI;
import net.wigoftime.open_komodo.gui.MarriageGUI;
import net.wigoftime.open_komodo.gui.MarriagePartnerSelectionGui;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MarriageCommand extends Command {
    public MarriageCommand(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String command, @NotNull String @NotNull [] arguments) {
        if (!(commandSender instanceof Player)) return true;

        CustomPlayer commanderCustom = CustomPlayer.get(((Player) commandSender).getUniqueId());
        if (commanderCustom == null) return true;

        if (arguments.length < 1) {
            if (commanderCustom.getPartners().size() < 1) {
                commandSender.sendMessage(ChatColor.DARK_RED+"You are not married to anyone");
                return true;
            }

            CustomGUI gui;
            if (commanderCustom.getPartners().size() == 1) {
                gui = new MarriageGUI(commanderCustom, commanderCustom.getPartners().get(0));
            } else gui = new MarriagePartnerSelectionGui(commanderCustom);
            gui.open();
            return true;
        }

        if (arguments[0].equalsIgnoreCase("accept")) commanderCustom.getMarriageSystem().acceptMarry();
        else if (arguments[0].equalsIgnoreCase("reject")) commanderCustom.getMarriageSystem().rejectMarry();

        if (arguments.length < 2) return true;

        if (arguments[0].equalsIgnoreCase("marry")) marry(commanderCustom, arguments);
        else if (arguments[0].equalsIgnoreCase("divorce")) divorce(commanderCustom, arguments);
        return true;
    }

    private void marry(@NotNull CustomPlayer commanderCustom, String[] arguments) {
        for (CustomPlayer index : CustomPlayer.getOnlinePlayers()) {
            if (!index.getPlayer().getDisplayName().equalsIgnoreCase(arguments[1])) continue;

            commanderCustom.requestMarry(index);
        }
    }

    private void divorce(@NotNull CustomPlayer commanderCustom, String[] arguments) {
        commanderCustom.divorce(arguments[1]);
    }
}
