package net.wigoftime.open_komodo.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class SettingsGui extends CustomGUI {
	ItemStack onTpaRequests;
	ItemStack displayTips;
	ItemStack particleToggle;
	ItemStack DiscordChatToggle;
	
	public SettingsGui(CustomPlayer opener) {
		super(opener, null, Bukkit.createInventory(null, 9, "Settings"));
		refresh();
		opener.getPlayer().openInventory(gui);
	}
	
	private void refresh() {
		onTpaRequests = createTpaRequestButton(opener);
		displayTips = createDisplayTips(opener);
		particleToggle = createParticleToggle(opener);
		DiscordChatToggle = createDiscordChatToggle(opener);
		
		gui.setItem(0, displayTips);
		gui.setItem(1, onTpaRequests);
		gui.setItem(2, particleToggle);
		gui.setItem(3, DiscordChatToggle);
	}
	
	private static ItemStack createDiscordChatToggle(CustomPlayer player) {
		ItemStack onTpaRequests;
		if (player.getSettings().isPlayerParticlesEnabled()) {
			onTpaRequests = new ItemStack(Material.BOOK); {
				ItemMeta meta = onTpaRequests.getItemMeta();
				meta.setDisplayName("Discord Chat enabled");
				onTpaRequests.setItemMeta(meta);
			}
		} else {
			onTpaRequests = new ItemStack(Material.PAPER); {
				ItemMeta meta = onTpaRequests.getItemMeta();
				meta.setDisplayName("Discord Chat disabled");
				onTpaRequests.setItemMeta(meta);
			}
		}
		
		return onTpaRequests;
	}
	
	private static ItemStack createParticleToggle(CustomPlayer player) {
		ItemStack onTpaRequests;
		if (player.getSettings().isPlayerParticlesEnabled()) {
			onTpaRequests = new ItemStack(Material.POTION); {
				ItemMeta meta = onTpaRequests.getItemMeta();
				meta.setDisplayName("Player Particle's enabled");
				onTpaRequests.setItemMeta(meta);
			}
		} else {
			onTpaRequests = new ItemStack(Material.GLASS_BOTTLE); {
				ItemMeta meta = onTpaRequests.getItemMeta();
				meta.setDisplayName("Player Particle's disabled");
				onTpaRequests.setItemMeta(meta);
			}
		}
		
		return onTpaRequests;
	}
	
	private static ItemStack createTpaRequestButton(CustomPlayer player) {
		ItemStack onTpaRequests;
		if (player.getSettings().isTpaEnabled()) {
			onTpaRequests = new ItemStack(Material.ENDER_EYE); {
				ItemMeta meta = onTpaRequests.getItemMeta();
				meta.setDisplayName("Teleport Requests enabled");
				onTpaRequests.setItemMeta(meta);
			}
		} else {
			onTpaRequests = new ItemStack(Material.ENDER_PEARL); {
				ItemMeta meta = onTpaRequests.getItemMeta();
				meta.setDisplayName("Teleport Requests disabled");
				onTpaRequests.setItemMeta(meta);
			}
		}
		
		return onTpaRequests;
	}
	
	private static ItemStack createDisplayTips(CustomPlayer player) {
		ItemStack displayTips;
		if (player.getSettings().isDisplayTipEnabled()) {
			displayTips = new ItemStack(Material.GOLD_NUGGET); {
				ItemMeta meta = displayTips.getItemMeta();
				meta.setDisplayName("Tips Display enabled");
				displayTips.setItemMeta(meta);
			}
		} else {
			displayTips = new ItemStack(Material.IRON_INGOT); {
				ItemMeta meta = displayTips.getItemMeta();
				meta.setDisplayName("Tips Display disabled");
				displayTips.setItemMeta(meta);
			}
		}
		
		return displayTips;
	}

	@Override
	public void clicked(InventoryClickEvent clickEvent) {
		clickEvent.setCancelled(true);
		ItemStack clickedItem = clickEvent.getCurrentItem();
		
		if (clickedItem.equals(onTpaRequests)) {
			boolean toggleTpa;
			if (opener.getSettings().isTpaEnabled())
				toggleTpa = false;
			else
				toggleTpa = true;
			
			opener.getSettings().setTpa(toggleTpa);
			refresh();
			return;
		}
		
		if (clickedItem.equals(displayTips)) {
			boolean toggleTpa;
			if (opener.getSettings().isDisplayTipEnabled())
				toggleTpa = false;
			else
				toggleTpa = true;
			
			opener.getSettings().setDisplayTip(toggleTpa);
			refresh();
			return;
		}
		
		if (clickedItem.equals(particleToggle)) {
			boolean toggleTpa;
			if (opener.getSettings().isPlayerParticlesEnabled())
				toggleTpa = false;
			else
				toggleTpa = true;
			
			opener.getSettings().setPlayerParticlesEnabled(toggleTpa);
			if (toggleTpa)
			Main.particlesApi.togglePlayerParticleVisibility(opener.getPlayer(), false);
			else
			Main.particlesApi.togglePlayerParticleVisibility(opener.getPlayer(), true);
			refresh();
			return;
		}
		
		if (clickedItem.equals(DiscordChatToggle)) {
			boolean discordChatEnabled;
			if (opener.getSettings().isDiscordChatEnabled())
				discordChatEnabled = false;
			else
				discordChatEnabled = true;
			
			opener.getSettings().enableDiscordChat(discordChatEnabled);
			refresh();
			return;
		}
	}
}
