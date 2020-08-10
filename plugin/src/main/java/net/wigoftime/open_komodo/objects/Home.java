package net.wigoftime.open_komodo.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.json.simple.JSONObject;

public class Home implements Serializable {
	private static final long serialVersionUID = -7576933483734767273L;
	
	public Location location;
	public String name;
	
	public Home(String name, Location location) {
		this.name = name;
		this.location = location;
	}
	
	/*
	public static void main(String args[]) {
		JSONObject json = new JSONObject();
		
		List<JSONObject> listHomesJSON = new ArrayList<JSONObject>(3);
		
		JSONObject homeJson = new JSONObject();
		homeJson.put("Name", "Home Name");
		
		JSONObject locationJson = new JSONObject();
		locationJson.put("World Name", "Name");
		locationJson.put("x", 0);
		locationJson.put("x", 0);
		locationJson.put("x", 0);
		locationJson.put("yaw", 0);
		locationJson.put("pitch", 0);
		
		homeJson.put("Name", "Home Name");
		homeJson.put("Location", locationJson);
		
		listHomesJSON.add(homeJson);
		
		json.put("Homes", listHomesJSON);
		System.out.println(json.toJSONString());
		
		jsonToHome(json);
	}*/
	
	public static List<Home> jsonToHomes(JSONObject json) {
		ArrayList<Home> homeList = new ArrayList<Home>(json.size());
		
		if (!json.containsKey("Homes"))
			return new ArrayList<Home>(0);
		
		for (JSONObject homeJson : ((List<JSONObject>) json.get("Homes"))) {
			String homeName = (String) homeJson.get("Name");
			
			final String worldName = (String) ((JSONObject) homeJson.get("Location")).get("World Name");
			final double x = (double) ((JSONObject) homeJson.get("Location")).get("x");
			final double y = (double) ((JSONObject) homeJson.get("Location")).get("y");
			final double z = (double) ((JSONObject) homeJson.get("Location")).get("z");
			final int yaw = (int) (double) ((JSONObject) homeJson.get("Location")).get("yaw");
			final int pitch = (int) (double) ((JSONObject) homeJson.get("Location")).get("pitch");
			
			Home home = new Home(homeName, new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch));
			homeList.add(home);
		}
		
		return homeList;
	}
}
