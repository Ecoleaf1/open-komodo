package net.wigoftime.open_komodo.objects;

public class SQLInfo {
	public final boolean enabled;
	public final String type;
	public final String host;
	public final String database;
	public final String user;
	public final String password;
	public final boolean sslEnabled;
	
	public SQLInfo(boolean enabled, String type, String database, String host, String user, String password, boolean sslEnabled) {
		this.enabled = enabled;
		this.type = type;
		this.database = database;
		this.host = host;
		this.user = user;
		this.password = password;
		this.sslEnabled = sslEnabled;
	}
}
