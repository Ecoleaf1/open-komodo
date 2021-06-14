package net.wigoftime.open_komodo.events;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class PlayerToggleSneakEvent implements EventExecutor {
    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException {
        org.bukkit.event.player.PlayerToggleSneakEvent toggleEvent = (org.bukkit.event.player.PlayerToggleSneakEvent) event;
        if (!toggleEvent.isSneaking()) return;
        if (toggleEvent.getPlayer().getPassengers().size() < 1) return;
        toggleEvent.getPlayer().getPassengers().stream().forEach(passenger -> toggleEvent.getPlayer().removePassenger(passenger));
    }
}
