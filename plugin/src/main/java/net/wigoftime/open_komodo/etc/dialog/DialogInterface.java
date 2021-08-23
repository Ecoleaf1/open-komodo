package net.wigoftime.open_komodo.etc.dialog;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface DialogInterface {
    public void sendChatMessage(@NotNull Player player);
    public void sendLeaveMessage(@NotNull Player player);
}
