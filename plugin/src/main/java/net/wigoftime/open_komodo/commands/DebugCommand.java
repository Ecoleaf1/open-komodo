package net.wigoftime.open_komodo.commands;

import net.wigoftime.open_komodo.etc.ParticlesUtil;
import net.wigoftime.open_komodo.etc.Permissions;
import net.wigoftime.open_komodo.gui.*;
import net.wigoftime.open_komodo.objects.CustomParticle;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class DebugCommand extends Command {
    public DebugCommand() {
        super(
                "debug",
                "Used to access/do things abnormally. Debug often used on server side to open GUIs to prevent cheating",
                "/debug {Subcommand} [Sub command ..]",
                Arrays.asList());
        setPermission("openkomodo.debugtool");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length < 1) return true;

        switch (args[0].toLowerCase()) {
            case "particlelootbox":
                lootBoxParticles(CustomPlayer.get(args[1]));
                break;
            case "particlestylelootbox":
                lootBoxParticleStyle(CustomPlayer.get(args[1]));
                break;
            case "particleshop":
                particleShopSubcommand(CustomPlayer.get(args[1]));
                break;
            case "propshop":
                openPropShop(CustomPlayer.get(args[1]), args[2]);
                break;
            case "hatshop":
                openHatMenu(CustomPlayer.get(args[1]), args[2], true);
                break;
            case "hatmenu":
                openHatMenu(CustomPlayer.get(args[1]), args[2], false);
                break;
            case "phoneshop":
                openPhoneShop(CustomPlayer.get(args[1]));
                break;
            case "petshop":
                openPetShop(CustomPlayer.get(args[1]));
                break;
            case "petmenu":
                openPetMenu(CustomPlayer.get(args[1]));
                break;
            case "tagshop":
                openTagShop(CustomPlayer.get(args[1]));
                break;
            case "tagmenu":
                openTagMenu(CustomPlayer.get(args[1]));
                break;
        }

        return true;
    }

    private void lootBoxParticles(CustomPlayer player) {
        ItemStack heldItem = player.getPlayer().getInventory().getItemInMainHand();

        if (heldItem.getType() != Material.INK_SAC) return;
        if (!heldItem.hasItemMeta()) return;
        if (!heldItem.getItemMeta().hasCustomModelData()) return;
        if (heldItem.getItemMeta().getCustomModelData() != 80) return;

        openParticleLootbox(player, ParticlesUtil.ParticleType.PARTICLES);
    }

    private void lootBoxParticleStyle(CustomPlayer player) {
        ItemStack heldItem = player.getPlayer().getInventory().getItemInMainHand();

        if (heldItem.getType() != Material.INK_SAC) return;
        if (!heldItem.hasItemMeta()) return;
        if (!heldItem.getItemMeta().hasCustomModelData()) return;
        if (heldItem.getItemMeta().getCustomModelData() != 81) return;

        openParticleLootbox(player, ParticlesUtil.ParticleType.PARTICLE_STYLES);
    }

    private void openParticleLootbox(@NotNull CustomPlayer player, @NotNull ParticlesUtil.ParticleType type) {

        List<CustomParticle> unownedParticles = ParticlesUtil.getParticles(player, type);
        if (unownedParticles.size() == 0) {
            player.getPlayer().sendMessage(ChatColor.DARK_RED+"Sorry, but you already unlocked everything with this lootbox!");
            return;
        }

        if (!player.getPlayer().hasPermission(Permissions.particleAccess)) {
            player.getPlayer().sendMessage(ChatColor.DARK_RED+ "Sorry, but you do not have permission to unlock particles");
            return;
        }

        ItemStack heldItem = player.getPlayer().getInventory().getItemInMainHand();
        heldItem.setAmount(heldItem.getAmount() - 1);
        BasicCrate crate = new BasicCrate(player,unownedParticles);
        crate.open();
    }

    private void particleShopSubcommand(@NotNull CustomPlayer player) {
        ParticleShop particleShop = new ParticleShop(player);
        particleShop.open();
    }

    private void openPropShop(@NotNull CustomPlayer targetPlayer, @NotNull String shopName) {
        CustomGUI propShop = new PropShop(targetPlayer, shopName);
        propShop.open();
    }

    private void openHatMenu(@NotNull CustomPlayer targetPlayer, @NotNull String menuName, boolean isInStore) {
        CustomGUI hatMenu = new HatMenu(targetPlayer, menuName, isInStore);
        hatMenu.open();
    }

    private void openPhoneShop(@NotNull CustomPlayer targetPlayer) {
        _PhoneSwitcher.open(targetPlayer);
    }

    private void openPetShop(@NotNull CustomPlayer targetPlayer) {
        CustomGUI petShop = new PetsShop(targetPlayer);
        petShop.open();
    }

    private void openPetMenu(@NotNull CustomPlayer targetPlayer) {
        CustomGUI petMenu = new PetMenu(targetPlayer);
        petMenu.open();
    }

    private void openTagShop(@NotNull CustomPlayer targetPlayer) {
        CustomGUI tagShop = new TagShop(targetPlayer);
        tagShop.open();
    }

    private void openTagMenu(@NotNull CustomPlayer targetPlayer) {
        CustomGUI tagMenu = new TagMenu(targetPlayer);
        tagMenu.open();
    }
}
