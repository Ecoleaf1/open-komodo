package net.wigoftime.open_komodo.gui;

import net.wigoftime.open_komodo.etc.connectfour.ConnectFourSession;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

public class PlayerMenu extends CustomGUI {
    private static ItemStack head, mount, connect4;
    private static @NotNull ItemStack marry = new ItemStack(Material.GOLD_NUGGET); {
        ItemMeta meta = marry.getItemMeta();
        meta.setDisplayName("Marry");
        marry.setItemMeta(meta);
    }

    private static @NotNull ItemStack marriageMenu = new ItemStack(Material.ROSE_BUSH); {
        ItemMeta meta = marriageMenu.getItemMeta();
        meta.setDisplayName("Marriage Menu");
        marriageMenu.setItemMeta(meta);
    }

    private final @NotNull CustomPlayer openerCustom, selectedPlayerCustom;

    public PlayerMenu(@NotNull CustomPlayer openerCustom, @NotNull CustomPlayer selectedPlayerCustom) {
        super(openerCustom, null, Bukkit.createInventory(null, 27, "Player Menu"));

        this.openerCustom = openerCustom;
        this.selectedPlayerCustom = selectedPlayerCustom;

        head = new ItemStack(Material.PLAYER_HEAD);
        {
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwningPlayer(selectedPlayerCustom.getPlayer());
            meta.setDisplayName(selectedPlayerCustom.getPlayer().getDisplayName());
            head.setItemMeta(meta);
        }

        mount = new ItemStack(Material.SADDLE);
        {
            ItemMeta meta = mount.getItemMeta();
            meta.setDisplayName("Mount");
            mount.setItemMeta(meta);
        }

        connect4 = new ItemStack(Material.BLUE_DYE);
        {
            ItemMeta meta = connect4.getItemMeta();
            meta.setDisplayName("Connect4");
            connect4.setItemMeta(meta);
        }

        gui.setItem(10, head);
        gui.setItem(13, mount);
        gui.setItem(14, connect4);

        if (!openerCustom.isMarried(selectedPlayerCustom))
            gui.setItem(15, marry);
        else
            gui.setItem(15, marriageMenu);
    }

    @Override
    public void clicked(@NotNull InventoryClickEvent clickEvent) {
        clickEvent.setCancelled(true);

        if (clickEvent.getCurrentItem().equals(mount)) mountClicked();
        else if (clickEvent.getCurrentItem().equals(connect4)) connect4Clicked();
        else if (clickEvent.getCurrentItem().equals(marry)) marryClicked();
        else if (clickEvent.getCurrentItem().equals(marriageMenu)) marriageMenuClicked();
    }

    private void mountClicked() {
        Entity lastMountedEntity = selectedPlayerCustom.getPlayer();
        while (true) {
            if (lastMountedEntity.getPassengers().size() < 1) break;
            lastMountedEntity = lastMountedEntity.getPassengers().get(0);
        }
        lastMountedEntity.addPassenger(openerCustom.getPlayer());
        opener.getPlayer().closeInventory();
    }

    private void connect4Clicked() {
        opener.getPlayer().closeInventory();
        ConnectFourSession.requestToPlay(opener, selectedPlayerCustom);
    }

    private void marryClicked() {
        opener.getPlayer().closeInventory();
        opener.requestMarry(selectedPlayerCustom);
    }

    private void marriageMenuClicked() {
        opener.getPlayer().closeInventory();
        CustomGUI marriageMenu = new MarriageGUI(opener, selectedPlayerCustom.getPlayer());
        marriageMenu.open();
    }
}
