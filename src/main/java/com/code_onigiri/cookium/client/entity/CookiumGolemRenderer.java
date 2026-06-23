package com.code_onigiri.cookium.client.entity;

import com.code_onigiri.cookium.CookiumMod;
import com.code_onigiri.cookium.entity.CookiumGolem;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * CookiumGolemRenderer - Renders the CookiumGolem entity
 *
 * Key NeoForge 26.1 Renderer Patterns:
 * - Extend MobRenderer<Entity, RenderState, Model> for mob entities
 * - Override createRenderState() to create new render state instances
 * - Override extractRenderState() to copy data from entity to render state
 * - Override getTextureLocation() to provide the entity texture
 *
 * MobRenderer generics:
 * - T: The entity type (CookiumGolem)
 * - S: The render state type (CookiumGolemRenderState)
 * - M: The model type (CookiumGolemModel)
 *
 * Rendering Pipeline (NeoForge 26.1):
 * 1. createRenderState() - Called once per entity to create state holder
 * 2. extractRenderState() - Called each frame to update state from entity
 * 3. getTextureLocation() - Called to get texture for rendering
 * 4. Model's setupAnim() - Called with render state to animate
 * 5. Rendering occurs via inherited methods
 */
@OnlyIn(Dist.CLIENT)
public class CookiumGolemRenderer extends MobRenderer<CookiumGolem, CookiumGolemRenderState, CookiumGolemModel> {

    /**
     * Texture location for this entity
     * Path: assets/[modid]/textures/entity/[path].png
     */
    private static final Identifier TEXTURE_LOCATION =
            Identifier.fromNamespaceAndPath(CookiumMod.MODID, "textures/entity/cookium_golem.png");

    /**
     * Constructor - initializes the renderer
     *
     * @param context Provides access to model baking and other rendering utilities
     *
     * MobRenderer constructor parameters:
     * - context: EntityRendererProvider.Context for accessing resources
     * - model: The entity model instance (baked from LayerDefinition)
     * - shadowRadius: Size of the shadow underneath the entity
     */
    public CookiumGolemRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new CookiumGolemModel(context.bakeLayer(CookiumGolemModel.LAYER_LOCATION)),
                0.7F  // Shadow radius (same as Iron Golem)
        );

        // Add render layers here if needed (e.g., glowing eyes, armor)
        // this.addLayer(new MyCustomLayer(this));
    }

    /**
     * Get the texture to use for rendering
     *
     * @param state The current render state
     * @return Texture identifier
     */
    @Override
    public Identifier getTextureLocation(CookiumGolemRenderState state) {
        return TEXTURE_LOCATION;
    }

    /**
     * Create a new render state instance
     * Called once when the entity starts being rendered
     *
     * @return New render state instance
     */
    @Override
    public CookiumGolemRenderState createRenderState() {
        return new CookiumGolemRenderState();
    }

    /**
     * Extract data from the entity into the render state
     * Called every frame before rendering
     *
     * @param entity The entity being rendered
     * @param state The render state to populate
     * @param partialTicks Partial tick for interpolation (0.0 to 1.0 within a tick)
     *
     * Important: Call super.extractRenderState() first to populate inherited fields!
     * This handles common fields like position, rotation, walk animation, etc.
     */
    @Override
    public void extractRenderState(CookiumGolem entity, CookiumGolemRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);

        // Extract custom data for our model
        state.attackAnimationTick = entity.getAttackAnimationTick();
        state.isAngry = entity.getTarget() != null;
    }

    // ==================== OPTIONAL OVERRIDES ====================

    /*
     * Override these methods for advanced rendering customization:
     *
     * protected void scale(S state, PoseStack poseStack) {
     *     // Scale the entity (e.g., make babies smaller)
     *     poseStack.scale(1.2F, 1.2F, 1.2F);
     * }
     *
     * protected void setupRotations(S state, PoseStack poseStack, float bodyRot, float entityScale) {
     *     // Custom rotation setup (e.g., tilt when damaged)
     *     super.setupRotations(state, poseStack, bodyRot, entityScale);
     * }
     *
     * protected float getWhiteOverlayProgress(S state) {
     *     // Return 0-1 for white overlay effect (used when hurt or for effects)
     *     return 0.0F;
     * }
     */
}
