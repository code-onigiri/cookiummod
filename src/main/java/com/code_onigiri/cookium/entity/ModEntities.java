package com.code_onigiri.cookium.entity;

import com.code_onigiri.cookium.CookiumMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * ModEntities - Entity registration for NeoForge 26.1
 *
 * Key NeoForge 26.1 Patterns:
 * - Use DeferredRegister<EntityType<?>> for entity registration
 * - EntityType.Builder.of(Constructor::new, MobCategory) creates the builder
 * - sized(width, height) sets hitbox dimensions
 * - clientTrackingRange(chunks) sets how far the server tracks this entity
 * - Attributes are registered separately via EntityAttributeCreationEvent
 * - Renderers are registered via EntityRenderersEvent.RegisterRenderers
 */
public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, CookiumMod.MODID);

    /**
     * CookiumGolem - A custom golem mob
     *
     * EntityType.Builder options:
     * - of(EntityFactory, MobCategory) - the entity constructor and category
     * - sized(width, height) - hitbox size
     * - eyeHeight(height) - eye level for rendering and AI
     * - clientTrackingRange(chunks) - how far clients track this entity
     * - updateInterval(ticks) - how often position updates are sent (lower = more frequent)
     * - noLootTable() - don't generate loot when killed
     * - fireImmune() - immune to fire damage
     * - canSpawnFarFromPlayer() - can spawn far from players
     * - spawnDimensionsScale(scale) - scale spawn boundary checks
     * - spawnBlockRadius(radius) - required blocks for spawning
     *
     * MobCategory options:
     * - MONSTER - hostile mobs, despawn when far, count toward monster cap
     * - CREATURE - passive mobs like cows, persist, count toward creature cap
     * - AMBIENT - bats and similar, very low cap
     * - WATER_CREATURE - dolphins, squid
     * - UNDERGROUND_WATER_CREATURE - glow squid
     * - WATER_AMBIENT - tropical fish, very low cap
     * - MISC - technical entities, don't count toward caps
     */
    public static final DeferredHolder<EntityType<?>, EntityType<CookiumGolem>> COOKIUM_GOLEM =
            ENTITY_TYPES.register("cookium_golem", () ->
                    EntityType.Builder.of(CookiumGolem::new, MobCategory.CREATURE)
                            .sized(1.4F, 2.7F)           // Same size as Iron Golem
                            .eyeHeight(2.4F)              // Eye position for AI targeting
                            .clientTrackingRange(10)      // Track within 10 chunks
                            .build(ResourceKey.create(Registries.ENTITY_TYPE,
                                    Identifier.fromNamespaceAndPath(CookiumMod.MODID, "cookium_golem")))
            );

    /**
     * Register the deferred register to the mod event bus
     * Call this from your mod constructor
     */
    public static void register(IEventBus modEventBus) {
        ENTITY_TYPES.register(modEventBus);
    }
}
