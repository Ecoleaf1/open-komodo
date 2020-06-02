package net.wigoftime.open_komodo.objects;

import java.util.UUID;

public class FriendProfile {

	public final String currentName;
	public final UUID uuid;
	public final boolean isOnline;
	
	public FriendProfile(String currentName, UUID uuid, boolean isOnline) {
		this.currentName = currentName;
		this.uuid = uuid;
		this.isOnline = isOnline;
	}
	
	public String getUsername() {
		return currentName;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public boolean isOnline() {
		return isOnline;
	}
	
}
