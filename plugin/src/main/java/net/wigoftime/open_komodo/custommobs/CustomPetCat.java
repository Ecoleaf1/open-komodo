package net.wigoftime.open_komodo.custommobs;

import java.lang.reflect.Field;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import net.minecraft.server.v1_14_R1.EntityCat;
import net.minecraft.server.v1_14_R1.EntityHuman;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.EnumHand;
import net.minecraft.server.v1_14_R1.PathfinderGoalSelector;
import net.wigoftime.open_komodo.gui.PetControl;
import net.wigoftime.open_komodo.objects.CustomPlayer;

public class CustomPetCat extends EntityCat
{
	// Get Owner
		private static Player owner;
		
		// CustomZombie for pets
		public CustomPetCat(Location loc, Player player) 
		{
			// Set what world it is in.
			super(EntityTypes.CAT, ((CraftWorld) loc.getWorld()).getHandle());
			((CraftWorld)player.getLocation().getWorld()).getHandle().addEntity(this, SpawnReason.CUSTOM);
			
			// Set location
			this.setLocation(loc.getX(), loc.getY(), loc.getZ(), 1, 1);
			
			// Set owner to player.
			owner = player;
			
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
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				return;
			}
			
			// Clear all targets and goals
	        targetD.clear();
	        goalD.clear();
		}
		
		// When entity is clicked on
	    public boolean a(EntityHuman entity, EnumHand enumhand) 
	    {
	    	if (entity.getUniqueID() == owner.getUniqueId())
	    	{
	    		CustomPlayer customOwner = CustomPlayer.get(entity.getUniqueID());
	    		PetControl.create(customOwner);
	    	}
	    	
	    	return false;
	    }
}
