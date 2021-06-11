package net.wigoftime.open_komodo.custommobs;

import net.minecraft.server.v1_16_R1.EntityCreature;
import net.minecraft.server.v1_16_R1.EntityTypes;
import net.minecraft.server.v1_16_R1.PathfinderGoalSelector;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Set;

public class CustomPetMob extends EntityCreature
{
	// Get Owner
	private static Player owner;
	
	// CustomZombie for pets
	public CustomPetMob(@NotNull EntityTypes<? extends EntityCreature> type, @NotNull Location loc, @NotNull Player player)
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
	        
		} catch (@NotNull NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return;
		}
	}
}
