package net.wigoftime.open_komodo.tutorial;

import net.md_5.bungee.api.ChatColor;
import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.actions.Rules;
import net.wigoftime.open_komodo.chat.Emote;
import net.wigoftime.open_komodo.chat.NormalMessage;
import net.wigoftime.open_komodo.chat.ShoutMessage;
import net.wigoftime.open_komodo.commands.DisplayXPCommand;
import net.wigoftime.open_komodo.etc.homesystem.AddHome;
import net.wigoftime.open_komodo.etc.homesystem.TeleportHome;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class Tutorial {
	public enum TutorialState {TALKING, SHOUTING, EMOTES, XP, SETHOME, TELEPORTHOME, RULES};
	private @NotNull TutorialState currentState = TutorialState.TALKING;
	
	final CustomPlayer player;
	
	public Tutorial(CustomPlayer player) {
		this.player = player;
	}
	
	public boolean validState(Class<?> fromClass) {
		if (fromClass == NormalMessage.class)
			return currentState == TutorialState.TALKING ? true : false;
		else if (fromClass == ShoutMessage.class)
			return currentState == TutorialState.SHOUTING ? true : false;
		else if (fromClass == Emote.class)
			return currentState == TutorialState.EMOTES ? true : false;
		else if (fromClass == DisplayXPCommand.class)
			return currentState == TutorialState.XP ? true : false;
		else if (fromClass == AddHome.class)
			return currentState == TutorialState.SETHOME ? true : false;
		else if (fromClass == TeleportHome.class)
			return currentState == TutorialState.TELEPORTHOME ? true : false;
		else if (fromClass == Rules.class)
			return currentState == TutorialState.RULES ? true : false;
		
		return false;
	}
	
	public void trigger(Class<?> fromClass) {
		if (fromClass == NormalMessage.class) {
			if (currentState == TutorialState.TALKING)
			chatTrigger();
		} else if (fromClass == ShoutMessage.class) {
			if (currentState == TutorialState.SHOUTING)
			shoutTrigger();
		} else if (fromClass == Emote.class) {
			if (currentState == TutorialState.EMOTES)
			emoteTrigger();
		} else if (fromClass == DisplayXPCommand.class) {
			if (currentState == TutorialState.XP)
			xpCommandTrigger();
		}else if (fromClass == AddHome.class) {
			if (currentState == TutorialState.SETHOME)
			sethomeCommandTrigger();
		} else if (fromClass == TeleportHome.class) {
			if (currentState == TutorialState.TELEPORTHOME)
			homeCommandTrigger();
		} else if (fromClass == Rules.class) {
			if (currentState == TutorialState.RULES)
			rulesCommandTrigger();
		}
	}
	
	
	@NotNull List<BukkitTask> listOfTasks = new LinkedList<BukkitTask>();
	private int ticksIndex = 0;
	public void sendMessage(@NotNull String message, int ticks, Object... messageFormats) {
		
		synchronized (listOfTasks) {
			ticksIndex += ticks;
			
			BukkitTask task = Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
				@Override
				public void run() {
					player.getPlayer().sendMessage(String.format(message, messageFormats));
					player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.1f, 0.0f);
				}
			}, ticksIndex);
			
			listOfTasks.add(task);
		}
	}
	
	private void cancelPreviousMessages() {
		synchronized (listOfTasks) {
			for (BukkitTask task : listOfTasks) {
				task.cancel();
			}
			
			listOfTasks = new LinkedList<BukkitTask>();
			ticksIndex = 0;
		}
	}
	
	public void begin() {
		player.getPlayer().teleport(Main.tutorialLocation);
		sendMessage("%s: %sHello!", 0, ChatColor.DARK_GRAY + "TutorialLeaf", ChatColor.GRAY);
		sendMessage("%s: %sWelcome to Open Komodo!", 80, ChatColor.DARK_GRAY + "TutorialLeaf", ChatColor.GRAY);
		sendMessage("%s: %sBefore you get started, you need to learn how to use the basics first!", 80, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY);
		sendMessage("%s: %sFirst, type something in the chat!", 100, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY);
	}
	
	private void chatTrigger() {
		cancelPreviousMessages();
		currentState = TutorialState.SHOUTING;
		
		sendMessage("%s: %sGood job!", 0, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY);
		sendMessage("%s: %sHowever, talking normally can only be heard from within %d blocks.", 100, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY, Main.getDistanceRange());
		sendMessage("%s: %sIf you want to shout a message to the whole server, put ! before the text!.", 100, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY);
		sendMessage("%s: %sTry it now!", 20, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY);
	}
	
	private void shoutTrigger() {
		cancelPreviousMessages();
		currentState = TutorialState.EMOTES;
		sendMessage("%s: %sGood job!", 0, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY);
		sendMessage("%s: %sNext, we have emotes.", 100, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY);
		sendMessage("%s: %sEmotes are another way to express yourself, or to simply have fun.", 100, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY);
		sendMessage("%s: %sEmotes can be heard across the server", 100, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY);
		sendMessage("%s: %sType in %s/emote [emote name] %sto make the emote.", 100, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY, ChatColor.GOLD, ChatColor.GRAY);
		sendMessage("%s: %sDon't know what emotes are there? Just type in %s/emote %sto get a list of emotes to choose from!", 40, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY, ChatColor.GOLD, ChatColor.GRAY);
	}
	
	private void emoteTrigger() {
		cancelPreviousMessages();
		currentState = TutorialState.XP;
		sendMessage("%s: %sGood job!", 0, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY);
		sendMessage("%s: %sNext are ranks.", 80, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY);
		sendMessage("%s: %sTo rank up, you need to earn experience by chatting on the server, exploring, and playing games like connect four.", 80, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY);
		sendMessage("%s: %sEach rank unlocks more cool and unique stuff!", 80, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY);
		sendMessage("%s: %sTo view your experience, type in %s/xp%s", 80, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY, ChatColor.GOLD, ChatColor.GRAY);
	}
	
	private void xpCommandTrigger() {
		cancelPreviousMessages();
		currentState = TutorialState.SETHOME;
		sendMessage("%s: %sGood job!", 0, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY);
		sendMessage("%s: %sTo set a home, type in %s/sethome [name]%s", 60, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY, ChatColor.GOLD, ChatColor.GRAY);
	}
	
	private void sethomeCommandTrigger() {
		cancelPreviousMessages();
		currentState = TutorialState.TELEPORTHOME;
		sendMessage("%s: %sGood job!", 0, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY);
		sendMessage("%s: %sNow teleport to that home! %s/home [name]%s", 60, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY, ChatColor.GOLD, ChatColor.GRAY);
	}
	
	private void homeCommandTrigger() {
		cancelPreviousMessages();
		currentState = TutorialState.RULES;
		sendMessage("%s: %sGood job!", 60, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY);
		sendMessage("%s: %sTo look at the rules, type in %s/rules%s", 60, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY, ChatColor.GOLD, ChatColor.GRAY);
	}
	
	private void rulesCommandTrigger() {
		cancelPreviousMessages();
		sendMessage("%s: %sGood job!", 60, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY);
		sendMessage("%s: %sFollow by the rules, and you'll be right mate!", 60, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY);
		sendMessage("%s: %sI hope you enjoy your stay here, have fun! :D", 60, ChatColor.DARK_GRAY + "TutorialLeaf",  ChatColor.GRAY);
		player.setTutorial(false);
	}
}
