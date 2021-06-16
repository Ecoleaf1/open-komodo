package net.wigoftime.open_komodo.etc.systems;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.PrintConsole;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;

abstract public class DiscordCommands {
    private static void help() {
        TextChannel channel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("moderation");
        channel.sendMessage("To kick players - >kick {Username} [Reason]\n" +
                "To mute players - >mute {Username} {Duration} [Reason]\n" +
                "To ban players- >ban {Username} {Duration} [Reason]").queue();
    }

    public static void moderationCommand(String commandContext) {
        PrintConsole.test("ModerationCommand Function");

        String[] commandAndArgs = commandContext.split(" ");

        if (commandAndArgs[0].equalsIgnoreCase(">kick")) kickCommand(commandAndArgs);
        else if (commandAndArgs[0].equalsIgnoreCase(">mute")) muteCommand(commandAndArgs);
        else if (commandAndArgs[0].equalsIgnoreCase(">ban")) ban(commandAndArgs);
        else help();
    }

    private static void kickCommand(String[] commandAndArguments) {
        TextChannel channel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("moderation");

        if (commandAndArguments.length < 2) {
            channel.sendMessage("Invalid Usage, Correct Usage: >kick {Username} [Reason]").queue();
            return;
        }
        Player kickedPlayer = Bukkit.getPlayer(commandAndArguments[1]);

        StringBuilder builder = new StringBuilder();
        Arrays.stream(commandAndArguments).skip(2).forEach(textIndex -> {
            builder.append(textIndex + " ");
        });

        Bukkit.getScheduler().runTask(Main.getPlugin(), new Runnable() {
            public void run() {
                if (builder.toString().length() > 0) kickedPlayer.kickPlayer(ChatColor.DARK_RED+"You have been kicked for "+ builder.toString());
                else kickedPlayer.kickPlayer(ChatColor.DARK_RED+"You have been kicked");
                channel.sendMessage(commandAndArguments[1] + " has been kicked!").queue();
            }
        });
    }

    private static void muteCommand(String[] commandAndArguments) {
        TextChannel channel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("moderation");

        if (commandAndArguments.length < 3) {
            channel.sendMessage("Invalid Usage, Correct Usage: >mute {Username} {Duration} [Reason]").queue();
            return;
        }

        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(commandAndArguments[1]);
        Instant muteTime = ModerationSystem.calculateTime(commandAndArguments[2]);

        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(commandAndArguments).skip(3).forEach(textIndex -> {
            stringBuilder.append(textIndex + " ");
        });

        ModerationSystem.mute(targetPlayer, Date.from(muteTime), commandAndArguments.length > 3 ? stringBuilder.toString() : null);

        channel.sendMessage("muted " +
                targetPlayer.getName() +
                " for " + stringBuilder.toString() +
                " duration: " + Date.from(muteTime).toString()).queue();
    }

    private static void ban(String[] commandAndArguments) {
        TextChannel channel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("moderation");

        if (commandAndArguments.length < 3) {
            channel.sendMessage("Invalid Usage, Correct Usage: >ban {Username} {Duration} [Reason]").queue();
            return;
        }

        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(commandAndArguments[1]);
        Instant banTime = ModerationSystem.calculateTime(commandAndArguments[2]);

        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(commandAndArguments).skip(3).forEach(textIndex -> {
            stringBuilder.append(textIndex + " ");
        });

        ModerationSystem.ban(targetPlayer, Date.from(banTime), commandAndArguments.length > 3 ?stringBuilder.toString() : null);

        channel.sendMessage("banned " +
                targetPlayer.getName() +
                " for " + stringBuilder.toString() +
                " duration: " + Date.from(banTime).toString()).queue();
    }
}
