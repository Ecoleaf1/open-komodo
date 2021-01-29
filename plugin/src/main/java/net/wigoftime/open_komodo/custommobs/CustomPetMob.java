package net.wigoftime.open_komodo.custommobs;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
//import org.bukkit.craftbukkit.v1_16_R1.v1_16_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

//import net.minecraft.server.v1_16_R1.EntityCreature;
import net.minecraft.server.v1_16_R1.EntityHuman;
import net.minecraft.server.v1_16_R1.EntityTypes;
import net.minecraft.server.v1_16_R1.EnumHand;
import net.minecraft.server.v1_16_R1.EnumInteractionResult;
import net.minecraft.server.v1_16_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_16_R1.EntityCreature;
import net.wigoftime.open_komodo.etc.PrintConsole;
import net.wigoftime.open_komodo.gui.PetControl;
import net.wigoftime.open_komodo.gui.PetsGui;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class CustomPetMob extends EntityCreature
{
	// Get Owner
	private static Player owner;
	
	// CustomZombie for pets
	public CustomPetMob(EntityTypes<? extends EntityCreature> type, Location loc, Player player) 
	{
		super(type, ((CraftWorld) loc.getWorld()).getHandle());
		//super(type, ((CraftWorld) loc.getWorld()).getHandle());
		//net.minecraft.server.v1_16_R1.Entitya
		((CraftWorld)player.getLocation().getWorld()).getHandle().addEntity(this, SpawnReason.CUSTOM);
		
		// Set location
		this.setLocation(loc.getX(), loc.getY(), loc.getZ(), 1, 1);
		
		// Set owner to player.
		owner = player;
		
		// Set invulnerable
		this.setInvulnerable(true);
		
		// The targets and goals of the entity
		Set targetD;
        Set goalD;
        
        // Try get the targets and goals from the mob.
		try {
			// Get the targrts
			Field field;
			field = PathfinderGoalSelector.class.getDeclaredField("d");
			field.setAccessible(true);
			targetD = (Set) field.get(targetSelector);
			
			// Get the goals
			Field field2;
			field2 = PathfinderGoalSelector.class.getDeclaredField("d");
			field2.setAccessible(true);
			goalD = (Set) field.get(goalSelector);
			
			// Clear all targets and goals
	        targetD.clear();
	        goalD.clear();
	        
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return;
		}
	}
}
