package net.wigoftime.open_komodo.etc;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

public class CollisionSystem {
	private static final boolean enabled = false;
	
	private static Team getDefaultTeam(Scoreboard scoreboard) {
		Team team = scoreboard.getTeam("Default");
		if (team == null) {
			team = scoreboard.registerNewTeam("Default");
		}
		
		team.setOption(Option.COLLISION_RULE, OptionStatus.NEVER);
		return team;
	}
	
	public static void playerJoins(Player player) {
		
		Scoreboard scoreboard = player.getScoreboard();
		
		getDefaultTeam(scoreboard).addEntry(player.getDisplayName());
	}
}
