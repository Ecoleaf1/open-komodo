package net.wigoftime.open_komodo.gui;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.objects.CustomParticle;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Deprecated
public class ParticlesGUI extends CustomGUI {
	
	private static final ItemStack lootBoxOpenIcon = new ItemStack(Material.TRIPWIRE_HOOK);
	private static final ItemStack lootBoxStylesOpenIcon = new ItemStack(Material.TRIPWIRE_HOOK);
	private static final ItemStack particleMenu = new ItemStack(Material.BLAZE_POWDER);
	
	public static void setup() {
		{
			ItemMeta meta = lootBoxOpenIcon.getItemMeta();
			meta.setDisplayName(String.format("%sUnlock more particles", ChatColor.BLUE));
			meta.setLore(Arrays.asList(String.format("%sPrice %d Coins", ChatColor.DARK_AQUA, BasicCrate.cost)));
			lootBoxOpenIcon.setItemMeta(meta);
		}
		{
			ItemMeta meta = lootBoxStylesOpenIcon.getItemMeta();
			meta.setDisplayName(String.format("%sUnlock more particle styles", ChatColor.BLUE));
			meta.setLore(Arrays.asList(String.format("%sPrice %d Coins", ChatColor.DARK_AQUA, BasicCrate.cost)));
			lootBoxStylesOpenIcon.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = particleMenu.getItemMeta();
			meta.setDisplayName(String.format("%sOpen Particles Menu", ChatColor.BLUE));
			particleMenu.setItemMeta(meta);
		}
	}
	
	public ParticlesGUI(CustomPlayer player) {
		super(player, Permissions.particleAccess, Bukkit.createInventory(null, 27, "Particles Menu"));
		
		gui.setItem(10, lootBoxOpenIcon);
		gui.setItem(11, lootBoxStylesOpenIcon);
		gui.setItem(16, particleMenu);
	}

	@Override
	public void clicked(@NotNull InventoryClickEvent clickEvent) {
		clickEvent.setCancelled(true);
		ItemStack clickedItem = clickEvent.getCurrentItem();
		
		if (clickedItem.equals(lootBoxOpenIcon)) {
			List<CustomParticle> unownedParticles = getShuffledUnownedParticles(CustomParticle.particleList);
			if (unownedParticles.size() == 0) {
				opener.getPlayer().sendMessage(String.format("%s» %sYou already got everything in this lootbox", ChatColor.GOLD, ChatColor.GRAY));
				opener.getPlayer().closeInventory();
				return;
			}
			
			BasicCrate crate = new BasicCrate(opener, unownedParticles);
			crate.open();
			return;
		}
		
		if (clickedItem.equals(lootBoxStylesOpenIcon)) {
			List<CustomParticle> unownedParticles = getShuffledUnownedParticles(CustomParticle.mvpStyleList);
			if (unownedParticles.size() == 0) {
				opener.getPlayer().sendMessage(String.format("%s» %sYou already got everything in this lootbox", ChatColor.GOLD, ChatColor.GRAY));
				opener.getPlayer().closeInventory();
				return;
			}
			
			BasicCrate crate = new BasicCrate(opener, unownedParticles);
			crate.open();
			return;
		}
		
		if (clickedItem.equals(particleMenu))
			Main.particlesApi.openParticlesGui(opener.getPlayer());
	}
	
	private @NotNull List<CustomParticle> getShuffledUnownedParticles(@NotNull List<CustomParticle> unfilteredParticles) {
		List<CustomParticle> particles = new ArrayList<CustomParticle>(9);
		
		byte count = 0;
		for (CustomParticle particle : unfilteredParticles)
			if (count > 8)
				break;
			else
			if (!opener.getPlayer().hasPermission(particle.permission)) {
			particles.add(particle);
			count++;
			}
		
		if (particles.size() == 0)
			return new ArrayList<CustomParticle>(0);
		
		if (particles.size() < 9) {
			Random random = new Random();
				
				if (particles.size() == 1) {
					for (int i = 0; i < 9; i++)
						particles.add(particles.get(0));
					
				} else while(particles.size() < 9)
					particles.add(particles.get(random.nextInt(particles.size() - 1)));
		}
		
		return particles;
	}
}
