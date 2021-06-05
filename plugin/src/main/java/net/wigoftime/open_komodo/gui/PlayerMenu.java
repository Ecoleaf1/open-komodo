package net.wigoftime.open_komodo.gui;

import net.wigoftime.open_komodo.etc.connectfour.ConnectFourSession;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.permissions.Permission;

public class PlayerMenu extends CustomGUI {
    private ItemStack head, mount, connect4, marry;

    private final CustomPlayer openerCustom, selectedPlayerCustom;

    public PlayerMenu(CustomPlayer openerCustom, CustomPlayer selectedPlayerCustom) {
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

        marry = new ItemStack(Material.GOLD_NUGGET);
        {
            ItemMeta meta = marry.getItemMeta();
            meta.setDisplayName("Marry [Coming Soon]");
            marry.setItemMeta(meta);
        }

        gui.setItem(10, head);
        gui.setItem(13, mount);
        gui.setItem(14, connect4);
        gui.setItem(15, marry);
    }

    @Override
    public void clicked(InventoryClickEvent clickEvent) {
        clickEvent.setCancelled(true);

        if (clickEvent.getCurrentItem().equals(mount)) mountClicked();
        else if (clickEvent.getCurrentItem().equals(connect4)) connect4Clicked();
    }

    private void mountClicked() {
        selectedPlayerCustom.getPlayer().addPassenger(openerCustom.getPlayer());
        opener.getPlayer().closeInventory();
    }

    private void connect4Clicked() {
        opener.getPlayer().closeInventory();
        ConnectFourSession.requestToPlay(opener, selectedPlayerCustom);
    }
}
