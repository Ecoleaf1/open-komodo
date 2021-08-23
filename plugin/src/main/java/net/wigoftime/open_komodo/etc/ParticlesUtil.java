package net.wigoftime.open_komodo.etc;

import net.wigoftime.open_komodo.objects.CustomParticle;
import net.wigoftime.open_komodo.objects.CustomPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

abstract public class ParticlesUtil {
    public enum ParticleType {
        PARTICLES, PARTICLE_STYLES
    }

    public static @NotNull List<CustomParticle> getParticles(@NotNull CustomPlayer player, @NotNull ParticleType type) {
        List<CustomParticle> particleList =
                type == ParticleType.PARTICLES ?
                        CustomParticle.particleList :
                        CustomParticle.mvpStyleList;
        List<CustomParticle> particles = new ArrayList<CustomParticle>(9);

        byte count = 0;
        for (CustomParticle particle : particleList)
            if (count > 8)
                break;
            else
            if (!player.getPlayer().hasPermission(particle.permission)) {
                particles.add(particle);
                count++;
            }

        if (particles.size() == 0)
            return new ArrayList<CustomParticle>(0);

        if (particles.size() < 9) {
            Random random = new Random();

            if (particles.size() == 1) {
                for (int i = 0; i < 9; i++)
                    particles.add(particles.get(0));

            } else while(particles.size() < 9)
                particles.add(particles.get(random.nextInt(particles.size() - 1)));
        }

        return particles;
    }
}
