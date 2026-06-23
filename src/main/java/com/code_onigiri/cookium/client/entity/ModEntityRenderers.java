package com.code_onigiri.cookium.client.entity;

import com.code_onigiri.cookium.CookiumMod;
import com.code_onigiri.cookium.entity.ModEntities;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

/**
 * ModEntityRenderers - Client-side entity renderer registration for NeoForge 26.1
 *
 * NeoForge 26.1 Client Entity Registration requires TWO events:
 * 1. EntityRenderersEvent.RegisterLayerDefinitions - Register model layer definitions
 * 2. EntityRenderersEvent.RegisterRenderers - Register entity renderers
 *
 * Both events fire on the MOD event bus during client initialization.
 * Use @EventBusSubscriber with value = Dist.CLIENT to ensure this only runs on client.
 */
@EventBusSubscriber(modid = CookiumMod.MODID, value = Dist.CLIENT)
public class ModEntityRenderers {

    /**
     * Register model layer definitions
     * 
     * Called early in client initialization to register LayerDefinition factories.
     * The LayerDefinition is baked into ModelPart instances that renderers use.
     * 
     * Pattern: event.registerLayerDefinition(ModelLayerLocation, LayerDefinition::factory)
     */
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(
                CookiumGolemModel.LAYER_LOCATION,
                CookiumGolemModel::createBodyLayer
        );
    }

    /**
     * Register entity renderers
     * 
     * Called after layer definitions are registered.
     * Associates EntityType with its renderer constructor.
     * 
     * Pattern: event.registerEntityRenderer(EntityType, Renderer::new)
     */
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                ModEntities.COOKIUM_GOLEM.get(),
                CookiumGolemRenderer::new
        );
    }
}
