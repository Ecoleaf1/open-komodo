package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.Main;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.etc.dialog.DialogManager;
import net.wigoftime.open_komodo.gui.*;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class DebugNPCCommand extends Command {
    public DebugNPCCommand() {
        super(
                "debugnpc",
                "Used to access/do things abnormally as an NPC.",
                "/debug {NPC Name } {username}",
                Arrays.asList());
        setPermission("openkomodo.debugtool");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length < 2) return true;

        switch (args[0].toLowerCase()) {
            case "particlenpc1":
                interactParticleNPC1(CustomPlayer.get(args[1]));
                break;
            case "particlenpc2":
                interactParticleNPC2(CustomPlayer.get(args[1]));
                break;
            case "propcashier1":
                interactPropCashier1(CustomPlayer.get(args[1]), args[2]);
                break;
            case "propcashier2":
                interactPropCashier2(CustomPlayer.get(args[1]), args[2]);
                break;
            case "phonecashier1":
                interactPhoneNPC(CustomPlayer.get(args[1]));
                break;
            case "hatcashier1":
                interactHatNPC(CustomPlayer.get(args[1]), args[2], true);
                break;
            case "petcashier1":
                interactPetNPC(CustomPlayer.get(args[1]));
                break;
            case "tagcashier":
                interactTagNPC(CustomPlayer.get(args[1]));
                break;
        }

        return true;
    }

    private void interactParticleNPC1(@NotNull CustomPlayer player) {
        if (!player.getPlayer().hasPermission(Permissions.particleAccess)) {
            player.getPlayer().sendMessage(ChatColor.DARK_RED + "Sorry, but you do not have permission to access this.");
            return;
        }

        DialogManager.addActive(5, player.getPlayer());

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                ParticleShop particleShop = new ParticleShop(player);
                particleShop.open();
            }
        };
        runnable.runTaskLater(Main.getPlugin(), 60);
    }

    private void interactParticleNPC2(@NotNull CustomPlayer player) {
        if (!player.getPlayer().hasPermission(Permissions.particleAccess)) {
            player.getPlayer().sendMessage(ChatColor.DARK_RED + "Sorry, but you do not have permission to access this.");
            return;
        }

        DialogManager.addActive(6,player.getPlayer());

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                ParticleShop particleShop = new ParticleShop(player);
                particleShop.open();
            }
        };
        runnable.runTaskLater(Main.getPlugin(), 60);
    }

    private void interactPropCashier1(@NotNull CustomPlayer targetPlayer, @NotNull String shopName) {
        DialogManager.addActive(3,targetPlayer.getPlayer());

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                CustomGUI propShop = new PropShop(targetPlayer, shopName);
                propShop.open();
            }
        };
        runnable.runTaskLater(Main.getPlugin(), 60);
    }

    private void interactPropCashier2(@NotNull CustomPlayer targetPlayer, @NotNull String shopName) {
        DialogManager.addActive(4,targetPlayer.getPlayer());

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                CustomGUI propShop = new PropShop(targetPlayer, shopName);
                propShop.open();
            }
        };
        runnable.runTaskLater(Main.getPlugin(), 60);
    }

    private void interactHatNPC(@NotNull CustomPlayer targetPlayer, @NotNull String menuName, boolean isInStore) {
        if (!targetPlayer.getPlayer().hasPermission(Permissions.particleAccess)) {
            targetPlayer.getPlayer().sendMessage(ChatColor.DARK_RED + "Sorry, but you do not have permission to access this.");
            return;
        }

        DialogManager.addActive(2,targetPlayer.getPlayer());
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                CustomGUI hatMenu = new HatMenu(targetPlayer, menuName, isInStore);
                hatMenu.open();
            }
        };
        runnable.runTaskLater(Main.getPlugin(), 60);
    }

    private void interactPhoneNPC(@NotNull CustomPlayer targetPlayer) {
        if (!targetPlayer.getPlayer().hasPermission("openkomodo.abilities.phoneswitching")) {
            targetPlayer.getPlayer().sendMessage(ChatColor.DARK_RED + "Sorry, but you do not have permission to access this.");
            return;
        }

        DialogManager.addActive(9,targetPlayer.getPlayer());

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                PhoneSwitcher phoneSwitcher = new PhoneSwitcher(targetPlayer);
                phoneSwitcher.open();
            }
        };

        runnable.runTaskLater(Main.getPlugin(), 60);
    }

    private void interactPetNPC(@NotNull CustomPlayer targetPlayer) {
        if (!targetPlayer.getPlayer().hasPermission(Permissions.petAccess)) {
            targetPlayer.getPlayer().sendMessage(ChatColor.DARK_RED + "Sorry, but you do not have permission to access this.");
            return;
        }

        DialogManager.addActive(7, targetPlayer.getPlayer());

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                CustomGUI petShop = new PetsShop(targetPlayer);
                petShop.open();
            }
        };

        runnable.runTaskLater(Main.getPlugin(), 60);
    }

    private void interactTagNPC(@NotNull CustomPlayer targetPlayer) {
        DialogManager.addActive(8, targetPlayer.getPlayer());

        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                CustomGUI tagShop = new TagShop(targetPlayer);
                tagShop.open();
            }
        };

        runnable.runTaskLater(Main.getPlugin(), 60);
    }
}
