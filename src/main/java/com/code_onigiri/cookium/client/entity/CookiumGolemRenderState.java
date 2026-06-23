package com.code_onigiri.cookium.client.entity;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * CookiumGolemRenderState - Render state for CookiumGolem entity
 *
 * Key NeoForge 26.1 Render State Pattern:
 * - RenderState classes store data needed for rendering, extracted from the entity
 * - This decouples entity logic from rendering logic
 * - Extend LivingEntityRenderState for living entities (provides health, hurt, etc.)
 * - Fields should be simple data types that the renderer needs
 *
 * The renderer extracts data from the entity into this state via extractRenderState(),
 * then uses this state in the model's setupAnim() and the renderer's methods.
 *
 * Common inherited fields from LivingEntityRenderState:
 * - xRot, yRot: Head rotation
 * - walkAnimationPos, walkAnimationSpeed: Walking animation progress
 * - isUsingItem, mainHandItem, offhandItem: Item usage state
 * - hurt: Is the entity currently in hurt animation
 * - health, maxHealth: Health values
 * - ageScale: For baby mobs
 */
@OnlyIn(Dist.CLIENT)
public class CookiumGolemRenderState extends LivingEntityRenderState {

    /**
     * Attack animation progress (0 when not attacking, counts down during attack)
     * Used by the model to animate attack swing
     */
    public float attackAnimationTick;

    /**
     * Whether this golem is currently in combat (has a target)
     * Can be used for eye glow effects or other visual indicators
     */
    public boolean isAngry;
}
