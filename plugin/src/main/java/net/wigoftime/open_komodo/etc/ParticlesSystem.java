package net.wigoftime.open_komodo.etc;

import net.wigoftime.open_komodo.gui.BasicCrate;
import net.wigoftime.open_komodo.objects.CustomParticle;
import net.wigoftime.open_komodo.objects.CustomParticle.ParticleType;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

abstract public class ParticlesSystem {
	public static void openParticleBoxGui(@NotNull CustomPlayer player, ParticleType type) {
		List<CustomParticle> randomIcons;
		if (type == ParticleType.PARTICLE) {
		randomIcons = new ArrayList<CustomParticle>(CustomParticle.particleList.size());
		randomIcons.addAll(CustomParticle.particleList);
		} else if (type == ParticleType.STYLE) {
			randomIcons = new ArrayList<CustomParticle>(CustomParticle.mvpStyleList.size());
			randomIcons.addAll(CustomParticle.mvpStyleList);
		} else
			return;
		
		Iterator<CustomParticle> particleIterator = randomIcons.iterator();
		while (particleIterator.hasNext()) {
			CustomParticle particle = particleIterator.next();
			
			if (player.getPlayer().hasPermission(particle.permission))
				particleIterator.remove();
		}
		
		if (randomIcons.size() < 1) {
			player.getPlayer().sendMessage(String.format("%s» %sYou already obtained all the items from this crate.", ChatColor.GOLD, ChatColor.GRAY));
			return;
		} else if (randomIcons.size() < 10) {
			ArrayList<CustomParticle> notOwnedParticles = new ArrayList<CustomParticle>(randomIcons.size());
			notOwnedParticles.addAll(randomIcons);
			
			while (randomIcons.size() < 10) {
				Random random = new Random();
				randomIcons.add(notOwnedParticles.get(random.nextInt(notOwnedParticles.size())));
			}
		}
		
		Collections.shuffle(randomIcons);
		List<CustomParticle> chosenCrateOfParticles = new ArrayList<CustomParticle>(9);
		
		for (int i = 0; i < 10; i++)
		chosenCrateOfParticles.add(randomIcons.get(i));
		
		if (!CurrencyClass.takeOutFromBalance(player, 1280, Currency.COINS))
		return;
		
		new BasicCrate(player, chosenCrateOfParticles);
	}
}
