package net.wigoftime.open_komodo.etc;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.MailWrapper;
import net.wigoftime.open_komodo.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class MailSystem {
    private final CustomPlayer playerCustom;

    public static final int mailMax = 15;

    public MailSystem(CustomPlayer playerCustom) {
        this.playerCustom = playerCustom;
    }

    public void clearMail() {
        SQLManager.clearMail(playerCustom.getUniqueId());
        playerCustom.getPlayer().sendMessage(String.format("%s» %sInbox cleared", ChatColor.GOLD, ChatColor.GRAY));
    }

    public boolean hasMail() {
        return SQLManager.countMail(playerCustom.getUniqueId()) > 0 ? true : false;
    }

    public void sendMail(UUID recipientUUID, String message) {
        if (!isEnabled(playerCustom.getPlayer())) return;

        if (SQLManager.countMail(recipientUUID) > mailMax) {
            playerCustom.getPlayer().sendMessage(String.format("%sSorry, but their mail inbox is full!", ChatColor.DARK_RED));
            return;
        }

        if (!Filter.checkMessage(playerCustom.getPlayer(), message)) return;

        SQLManager.sendMail(recipientUUID, playerCustom.getUniqueId() ,message);
        playerCustom.getPlayer().sendMessage(String.format("%s» %sMail sent", ChatColor.GOLD, ChatColor.GRAY));

        Player recipientPlayer = Bukkit.getPlayer(recipientUUID);

        if (recipientPlayer == null) return;

        recipientPlayer.sendMessage(String.format("%sYou got mail!", ChatColor.GREEN));
        recipientPlayer.playNote(recipientPlayer.getLocation(), Instrument.CHIME, Note.natural(1,Note.Tone.C));
    }

    public void displayMail(byte page) {
        if (!isEnabled(playerCustom.getPlayer())) return;

        List<MailWrapper> mail = SQLManager.getMail(playerCustom.getUniqueId());

        byte pageIndex = 0;
        for (MailWrapper mailIndex: mail) {
            if (pageIndex != page) {
                pageIndex++;
                continue;
            }
            OfflinePlayer offlineSender = Bukkit.getOfflinePlayer(mailIndex.sender);
            String senderUsername = offlineSender.getName();

            String dateString = String.format("%02d %s %04d", mailIndex.date.getDayOfMonth(), mailIndex.date.getMonth().toString(), mailIndex.date.getYear());
            mailIndex.date.getMonth().toString();
            playerCustom.getPlayer().sendMessage(String.format("%s» %sDate: %s, By: %s", ChatColor.GOLD, ChatColor.YELLOW, dateString, senderUsername));
            playerCustom.getPlayer().sendMessage(ChatColor.GRAY + mailIndex.message);

            if (pageIndex + 1 >= mail.size()) {
                ComponentBuilder clearMailMsg = new ComponentBuilder();
                clearMailMsg.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/mail clear",page + 2)));
                clearMailMsg.color(ChatColor.GOLD).append("» ").color(ChatColor.GREEN).append("No more mail, click here to clear your inbox");
                playerCustom.getPlayer().spigot().sendMessage(clearMailMsg.create());
                break;
            }

            ComponentBuilder nextPageMessage = new ComponentBuilder();
            nextPageMessage.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/mail page %d",page + 2)));
            nextPageMessage.color(ChatColor.GOLD).append("» ").color(ChatColor.GREEN).append("Click here to go to next page!");

            playerCustom.getPlayer().spigot().sendMessage(nextPageMessage.create());

            break;
        }
    }

    public int countMail() {
        if (!isEnabled(playerCustom.getPlayer())) return 0;
        return SQLManager.countMail(playerCustom.getUniqueId());
    }

    public boolean isInboxFull() {
        if (countMail() > mailMax) return true;
        else return false;
    }

    private static boolean isEnabled(@NotNull Player player) {
        if (!SQLManager.isEnabled()) {
            if (player.getPlayer().hasPermission(Permissions.visibleFullErrors))
                player.getPlayer().sendMessage(ChatColor.DARK_RED + "Sorry, but you need to use SQL to use mail at the moment!");
            else player.getPlayer().sendMessage(ChatColor.DARK_RED + "Sorry, but mail is disabled!");

            return false;
        }
        return true;
    }

}
