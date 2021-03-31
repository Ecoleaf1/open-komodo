package net.wigoftime.open_komodo.gui;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.etc.CurrencyClass;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.objects.CustomParticle;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class BasicCrate extends CustomGUI {
	private boolean isPaid = false;
	
	private int taskID;
	private int count;
	private Runnable runnable;
	public static final short cost = 18;
	
	public BasicCrate(CustomPlayer player, List<CustomParticle> items) {
		super(player, Permissions.particleAccess, Bukkit.getServer().createInventory(null, 27, "Particles Loot Box"));
		
		if (!CurrencyClass.takeOutFromBalance(opener, cost, Currency.COINS)) {
			opener.getPlayer().closeInventory();
			return;
		}
		
		isPaid = true;
		
		final ItemStack barrierItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE); {
			ItemMeta meta = barrierItem.getItemMeta();
			meta.setDisplayName(" ");
			barrierItem.setItemMeta(meta);
		}
		final ItemStack selectorItem = new ItemStack(Material.RED_STAINED_GLASS_PANE); {
			ItemMeta meta = selectorItem.getItemMeta();
			meta.setDisplayName(" ");
			selectorItem.setItemMeta(meta);
		}
		
		gui.setItem(0, barrierItem);
		gui.setItem(1, barrierItem);
		gui.setItem(2, barrierItem);
		gui.setItem(3, barrierItem);
		gui.setItem(4, selectorItem);
		gui.setItem(5, barrierItem);
		gui.setItem(6, barrierItem);
		gui.setItem(7, barrierItem);
		gui.setItem(8, barrierItem);
		
		gui.setItem(18, barrierItem);
		gui.setItem(19, barrierItem);
		gui.setItem(20, barrierItem);
		gui.setItem(21, barrierItem);
		gui.setItem(22, selectorItem);
		gui.setItem(23, barrierItem);
		gui.setItem(24, barrierItem);
		gui.setItem(25, barrierItem);
		gui.setItem(26, barrierItem);
		
		items.add(new CustomParticle(items.get(0)));
		items.add(new CustomParticle(items.get(1)));
		items.add(new CustomParticle(items.get(2)));
		items.add(new CustomParticle(items.get(3)));
		items.add(new CustomParticle(items.get(4)));
		items.add(new CustomParticle(items.get(5)));
		items.add(new CustomParticle(items.get(6)));
		items.add(new CustomParticle(items.get(7)));
		items.add(new CustomParticle(items.get(8)));
		
		Iterator<CustomParticle> item = items.iterator();
		for (int index = 9; index < 18; index++)
			gui.setItem(index, item.next().icon);
		
		Random random = new Random();
		int randomNum = random.nextInt(40) + 50;
		
		
		
		runnable = new Runnable() {
			private int localcount;
			private byte count2;
			private byte count3;
			
			public void run() {
				if (localcount > randomNum * 0.7) {
					if (localcount > randomNum) {
						player.setPermission(items.get(4).permission, null, true);
						player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 1f, 1f);
						cancel();
					}
					
					if (localcount > randomNum * 0.95) {
						count3++;
						
						if (count3 < 7)
							return;
						
						count3 = 0;
					} else if (localcount > randomNum * 0.7) {
					count2++;
					
					if (count2 < 3)
						return;
					
					count2 = 0;
					}
				}
				
				PrintConsole.test(count+"");
				
				for (int i = items.size() - 1; i > 0; i--) {
					CustomParticle currentPoint;
					CustomParticle previousPoint;
					previousPoint = new CustomParticle(items.get(i-1));
					currentPoint = new CustomParticle(items.get(i));
					items.set(i-1, currentPoint);
					items.set(i, previousPoint);
				}
				
				Iterator<CustomParticle> item = items.iterator();
				for (int index = 9; index < 18; index++)
					gui.setItem(index, item.next().icon);
				
				player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f);
				localcount++;
				count++;
			}
		};
			
	}
	
	public void open() {
		if (!isPaid)
			return;
		
		super.open();
		
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), runnable, 2, 2);
	}
	
	private void cancel() {
		Bukkit.getScheduler().cancelTask(taskID);
	}
	
	public void clicked(InventoryClickEvent clickEvent) {
		clickEvent.setCancelled(true);
	}
}
