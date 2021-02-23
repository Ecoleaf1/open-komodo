package net.wigoftime.open_komodo.gui;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.permissions.Permission;

import net.wigoftime.open_komodo.enums.ErrorType;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public abstract class CustomGUI 
{
	protected final CustomPlayer opener;
	private final Permission requiredPermission;
	protected final Inventory gui;
	
	public CustomGUI(CustomPlayer openerCustomPlayer, Permission requiredPermission, Inventory gui) {
		opener = openerCustomPlayer;
		this.requiredPermission = requiredPermission;
		this.gui = gui;
	}
	
	public void open() {
		if (!canAccess())
			return;
		if (opener.getActiveGui() != null)
			opener.getActiveGui().closed();
		
		opener.getPlayer().openInventory(gui);
		opener.setActiveGui(this);
	}
	
	static void sendErrorMessage(Player player, ErrorType type)
	{
		switch (type) 
		{
			case IN_BUILDMODE:
				player.sendMessage(ChatColor.DARK_RED + 
						"Sorry, but you need to get out of"
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
			sendErrorMessage(opener.getPlayer(), ErrorType.IN_BUILDMODE);
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
