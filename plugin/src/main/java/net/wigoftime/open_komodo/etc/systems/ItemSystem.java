package net.wigoftime.open_komodo.etc.systems;

import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.objects.CustomItem;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.ItemType;
import net.wigoftime.open_komodo.sql.SQLManager;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ItemSystem {
    private final CustomPlayer playerCustom;

    private @NotNull List<CustomItem> ownedHats = new LinkedList<CustomItem>();
    private @NotNull List<CustomItem> ownedTags = new LinkedList<CustomItem>();;
    private @NotNull List<CustomItem> ownedPhones = new LinkedList<CustomItem>();;

    public ItemSystem(CustomPlayer playerCustom, @NotNull List<CustomItem> items) {
        this.playerCustom = playerCustom;

        for (CustomItem item : items) {
            if (item == null) continue;

            switch (item.getType()) {
                case HAT:
                    ownedHats.add(item);
                    break;
                case TAG:
                    ownedTags.add(item);
                    break;
                case PHONE:
                    ownedPhones.add(item);
                    break;
                default:
                    break;
            }
        }
    }

    public @NotNull List<CustomItem> getItems(@NotNull ItemType type) {
        switch (type) {
            case HAT:
                return ownedHats;
            case TAG:
                return ownedTags;
            case PHONE:
                return ownedPhones;
            default:
                PrintConsole.print(String.format("%sWARNING: getItems is none of the slection and will return empty.", ChatColor.YELLOW));
                playerCustom.getPlayer().sendMessage(String.format("%s» %sError, you might want to report this message: default case in switch in getItems()", ChatColor.RED, ChatColor.DARK_RED));
                return new ArrayList<CustomItem>(0);
        }
    }

    public @NotNull List<CustomItem> getItems() {
        List<CustomItem> items = new ArrayList<CustomItem>(ownedHats.size() + ownedTags.size() + ownedPhones.size());
        items.addAll(ownedTags);
        for (CustomItem hat : ownedHats)
            items.add(hat);
        for (CustomItem phone : ownedPhones)
            items.add(phone);

        return items;
    }

    public void addItem(@NotNull CustomItem addedItem) {
        switch (addedItem.getType()) {
            case HAT:
                ownedHats.add(addedItem);
                break;
            case TAG:
                ownedTags.add(addedItem);
                break;
            case PHONE:
                ownedPhones.add(addedItem);
                break;
            default:
                break;
        }

        PrintConsole.test("Adding.. "+ addedItem.getID());

        // Add items in SQL Database or Player Config file in another task asynchronously to reduce the server pausing
        // from latency the server to the SQL
        Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
            public void run() {
                if (SQLManager.isEnabled())
                    SQLManager.setItems(playerCustom);
                else
                    PlayerConfig.setItems(playerCustom.getUniqueId(), getItems());
            }
        });
    }

    public boolean hasItem(int id, @NotNull ItemType type) {
        for (CustomItem item : getItems(type))
            if (id == item.getID())
                return true;

        return false;
    }
}
