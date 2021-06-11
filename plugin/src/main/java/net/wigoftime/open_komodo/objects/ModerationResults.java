package net.wigoftime.open_komodo.objects;

import org.bukkit.OfflinePlayer;

import java.util.Date;

public class ModerationResults {
	public OfflinePlayer affectedPlayer;
	public final Date date;
	public final String reason;
	
	public ModerationResults(OfflinePlayer affectedPlayer, Date date, String reason) {
		this.affectedPlayer = affectedPlayer;
		this.date = date;
		this.reason = reason;
	}
}
