package net.wigoftime.open_komodo.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class MarriageGUI extends CustomGUI {

    final ItemStack partnerIcon;

    static final ItemStack divorceIcon = new ItemStack(Material.REDSTONE); {
        ItemMeta meta = divorceIcon.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_RED+"Divorce");
        divorceIcon.setItemMeta(meta);
    }

    static final ItemStack teleportIcon = new ItemStack(Material.ENDER_EYE); {
        ItemMeta meta = teleportIcon.getItemMeta();
        meta.setDisplayName("Teleport");
        teleportIcon.setItemMeta(meta);
    }

    static final ItemStack loveEmote = new ItemStack(Material.POPPY); {
        ItemMeta meta = loveEmote.getItemMeta();
        meta.setDisplayName("Seek Affection");

        loveEmote.setItemMeta(meta);
    }

    final OfflinePlayer partner;

    public MarriageGUI(@NotNull CustomPlayer openerCustomPlayer, @NotNull OfflinePlayer partner) {
        super(openerCustomPlayer, null, Bukkit.createInventory(null,27, "Marriage Menu"));
        this.partner = partner;

        partnerIcon = new ItemStack(Material.PLAYER_HEAD); {
            SkullMeta meta = (SkullMeta) partnerIcon.getItemMeta();
            meta.setOwningPlayer(partner);
            meta.setDisplayName(partner.getName());
            partnerIcon.setItemMeta(meta);
        }

        gui.setItem(10, partnerIcon);

        if (partner instanceof Player) {
            gui.setItem(15, teleportIcon);
            gui.setItem(14, loveEmote);
        }

        gui.setItem(16, divorceIcon);
    }

    @Override
    public void clicked(InventoryClickEvent clickEvent) {
        clickEvent.setCancelled(true);

        if (clickEvent.getCurrentItem().equals(divorceIcon)) {
            opener.getPlayer().closeInventory();
            opener.divorce(partner.getName());
            return;
        } else if (clickEvent.getCurrentItem().equals(teleportIcon)) {
            opener.getMarriageSystem().teleport((Player) partner);
            opener.getPlayer().closeInventory();
            return;
        } else if (clickEvent.getCurrentItem().equals(loveEmote)) {
            opener.getPlayer().sendMessage(ChatColor.GOLD + "» " + ChatColor.DARK_PURPLE+"You seek for "+ partner.getName()+"'s affection");
            partner.getPlayer().sendMessage(String.format("%s» %s%s wants attention!", ChatColor.GOLD, ChatColor.DARK_PURPLE, opener.getPlayer().getDisplayName()));
            return;
        }
    }
}
