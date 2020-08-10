package net.wigoftime.open_komodo.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;

import dev.esophose.playerparticles.styles.DefaultStyles;

public class CustomParticle {
	public static enum ParticleType {
		PARTICLE, STYLE;
	}
	public static List<CustomParticle> particleList = new ArrayList<CustomParticle>(27);
	public static List<CustomParticle> mvpStyleList = new ArrayList<CustomParticle>(6);
	
	public final ParticleType type;
	public final ItemStack icon;
	public final Permission permission;
	
	public static void setup() {
		final ItemStack ambientEntityEffect = new ItemStack(Material.BEACON); {
			ItemMeta meta = ambientEntityEffect.getItemMeta();
			meta.setDisplayName(String.format("%sAmbient Entity Effect", ChatColor.GREEN));
			ambientEntityEffect.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, ambientEntityEffect, new Permission("playerparticles.effect.ambient_entity_effect")));
		
		final ItemStack angryVillager = new ItemStack(Material.IRON_DOOR); {
			ItemMeta meta = angryVillager.getItemMeta();
			meta.setDisplayName(String.format("%sAngry Villager", ChatColor.GREEN));
			angryVillager.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, angryVillager, new Permission("playerparticles.effect.angry_villager")));
		
		/*
		final ItemStack barrier = new ItemStack(Material.BARRIER); {
			ItemMeta meta = barrier.getItemMeta();
			meta.setDisplayName(String.format("%sBarrier", ChatColor.GREEN));
			barrier.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, barrier, new Permission("playerparticles.effect.barrier")));
		*/
		
		final ItemStack block = new ItemStack(Material.STONE); {
			ItemMeta meta = block.getItemMeta();
			meta.setDisplayName(String.format("%sBlock", ChatColor.GREEN));
			block.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, block, new Permission("playerparticles.effect.block")));
		
		final ItemStack bubble = new ItemStack(Material.BUBBLE_CORAL); {
			ItemMeta meta = bubble.getItemMeta();
			meta.setDisplayName(String.format("%sBubble", ChatColor.GREEN));
			bubble.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, bubble, new Permission("playerparticles.effect.bubble")));
		
		final ItemStack bubbleColumnUp = new ItemStack(Material.MAGMA_BLOCK); {
			ItemMeta meta = bubbleColumnUp.getItemMeta();
			meta.setDisplayName(String.format("%sBubble Column Up", ChatColor.GREEN));
			bubbleColumnUp.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, bubbleColumnUp, new Permission("playerparticles.effect.bubble_column_up")));
		
		final ItemStack bubblePop = new ItemStack(Material.BUBBLE_CORAL_FAN); {
			ItemMeta meta = bubblePop.getItemMeta();
			meta.setDisplayName(String.format("%sBubble Pop", ChatColor.GREEN));
			bubblePop.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, bubblePop, new Permission("playerparticles.effect.bubble_pop")));
		
		final ItemStack campfireCosySmoke = new ItemStack(Material.CAMPFIRE); {
			ItemMeta meta = campfireCosySmoke.getItemMeta();
			meta.setDisplayName(String.format("%sCampfire Cosy Smoke", ChatColor.GREEN));
			campfireCosySmoke.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, campfireCosySmoke, new Permission("playerparticles.effect.campfire_cosy_smoke")));
		
		final ItemStack campfireSignalSmoke = new ItemStack(Material.REDSTONE_TORCH); {
			ItemMeta meta = campfireSignalSmoke.getItemMeta();
			meta.setDisplayName(String.format("%sCampfire Signal Smoke", ChatColor.GREEN));
			campfireSignalSmoke.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, campfireSignalSmoke, new Permission("playerparticles.effect.campfire_signal_smoke")));
		
		final ItemStack cloud = new ItemStack(Material.WHITE_WOOL); {
			ItemMeta meta = cloud.getItemMeta();
			meta.setDisplayName(String.format("%sCloud", ChatColor.GREEN));
			cloud.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, cloud, new Permission("playerparticles.effect.cloud")));
		
		final ItemStack composter = new ItemStack(Material.COMPOSTER); {
			ItemMeta meta = composter.getItemMeta();
			meta.setDisplayName(String.format("%sComposter", ChatColor.GREEN));
			composter.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, composter, new Permission("playerparticles.effect.composter")));
		
		final ItemStack crit = new ItemStack(Material.IRON_SWORD); {
			ItemMeta meta = crit.getItemMeta();
			meta.setDisplayName(String.format("%sCrit", ChatColor.GREEN));
			crit.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, crit, new Permission("playerparticles.effect.crit")));
		
		final ItemStack currentDown = new ItemStack(Material.SOUL_SAND); {
			ItemMeta meta = currentDown.getItemMeta();
			meta.setDisplayName(String.format("%sCurrent Down", ChatColor.GREEN));
			currentDown.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, currentDown, new Permission("playerparticles.effect.current_down")));
		
		final ItemStack damageIndicator = new ItemStack(Material.BOW); {
			ItemMeta meta = damageIndicator.getItemMeta();
			meta.setDisplayName(String.format("%sDamage Indicator", ChatColor.GREEN));
			damageIndicator.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, damageIndicator, new Permission("playerparticles.effect.damage_indicator")));
		
		final ItemStack dolphin = new ItemStack(Material.DOLPHIN_SPAWN_EGG); {
			ItemMeta meta = dolphin.getItemMeta();
			meta.setDisplayName(String.format("%sDolphin", ChatColor.GREEN));
			dolphin.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, dolphin, new Permission("playerparticles.effect.dolphin")));
		
		final ItemStack dragonBreath = new ItemStack(Material.DRAGON_BREATH); {
			ItemMeta meta = dragonBreath.getItemMeta();
			meta.setDisplayName(String.format("%sDragon Breath", ChatColor.GREEN));
			dragonBreath.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, dragonBreath, new Permission("playerparticles.effect.dragon_breath")));
		
		final ItemStack drippingLava = new ItemStack(Material.LAVA_BUCKET); {
			ItemMeta meta = drippingLava.getItemMeta();
			meta.setDisplayName(String.format("%sDripping Lava", ChatColor.GREEN));
			drippingLava.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, drippingLava, new Permission("playerparticles.effect.dripping_lava")));
		
		final ItemStack drippingWater = new ItemStack(Material.WATER_BUCKET); {
			ItemMeta meta = drippingWater.getItemMeta();
			meta.setDisplayName(String.format("%sDripping Water", ChatColor.GREEN));
			drippingWater.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, drippingWater, new Permission("playerparticles.effect.dripping_water")));
		
		final ItemStack dust = new ItemStack(Material.REDSTONE); {
			ItemMeta meta = dust.getItemMeta();
			meta.setDisplayName(String.format("%sDust", ChatColor.GREEN));
			dust.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, dust, new Permission("playerparticles.effect.dust")));
		
		final ItemStack enchant = new ItemStack(Material.ENCHANTING_TABLE); {
			ItemMeta meta = enchant.getItemMeta();
			meta.setDisplayName(String.format("%sEnchant", ChatColor.GREEN));
			enchant.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, enchant, new Permission("playerparticles.effect.enchant")));
		
		final ItemStack enchantHit = new ItemStack(Material.DIAMOND_SWORD); {
			ItemMeta meta = enchantHit.getItemMeta();
			meta.setDisplayName(String.format("%sEnchanted Hit", ChatColor.GREEN));
			enchantHit.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, enchantHit, new Permission("playerparticles.effect.enchanted_hit")));
		
		final ItemStack endRod = new ItemStack(Material.END_ROD); {
			ItemMeta meta = endRod.getItemMeta();
			meta.setDisplayName(String.format("%sEnd Rod", ChatColor.GREEN));
			endRod.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, endRod, new Permission("playerparticles.effect.end_rod")));
		
		final ItemStack entityEffect = new ItemStack(Material.GLOWSTONE_DUST); {
			ItemMeta meta = entityEffect.getItemMeta();
			meta.setDisplayName(String.format("%sEntity Effect", ChatColor.GREEN));
			entityEffect.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, entityEffect, new Permission("playerparticles.effect.entity_effect")));
		
		/*
		final ItemStack explosion = new ItemStack(Material.FIRE_CHARGE); {
			ItemMeta meta = explosion.getItemMeta();
			meta.setDisplayName(String.format("%sExplosion", ChatColor.GREEN));
			explosion.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, explosion, new Permission("playerparticles.effect.explosion")));
		
		final ItemStack explosionEmitter = new ItemStack(Material.TNT); {
			ItemMeta meta = explosionEmitter.getItemMeta();
			meta.setDisplayName(String.format("%sExplosion Emitter", ChatColor.GREEN));
			explosionEmitter.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, explosionEmitter, new Permission("playerparticles.effect.explosionEmitter")));
		 */
		
		final ItemStack fallingDust = new ItemStack(Material.SAND); {
			ItemMeta meta = fallingDust.getItemMeta();
			meta.setDisplayName(String.format("%sFalling Dust", ChatColor.GREEN));
			fallingDust.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, fallingDust, new Permission("playerparticles.effect.falling_dust")));
		
		final ItemStack fallingLava = new ItemStack(Material.RED_DYE); {
			ItemMeta meta = fallingLava.getItemMeta();
			meta.setDisplayName(String.format("%sFalling Lava", ChatColor.GREEN));
			fallingLava.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, fallingLava, new Permission("playerparticles.effect.falling_lava")));
		
		final ItemStack fallingWater = new ItemStack(Material.BLUE_DYE); {
			ItemMeta meta = fallingWater.getItemMeta();
			meta.setDisplayName(String.format("%sFalling Water", ChatColor.GREEN));
			fallingWater.setItemMeta(meta);
		}
		particleList.add(new CustomParticle(ParticleType.PARTICLE, fallingWater, new Permission("playerparticles.effect.falling_water")));
		
		// Styles
		final ItemStack feetStyle = new ItemStack(DefaultStyles.FEET.getGuiIconMaterial()); {
			ItemMeta meta = feetStyle.getItemMeta();
			meta.setDisplayName(String.format("%s%s",ChatColor.GREEN, DefaultStyles.FEET.getName()));
			feetStyle.setItemMeta(meta);
			mvpStyleList.add(new CustomParticle(ParticleType.STYLE, feetStyle, new Permission("playerparticles.style.feet")));
		}
		final ItemStack moveStyle = new ItemStack(DefaultStyles.MOVE.getGuiIconMaterial()); {
			ItemMeta meta = moveStyle.getItemMeta();
			meta.setDisplayName(String.format("%s%s",ChatColor.GREEN, DefaultStyles.MOVE.getName()));
			moveStyle.setItemMeta(meta);
			mvpStyleList.add(new CustomParticle(ParticleType.STYLE, moveStyle, new Permission("playerparticles.style.move")));
		}
		final ItemStack normalStyle = new ItemStack(DefaultStyles.NORMAL.getGuiIconMaterial()); {
			ItemMeta meta = normalStyle.getItemMeta();
			meta.setDisplayName(String.format("%s%s",ChatColor.GREEN, DefaultStyles.NORMAL.getName()));
			normalStyle.setItemMeta(meta);
			mvpStyleList.add(new CustomParticle(ParticleType.STYLE, normalStyle, new Permission("playerparticles.style.normal")));
		}
		final ItemStack overheadStyle = new ItemStack(DefaultStyles.OVERHEAD.getGuiIconMaterial()); {
			ItemMeta meta = overheadStyle.getItemMeta();
			meta.setDisplayName(String.format("%s%s",ChatColor.GREEN, DefaultStyles.OVERHEAD.getName()));
			overheadStyle.setItemMeta(meta);
			mvpStyleList.add(new CustomParticle(ParticleType.STYLE, overheadStyle, new Permission("playerparticles.style.overhead")));
		}
		final ItemStack pointStyle = new ItemStack(DefaultStyles.POINT.getGuiIconMaterial()); {
			ItemMeta meta = pointStyle.getItemMeta();
			meta.setDisplayName(String.format("%s%s",ChatColor.GREEN, DefaultStyles.POINT.getName()));
			pointStyle.setItemMeta(meta);
			mvpStyleList.add(new CustomParticle(ParticleType.STYLE, pointStyle, new Permission("playerparticles.style.point")));
		}
		final ItemStack spinStyle = new ItemStack(DefaultStyles.SPIN.getGuiIconMaterial()); {
			ItemMeta meta = spinStyle.getItemMeta();
			meta.setDisplayName(String.format("%s%s",ChatColor.GREEN, DefaultStyles.SPIN.getName()));
			spinStyle.setItemMeta(meta);
			mvpStyleList.add(new CustomParticle(ParticleType.STYLE, spinStyle, new Permission("playerparticles.style.spin")));
		}
	}
	
	public CustomParticle(ParticleType type, ItemStack icon, Permission permission) {
		this.type = type;
		this.icon = icon;
		this.permission = permission;
	}
	
	public CustomParticle(CustomParticle customParticle) {
		this.type = customParticle.type;
		this.icon = customParticle.icon;
		this.permission = customParticle.permission;
	}
}
