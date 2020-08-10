package net.wigoftime.open_komodo.objects;

public class SQLInfo {
	public final boolean enabled;
	public final String url;
	public final String user;
	public final String password;
	public final boolean sslEnabled;
	
	public SQLInfo(boolean enabled, String url, String user, String password, boolean sslEnabled) {
		this.enabled = enabled;
		this.url = url;
		this.user = user;
		this.password = password;
		this.sslEnabled = sslEnabled;
	}
}
