package com.code_onigiri.cookium.particles;

import com.code_onigiri.cookium.CookiumMod;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = 
            DeferredRegister.create(net.minecraft.core.registries.Registries.PARTICLE_TYPE, CookiumMod.MODID);

    public static final DeferredHolder<ParticleType<?>, ParticleType<CookiumParticleOptions>> COOKIUM_SPARKLE = 
            PARTICLE_TYPES.register("cookium_sparkle", () -> 
                    new ParticleType<CookiumParticleOptions>(false) {
                        @Override
                        public Codec<CookiumParticleOptions> codec() {
                            return CookiumParticleOptions.CODEC;
                        }

                        @Override
                        public StreamCodec<RegistryFriendlyByteBuf, CookiumParticleOptions> streamCodec() {
                            return CookiumParticleOptions.STREAM_CODEC;
                        }
                    });

    public record CookiumParticleOptions(float red, float green, float blue, float scale) implements ParticleOptions {
        public static final Codec<CookiumParticleOptions> CODEC = Codec.FLOAT.listOf().xmap(
                floats -> {
                    if (floats.size() >= 4) {
                        return new CookiumParticleOptions(floats.get(0), floats.get(1), floats.get(2), floats.get(3));
                    }
                    return new CookiumParticleOptions(1.0f, 1.0f, 1.0f, 1.0f);
                },
                options -> java.util.List.of(options.red(), options.green(), options.blue(), options.scale())
        );

        public static final StreamCodec<RegistryFriendlyByteBuf, CookiumParticleOptions> STREAM_CODEC = 
                StreamCodec.composite(
                        ByteBufCodecs.FLOAT, CookiumParticleOptions::red,
                        ByteBufCodecs.FLOAT, CookiumParticleOptions::green,
                        ByteBufCodecs.FLOAT, CookiumParticleOptions::blue,
                        ByteBufCodecs.FLOAT, CookiumParticleOptions::scale,
                        CookiumParticleOptions::new
                );

        @Override
        public ParticleType<CookiumParticleOptions> getType() {
            return ModParticleTypes.COOKIUM_SPARKLE.get();
        }
    }
}
