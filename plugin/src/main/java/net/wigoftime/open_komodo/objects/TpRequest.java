package net.wigoftime.open_komodo.objects;

import org.bukkit.entity.Player;

public class TpRequest 
{
	public static enum tpType 
	{
		TPA, TPAHERE
	}
	
	private final Player target;
	private final Player requester;
	private final tpType type;
	
	public TpRequest(Player target, Player requester, tpType type)
	{
		this.target = target;
		this.requester = requester;
		this.type = type;
	}
	
	public Player getTarget()
	{
		return target;
	}
	
	public Player getRequester()
	{
		return requester;
	}
	
	public tpType getType()
	{
		return type;
	}
	
	public void destroy()
	{
		this.destroy();
	}
}
