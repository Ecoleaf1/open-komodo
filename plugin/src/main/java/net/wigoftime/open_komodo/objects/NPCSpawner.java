package net.wigoftime.open_komodo.objects;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTEntity;
import de.tr7zw.nbtinjector.NBTInjector;
import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.gui.GUIManager;

public abstract class NPCSpawner 
{
	public static void spawn(Location location, String type, String gui) 
	{
		type = type.toLowerCase();
		
		if (gui != null)
			gui = gui.toLowerCase();
		
		EntityType entityType;
		byte entityProfession;
	
		switch (type) 
		{
			case "principal":
				entityType = EntityType.VILLAGER;
				entityProfession = 0;
			break;
		
			case "lunchman":
				entityType = EntityType.VILLAGER;
				entityProfession = 1;
			break;
			
			case "barista":
				entityType = EntityType.VILLAGER;
				entityProfession = 2;
			break;
			
			case "popseller":
				entityType = EntityType.VILLAGER;
				entityProfession = 3;
			break;
			
			case "gardner":
				entityType = EntityType.VILLAGER;
				entityProfession = 4;
			break;
			
			case "teacher":
				entityType = EntityType.VILLAGER;
				entityProfession = 5;
			break;
			
			default:
				entityType = EntityType.VILLAGER;
				entityProfession = 5;
			break;
		}	
		
		if (entityType == EntityType.VILLAGER)
		{
			Location gridLocation = new Location(location.getWorld(), Math.floor(location.getX() - 0.25), Math.floor(location.getY()),  Math.floor(location.getZ() - 0.25), location.getYaw(), location.getPitch());
			
			Entity entity = Main.world.spawnEntity(gridLocation, entityType);
			NBTEntity nbtEntity = new NBTEntity(entity);
			
			nbtEntity.setBoolean("NoAI", true);
			nbtEntity.setByte("Profession", entityProfession);
			
			if (gui == null)
				return;
			
			int guiID = GUIManager.getGuiID(gui);
			
			if (guiID == 0)
				return;
			
			entity = NBTInjector.patchEntity(entity);
			NBTCompound comp = NBTInjector.getNbtData(entity);
			
			comp.setInteger("GuiID", guiID);
		}
	}
	
}
