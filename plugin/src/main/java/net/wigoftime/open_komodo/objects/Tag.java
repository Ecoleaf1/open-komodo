package net.wigoftime.open_komodo.objects;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import net.wigoftime.open_komodo.config.TagConfig;

public class Tag {

	//private static HashMap<String, Tag> tags = new HashMap<String, Tag>();
	private static List<Tag> tags = new LinkedList<Tag>();
	
	private final String name;
	private final String display;
	private final int pointsPrice;
	private final int coinsPrice;
	
	public Tag(String name, String display, int pointsPrice, int coinsPrice) {
		
		this.name = name;
		this.display = display;
		this.pointsPrice = pointsPrice;
		this.coinsPrice = coinsPrice;
		
		tags.add(this);
	}
	
	public String getName() {
		return name;
	}
	
	public String getDisplay() {
		return display;
	}
	
	public int getPointPrice() {
		return pointsPrice;
	}
	
	public int getCoinPrice() {
		return coinsPrice;
	}
	
	public static List<Tag> getList() {
		return tags;
	}
	
	public static Tag findByDisplay(String display) {
		for (Tag t : tags) {
			
			if (t.getDisplay().equals(display))
				return t;
			
		}
		return null;
	}
	
	public static void setUp() {
		File config = TagConfig.get();
		YamlConfiguration configYaml = YamlConfiguration.loadConfiguration(config);
		ConfigurationSection tagSection =  configYaml.getConfigurationSection("Tags");
		
		for (String k :tagSection.getKeys(false)) {
			ConfigurationSection keySection = tagSection.getConfigurationSection(k);
			
			String name = keySection.getString("Name");
			String display = keySection.getString("Name");
			
			int pointsPrice;
			if (keySection.contains("Points Price"))
					pointsPrice = keySection.getInt("Points Price");
			else
				pointsPrice = -1;
				
			
			int coinPrice;
			if (keySection.contains("Coins Price"))
				coinPrice = keySection.getInt("Coins Price");
			else
				coinPrice = -1;
			
			new Tag(name,display,pointsPrice,coinPrice);
		}
		
		
	}
	
}
