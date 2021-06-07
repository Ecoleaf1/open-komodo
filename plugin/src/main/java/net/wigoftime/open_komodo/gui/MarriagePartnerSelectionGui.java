package net.wigoftime.open_komodo.gui;

import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MarriagePartnerSelectionGui extends CustomGUI {
    private final CustomPlayer opener;
    private static ItemStack nextPageIcon = new ItemStack(Material.ARROW); {
        ItemMeta meta = nextPageIcon.getItemMeta();
        meta.setDisplayName("Next Page");
        nextPageIcon.setItemMeta(meta);
    }

    public MarriagePartnerSelectionGui(CustomPlayer openerCustomPlayer) {
        super(openerCustomPlayer, null, Bukkit.createInventory(null, 9, "Partner Selection"));
        this.opener = openerCustomPlayer;

        refreshPage((byte) 1);
    }

    @Override
    public void clicked(InventoryClickEvent clickEvent) {
        clickEvent.setCancelled(true);

        if (clickEvent.getCurrentItem() == null) return;

        if (clickEvent.getCurrentItem().equals(nextPageIcon)) {
            refreshPage((byte) (page+1));
            return;
        }
        if (clickEvent.getCurrentItem().getType() != Material.PLAYER_HEAD) return;

        opener.getPlayer().closeInventory();

        OfflinePlayer targetOffinePlayer = ((SkullMeta) clickEvent.getCurrentItem().getItemMeta()).getOwningPlayer();
        Player onlinePlayer = targetOffinePlayer.getPlayer();

        CustomGUI partnerGUI = new MarriageGUI(opener, onlinePlayer == null ? targetOffinePlayer : onlinePlayer);
        partnerGUI.open();
    }

    byte page;
    private void refreshPage(@NotNull byte page) {
        this.page = page;

        List<CustomPlayer> onlineList = opener.getMarriageSystem().onlinePartners;
        List<OfflinePlayer>  offlineList = opener.getMarriageSystem().offlinePartners;

        List<ItemStack> heads = new ArrayList<ItemStack>(onlineList.size() + offlineList.size());

        byte indexCount = -1;

        synchronized (onlineList) {
            Iterator<CustomPlayer> iterator = onlineList.iterator();
            while (iterator.hasNext()) {
                indexCount++;
                if (indexCount >= 8 * (page)) break;
                if (indexCount < 8 * (page - 1)) continue;

                CustomPlayer player = iterator.next();

                ItemStack index = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta) index.getItemMeta();

                meta.setOwningPlayer(player.getPlayer());
                meta.setDisplayName(player.getPlayer().getDisplayName());

                List<String> lore = new ArrayList<String>(1);
                lore.add(ChatColor.GREEN + "Online");
                meta.setLore(lore);

                index.setItemMeta(meta);
                heads.add(index);
            }
        }

        if (indexCount < 8 * (page)) {
            synchronized (offlineList) {
                Iterator<OfflinePlayer> offlinePlayerIterator = offlineList.iterator();
                while (offlinePlayerIterator.hasNext()) {
                    indexCount++;
                    if (indexCount >= 8 * (page)) break;
                    if (indexCount < 8 * (page - 1)) continue;
                    OfflinePlayer player = offlinePlayerIterator.next();

                    ItemStack index = new ItemStack(Material.PLAYER_HEAD);
                    SkullMeta meta = (SkullMeta) index.getItemMeta();

                    meta.setOwningPlayer(player);
                    meta.setDisplayName(player.getName());

                    List<String> lore = new ArrayList<String>(1);
                    lore.add(ChatColor.DARK_RED + "Offline");
                    meta.setLore(lore);

                    index.setItemMeta(meta);
                    heads.add(index);
                }
            }
        }

        for (byte index = 0; index < heads.size(); index++) {
            gui.setItem(index, heads.get(index));
        }

        if (onlineList.size() + offlineList.size() >= 8 * (page))
            gui.setItem(8, nextPageIcon);
    }
}
