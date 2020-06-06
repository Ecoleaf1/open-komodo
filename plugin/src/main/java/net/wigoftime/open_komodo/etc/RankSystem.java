package net.wigoftime.open_komodo.etc;

import java.util.HashMap;
import java.util.Map.Entry;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.objects.Rank;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

abstract public class RankSystem 
{
	public static HashMap<UUID, Double> pendingXP = new HashMap<UUID, Double>();
	
	public static final String rankedUp = ChatColor.translateAlternateColorCodes('&', "&b&l" + Main.name + "&eYou sucessfully ranked up!");
	
	// The setup function, should only be used once when server starting up
	public static void setup()
	{
		
		// Create a runnable that loops for 10 minutes
		// Turning pending XP into actual XP that is saved
		// As well as getting paid salery
		BukkitRunnable runnable = new BukkitRunnable() 
		{
			
			@Override
			public void run() 
			{
				giveDailyXP();
			}
		};
		
		runnable.runTaskTimer(Main.getPlugin(), 0, 12000);
	}
	
	private static void giveDailyXP()
	{
		for (Entry<UUID, Double> e : pendingXP.entrySet())
		{
			// Get player
			Player player = Bukkit.getPlayer(e.getKey());
			
			// If player isn't online
			if (player == null)
			{
				pendingXP.remove(e.getKey());
				return;
			}
			
			// Get Player's Rank
			String rankStr = PlayerConfig.getRank(player);
			Rank rank = Rank.getRank(rankStr);
			
			// Get Rank's ID
			int rankID;
			if (rank == null)
				rankID = 0;
			else
				rankID = rank.getID();
			
			
			// get and caulcate XP
			double currentXP = PlayerConfig.getXP(player);
			double pendingXP = e.getValue();
			
			double totalXP = currentXP + pendingXP;
			
			PlayerConfig.setXP(player, totalXP);
			
			// get and caulcate the salery of points
			int pSalery;
			if (rank == null)
				pSalery = 15;
			else
				pSalery = rank.getSalery(Currency.POINTS);
			
			// If has salery, pay player salery
			if (pSalery > 0)
				CurrencyClass.genPay(player, pSalery, Currency.POINTS);
			
			e.setValue(0.0);
			
			if (rankID > 0)
				for (Rank r : Rank.getRanks())
				{
					
					// If not next rank, skip
					if ((rankID + 1) != r.getID())
						continue;
					
					if (r.getXPPrice() > totalXP || r.getXPPrice() == 0)
						break;
					
					PlayerConfig.setRank(player, r.getName());
					PlayerConfig.setXP(player, 0.0);
					
					// Play sound effect
					//player.playSound)
					
					player.sendMessage(rankedUp);
				}
		}
	}
	
	public static void addPendingPoints(Player player, double xp)
	{
		UUID uuid = player.getUniqueId();
		
		if (!pendingXP.containsKey(uuid))
			pendingXP.put(uuid, 0.0);
		
		// Add up XP
		double currentXP = pendingXP.get(uuid);
		double totalXP = currentXP + xp;
		
		pendingXP.replace(uuid, totalXP);
	}
	
	public static void lettersToXP(Player player, String text)
	{
		double xp = 0;
		char[] chars = text.toCharArray();
		
		double tempxp = 0;
		short tempcount = 0;
		for (int i = 0; i < chars.length; i++)
		{	
			if (chars[i] == ' ')
			{
				xp += tempxp;
				tempxp = 0;
				
				tempcount = 0;
				continue;
			}
			
			// If over word cap, continue til there's new word
			if (tempcount == -1)
				continue;
			
			if (tempcount > 10)
			{
				tempxp = 0;
				tempcount = -1;
				continue;
			}
			
			if (i + 2 < chars.length)
			{
				if (chars[i] == chars[i+1] && chars[i] == chars[i+2])
				{
					tempxp = 0;
					tempcount = 0;
					continue;
				}
			}
			
			
			tempxp += 0.001;
			tempcount++;
			
			// If last letter
			if (i + 1 >= chars.length)
			{
				xp += tempxp;
				tempxp = 0;
				tempcount = 0;
			}
		}
		
		addPendingPoints(player, xp);
	}
	
	public static void putPlayer(Player player)
	{
		if (pendingXP.containsKey(player.getUniqueId()))
			return;
		
		// Add player to pendingXP list
		pendingXP.put(player.getUniqueId(), 0.0);
	}
}
