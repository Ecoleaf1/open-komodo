package net.wigoftime.open_komodo.chat;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.config.PlayerConfig;
import net.wigoftime.open_komodo.config.TagConfig;
import net.wigoftime.open_komodo.etc.CurrencyClass;

public abstract class MessageFormat 
{
	private static final String format = Main.getNormalMessageFormat();
	private static final String tagFormat = Main.getTagNormalMessageFormat();
	
	private static final String modFormat = "[\"\",{\"text\":\"$G\",\"color\":\"gray\"},{\"text\":\"$S\",\"color\":\"dark_gray\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/msg $N \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Username: $N\"}]}}},{\"text\":\" : \",\"color\":\"gray\"},{\"text\":\"$M\",\"color\":\"gray\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/msg $N \"}},{\"text\":\" [¶]\",\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/mod $N\"}}]";
	private static final String tagModFormat = "[\"\",{\"text\":\"$W\",\"color\":\"gray\"},{\"text\":\" | \",\"color\":\"dark_gray\"},{\"text\":\"$G\",\"color\":\"gray\"},{\"text\":\"$S\",\"color\":\"dark_gray\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/msg $N \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Username: $N\"}]}}},{\"text\":\" : \",\"color\":\"gray\"},{\"text\":\"$M\",\"color\":\"gray\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/msg $N \"}},{\"text\":\" [¶]\",\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/mod $N\"}}]";
	
	// Convert it so the message can be understood by JSON, like /tellraw
	public static String json(Player sender, String message) 
	{
		message = message.replace("\\", "\\\\");
		message = message.replace("\"", "\\\"");
		
		String userTag = TagConfig.getTag(sender);
		
		if (userTag.equals(""))
			return MessageFormat.format(format,sender,message);
		else
			return MessageFormat.format(tagFormat,sender,message);
	}
	
	public static String jsonMod(Player sender, String message) 
	{
		message = message.replace("\\", "\\\\");
		message = message.replace("\"", "\\\"");
		
		String userTag = TagConfig.getTag(sender);
		
		if (userTag.equals(""))
			return MessageFormat.format(modFormat,sender,message);
		else
			return MessageFormat.format(tagModFormat,sender,message);
	}
	
	public static String format(String messageFormat,Player sender,String message) 
	{
		String tag = TagConfig.getTag(sender);
		String rank = PlayerConfig.getRankPrefix(sender);
		
		if (sender != null) 
		{
			messageFormat = messageFormat.replace("$N", sender.getDisplayName());
			
			if (sender.getCustomName() != null)
				messageFormat = messageFormat.replace("$S", sender.getCustomName());
			else
				messageFormat = messageFormat.replace("$S", sender.getDisplayName());
			
			messageFormat = messageFormat.replace("$W", tag);
			messageFormat = messageFormat.replace("$G", rank);
		}
		
		if (message != null)
			messageFormat = messageFormat.replace("$M", message);
		
		return messageFormat;
	}
	
	public static String format(Player player, String text)
	{
		text = text.replace("$N", player.getDisplayName());
		
		if (player.getCustomName() != null)
			text = text.replace("$S", player.getCustomName());
		else
			text = text.replace("$S", player.getDisplayName());
		
		if (TagConfig.getTag(player) != null)
			text = text.replace("$W", TagConfig.getTag(player));
		else
			text = text.replace("$W", "");
		
		text = text.replace("$G", PlayerConfig.getRankPrefix(player));
		
		text = text.replace("$T", CurrencyClass.getPoints(player) + "");
		text = text.replace("$C", CurrencyClass.getCoins(player) + "");
		
		return text;
	}

	public static String format(String messageFormat,String senderUsername,String recipientUsername,String message) 
	{
		if (senderUsername != null) 
			messageFormat = messageFormat.replace("$N", senderUsername);
		
		if (recipientUsername != null) 
			messageFormat = messageFormat.replace("$D", recipientUsername);
		
		if (message != null)
			messageFormat = messageFormat.replace("$M", message);
		
		return messageFormat;
	}
	
	public static String format(String messageFormat,Player sender,Player recipient,String message) 
	{
		if (sender != null) 
		{
			messageFormat = messageFormat.replace("$N", sender.getDisplayName());
			
			if (sender.getCustomName() == null)
				messageFormat = messageFormat.replace("$S", sender.getDisplayName());
			else
				messageFormat = messageFormat.replace("$S", sender.getCustomName());
			
			messageFormat = messageFormat.replace("$W", TagConfig.getTag(sender));
			messageFormat = messageFormat.replace("$G", PlayerConfig.getRankPrefix(sender));
		}
		
		if (recipient != null) 
		{
			messageFormat = messageFormat.replace("$D", recipient.getDisplayName());
			
			if (recipient.getCustomName() == null)
				messageFormat = messageFormat.replace("$R", recipient.getDisplayName());
			else
				messageFormat = messageFormat.replace("$R", recipient.getCustomName());
			
			messageFormat = messageFormat.replace("$E", TagConfig.getTag(recipient));
			messageFormat = messageFormat.replace("$H", PlayerConfig.getRankPrefix(recipient));
		}
		
		if (message != null)
			messageFormat = messageFormat.replace("$M", message);
		
		return messageFormat;
	}
	
	// Copy from above except changing it to Commendsender
	
	public static String format(String messageFormat,CommandSender sender,Player recipient,String message) 
	{
		if (sender != null) 
		{
			if (sender instanceof Player)
			{
				// Get sender as player
				Player player = (Player) sender;
				
				messageFormat = messageFormat.replace("$N", player.getDisplayName());
				
				if (player.getCustomName() == null)
					messageFormat = messageFormat.replace("$S", player.getDisplayName());
				else
					messageFormat = messageFormat.replace("$S", player.getCustomName());
				
				messageFormat = messageFormat.replace("$W", TagConfig.getTag(player));
				messageFormat = messageFormat.replace("$G", PlayerConfig.getRankPrefix(player));
			}
			else
			{
				messageFormat = messageFormat.replace("$N", sender.getName());
			}
		}
		
		if (recipient != null) 
		{
			messageFormat = messageFormat.replace("$D", recipient.getDisplayName());
			
			if (recipient.getCustomName() == null)
				messageFormat = messageFormat.replace("$R", recipient.getDisplayName());
			else
				messageFormat = messageFormat.replace("$R", recipient.getCustomName());
			
			messageFormat = messageFormat.replace("$E", TagConfig.getTag(recipient));
			messageFormat = messageFormat.replace("$H", PlayerConfig.getRankPrefix(recipient));
		}
		
		if (message != null)
			messageFormat = messageFormat.replace("$M", message);
		
		return messageFormat;
	}
	
	public static String format(String messageFormat,Player sender,OfflinePlayer recipient,String message) 
	{
		if (sender != null) 
		{
			messageFormat = messageFormat.replace("$N", sender.getDisplayName());
			
			if (sender.getCustomName() != null)
				messageFormat = messageFormat.replace("$S", sender.getCustomName());
			else
				messageFormat = messageFormat.replace("$S", sender.getDisplayName());
			
			messageFormat = messageFormat.replace("$W", TagConfig.getTag(sender));
			messageFormat = messageFormat.replace("$G", PlayerConfig.getRankPrefix(sender));
			}
			
			if (recipient != null) 
			messageFormat = messageFormat.replace("$D", recipient.getName());
			
			if (message != null)
				messageFormat = messageFormat.replace("$M", message);
			
		return messageFormat;
	}
	
	
	
}
