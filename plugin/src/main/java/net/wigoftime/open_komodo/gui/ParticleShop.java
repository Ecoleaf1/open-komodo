package net.wigoftime.open_komodo.gui;

import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParticleShop extends CustomGUI {
	private static final ItemStack ambientEntityEffect = new ItemStack(Material.BEACON);
	private static final ItemStack angryVillager = new ItemStack(Material.IRON_DOOR);
	private static final ItemStack barrier = new ItemStack(Material.BARRIER);
	private static final ItemStack block = new ItemStack(Material.STONE);
	private static final ItemStack bubble = new ItemStack(Material.BUBBLE_CORAL);
	private static final ItemStack bubbleColumnUp = new ItemStack(Material.MAGMA_BLOCK);
	private static final ItemStack bubblePop = new ItemStack(Material.BUBBLE_CORAL_FAN);
	private static final ItemStack campfireCosySmoke = new ItemStack(Material.CAMPFIRE); 
	private static final ItemStack campfireSignalSmoke = new ItemStack(Material.REDSTONE_TORCH); 
	private static final ItemStack cloud = new ItemStack(Material.WHITE_WOOL); 
	private static final ItemStack composter = new ItemStack(Material.COMPOSTER); 
	private static final ItemStack crit = new ItemStack(Material.IRON_SWORD);
	private static final ItemStack currentDown = new ItemStack(Material.SOUL_SAND);
	private static final ItemStack damageIndicator = new ItemStack(Material.BOW);
	private static final ItemStack dolphin = new ItemStack(Material.DOLPHIN_SPAWN_EGG); 
	private static final ItemStack dragonBreath = new ItemStack(Material.DOLPHIN_SPAWN_EGG); 
	private static final ItemStack drippingLava = new ItemStack(Material.LAVA_BUCKET); 
	private static final ItemStack drippingWater = new ItemStack(Material.WATER_BUCKET); 
	private static final ItemStack dust = new ItemStack(Material.REDSTONE); 
	private static final ItemStack enchant = new ItemStack(Material.ENCHANTING_TABLE); 
	private static final ItemStack enchantHit = new ItemStack(Material.DIAMOND_SWORD); 
	private static final ItemStack endRod = new ItemStack(Material.END_ROD); 
	private static final ItemStack entityEffect = new ItemStack(Material.GLOWSTONE_DUST); 
	private static final ItemStack explosion = new ItemStack(Material.FIRE_CHARGE); 
	private static final ItemStack explosionEmitter = new ItemStack(Material.TNT); 
	private static final ItemStack fallingDust = new ItemStack(Material.SAND);
	private static final ItemStack fallingLava = new ItemStack(Material.RED_DYE); 
	private static final ItemStack fallingWater = new ItemStack(Material.BLUE_DYE); 
	
	public void setup() {
		{
			ItemMeta meta = ambientEntityEffect.getItemMeta();
			meta.setDisplayName(String.format("%sAmbient Entity Effect", ChatColor.GREEN));
			ambientEntityEffect.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = angryVillager.getItemMeta();
			meta.setDisplayName(String.format("%sAngry Villager", ChatColor.GREEN));
			angryVillager.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = barrier.getItemMeta();
			meta.setDisplayName(String.format("%sBarrier", ChatColor.GREEN));
			barrier.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = block.getItemMeta();
			meta.setDisplayName(String.format("%sBlock", ChatColor.GREEN));
			block.setItemMeta(meta);
		}
		 
		{
			ItemMeta meta = bubble.getItemMeta();
			meta.setDisplayName(String.format("%sBubble", ChatColor.GREEN));
			bubble.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = bubbleColumnUp.getItemMeta();
			meta.setDisplayName(String.format("%sBubble Column Up", ChatColor.GREEN));
			bubbleColumnUp.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = bubblePop.getItemMeta();
			meta.setDisplayName(String.format("%sBubble Pop", ChatColor.GREEN));
			bubblePop.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = campfireCosySmoke.getItemMeta();
			meta.setDisplayName(String.format("%sCampfire Cosy Smoke", ChatColor.GREEN));
			campfireCosySmoke.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = campfireSignalSmoke.getItemMeta();
			meta.setDisplayName(String.format("%sCampfire Signal Smoke", ChatColor.GREEN));
			campfireSignalSmoke.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = cloud.getItemMeta();
			meta.setDisplayName(String.format("%sCloud", ChatColor.GREEN));
			cloud.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = composter.getItemMeta();
			meta.setDisplayName(String.format("%sComposter", ChatColor.GREEN));
			composter.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = crit.getItemMeta();
			meta.setDisplayName(String.format("%sCrit", ChatColor.GREEN));
			crit.setItemMeta(meta);
		}
		 
		{
			ItemMeta meta = currentDown.getItemMeta();
			meta.setDisplayName(String.format("%sCurrent Down", ChatColor.GREEN));
			currentDown.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = damageIndicator.getItemMeta();
			meta.setDisplayName(String.format("%sDamage Indicator", ChatColor.GREEN));
			damageIndicator.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = dolphin.getItemMeta();
			meta.setDisplayName(String.format("%sDolphin", ChatColor.GREEN));
			dolphin.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = dragonBreath.getItemMeta();
			meta.setDisplayName(String.format("%sDragon Breath", ChatColor.GREEN));
			dragonBreath.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = drippingLava.getItemMeta();
			meta.setDisplayName(String.format("%sDripping Lava", ChatColor.GREEN));
			drippingLava.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = drippingWater.getItemMeta();
			meta.setDisplayName(String.format("%sDripping Water", ChatColor.GREEN));
			drippingWater.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = dust.getItemMeta();
			meta.setDisplayName(String.format("%sDust", ChatColor.GREEN));
			dust.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = enchant.getItemMeta();
			meta.setDisplayName(String.format("%sEnchant", ChatColor.GREEN));
			enchant.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = enchantHit.getItemMeta();
			meta.setDisplayName(String.format("%sEnchanted Hit", ChatColor.GREEN));
			enchantHit.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = endRod.getItemMeta();
			meta.setDisplayName(String.format("%sEnd Rod", ChatColor.GREEN));
			endRod.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = entityEffect.getItemMeta();
			meta.setDisplayName(String.format("%sEntity Effect", ChatColor.GREEN));
			entityEffect.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = explosion.getItemMeta();
			meta.setDisplayName(String.format("%sExplosion", ChatColor.GREEN));
			explosion.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = explosionEmitter.getItemMeta();
			meta.setDisplayName(String.format("%sExplosion Emitter", ChatColor.GREEN));
			explosionEmitter.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = fallingDust.getItemMeta();
			meta.setDisplayName(String.format("%sFalling Dust", ChatColor.GREEN));
			fallingDust.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = fallingLava.getItemMeta();
			meta.setDisplayName(String.format("%sFalling Lava", ChatColor.GREEN));
			fallingLava.setItemMeta(meta);
		}
		
		{
			ItemMeta meta = fallingWater.getItemMeta();
			meta.setDisplayName(String.format("%sFalling Water", ChatColor.GREEN));
			fallingWater.setItemMeta(meta);
		}
	}
	
	public ParticleShop(@NotNull CustomPlayer player) {
		super(player, null, Bukkit.createInventory(null, 54, "Particle Store"));
		List<ItemStack> icons = new ArrayList<ItemStack>(27);
		icons.add(ambientEntityEffect);
		icons.add(angryVillager);
		icons.add(barrier);
		icons.add(block);
		icons.add(bubble);
		icons.add(bubbleColumnUp);
		icons.add(bubblePop);
		icons.add(campfireCosySmoke);
		icons.add(campfireSignalSmoke);
		icons.add(cloud);
		icons.add(composter);
		icons.add(crit);
		icons.add(currentDown);
		icons.add(damageIndicator);
		icons.add(dolphin);
		icons.add(dragonBreath);
		icons.add(drippingLava);
		icons.add(drippingWater);
		icons.add(dust);
		icons.add(enchant);
		icons.add(enchantHit);
		icons.add(endRod);
		icons.add(entityEffect);
		icons.add(explosion);
		icons.add(explosionEmitter);
		icons.add(fallingDust);
		icons.add(fallingLava);
		icons.add(fallingWater);
		
		Iterator<ItemStack> iteratorOfIcons = icons.iterator();
		
		final Inventory gui = Bukkit.createInventory(null, 54);
		final ItemStack barrierItem = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
		
		for (int i = 0; i < 9; i++)
			gui.setItem(i, barrierItem);
		
		for (int i = 45; i < 54; i++)
			gui.setItem(i, barrierItem);
		
		gui.setItem(9, barrierItem);
		gui.setItem(17, barrierItem);
		gui.setItem(18, barrierItem);
		gui.setItem(26, barrierItem);
		gui.setItem(27, barrierItem);
		gui.setItem(35, barrierItem);
		gui.setItem(36, barrierItem);
		gui.setItem(44, barrierItem);
		
		for (int i = 10; i < 17; i++) {
			if (!iteratorOfIcons.hasNext())
				break;
			
			gui.setItem(i, iteratorOfIcons.next());
		}
		
		if (!iteratorOfIcons.hasNext()) {
			player.getPlayer().openInventory(gui);
			return;
		}
		
		for (int i = 19; i < 26; i++) {
			if (!iteratorOfIcons.hasNext())
				break;
			
			gui.setItem(i, iteratorOfIcons.next());
		}
		
		if (!iteratorOfIcons.hasNext()) {
			player.getPlayer().openInventory(gui);
			return;
		}
		
		for (int i = 28; i < 35; i++) {
			if (!iteratorOfIcons.hasNext())
				break;
			
			gui.setItem(i, iteratorOfIcons.next());
		}
		
		if (!iteratorOfIcons.hasNext()) {
			player.getPlayer().openInventory(gui);
			return;
		}
		
		for (int i = 37; i < 44; i++) {
			if (!iteratorOfIcons.hasNext())
				break;
			
			gui.setItem(i, iteratorOfIcons.next());
		}
	}

	
	public void clicked(InventoryClickEvent clickEvent) {
		
	}
}
