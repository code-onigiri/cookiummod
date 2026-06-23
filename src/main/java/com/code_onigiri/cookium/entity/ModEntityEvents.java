package com.code_onigiri.cookium.entity;

import com.code_onigiri.cookium.CookiumMod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

/**
 * ModEntityEvents - Server-side entity event handlers for NeoForge 26.1
 *
 * Key NeoForge 26.1 Patterns:
 * - Register event listeners manually via modEventBus.addListener()
 * - EntityAttributeCreationEvent is a MOD bus event for registering entity attributes
 *
 * Event Bus Types:
 * - MOD bus: Mod lifecycle events (FMLCommonSetupEvent, EntityAttributeCreationEvent, etc.)
 * - GAME/FORGE bus: Runtime game events (LivingHurtEvent, PlayerEvent, etc.)
 *
 * This class handles server-side entity registration.
 * Client-side registration (renderers, models) goes in a separate class with Dist.CLIENT.
 */
public class ModEntityEvents {

    /**
     * Register all entity event listeners to the mod event bus
     * Call this from your mod constructor
     *
     * @param modEventBus The mod's event bus
     */
    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(ModEntityEvents::registerEntityAttributes);
    }

    /**
     * Register entity attributes for all custom entities
     *
     * This event is fired during mod loading to register default attributes
     * for custom LivingEntity types. Without this registration, entities
     * will crash when spawned because they have no attribute map.
     *
     * @param event The attribute creation event
     *
     * Pattern:
     * event.put(EntityType, AttributeSupplier)
     * - EntityType: Your registered entity type
     * - AttributeSupplier: Built from Entity.createAttributes().build()
     */
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        // Register CookiumGolem attributes
        event.put(
                ModEntities.COOKIUM_GOLEM.get(),
                CookiumGolem.createAttributes().build()
        );

        // Add more entity attribute registrations here:
        // event.put(ModEntities.ANOTHER_ENTITY.get(), AnotherEntity.createAttributes().build());
    }
}
