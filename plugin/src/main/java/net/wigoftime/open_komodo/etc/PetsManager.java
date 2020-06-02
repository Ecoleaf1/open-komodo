package net.wigoftime.open_komodo.etc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.CraftServer;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_14_R1.EntityCreature;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.custommobs.CustomPetMob;
import net.wigoftime.open_komodo.objects.Pet;

abstract public class PetsManager 
{
	private static HashMap<UUID, CraftCreature> pets = new HashMap<UUID, CraftCreature>();
	private static Set<UUID> awaitingInput = new HashSet<UUID>();
	
	public static void create(Player player, Pet pet)
	{
		if (pets.containsKey(player.getUniqueId()))
			removePet(player);
		
		// Get player location
		Location loc = player.getLocation();
		
		// Get Pet Entity
		CraftCreature entityPet;
		
		// Get Type
		EntityTypes<? extends EntityCreature> type = (EntityTypes<? extends EntityCreature>) pet.getType();
		
		// Spawn Pet
		entityPet = new CraftCreature((CraftServer) Bukkit.getServer(), new CustomPetMob(type, loc, player));
		
		// Put in Hashmap to keep track
		pets.put(player.getUniqueId(), entityPet);
		
		if (entityPet.getType() == EntityType.HORSE)
			((CraftHorse) entityPet).getInventory().setSaddle(new ItemStack(Material.SADDLE));
		
	}
	
	public static CraftCreature getCreature(Player player)
	{
		return pets.get(player.getUniqueId());
	}
	
	public static void removePet(Player player)
	{
		PrintConsole.test("Test #003");
		CraftCreature creature = pets.get(player.getUniqueId());
		
		pets.remove(player.getUniqueId());
		creature.remove();
	}
	
	public static void changeName(Player player, String name)
	{
		if (awaitingInput.contains(player.getUniqueId()))
			awaitingInput.remove(player.getUniqueId());
			
		
		CraftCreature entity = pets.get(player.getUniqueId());
		
    	entity.setCustomName(name);
    	entity.setCustomNameVisible(true);
    	return;
	}
	
	public static void mount(Player player)
	{
		CraftCreature pet  = pets.get(player.getUniqueId());
		
		pet.addPassenger(player);
	}
	
	public static void startLoop()
	{
		// Make a loop
		BukkitRunnable run = new BukkitRunnable() 
		{
			
			@Override
			public void run() 
			{
				for (Entry<UUID, CraftCreature> entry : pets.entrySet())
				{
					Player player = Bukkit.getPlayer(entry.getKey());
					CraftCreature entityPet = entry.getValue();
						
					PrintConsole.test("Entry " +entry.getKey());
					
					// If pet in the list is null, delete it and stop loop
					if (!pets.containsValue(entityPet))
					{
						entityPet.remove();
						
						continue;
					}
					
					
					if (player == null)
					{
						pets.remove(entry.getKey());
						entityPet.remove();
						continue;
					}
					
					// If player not in the same world as the pet, delete.
					if (entityPet.getLocation().getWorld() != player.getWorld())
					{
						entityPet.remove();
						continue;
					}
					
					// Get player locatoin
					Location l = player.getLocation();
					
					// If entity more than 13 blocks away from player, teleport
					if (entityPet.getLocation().distance(l) > 15)
						entityPet.teleport(l);
					
					// Follow player
					entityPet.getHandle().getNavigation().a(l.getX(), l.getY(), l.getZ(), 0.5);
				}
			}
		};
		
		
		// Start the loop
		run.runTaskTimer(Main.getPlugin(), 60, 60);
	}
	
	public static void setAwaitingNameInput(Player player)
	{
		if (awaitingInput.contains(player.getUniqueId()))
			return;
		
		awaitingInput.add(player.getUniqueId());
		
		String msg = ChatColor.translateAlternateColorCodes('&', "&e&l Type in your pet name!");
		player.sendMessage(msg);
	}
	
	public static boolean awaitingNameInput(Player player)
	{
		return awaitingInput.contains(player.getUniqueId());
	}
}
