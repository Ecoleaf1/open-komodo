package net.wigoftime.open_komodo.etc;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers;
import net.minecraft.server.v1_16_R1.EntityCreature;
import net.minecraft.server.v1_16_R1.EntityTypes;
import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.custommobs.CustomPetMob;
import net.wigoftime.open_komodo.objects.Pet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

abstract public class PetsManager 
{
	private static @NotNull HashMap<UUID, CraftCreature> pets = new HashMap<UUID, CraftCreature>();
	private static @NotNull Set<UUID> awaitingInput = new HashSet<UUID>();
	public static void create(@NotNull Player player, @NotNull Pet pet)
	{
		if (pets.containsKey(player.getUniqueId()))
			removePet(player);
		
		// Get player location
		Location loc = player.getLocation();
		
		// Get Type
		EntityTypes<? extends EntityCreature> type = (EntityTypes<? extends EntityCreature>) pet.getType();

		CustomPetMob petMob = new CustomPetMob(type, loc, player);
		CraftEntity craftPet = petMob.getBukkitEntity();

		// Put in Hashmap to keep track
		pets.put(player.getUniqueId(), (CraftCreature) craftPet);
		
		if (craftPet.getType() == EntityType.HORSE)
			((CraftHorse) craftPet).getInventory().setSaddle(new ItemStack(Material.SADDLE));
	}
	
	public static boolean isPet(UUID playerUUID, @NotNull UUID entryUUID) {
		CraftCreature petCreature = pets.get(playerUUID);
		if (petCreature == null)
			return false;
		
		if (petCreature.getUniqueId().toString().equals(entryUUID.toString()))
			return true;
		
		return false;
	}
	
	public static CraftCreature getCreature(@NotNull Player player)
	{
		return pets.get(player.getUniqueId());
	}
	
	public static void removePet(@NotNull Player player)
	{
		CraftCreature creature = pets.get(player.getUniqueId());
		
		pets.remove(player.getUniqueId());
		creature.remove();
	}
	
	public static void changeName(@NotNull Player player, String name)
	{
		if (awaitingInput.contains(player.getUniqueId()))
			awaitingInput.remove(player.getUniqueId());
			
		
		CraftCreature entity = pets.get(player.getUniqueId());
		
    	entity.setCustomName(name);
    	entity.setCustomNameVisible(true);
    	return;
	}
	
	public static void mount(@NotNull Player player)
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

					if (entityPet.getPassengers().size() > 0)
						continue;
					
					// If player not in the same world as the pet, delete.
					if (entityPet.getLocation().getWorld() != player.getWorld())
					{
						entityPet.remove();
						continue;
					}
					
					// Get player locatoin
					Location l = player.getLocation();
					
					// If entity more than 15 blocks away from player, teleport
					if (entityPet.getLocation().distance(l) > 15)
						entityPet.teleport(l);
					
					// Follow player
					entityPet.getHandle().getNavigation().a(l.getX(), l.getY(), l.getZ(), 1f);
				}
			}
		};
		
		
		// Start the loop
		run.runTaskTimer(Main.getPlugin(), 60, 60);
	}
	
	public static void setAwaitingNameInput(@NotNull Player player)
	{
		if (awaitingInput.contains(player.getUniqueId()))
			return;
		
		awaitingInput.add(player.getUniqueId());
		
		String msg = ChatColor.translateAlternateColorCodes('&', "&e&l Type in your pet name!");
		player.sendMessage(msg);
	}
	
	public static boolean awaitingNameInput(@NotNull Player player)
	{
		return awaitingInput.contains(player.getUniqueId());
	}
	
	public static void serverShuttingDown() {
		for (Entry<UUID, CraftCreature> entry : pets.entrySet())
			entry.getValue().setHealth(0);
	}

	public static void steer(PacketEvent event) {
		Player player = event.getPlayer();
		@Nullable CraftCreature pet = pets.get(player.getUniqueId());
		if (pet == null) return;

		PacketContainer packet = event.getPacket();

		boolean hasJumped = packet.getBooleans().read(0);
		if (hasJumped)
			pet.getHandle().getControllerJump().jump();

		float sideways = packet.getFloat().read(0);
		float forward = packet.getFloat().read(1);

		if (sideways == 0 && forward == 0) return;

		pet.setRotation(player.getEyeLocation().getYaw(), player.getEyeLocation().getPitch());
		pet.getHandle().getControllerMove().a(forward, sideways);
	}
}
