package com.code_onigiri.cookium.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.AbstractGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * CookiumGolem - A custom golem entity example for NeoForge 26.1
 *
 * Key NeoForge 26.1 Patterns:
 * - Extend PathfinderMob or AbstractGolem for mob AI
 * - Override registerGoals() for AI behavior
 * - Use createAttributes() static method for attribute registration
 * - ValueInput/ValueOutput replaces CompoundTag for NBT serialization
 *
 * AI Goal System:
 * - goalSelector - movement and behaviors
 * - targetSelector - targeting enemies
 * - Lower priority numbers = higher priority (1 runs before 2)
 *
 * Common AI Goals:
 * - FloatGoal - swim in water
 * - MeleeAttackGoal - attack target in melee
 * - MoveTowardsTargetGoal - move toward attack target
 * - WaterAvoidingRandomStrollGoal - random walking, avoids water
 * - LookAtPlayerGoal - look at nearby players
 * - RandomLookAroundGoal - look around randomly
 * - HurtByTargetGoal - target whoever hurt this mob
 * - NearestAttackableTargetGoal - target nearest entity of type
 */
public class CookiumGolem extends AbstractGolem {

    // Track attack animation for rendering
    private int attackAnimationTick;
    private static final int ATTACK_ANIMATION_DURATION = 10;

    public CookiumGolem(EntityType<? extends CookiumGolem> type, Level level) {
        super(type, level);
    }

    /**
     * Register AI goals - called once when entity is created
     *
     * Goal Priority Guide:
     * 1-2: Critical behaviors (swimming, panic)
     * 3-4: Combat behaviors (attack, move to target)
     * 5-6: Idle behaviors (wander, look around)
     */
    @Override
    protected void registerGoals() {
        // Movement goals
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(3, new MoveTowardsTargetGoal(this, 0.9, 32.0F));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        // Targeting goals
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        // Target hostile mobs, excluding creepers (like Iron Golem)
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(
                this, Mob.class, 5, false, false,
                (target, level) -> target instanceof Enemy
        ));
    }

    /**
     * Create attribute supplier for this entity type
     *
     * Required Attributes:
     * - MAX_HEALTH: Maximum health points
     * - MOVEMENT_SPEED: Base movement speed
     * - ATTACK_DAMAGE: Base melee damage
     * - KNOCKBACK_RESISTANCE: Resistance to knockback (0.0 to 1.0)
     * - FOLLOW_RANGE: How far the mob can "see" targets
     * - STEP_HEIGHT: How high the mob can step up blocks
     *
     * This is called from EntityAttributeCreationEvent
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 80.0)        // Less than Iron Golem (100)
                .add(Attributes.MOVEMENT_SPEED, 0.3)      // Slightly faster than Iron Golem (0.25)
                .add(Attributes.ATTACK_DAMAGE, 12.0)      // Less than Iron Golem (15)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8) // High knockback resistance
                .add(Attributes.FOLLOW_RANGE, 20.0)       // Can see targets from 20 blocks
                .add(Attributes.STEP_HEIGHT, 1.0);        // Can step up 1 block high
    }

    // ==================== COMBAT BEHAVIOR ====================

    /**
     * Called when this entity attacks another
     */
    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        this.attackAnimationTick = ATTACK_ANIMATION_DURATION;
        this.level().broadcastEntityEvent(this, (byte) 4); // Trigger attack animation on clients
        return super.doHurtTarget(level, target);
    }

    /**
     * Handle entity events from server (animations, effects)
     */
    @Override
    public void handleEntityEvent(byte eventId) {
        if (eventId == 4) {
            this.attackAnimationTick = ATTACK_ANIMATION_DURATION;
        } else {
            super.handleEntityEvent(eventId);
        }
    }

    /**
     * Called every tick
     */
    @Override
    public void aiStep() {
        super.aiStep();
        if (this.attackAnimationTick > 0) {
            this.attackAnimationTick--;
        }
    }

    /**
     * Get attack animation progress for rendering
     * Returns a value from 0.0 (no attack) to 1.0 (peak of attack)
     */
    public float getAttackAnimationTick() {
        return this.attackAnimationTick;
    }

    // ==================== SOUNDS ====================

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    // ==================== NBT SAVE/LOAD ====================

    /**
     * Save additional data to NBT
     * NeoForge 26.1: Uses ValueOutput instead of CompoundTag
     */
    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        // Example: Save custom data
        // output.putInt("CustomData", this.customValue);
    }

    /**
     * Load additional data from NBT
     * NeoForge 26.1: Uses ValueInput instead of CompoundTag
     */
    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        // Example: Load custom data
        // this.customValue = input.getIntOr("CustomData", 0);
    }

    // ==================== UTILITY ====================

    /**
     * This golem doesn't breathe, so no drowning
     */
    @Override
    protected int decreaseAirSupply(int currentSupply) {
        return currentSupply;
    }

    /**
     * Check if this entity can attack a target
     */
    @Override
    public boolean canAttack(LivingEntity target) {
        // Don't attack players (friendly golem)
        if (target.is(EntityType.PLAYER)) {
            return false;
        }
        return super.canAttack(target);
    }
}
