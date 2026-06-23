package com.code_onigiri.cookium.client.entity;

import com.code_onigiri.cookium.CookiumMod;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * CookiumGolemModel - Entity model for CookiumGolem
 *
 * Key NeoForge 26.1 Model Patterns:
 * - Extend EntityModel<RenderState> (not the entity class!)
 * - Use ModelLayerLocation for model layer registration
 * - createBodyLayer() returns LayerDefinition for registration
 * - setupAnim() animates based on RenderState data
 *
 * Model Structure:
 * - MeshDefinition: Root container for the mesh
 * - PartDefinition: Named parts of the model (head, body, arms, etc.)
 * - CubeListBuilder: Builds cubes for each part
 * - PartPose: Position and rotation offset for each part
 *
 * Texture Mapping:
 * - texOffs(u, v): Sets UV offset for subsequent cubes
 * - addBox(x, y, z, width, height, depth): Adds a cube
 * - LayerDefinition.create(mesh, textureWidth, textureHeight): Creates the final definition
 */
@OnlyIn(Dist.CLIENT)
public class CookiumGolemModel extends EntityModel<CookiumGolemRenderState> {

    /**
     * Model layer location - used to register and retrieve this model
     * Format: new ModelLayerLocation(Identifier.fromNamespaceAndPath("modid", "modelname"), "main")
     * The "main" layer is the default; use other names for armor layers, etc.
     */
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(CookiumMod.MODID, "cookium_golem"), "main");

    // Model parts - stored for animation
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    /**
     * Constructor - receives the baked ModelPart root from the layer definition
     */
    public CookiumGolemModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.rightArm = root.getChild("right_arm");
        this.leftArm = root.getChild("left_arm");
        this.rightLeg = root.getChild("right_leg");
        this.leftLeg = root.getChild("left_leg");
    }

    /**
     * Create the layer definition - called during model registration
     *
     * This defines the structure of the model:
     * - Part hierarchy
     * - Cube positions and sizes
     * - Texture mappings
     *
     * @return LayerDefinition to be registered
     */
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Head - similar to Iron Golem but slightly different proportions
        root.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, -12.0F, -5.5F, 8.0F, 10.0F, 8.0F)  // Main head
                        .texOffs(24, 0)
                        .addBox(-1.0F, -5.0F, -7.5F, 2.0F, 4.0F, 2.0F),    // Nose
                PartPose.offset(0.0F, -7.0F, -2.0F)
        );

        // Body - two parts: torso and waist
        root.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(0, 40)
                        .addBox(-9.0F, -2.0F, -6.0F, 18.0F, 12.0F, 11.0F)   // Torso
                        .texOffs(0, 70)
                        .addBox(-4.5F, 10.0F, -3.0F, 9.0F, 5.0F, 6.0F, new CubeDeformation(0.5F)),  // Waist
                PartPose.offset(0.0F, -7.0F, 0.0F)
        );

        // Right arm - long golem arm
        root.addOrReplaceChild("right_arm",
                CubeListBuilder.create()
                        .texOffs(60, 21)
                        .addBox(-13.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F),
                PartPose.offset(0.0F, -7.0F, 0.0F)
        );

        // Left arm
        root.addOrReplaceChild("left_arm",
                CubeListBuilder.create()
                        .texOffs(60, 58)
                        .addBox(9.0F, -2.5F, -3.0F, 4.0F, 30.0F, 6.0F),
                PartPose.offset(0.0F, -7.0F, 0.0F)
        );

        // Right leg
        root.addOrReplaceChild("right_leg",
                CubeListBuilder.create()
                        .texOffs(37, 0)
                        .addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F),
                PartPose.offset(-4.0F, 11.0F, 0.0F)
        );

        // Left leg (mirrored)
        root.addOrReplaceChild("left_leg",
                CubeListBuilder.create()
                        .texOffs(60, 0)
                        .mirror()  // Mirror the texture for left side
                        .addBox(-3.5F, -3.0F, -3.0F, 6.0F, 16.0F, 5.0F),
                PartPose.offset(5.0F, 11.0F, 0.0F)
        );

        // Texture size: 128x128 pixels
        return LayerDefinition.create(mesh, 128, 128);
    }

    /**
     * Setup animations based on render state
     *
     * @param state The render state containing animation data
     *
     * Animation Math:
     * - Mth.triangleWave(pos, period): Creates oscillating value for walking
     * - xRot: Rotation around X axis (pitch - up/down)
     * - yRot: Rotation around Y axis (yaw - left/right)
     * - zRot: Rotation around Z axis (roll)
     *
     * Values are in radians! Convert degrees with: degrees * (Math.PI / 180.0)
     */
    @Override
    public void setupAnim(CookiumGolemRenderState state) {
        super.setupAnim(state);

        // Head rotation - follows where entity is looking
        this.head.yRot = state.yRot * ((float) Math.PI / 180.0F);
        this.head.xRot = state.xRot * ((float) Math.PI / 180.0F);

        // Walking animation
        float walkSpeed = state.walkAnimationSpeed;
        float walkPos = state.walkAnimationPos;

        // Legs swing opposite to each other during walking
        this.rightLeg.xRot = -1.5F * Mth.triangleWave(walkPos, 13.0F) * walkSpeed;
        this.leftLeg.xRot = 1.5F * Mth.triangleWave(walkPos, 13.0F) * walkSpeed;
        this.rightLeg.yRot = 0.0F;
        this.leftLeg.yRot = 0.0F;

        // Attack animation - swing arms down
        float attackTick = state.attackAnimationTick;
        if (attackTick > 0.0F) {
            // Both arms swing down during attack
            this.rightArm.xRot = -2.0F + 1.5F * Mth.triangleWave(attackTick, 10.0F);
            this.leftArm.xRot = -2.0F + 1.5F * Mth.triangleWave(attackTick, 10.0F);
        } else {
            // Normal walking arm swing
            this.rightArm.xRot = (-0.2F + 1.5F * Mth.triangleWave(walkPos, 13.0F)) * walkSpeed;
            this.leftArm.xRot = (-0.2F - 1.5F * Mth.triangleWave(walkPos, 13.0F)) * walkSpeed;
        }
    }
}
