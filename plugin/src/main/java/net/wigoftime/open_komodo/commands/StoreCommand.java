package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.config.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class StoreCommand extends Command {
    private final String storeDescription = Config.getStoreDescription();

    public StoreCommand() {
        super("store", "Displays the link to the store!", "/store", Arrays.asList("donate"));
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        commandSender.sendMessage(storeDescription);
        return true;
    }
}
