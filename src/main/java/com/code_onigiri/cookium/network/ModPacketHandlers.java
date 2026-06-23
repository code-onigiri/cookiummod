package com.code_onigiri.cookium.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.client.ClientPacketDistributor;

public class ModPacketHandlers {
    
    public static void sendExampleC2S(int entityId, int specialData) {
        ClientPacketDistributor.sendToServer(new ModMessages.ExampleC2SPayload(entityId, specialData));
    }

    public static void sendToPlayer(ServerPlayer player, int entityId, String message) {
        PacketDistributor.sendToPlayer(player, new ModMessages.ExampleS2CPayload(entityId, message));
    }

    public static void sendToAllPlayers(ServerPlayer sender, int entityId, String message) {
        PacketDistributor.sendToAllPlayers(new ModMessages.ExampleS2CPayload(entityId, message));
    }

    public static void sendToTrackingEntity(Entity entity, int entityId, String message) {
        PacketDistributor.sendToPlayersTrackingEntity(entity, new ModMessages.ExampleS2CPayload(entityId, message));
    }
}
