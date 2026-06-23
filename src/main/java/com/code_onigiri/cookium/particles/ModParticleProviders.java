package com.code_onigiri.cookium.particles;

import com.code_onigiri.cookium.CookiumMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import javax.annotation.Nullable;

@EventBusSubscriber(modid = CookiumMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModParticleProviders {
    
    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(
                ModParticleTypes.COOKIUM_SPARKLE.get(),
                (spriteSet, options) -> new CookiumSparkleParticle(spriteSet)
        );
    }

    public static class CookiumSparkleParticle extends net.minecraft.client.particle.SpriteSetParticle {
        private final SpriteSet sprites;

        public CookiumSparkleParticle(SpriteSet spriteSet) {
            super(Minecraft.getInstance().particleEngine.spriteAware(spriteSet));
            this.sprites = spriteSet;
            this.lifetime = 40;
            this.gravity = 0.05f;
            this.hasPhysics = true;
        }

        @Override
        public ParticleProvider<?> getProvider() {
            return (spriteSet, options) -> new CookiumSparkleParticle(spriteSet);
        }

        @Override
        public void tick() {
            super.tick();
            this.alpha = 1.0f - ((float) this.age / (float) this.lifetime);
        }
    }
}
