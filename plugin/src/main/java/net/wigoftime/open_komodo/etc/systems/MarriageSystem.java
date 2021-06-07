package net.wigoftime.open_komodo.etc.systems;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MarriageSystem {
    final private CustomPlayer playerCustom;
    public List<CustomPlayer> onlinePartners;
    public List<OfflinePlayer> offlinePartners;
    public List<OfflinePlayer> totalPartners = new ArrayList<OfflinePlayer>(0);

    public CustomPlayer purposer = null;

    public MarriageSystem(@NotNull CustomPlayer player, @NotNull List<UUID> partners) {

        onlinePartners = new LinkedList<CustomPlayer>();
        offlinePartners = new LinkedList<OfflinePlayer>();

        this.playerCustom = player;

        for (UUID index : partners) {
            CustomPlayer playerOnlineIndex = CustomPlayer.get(index);
            if (playerOnlineIndex != null) {
                onlinePartners.add(playerOnlineIndex);
                continue;
            }

            OfflinePlayer playerOffline = Bukkit.getOfflinePlayer(index);
            if (!playerOffline.hasPlayedBefore()) continue;
            offlinePartners.add(playerOffline);
        }

        syncTotalPartners();
    }

    public void requestMarry(@NotNull CustomPlayer targetPlayer) {
        if (targetPlayer == playerCustom) return;
        if (isMarriedTo(targetPlayer)) {
            playerCustom.getPlayer().sendMessage(ChatColor.DARK_RED+ "You are already married to "+targetPlayer.getPlayer().getDisplayName()+"!");
            return;
        }

        targetPlayer.setPurposer(playerCustom);

        ComponentBuilder builder = new ComponentBuilder();
        builder.color(ChatColor.GOLD).append("» ").color(ChatColor.GRAY).append("You getting purposed by ")
                .append(playerCustom.getPlayer().getDisplayName()).append("!");

        ComponentBuilder acceptMessage = new ComponentBuilder();
        acceptMessage.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/marriage accept")).color(ChatColor.GOLD).append("» ").color(ChatColor.GREEN).append("Click here to accept marriage proposal");

        ComponentBuilder denyMessage = new ComponentBuilder();
        denyMessage.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/marriage reject")).color(ChatColor.GOLD).append("» ").color(ChatColor.RED).append("Click here to deny marriage proposal");

        targetPlayer.getPlayer().spigot().sendMessage(builder.create());
        targetPlayer.getPlayer().spigot().sendMessage(acceptMessage.create());
        targetPlayer.getPlayer().spigot().sendMessage(denyMessage.create());

        targetPlayer.setPurposer(playerCustom);
        playerCustom.getPlayer().sendMessage(ChatColor.GOLD+"» "+ ChatColor.GRAY + "You sent a marriage proposal to "+ targetPlayer.getPlayer().getDisplayName());
    }

    public void acceptMarry() {
        if (purposer == null) return;

        marry(purposer);
        purposer = null;
    }

    public void rejectMarry() {
        if (purposer == null) return;

        purposer.getPlayer().sendMessage(ChatColor.GOLD+"» " + ChatColor.DARK_RED +playerCustom.getPlayer().getDisplayName()+" has rejected your marriage proposal");

        playerCustom.getPlayer().sendMessage(ChatColor.GOLD+"» " + ChatColor.DARK_RED +"You rejected " +playerCustom.getPlayer().getDisplayName()+" marriage's proposal");;
        purposer = null;
    }

    public void setPurposer(@Nullable CustomPlayer player) {
        purposer = player;
    }

    public boolean isMarriedTo(CustomPlayer targetPlayer) {
        synchronized (totalPartners) { return onlinePartners.contains(targetPlayer) ? true : false; }
    }

    private void marry(@NotNull CustomPlayer player) {
        if (isMarriedTo(player)) return;

        SQLManager.addMarry(playerCustom.getUniqueId(), player.getUniqueId());

        synchronized (totalPartners) {
            onlinePartners.add(player);
            syncTotalPartners();
        }

        synchronized (player.getMarriageSystem().totalPartners) {
            player.getMarriageSystem().onlinePartners.add(playerCustom);
            player.getMarriageSystem().syncTotalPartners();
        }

        player.getPlayer().sendMessage(ChatColor.GOLD+"» " + ChatColor.GRAY +"you are now married to "+playerCustom.getPlayer().getDisplayName()+"!");
        playerCustom.getPlayer().sendMessage(ChatColor.GOLD+"» " + ChatColor.GRAY +"you are now married to "+player.getPlayer().getDisplayName()+"!");
    }

    public void teleport(Player partner) {
        playerCustom.getPlayer().teleport(partner.getPlayer());
        partner.getPlayer().sendMessage(org.bukkit.ChatColor.GOLD+"» "+ org.bukkit.ChatColor.GREEN+ playerCustom.getPlayer().getDisplayName() +" has teleported to you");
    }

    public void divorce(@NotNull String username) {
        synchronized (totalPartners) {
            for (CustomPlayer index : onlinePartners) {
                if (index.getPlayer().getDisplayName().equalsIgnoreCase(username)) {
                    SQLManager.removeMarry(playerCustom.getUniqueId(), index.getPlayer().getUniqueId());

                    synchronized (index.getPartners()) {
                        index.getMarriageSystem().onlinePartners.remove(playerCustom);
                        index.getMarriageSystem().syncTotalPartners();
                    }


                    onlinePartners.remove(index);
                    syncTotalPartners();

                    playerCustom.getPlayer().sendMessage(ChatColor.GOLD + "» " + ChatColor.DARK_RED + "You have divorced " + index.getPlayer().getDisplayName() + "!");
                    index.getPlayer().sendMessage(ChatColor.GOLD + "» " + ChatColor.GRAY + playerCustom.getPlayer().getDisplayName() + "has divorced you");
                    return;
                }
            }
        }

        synchronized (totalPartners) {
            for (OfflinePlayer partner : offlinePartners) {
                if (partner.getName().equalsIgnoreCase(username)) {
                    offlinePartners.remove(partner);
                    SQLManager.removeMarry(playerCustom.getUniqueId(), partner.getUniqueId());
                    syncTotalPartners();
                    playerCustom.getPlayer().sendMessage(ChatColor.GOLD + "» " + ChatColor.GRAY + "You have divorced " + playerCustom.getPlayer().getDisplayName() + "!");
                    return;
                }
            }

            syncTotalPartners();
        }
    }

    public void joined() {
        synchronized (totalPartners) {
            for (CustomPlayer playerIndex : onlinePartners) {
                synchronized (playerIndex.getPartners()) {
                    playerIndex.getMarriageSystem().onlinePartners.add(playerCustom);
                    for (OfflinePlayer partnerIndex : playerIndex.getMarriageSystem().offlinePartners)
                        if (partnerIndex.getUniqueId().equals(playerCustom.getUniqueId()))
                            playerIndex.getMarriageSystem().offlinePartners.remove(partnerIndex);

                    playerIndex.getMarriageSystem().syncTotalPartners();
                }
                playerIndex.getPlayer().sendMessage(ChatColor.GOLD + "» " + ChatColor.GREEN + " Your partner " + playerCustom.getPlayer().getDisplayName() + " has joined!");
            }
        }
    }

    public void quiting() {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerCustom.getPlayer().getUniqueId());
        synchronized (totalPartners) {
            for (CustomPlayer playerIndex : onlinePartners) {
                synchronized (playerIndex.getPartners()) {
                    playerIndex.getMarriageSystem().onlinePartners.remove(playerCustom);
                    playerIndex.getMarriageSystem().offlinePartners.add(offlinePlayer);
                    playerIndex.getMarriageSystem().syncTotalPartners();
                }

                playerIndex.getPlayer().sendMessage(ChatColor.GOLD + "» " + ChatColor.DARK_RED + " Your partner " + playerCustom.getPlayer().getDisplayName() + " left");
            }
        }
    }

    public void syncTotalPartners() {
        List<OfflinePlayer> updatedTotalPartners = new ArrayList<OfflinePlayer>(offlinePartners.size() + onlinePartners.size());
        for (OfflinePlayer partnerIndex : offlinePartners)
            updatedTotalPartners.add(partnerIndex);

        for (CustomPlayer partnerIndex : onlinePartners)
            updatedTotalPartners.add(partnerIndex.getPlayer());

        this.totalPartners = updatedTotalPartners;
        PrintConsole.test("Size:" + totalPartners.size());
    }

    public synchronized @NotNull List<OfflinePlayer> getPartners() {
        return totalPartners;
    }
}
