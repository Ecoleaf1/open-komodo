package net.wigoftime.open_komodo.chat;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.Currency;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import net.wigoftime.open_komodo.objects.Rank;

public abstract class MessageFormat 
{
	private static final String format = Main.getNormalMessageFormat();
	private static final String tagFormat = Main.getTagNormalMessageFormat();
	
	private static final String modFormat = "[\"\",{\"text\":\"$G\",\"color\":\"gray\"},{\"text\":\"$S\",\"color\":\"dark_gray\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/msg $N \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Username: $N\"}]}}},{\"text\":\" : \",\"color\":\"gray\"},{\"text\":\"$M\",\"color\":\"gray\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/msg $N \"}},{\"text\":\" [¶]\",\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/mod $N\"}}]";
	private static final String tagModFormat = "[\"\",{\"text\":\"$W\",\"color\":\"gray\"},{\"text\":\" | \",\"color\":\"dark_gray\"},{\"text\":\"$G\",\"color\":\"gray\"},{\"text\":\"$S\",\"color\":\"dark_gray\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/msg $N \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Username: $N\"}]}}},{\"text\":\" : \",\"color\":\"gray\"},{\"text\":\"$M\",\"color\":\"gray\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/msg $N \"}},{\"text\":\" [¶]\",\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/mod $N\"}}]";
	
	// Convert it so the message can be understood by JSON, like /tellraw
	public static String json(CustomPlayer senderCustomPlayer, String message) 
	{
		message = message.replace("\\", "\\\\");
		message = message.replace("\"", "\\\"");
		
		if (senderCustomPlayer.getTagDisplay().equals(""))
			return MessageFormat.format(format,senderCustomPlayer,message);
		else
			return MessageFormat.format(tagFormat, senderCustomPlayer,message);
	}
	
	public static String jsonMod(CustomPlayer senderCustomPlayer, String message) 
	{
		message = message.replace("\\", "\\\\");
		message = message.replace("\"", "\\\"");
		
		if (senderCustomPlayer.getTagDisplay().equals(""))
			return MessageFormat.format(modFormat, senderCustomPlayer, message);
		else
			return MessageFormat.format(tagModFormat, senderCustomPlayer, message);
	}
	
	public static String format(String messageFormat,CustomPlayer customSender,String message) 
	{
		
		if (customSender != null) 
		{
			Rank rank = customSender.getRank();
			
			String rankPrefix;
			
			if (rank == null)
				rankPrefix = "";
			else
				rankPrefix = customSender.getRank().getPrefix();
			
			messageFormat = messageFormat.replace("$N", customSender.getPlayer().getDisplayName());
			
			if (customSender.getPlayer().getCustomName() != null)
				messageFormat = messageFormat.replace("$S", customSender.getPlayer().getCustomName());
			else
				messageFormat = messageFormat.replace("$S", customSender.getPlayer().getDisplayName());
			
			messageFormat = messageFormat.replace("$W", customSender.getTagDisplay());
			
			messageFormat = messageFormat.replace("$G", rankPrefix);
		}
		
		if (message != null)
			messageFormat = messageFormat.replace("$M", message);
		
		return messageFormat;
	}
	
	public static String format(CustomPlayer customPlayer, String text)
	{
		text = text.replace("$N", customPlayer.getPlayer().getDisplayName());
		
		if (customPlayer.getPlayer().getCustomName() != null)
			text = text.replace("$S", customPlayer.getPlayer().getCustomName());
		else
			text = text.replace("$S", customPlayer.getPlayer().getDisplayName());
		
		if (customPlayer.getTagDisplay().equals(""))
			text = text.replace("$W", "");
		else
			text = text.replace("$W", customPlayer.getTagDisplay());
		
		Rank rank = customPlayer.getRank();
		if (rank == null)
			text = text.replace("$G", "");
		else
			text = text.replace("$G", customPlayer.getRank().getPrefix());
		
		text = text.replace("$T", customPlayer.getCurrency(Currency.POINTS)+"");
		text = text.replace("$C", customPlayer.getCurrency(Currency.COINS)+"");
		
		return text;
	}

	public static String format(String messageFormat,String senderUsername,String recipientUsername,String message) {
		if (senderUsername != null) 
			messageFormat = messageFormat.replace("$N", senderUsername);
		
		if (recipientUsername != null) 
			messageFormat = messageFormat.replace("$D", recipientUsername);
		
		if (message != null)
			messageFormat = messageFormat.replace("$M", message);
		
		return messageFormat;
	}
	
	public static String format(String messageFormat,CustomPlayer customPlayerSender,CustomPlayer customPlayerRecipient,String message) {
		//  If there is a sender, format sender
		if (customPlayerSender != null) {
			messageFormat = messageFormat.replace("$N", customPlayerSender.getPlayer().getDisplayName());
			
			if (customPlayerSender.getPlayer().getCustomName() == null)
				messageFormat = messageFormat.replace("$S", customPlayerSender.getPlayer().getDisplayName());
			else
				messageFormat = messageFormat.replace("$S", customPlayerSender.getPlayer().getCustomName());
			
			messageFormat = messageFormat.replace("$W", customPlayerSender.getTagDisplay());
			
			Rank rank = customPlayerSender.getRank();
			
			if (rank == null)
				messageFormat = messageFormat.replace("$G", "");
			else
				messageFormat = messageFormat.replace("$G", customPlayerSender.getRank().getPrefix());
		}
		
	//  If there is a recipient, format recipient
		if (customPlayerRecipient != null) 
		{
			messageFormat = messageFormat.replace("$D", customPlayerRecipient.getPlayer().getDisplayName());
			
			if (customPlayerRecipient.getPlayer().getCustomName() == null)
				messageFormat = messageFormat.replace("$R", customPlayerRecipient.getPlayer().getDisplayName());
			else
				messageFormat = messageFormat.replace("$R", customPlayerRecipient.getPlayer().getCustomName());
			
			messageFormat = messageFormat.replace("$E", customPlayerRecipient.getTagDisplay());
			
			Rank rank = customPlayerRecipient.getRank();
			if (rank == null)
				messageFormat = messageFormat.replace("$H", "");
			else
				messageFormat = messageFormat.replace("$H", customPlayerRecipient.getRank().getPrefix());
		}
		
		if (message != null)
			messageFormat = messageFormat.replace("$M", message);
		
		return messageFormat;
	}
	
	// Copy from above except changing it to Commendsender
	
	public static String format(String messageFormat,CommandSender sender,OfflinePlayer recipient,String message) 
	{
		if (sender instanceof Player)
			if (recipient.isOnline())
				return format(messageFormat, CustomPlayer.get(((Player) sender).getUniqueId()), CustomPlayer.get(recipient.getUniqueId()), message);
			else
				return formatOfflineRecipient(messageFormat, CustomPlayer.get(((Player) sender).getUniqueId()), recipient, message);
		else
			if (recipient.isOnline())
				return format(messageFormat, null, CustomPlayer.get(recipient.getUniqueId()), message);
			else
				return formatOfflineRecipient(messageFormat, null, recipient, message);
		
		/*
		if (sender instanceof Player)
			if (recipient.isOnline())
			return formatOfflineRecipient(messageFormat, CustomPlayer.get(((Player) sender).getUniqueId()), recipient, message);
		else {
			messageFormat = messageFormat.replace("$N", "CONSOLE");
			//return format(messageFormat, sender, recipient, message);
			return messageFormat;
		}*/
	}
	
	public static String formatOfflineRecipient(String messageFormat,CustomPlayer senderCustomPlayer,OfflinePlayer recipient,String message) 
	{
		if (senderCustomPlayer != null) 
		{
			messageFormat = messageFormat.replace("$N", senderCustomPlayer.getPlayer().getDisplayName());
			
			if (senderCustomPlayer.getPlayer().getCustomName() != null)
				messageFormat = messageFormat.replace("$S", senderCustomPlayer.getPlayer().getCustomName());
			else
				messageFormat = messageFormat.replace("$S", senderCustomPlayer.getPlayer().getDisplayName());
			
			messageFormat = messageFormat.replace("$W", senderCustomPlayer.getTagDisplay());
			
			Rank senderRank = senderCustomPlayer.getRank();
			if (senderRank == null)
			messageFormat = messageFormat.replace("$G", "");
			else
				messageFormat = messageFormat.replace("$G", CustomPlayer.getRankOffline(senderCustomPlayer.getUniqueId()).getPrefix());
			}
			
			if (recipient != null) 
			messageFormat = messageFormat.replace("$D", recipient.getName());
			
			if (message != null)
				messageFormat = messageFormat.replace("$M", message);
			
		return messageFormat;
	}
	
	
	
}
