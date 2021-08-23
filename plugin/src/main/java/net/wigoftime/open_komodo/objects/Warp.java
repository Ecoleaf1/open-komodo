package net.wigoftime.open_komodo.objects;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class Warp {
    public ItemStack icon;
    public Location location;

    public Warp(ItemStack icon, Location warpLocation) {
        this.icon = icon;
        this.location = warpLocation;
    }
}
