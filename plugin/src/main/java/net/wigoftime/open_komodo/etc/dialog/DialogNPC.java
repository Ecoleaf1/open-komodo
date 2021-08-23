package net.wigoftime.open_komodo.etc.dialog;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class DialogNPC {
    private final int id;
    private final BaseComponent[] name;
    private final String[] chatDialog;
    private final String[] leaveDialog;

    public DialogNPC(int id, @NotNull BaseComponent[] name, @NotNull String[] chatDialog, @NotNull String[] leaveDialog) {
        this.id = id;
        this.name = name;
        this.chatDialog = chatDialog;
        this.leaveDialog = leaveDialog;
    }

    public int getID() {
        return id;
    }

    public void sendChatMessage(Player player) {
        if (chatDialog.length < 1) return;

        String dialogMessage = getRandomChatMessage();
        sendMessage(player, dialogMessage);
    }

    public void sendLeaveMessage(Player player) {
        if (leaveDialog.length < 1) return;

        String dialogMessage = getRandomLeaveMessage();
        sendMessage(player, dialogMessage);
    }

    private void sendMessage(@NotNull Player player, @NotNull String message) {
        player.spigot().sendMessage(
                new ComponentBuilder().append(name).append(": ").color(ChatColor.RESET).bold(false).italic(false)
                        .append(message).color(ChatColor.GRAY).create()
        );
    }

    private @NotNull String getRandomChatMessage() {
        Random random = new Random();
        int randomIndex = random.nextInt(chatDialog.length);
        return chatDialog[randomIndex];
    }

    private @NotNull String getRandomLeaveMessage() {
        Random random = new Random();
        int randomIndex = random.nextInt(leaveDialog.length);
        return leaveDialog[randomIndex];
    }
}
