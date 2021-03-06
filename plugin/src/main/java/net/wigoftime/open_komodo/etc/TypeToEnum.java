package net.wigoftime.open_komodo.etc;

import net.minecraft.server.v1_16_R1.EntityTypes;
import org.jetbrains.annotations.NotNull;

public abstract class TypeToEnum 
{
	// Get Entity Type from a String
	public static EntityTypes<?> convertToEntityTypes(@NotNull String type)
	{
		
		switch(type)
		{
			case "AREA_EFFECT_CLOUD":
				return EntityTypes.AREA_EFFECT_CLOUD;
			case "ARMOR_STAND":
				return EntityTypes.ARMOR_STAND;
			case "ARROW":
				return EntityTypes.ARROW;
			case "BAT":
				return EntityTypes.BAT;
			case "BLAZE":
				return EntityTypes.BLAZE;
			case "BOAT":
				return EntityTypes.BOAT;
			case "CAT":
				return EntityTypes.CAT;
			case "CAVE_SPIDER":
				return EntityTypes.CAVE_SPIDER;
			case "CHEST_MINECART":
				return EntityTypes.CHEST_MINECART;
			case "CHICKEN":
				return EntityTypes.CHICKEN;
			case "COD":
				return EntityTypes.COD;
			case "COMMAND_BLOCK_MINECART":
				return EntityTypes.COMMAND_BLOCK_MINECART;
			case "COW":
				return EntityTypes.COW;
			case "CREEPER":
				return EntityTypes.CREEPER;
			case "DOLPHIN":
				return EntityTypes.DOLPHIN;
			case "DONKEY":
				return EntityTypes.DONKEY;
			case "DRAGON_FIREBALL":
				return EntityTypes.DRAGON_FIREBALL;
			case "DROWNED":
				return EntityTypes.DROWNED;
			case "EGG":
				return EntityTypes.EGG;
			case "ELDER_GUARDIAN":
				return EntityTypes.ELDER_GUARDIAN;
			case "END_CRYSTAL":
				return EntityTypes.END_CRYSTAL;
			case "ENDER_DRAGON":
				return EntityTypes.ENDER_DRAGON;
			case "ENDER_PEARL":
				return EntityTypes.ENDER_PEARL;
			case "ENDERMAN":
				return EntityTypes.ENDERMAN;
			case "ENDERMITE":
				return EntityTypes.ENDERMITE;
			case "EVOKER":
				return EntityTypes.EVOKER;
			case "EVOKER_FANGS":
				return EntityTypes.EVOKER_FANGS;
			case "EXPERIENCE_BOTTLE":
				return EntityTypes.EXPERIENCE_BOTTLE;
			case "EXPERIENCE_ORB":
				return EntityTypes.EXPERIENCE_ORB;
			case "EYE_OF_ENDER":
				return EntityTypes.EYE_OF_ENDER;
			case "FALLING_BLOCK":
				return EntityTypes.FALLING_BLOCK;
			case "FIREBALL":
				return EntityTypes.FIREBALL;
			case "FIREWORK_ROCKET":
				return EntityTypes.FIREWORK_ROCKET;
			case "FISHING_BOBBER":
				return EntityTypes.FISHING_BOBBER;
			case "FOX":
				return EntityTypes.FOX;
			case "FURNACE_MINECART":
				return EntityTypes.FURNACE_MINECART;
			case "GHAST":
				return EntityTypes.GHAST;
			case "GIANT":
				return EntityTypes.GIANT;
			case "GUARDIAN":
				return EntityTypes.GUARDIAN;
			case "HOPPER_MINECART":
				return EntityTypes.HOPPER_MINECART;
			case "HORSE":
				return EntityTypes.HORSE;
			case "HUSK":
				return EntityTypes.HUSK;
			case "ILLUSIONER":
				return EntityTypes.ILLUSIONER;
			case "IRON_GOLEM":
				return EntityTypes.IRON_GOLEM;
			case "ITEM":
				return EntityTypes.ITEM;
			case "ITEM_FRAME":
				return EntityTypes.ITEM_FRAME;
			case "LEASH_KNOT":
				return EntityTypes.LEASH_KNOT;
			case "LIGHTNING_BOLT":
				return EntityTypes.LIGHTNING_BOLT;
			case "LLAMA":
				return EntityTypes.LLAMA;
			case "LLAMA_SPIT":
				return EntityTypes.LLAMA_SPIT;
			case "MAGMA_CUBE":
				return EntityTypes.MAGMA_CUBE;
			case "MINECART":
				return EntityTypes.MINECART;
			case "MOOSHROOM":
				return EntityTypes.MOOSHROOM;
			case "MULE":
				return EntityTypes.MULE;
			case "OCELOT":
				return EntityTypes.OCELOT;
			case "PAINTING":
				return EntityTypes.PAINTING;
			case "PANDA":
				return EntityTypes.PANDA;
			case "PARROT":
				return EntityTypes.PARROT;
			case "PHANTOM":
				return EntityTypes.PHANTOM;
			case "PIG":
				return EntityTypes.PIG;
			case "PILLAGER":
				return EntityTypes.PILLAGER;
			case "PLAYER":
				return EntityTypes.PLAYER;
			case "POLAR_BEAR":
				return EntityTypes.POLAR_BEAR;
			case "POTION":
				return EntityTypes.POTION;
			case "PUFFERFISH":
				return EntityTypes.PUFFERFISH;
			case "RABBIT":
				return EntityTypes.RABBIT;
			case "RAVAGER":
				return EntityTypes.RAVAGER;
			case "SALMON":
				return EntityTypes.SALMON;
			case "SHEEP":
				return EntityTypes.SHEEP;
			case "SHULKER":
				return EntityTypes.SHULKER;
			case "SHULKER_BULLET":
				return EntityTypes.SHULKER_BULLET;
			case "SILVERFISH":
				return EntityTypes.SILVERFISH;
			case "SKELETON":
				return EntityTypes.SKELETON;
			case "SKELETON_HORSE":
				return EntityTypes.SKELETON_HORSE;
			case "SLIME":
				return EntityTypes.SLIME;
			case "SMALL_FIREBALL":
				return EntityTypes.SMALL_FIREBALL;
			case "SNOW_GOLEM":
				return EntityTypes.SNOW_GOLEM;
			case "SNOWBALL":
				return EntityTypes.SNOWBALL;
			case "SPAWNER_MINECART":
				return EntityTypes.SPAWNER_MINECART;
			case "SPECTRAL_ARROW":
				return EntityTypes.SPECTRAL_ARROW;
			case "SPIDER":
				return EntityTypes.SPIDER;
			case "SQUID":
				return EntityTypes.SQUID;
			case "STRAY":
				return EntityTypes.STRAY;
			case "TNT":
				return EntityTypes.TNT;
			case "TNT_MINECART":
				return EntityTypes.TNT_MINECART;
			case "TRADER_LLAMA":
				return EntityTypes.TRADER_LLAMA;
			case "TRIDENT":
				return EntityTypes.TRIDENT;
			case "TROPICAL_FISH":
				return EntityTypes.TROPICAL_FISH;
			case "TURTLE":
				return EntityTypes.TURTLE;
			case "VEX":
				return EntityTypes.VEX;
			case "VILLAGER":
				return EntityTypes.VILLAGER;
			case "VINDICATOR":
				return EntityTypes.VINDICATOR;
			case "WANDERING_TRADER":
				return EntityTypes.WANDERING_TRADER;
			case "WITCH":
				return EntityTypes.WITCH;
			case "WITHER_SKELETON":
				return EntityTypes.WITHER_SKELETON;
			case "WITHER_SKULL":
				return EntityTypes.WITHER_SKULL;
			case "WOLF":
				return EntityTypes.WOLF;
			case "ZOMBIE":
				return EntityTypes.ZOMBIE;
			case "ZOMBIE_HORSE":
				return EntityTypes.ZOMBIE_HORSE;
			case "ZOMBIE_PIGMAN":
				return EntityTypes.ZOMBIFIED_PIGLIN;
			case "ZOMBIE_VILLAGER":
				return EntityTypes.ZOMBIE_VILLAGER;
			
			default:
				return EntityTypes.WOLF;
		}
	}
}
