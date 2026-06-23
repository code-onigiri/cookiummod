package com.code_onigiri.cookium.network;

import com.code_onigiri.cookium.CookiumMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.registration.RegisterPayloadHandlersEvent;

@EventBusSubscriber(modid = CookiumMod.MODID)
public class ModNetworkEvents {
    
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        ModMessages.register(event);
    }
}
