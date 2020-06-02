package net.wigoftime.open_komodo.objects;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.permissions.Permission;

import net.minecraft.server.v1_14_R1.EntityTypes;
import net.wigoftime.open_komodo.etc.Currency;

public class Pet
{
	// Keep track of sets of pets
	private static Set<Pet> pets = new HashSet<Pet>();
	
	private final String displayName;
	private final List<String> description;
	private final Permission permission;
	
	private final int pPrice;
	private final int cPrice;
	//private final EntityType type;
	private final EntityTypes<?> type;
	
	private final int id;
	
	public Pet(String displayName, List<String> description, Permission permission, EntityTypes<?> type,int id, int pPrice, int cPrice)
	{
		this.displayName = displayName;
		this.description = description;
		this.permission = permission;
		
		this.pPrice = pPrice;
		this.cPrice = cPrice;
		//this.type = type;
		this.id = id;
		this.type = type;
		
		pets.add(this);
	}
	
	public String getDisplayName()
	{
		return displayName;
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
	
	/*
	public EntityType getType()
	{
		return type;
	}*/
	
	public int getID()
	{
		return id;
	}
	
	public static Pet getPet(int id)
	{
		for (Pet p : pets)
		{
			if (p.getID() == id)
				return p;
		}
		
		return null;
	}
	
	public static Set<Pet> getPets()
	{
		return pets;
	}
}
