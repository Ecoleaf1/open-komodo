package net.wigoftime.open_komodo.etc.systems;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.etc.*;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Rank;
import net.wigoftime.open_komodo.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.UUID;

public class RankSystem
{
	private static @NotNull HashMap<UUID, Double> pendingXP = new HashMap<UUID, Double>();
	
	private static final String rankedUp = String.format(" \n                      %s%s| %sOpen Komodo %s|%s\n       You have ranked up to the next %%s!\n       %sCongraz!!\n ",
			ChatColor.YELLOW, ChatColor.MAGIC, ChatColor.YELLOW, ChatColor.MAGIC, ChatColor.DARK_AQUA, ChatColor.DARK_AQUA);
	// The setup function, should only be used once when server starting up
	public static void setup()
	{
		// Create a runnable that loops for 10 minutes
		// Turning pending XP into actual XP that is saved
		// As well as getting paid salery
		BukkitRunnable runnable = new BukkitRunnable() {
			
			@Override
			public void run() 
			{
				giveDailyXP();
			}
		};
		
		runnable.runTaskTimerAsynchronously(Main.getPlugin(), 0, 12000); //12000
	}
	
	private static void giveDailyXP() {
		
		synchronized (pendingXP) {
			LinkedList<UUID> removeQuery = new LinkedList<UUID>();
			
			for (Entry<UUID, Double> e : pendingXP.entrySet()) {
				// Get CustomPlayer
				CustomPlayer customPlayer = CustomPlayer.get(e.getKey());
				
				if (customPlayer == null)
				{
					PrintConsole.test("loop: ");
					removeQuery.add(e.getKey());
					continue;
				}
				
				// Get Rank's ID
				int rankID;
				if (customPlayer.getRank() == null)
					rankID = 0;
				else
					rankID = customPlayer.getRank().getID();
				
				
				// get and caulcate XP
				double currentXP = customPlayer.getXP();
				double pendingXP = e.getValue();
				
				double totalXP = currentXP + pendingXP;
				
				customPlayer.setXP(totalXP);
				
				// get and caulcate the salery of points
				int pSalery;
				if (customPlayer.getRank() == null)
					pSalery = 15;
				else
					pSalery = customPlayer.getRank().getSalery(Currency.POINTS);
				
				// If has salery, pay player salery
				if (pSalery > 0)
					if (!customPlayer.isAfk())
						CurrencyClass.genPay(customPlayer, pSalery, Currency.POINTS);
				
				e.setValue(0.0);
				
				//if (rankID > 0)
					for (Rank r : Rank.getRanks()) {
						// If not next rank, skip
						if ((rankID + 1) != r.getID())
							continue;
						
						if (r.getXPPrice() <= 0)
							break;
						
						if (r.getXPPrice() > totalXP)
							break;
						
						customPlayer.getRankSystem().setRank(r);
						
						customPlayer.getPlayer().sendMessage(String.format(rankedUp, r.getPrefix()));
						customPlayer.getPlayer().playSound(customPlayer.getPlayer().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
					}
			}
			
			for (UUID uuidIndex : removeQuery)
				pendingXP.remove(uuidIndex);
		}
	}
	
	public static void lettersToXP(@NotNull Player player, @NotNull String text)
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
	
	
	public static void addPendingPoints(@NotNull Player player, double xp)
	{
		synchronized (pendingXP) {
			UUID uuid = player.getUniqueId();
			CustomPlayer playerCustomPlayer = CustomPlayer.get(uuid);
			
			// If customplayer format of player doesn't exist, cancel
			// Can happen if the server hasn't loaded all of the player information when they join
			if (playerCustomPlayer == null)
				return;
			
			if (playerCustomPlayer.isAfk())
				return;
			
			if (!pendingXP.containsKey(uuid))
				pendingXP.put(uuid, 0.0);
			
			// Add up XP
			double currentXP = pendingXP.get(uuid);
			double totalXP = currentXP + xp;
			
			pendingXP.replace(uuid, totalXP);
		}
	}
	
	public static void putPlayer(@NotNull Player player)
	{
		if (pendingXP.containsKey(player.getUniqueId()))
			return;
		
		// Add player to pendingXP list
		synchronized (pendingXP) {
			pendingXP.put(player.getUniqueId(), 0.0);
		}
	}

	public static void setRankOffline(UUID uuid, @Nullable Rank rank) {
		// Set rank in SQL Database or in player's config file in another task asynchronously to reduce the server pausing
		// from latency the server to the SQL
		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				if (SQLManager.isEnabled()) {
					SQLManager.setRankID(uuid, rank == null ? 0 : rank.getID());
					SQLManager.setXP(uuid, 0);
				} else {
					PlayerConfig.setRankID(uuid, rank == null ? 0 : rank.getID());
					PlayerConfig.setXP(uuid, 0);
				}
			}
		});
	}

	// Non-static


	private final CustomPlayer playerCustom;
	private @Nullable Rank rank;
	private double xp;

	public RankSystem(CustomPlayer playerCustom, Rank rank, double xp) {
		this.playerCustom = playerCustom;
		this.rank = rank;
		this.xp = xp;
	}

	public void setRank(@Nullable Rank rank) {
		// Set rank in SQL Database or in player's config file in another task asynchronously to reduce the server pausing
		// from latency the server to the SQL
		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				if (SQLManager.isEnabled()) {
					SQLManager.setRankID(playerCustom.getUniqueId(),rank == null ? 0 : rank.getID());
					SQLManager.setXP(playerCustom.getUniqueId(), 0);
				} else {
					PlayerConfig.setRankID(playerCustom.getUniqueId(), rank == null ? 0 : rank.getID());
					PlayerConfig.setXP(playerCustom.getUniqueId(), 0);
				}
			}
		});

		this.rank = rank;
		this.xp = xp;

		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			public void run() {
				Permissions.setUp(playerCustom);
				ServerScoreBoard.add(playerCustom);

				playerCustom.getPlayer().sendMessage(String.format("%s» %sYou have been promoted to %s!", ChatColor.GOLD, ChatColor.GRAY, rank.getName()));
			}
		}, 1);
	}

	public void setXP(double xp) {
		// Set rank in SQL Database or in player's config file in another task asynchronously to reduce the server pausing
		// from latency the server to the SQL
		Bukkit.getScheduler().runTaskAsynchronously(Main.getPlugin(), new Runnable() {
			public void run() {
				if (SQLManager.isEnabled()) {
					SQLManager.setXP(playerCustom.getUniqueId(),xp);
				} else {
					PlayerConfig.setXP(playerCustom.getUniqueId(),xp);
				}
			}
		});

		this.xp = xp;
	}

	public @Nullable Rank getRank() {
		return rank;
	}

	public double getXP() {
		return xp;
	}
}
