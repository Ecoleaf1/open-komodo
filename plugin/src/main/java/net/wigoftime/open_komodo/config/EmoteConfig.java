package net.wigoftime.open_komodo.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.chat.Emote;
import net.wigoftime.open_komodo.etc.PrintConsole;

abstract public class EmoteConfig 
{

	private static final File config = new File(Main.dataFolderPath+"/Emotes.yml");
	
	public static void create() 
	{
		
		if (config.exists())
			return;
		
		try {
			config.createNewFile();
		} catch (IOException e) {
		PrintConsole.print("Couldn't create Emotes.yml");
		}
		
		FileWriter fw;
		
		try {
			fw = new FileWriter(config);
			
			fw.write("Emotes:\n");
			
			fw.write("  Hug:\n");
				fw.write("    name: \"hug\"\n");
				fw.write("    command: \"/hug\"\n");
				fw.write("    self: \"&r&b$S&r&3 opens their arms for a hug.\"\n");
				fw.write("    others: \"&r&b$S&r&3 hugs &r&b$R&r&3.\"\n");
			fw.write("\n");
			
			fw.write("  Cry:\n");
				fw.write("    name: \"cry\"\n");
				fw.write("    command: \"/cry\"\n");
				fw.write("    self: \"&r&b$S&r&3 starts to cry.\"\n");
			fw.write("\n");
			
			fw.write("  Giggle:\n");
				fw.write("    name: \"giggle\"\n");
				fw.write("    command: \"/giggle\"\n");
				fw.write("    self: \"&r&b$S&r&3 giggles!\"\n");
				fw.write("    others: \"&r&b$S&r&3 giggles at &r&b$R&r&3!\"\n"); // Made up? XD
			fw.write("\n");
			
			fw.write("  Laugh:\n");
				fw.write("    name: \"laugh\"\n");
				fw.write("    command: \"/laugh\"\n");
				fw.write("    self: \"&r&b$S&r&3 laughs!\"\n");
				fw.write("    others: \"&r&b$S&r&3 laughs at &r&b$R&r&3!\"\n");
			fw.write("\n");
			
			fw.write("  Poke:\n");
				fw.write("    name: \"poke\"\n");
				fw.write("    command: \"/poke\"\n");
				fw.write("    self: \"&r&b$S&r&3 opens their arms for a hug.\"\n");
				fw.write("    others: \"&r&b$S&r&3 Hugs &r&b$R&r&3.\"\n");
			fw.write("\n");
			
			fw.write("  Poke:\n");
				fw.write("    name: \"poke\"\n");
				fw.write("    command: \"/poke\"\n");
				fw.write("    self: \"&r&b$S&r&3 poked themselves\"\n");
				fw.write("    others: \"&r&b$S&r&3 pokes &r&b$R&r&3.\"\n");
			fw.write("\n");
			
			fw.write("  Push:\n");
				fw.write("    name: \"push\"\n");
				fw.write("    command: \"/push\"\n");
				fw.write("    self: \"&r&b$S&r&3 pushes the air!\"\n");
				fw.write("    others: \"&r&b$S&r&3 pushes &r&b$R&r&3.\"\n");
			fw.write("\n");
			
			fw.write("  Run:\n");
				fw.write("    name: \"run\"\n");
				fw.write("    command: \"/run\"\n");
				fw.write("    self: \"&r&b$S&r&3 runs!\"\n");
				fw.write("    others: \"&r&b$S&r&3 runs at &r&b$R&r&3!\"\n");
			fw.write("\n");
			
			fw.write("  Slap:\n");
				fw.write("    name: \"slap\"\n");
				fw.write("    command: \"/slap\"\n");
				fw.write("    self: \"&r&b$S&r&3 slaps themselves!\"\n");
				fw.write("    others: \"&r&b$S&r&3 slaps &r&b$R&r&3!\"\n");
			fw.write("\n");
			
			fw.write("  Stab:\n");
				fw.write("    name: \"stab\"\n");
				fw.write("    command: \"/stab\"\n");
				fw.write("    self: \"&r&b$S&r&3 sharpen their knife!\"\n");
				fw.write("    others: \"&r&b$S&r&3 stabs &r&b$R&r&3!\"\n");
			fw.write("\n");
			
			fw.write("  Smile:\n");
				fw.write("    name: \"smile\"\n");
				fw.write("    command: \"/smile\"\n");
				fw.write("    self: \"&r&b$S&r&3 smiles.\"\n");
				fw.write("    others: \"&r&b$S&r&3 smiles at &r&b$R&r&3.\"\n");
			fw.write("\n");
			
			fw.write("  Text:\n");
				fw.write("    name: \"text\"\n");
				fw.write("    self: \"&r&b$S&r&3 texts.\"\n");
				fw.write("    others: \"&r&b$S&r&3 sends &r&b$R&r&3 a text message on the phone!\"\n");
			fw.write("\n");
			
			fw.write("  Blush:\n");
				fw.write("    name: \"blush\"\n");
				fw.write("    command: \"/blush\"\n");
				fw.write("    self: \"&r&b$S&r&3 blushes..\"\n");
				fw.write("    others: \"&r&b$S&r&3 blushes at &r&b$R&r&3..\"\n");
			fw.write("\n");
			
			fw.write("  Kiss:\n");
				fw.write("    name: \"kiss\"\n");
				fw.write("    command: \"/kiss\"\n");
				fw.write("    self: \"&r&b$S&r&3 kisses themselves\"\n");
				fw.write("    others: \"&r&b$S&r&3 kisses &r&b$R&r&3..\"\n");
			fw.write("\n");
			
			fw.write("  Clap:\n");
				fw.write("    name: \"clap\"\n");
				fw.write("    command: \"/clap\"\n");
				fw.write("    self: \"&r&b$S&r&3 claps!\"\n");
				fw.write("    others: \"&r&b$S&r&3 claps at &r&b$R&r&3!\"\n");
			fw.write("\n");
			
			fw.write("  Punch:\n");
				fw.write("    name: \"punch\"\n");
				fw.write("    command: \"/punch\"\n");
				fw.write("    self: \"&r&b$S&r&3 punches!\"\n");
				fw.write("    others: \"&r&b$S&r&3 punches &r&b$R&r&3!\"\n");
			fw.write("\n");
			
			fw.write("  Kick:\n");
				fw.write("    name: \"kick\"\n");
				fw.write("    command: \"/kick\"\n");
				fw.write("    self: \"&r&b$S&r&3 kicks!\"\n");
				fw.write("    others: \"&r&b$S&r&3 kicks &r&b$R&r&3!\"\n");
			fw.write("\n");
			
			
			fw.close();
		} catch (IOException e) {
			
		}
	
		
		Emote.setup();
	}
	
	public static File getFile() {
		return config;
	}
	
}
