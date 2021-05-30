package net.wigoftime.open_komodo.etc.systems;

import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DonationSystem {
    private final CustomPlayer playerCustom;
    private float balance;

    public DonationSystem(CustomPlayer playerCustom, float tipAmount) {
        this.playerCustom = playerCustom;
        this.balance = tipAmount;
    }

    public float getDonated() {
        return balance;
    }

    public void addTip(float amount) {
        setTip(amount+balance);
    }

    public static void addTip(UUID uuid, float amount) {
        if (SQLManager.isEnabled())
            setTip(uuid,amount+SQLManager.getTip(uuid));
        else
            setTip(uuid,amount+PlayerConfig.getTip(uuid));
    }


    public static void setTip(UUID uuid, float amount) {
        if (SQLManager.isEnabled())
            SQLManager.setTip(uuid, amount);
        else
            PlayerConfig.setTip(uuid, amount);
    }

    public void setTip(float amount) {
        balance = amount;

        if (SQLManager.isEnabled())
            SQLManager.setTip(playerCustom.getUniqueId(), balance);
        else
            PlayerConfig.setTip(playerCustom.getUniqueId(), balance);
    }

    public void announceDonation(float amount) {
        if (amount < 0) return;

        announceDonation(playerCustom.getPlayer().getDisplayName(), amount);
        playerCustom.getPlayer().sendMessage(String.format("%s» %sOh dear grand user, we kindly thank you for your generous donation & support!", ChatColor.GOLD, ChatColor.YELLOW)); }

    public static void announceDonation(String username,float amount) {
        if (amount < 0) return;

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(String.format("%s%s%s has donated %.1f$ to the server! Thanks!", ChatColor.GOLD,
                    username, ChatColor.YELLOW, amount));
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
        }
    }
}
