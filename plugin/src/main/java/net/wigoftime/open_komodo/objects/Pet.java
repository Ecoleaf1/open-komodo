package net.wigoftime.open_komodo.objects;

import net.minecraft.server.v1_16_R1.EntityTypes;
import net.wigoftime.open_komodo.etc.Currency;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Pet
{
	// Keep track of sets of pets
	private static @NotNull Set<Pet> pets = new HashSet<Pet>();
	
	private final String displayName;
	private final List<String> description;
	private final Permission permission;
	
	public static final Material material = Material.GHAST_SPAWN_EGG;
	
	private final int pPrice;
	private final int cPrice;
	//private final EntityType type;
	private final EntityTypes<?> type;
	
	private final int id;
	
	public Pet(@NotNull String displayName, List<String> description, Permission permission, @NotNull  EntityTypes<?> type,int id, int pPrice, int cPrice)
	{
		this.displayName = displayName;
		this.description = description;
		this.permission = permission;
		
		this.pPrice = pPrice;
		this.cPrice = cPrice;
		this.id = id;
		this.type = type;
		
		pets.add(this);
	}
	
	public @NotNull ItemStack getItem()
	{
		ItemStack petDisplayItemStack = new ItemStack(material);
		ItemMeta petDisplayItemMeta = petDisplayItemStack.getItemMeta();
		
		petDisplayItemMeta.setDisplayName(ChatColor.DARK_AQUA + displayName);
		petDisplayItemMeta.setCustomModelData(id);
		
		petDisplayItemStack.setItemMeta(petDisplayItemMeta);
		return petDisplayItemStack;
	}
	
	public String getDisplayName()
	{
		return displayName;
	}
	
	public Permission getPermission()
	{
		return permission;
	}
	
	public int getPrice(Currency currency)
	{
		if (currency == Currency.POINTS)
			return pPrice;
		else if (currency == Currency.COINS)
			return cPrice;
		else
			return -1;
	}
	
	public EntityTypes<?> getType()
	{
		return type;
	}
	
	public int getID()
	{
		return id;
	}
	
	public static @Nullable Pet getPet(int id)
	{
		for (Pet p : pets)
		{
			if (p.getID() == id)
				return p;
		}
		
		return null;
	}
	
	public static @NotNull Set<Pet> getPets()
	{
		return pets;
	}
}
