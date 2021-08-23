package net.wigoftime.open_komodo.gui;

import net.wigoftime.open_komodo.enums.ErrorType;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CustomGUI 
{
	protected final CustomPlayer opener;
	public final Permission requiredPermission;
	protected final Inventory gui;
	
	private final byte roleplayModeOnly;
	
	public CustomGUI(@NotNull CustomPlayer openerCustomPlayer, @Nullable Permission requiredPermission, Inventory gui) {
		opener = openerCustomPlayer;
		this.requiredPermission = requiredPermission;
		this.gui = gui;
		this.roleplayModeOnly = 1;
	}
	
	public CustomGUI(@NotNull CustomPlayer openerCustomPlayer, @Nullable Permission requiredPermission, Inventory gui, byte roleplayModeOnly) {
		opener = openerCustomPlayer;
		this.requiredPermission = requiredPermission;
		this.gui = gui;
		
		this.roleplayModeOnly = roleplayModeOnly;
	}
	
	public void open() {
		if (!canAccess())
			return;
		if (opener.getActiveGui() != null)
			opener.getActiveGui().closed();
		
		opener.getPlayer().openInventory(gui);
		opener.setActiveGui(this);
	}
	
	static void sendErrorMessage(@NotNull Player player, @NotNull ErrorType type)
	{
		switch (type) 
		{
			case IN_BUILDMODE:
				player.sendMessage(ChatColor.DARK_RED + 
						"Sorry, but you need to get out of"
						+ "build mode to access this. "
						+ "(/build)");
				break;
			case IN_ROLEPLAYMODE:
				player.sendMessage(ChatColor.DARK_RED + 
						"Sorry, but you need to get into "
						+ "build mode to access this. "
						+ "(/build)");
				break;
			case NOT_PERMITTED:
				player.sendMessage(ChatColor.DARK_RED + 
						"Sorry, but you don't have "
						+ "permission to access this.");
				break;
		}
	}
	
	boolean canAccess() {
		if (opener.isBuilding()) {
			if (roleplayModeOnly == 1) {
				sendErrorMessage(opener.getPlayer(), ErrorType.IN_BUILDMODE);
				return false;
			}
		} else if (roleplayModeOnly == 0) {
			sendErrorMessage(opener.getPlayer(), ErrorType.IN_ROLEPLAYMODE);
			return false;
		}
		
		if (requiredPermission == null)
			return true;
		
		if (!opener.getPlayer().hasPermission(requiredPermission)) {
			sendErrorMessage(opener.getPlayer(), ErrorType.NOT_PERMITTED);
			return false;
		}
		
		return true;
	}
	
	public abstract void clicked(InventoryClickEvent clickEvent);
	
	public void closed() {
		opener.setActiveGui(null);
	}
}
