package net.wigoftime.open_komodo.etc.dialog;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class DialogManager {
    private static final HashMap<Integer, DialogNPC> dialogMap = new HashMap<>();
    private static final HashMap<UUID, DialogNPC> activeDialogNPCs = new HashMap<>();
    public static void addActive(int npcID, Player opener) {
        sendDialog(npcID, opener);
        opener.playNote(opener.getLocation(), Instrument.CHIME, Note.natural(1, Note.Tone.G));

        synchronized (activeDialogNPCs) {
            activeDialogNPCs.put(opener.getUniqueId(), dialogMap.get(npcID));
        }
    }

    public static void leaving(Player player) {
        synchronized (activeDialogNPCs) {
            DialogNPC targetDialogNPC = activeDialogNPCs.get(player.getUniqueId());
            if (targetDialogNPC == null)
                return;

            targetDialogNPC.sendLeaveMessage(player);
            activeDialogNPCs.remove(player.getUniqueId());
        }
    }

    public static void setup() {
        DialogNPC brewer1Dialog = new DialogNPC(
                5,
                new ComponentBuilder().color(ChatColor.LIGHT_PURPLE).bold(true).append("Chao Xing").create(),
                new String[]{
                        "Eyes to your self, don't get any ideas -.-",
                        "Ya gonna buy something?",
                        "*Sneezes* arghh! no it's not covid, It's just hayfever season.",
                        "Should I get into the renting girlfriend business? I heard the pay is good",
                        "Trust me, melting coins back to gold doesn't equal the same or greater value. I already tried that."
                },
                new String[]{
                        "Aww, that's it? That's all you're gonna buy?",
                        "Please come again! Make sure to tip me in the coin jar!",
                        "No refunds, come again! ^-^",
                        "From here on out, you agree to the terms that you are fully responsible for whatever happens, including injuries, explosions, and potentially... I won't go there.",
                        "I think I should raise the prices for my keys, they're doing so well! ...Fine fine, jeesh.",
                        "Come back, I am money deprived... :<",
                        "Thank you for committing to capitalism!"
                }
        );
        dialogMap.put(5, brewer1Dialog);

        DialogNPC brewer2Dialog = new DialogNPC(
                6,
                new ComponentBuilder().color(ChatColor.LIGHT_PURPLE).bold(true).append("Ayaka").create(),
                new String[]{
                        "Hello Sir/Ma'am!",
                        "Don't mind my sister over there... She is pretty materialistic.",
                        "When I get stressed, I love to drink green tea ^-^",
                        "*Sneezes* blehh hayfeverrr >~<",
                        "I feel like no one takes me seriously since I'm the youngest sibling"
                },
                new String[]{
                        "Thank you for your generosity!",
                        "Have a nice day!",
                        "Take care, safe travels!",
                        "Wait, don't tipp me that much!! >~<"
                }
        );
        dialogMap.put(6, brewer2Dialog);

        DialogNPC hatCashier = new DialogNPC(
                2,
                new ComponentBuilder().color(ChatColor.YELLOW).bold(true).append("Mad Hatter").create(),
                new String[]{
                        "There is a place, like no place on earth. A land full of wonder, mystery, and danger. Some say, to survive it, you need to be as mad as a hatter. Which, luckily, I am.",
                        "Have I gone mad?",
                        "What is the hatter with me?",
                        "Do you know why they call me Hatter?",
                        "Trust me. I know a thing or two about liking people, and in time, after much chocolate and cream cake, ‘like’ turns into ‘what was his name again?’."
                },
                new String[]{}
        );
        dialogMap.put(2, hatCashier);

        DialogNPC propCashier1 = new DialogNPC(
                3,
                new ComponentBuilder().color(ChatColor.YELLOW).bold(true).append("Hugo").create(),
                new String[]{
                        "Hahaha! WELCOME!!!",
                        "Diamonds are a scam! Get something else instead!",
                        "I don't see why we have to exclude the batteries from the chainsaw! :T",
                        "See that fine lady over there? She's my babe!",
                        "My babe's a pretty fine watchmaker! I keep telling her to pursue it as a profession, but she doesn't listen!"
                },
                new String[]{
                        "Don't break a leg!",
                        "Watch where ya step!"
                }
        );
        dialogMap.put(3, propCashier1);

        DialogNPC propCashier2 = new DialogNPC(
                4,
                new ComponentBuilder().color(ChatColor.YELLOW).bold(true).append("Selene").create(),
                new String[]{
                        "My husband is insane >.>",
                        "Feel free to browse around, let me know if you need any assistance.",
                        "I don't sell my watches, they are art to me.",
                        "Jeesh, the watchers we have today are awful!",
                },
                new String[]{
                        "Safe travels, stranger.",
                        "Do not forget, the chainsaw is not a toy.",
                        "Safe travels"
                }
        );
        dialogMap.put(4, propCashier2);

        DialogNPC petCashier = new DialogNPC(
                7,
                new ComponentBuilder().color(ChatColor.BLUE).bold(true).append("Jade").create(),
                new String[]{
                        "Eco is the best!",
                        "Puppies are adorable!",
                        "Eco looks like a puppy!",
                        "I have a kitten named Cloud, but she's pretty wild.",
                        "*Sneezes* I am allergic to fur, but I still love animals T~T",
                        "Don't mind the skeleton horse, the dogs loves them!",
                        "Do you know where Nemo is? I can't find him in the tank.."
                },
                new String[]{
                        "Have a good day! :D",
                        "Come again soon! :D",
                        "Poof! Now the UI is gone! :D"
                }
        );
        dialogMap.put(7, petCashier);

        DialogNPC tagCashier = new DialogNPC(
                8,
                new ComponentBuilder().color(ChatColor.YELLOW).bold(true).append("Ravenor").create(),
                new String[]{
                        "Good day, sir/ma'am!",
                        "How is your lovely evening?",
                        "Hugo is right about one thing, diamonds are an controlled market.",
                        "I would love to see musgravite, but I would have to check if it wasn't Taafeite.",
                        "Back in my day... I was younger",
                        "Ow, my back"
                },
                new String[]{
                        "See ya later!",
                        "Happy doing business!",
                        "Trading is a sign of a healthy economy!"
                }
        );
        dialogMap.put(8, tagCashier);

        DialogNPC phoneCashier = new DialogNPC(
                9,
                new ComponentBuilder().color(ChatColor.RED).bold(true).append("7")
                        .append("0").color(ChatColor.YELLOW)
                        .append("7").color(ChatColor.RED).create(),
                new String[]{
                        "Three glasses, subtle difference. Fashionista Seven!",
                        "Good Mooorning!",
                        "I must think of a way to take Honey Buddha Chips to outer space.",
                        "Ugh... Blinding morning sun rays... My eyes are so dry from staring at the monitor...",
                },
                new String[]{
                        "Bye bye!"
                }
        );
        dialogMap.put(9, phoneCashier);
    }

    public static void sendDialog(@NotNull Integer id, @NotNull Player targetPlayer) {
        dialogMap.get(id).sendChatMessage(targetPlayer);
    }

    public static void sendLeaveDialog(@NotNull Integer id, @NotNull Player targetPlayer) {
        dialogMap.get(id).sendLeaveMessage(targetPlayer);
    }
}
