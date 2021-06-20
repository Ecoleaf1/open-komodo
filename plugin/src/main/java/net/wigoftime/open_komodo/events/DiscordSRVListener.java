package net.wigoftime.open_komodo.events;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePostProcessEvent;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.etc.systems.DiscordCommands;
import net.wigoftime.open_komodo.etc.systems.ModerationSystem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;

public class DiscordSRVListener {
    @Subscribe
    public void discordMessageProcessed(@NotNull DiscordGuildMessagePostProcessEvent event) {
        Bukkit.getLogger().info("MessageProcessed");
        event.setCancelled(true);

        if (((DiscordSRV) Main.getDiscordSRV()).getDestinationTextChannelForGameChannelName("minecraft-chat").getId().equals(event.getChannel().getId()))
            for (CustomPlayer player : CustomPlayer.getOnlinePlayers()) {
                if (player.getSettings().isDiscordChatEnabled())
                    player.getPlayer().sendMessage(event.getProcessedMessage());
            }

        if (((DiscordSRV) Main.getDiscordSRV()).getDestinationTextChannelForGameChannelName("moderation").getId().equals(event.getChannel().getId())) {
        if (event.getMessage().getContentDisplay().startsWith(">")) {
            DiscordCommands.moderationCommand(event.getAuthor(),event.getMessage().getContentDisplay());
            return;
        }

        Message discordMessage = event.getMessage();
        for (CustomPlayer player : CustomPlayer.getOnlinePlayers())
            player.getPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD +
                    "Discord ModChat " + ChatColor.DARK_GRAY
                    + discordMessage.getAuthor().getAsTag()
                    + ChatColor.RESET + ": "
                    + ChatColor.GRAY + event.getMessage().getContentDisplay());
    }
    }
}
