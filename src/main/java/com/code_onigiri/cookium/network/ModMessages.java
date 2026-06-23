package com.code_onigiri.cookium.network;

import com.code_onigiri.cookium.CookiumMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.registration.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.handling.ServerPayloadContext;
import net.neoforged.neoforge.network.registration.payloads.CustomPacketPayload;
import net.neoforged.neoforge.client.network.ForgeClientPayloadContext;
import net.neoforged.neoforge.network.event.RegisterClientPayloadHandlersEvent;

public class ModMessages {
    public static final String PROTOCOL_VERSION = "1";
    
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        
        registrar.playBidirectional(
                ExampleC2SPayload.TYPE,
                ExampleC2SPayload.STREAM_CODEC,
                ExampleC2SPayload::handle
        );
        
        registrar.playToClient(
                ExampleS2CPayload.TYPE,
                ExampleS2CPayload.STREAM_CODEC,
                (payload, context) -> ExampleS2CPayload.handle(payload, context, Dist.CLIENT)
        );
    }

    public static void registerClient(RegisterClientPayloadHandlersEvent event) {
        event.register(
                ExampleS2CPayload.TYPE,
                (payload, context) -> ExampleS2CPayload.handle(payload, context, Dist.CLIENT)
        );
    }

    public record ExampleC2SPayload(int entityId, int specialData) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<ExampleC2SPayload> TYPE = 
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(CookiumMod.MODID, "example_c2s"));
        
        public static final StreamCodec<RegistryFriendlyByteBuf, ExampleC2SPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT,
                ExampleC2SPayload::entityId,
                ByteBufCodecs.VAR_INT,
                ExampleC2SPayload::specialData,
                ExampleC2SPayload::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public void handle(IPayloadContext context) {
            context.enqueueWork(() -> {
                if (context.player() instanceof ServerPlayer serverPlayer) {
                    Entity entity = serverPlayer.serverLevel().getEntity(entityId);
                    if (entity != null) {
                        CookiumMod.LOGGER.info("Received C2S packet for entity: {} with data: {}", entity, specialData);
                    }
                }
            });
        }
    }

    public record ExampleS2CPayload(int entityId, String message) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<ExampleS2CPayload> TYPE = 
                new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(CookiumMod.MODID, "example_s2c"));
        
        public static final StreamCodec<RegistryFriendlyByteBuf, ExampleS2CPayload> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT,
                ExampleS2CPayload::entityId,
                ByteBufCodecs.STRING_UTF8,
                ExampleS2CPayload::message,
                ExampleS2CPayload::new
        );

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public static void handle(ExampleS2CPayload payload, IPayloadContext context, Dist dist) {
            context.enqueueWork(() -> {
                if (dist.isClient()) {
                    CookiumMod.LOGGER.info("Received S2C packet on client: entity={}, message={}", 
                            payload.entityId(), payload.message());
                }
            });
        }
    }
}
