package com.code_onigiri.cookium.events;

import com.code_onigiri.cookium.CookiumMod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = CookiumMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEvents {
    
    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof ServerPlayer player) {
            CookiumMod.LOGGER.info("Player {} joined the game!", player.getName().getString());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        CookiumMod.LOGGER.info("Player cloned: {}", event.getOriginal().getName().getString());
    }

    @SubscribeEvent
    public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof LivingEntity living) {
            CookiumMod.LOGGER.debug("Player {} started tracking {}", 
                    event.getEntity().getName().getString(), living.getName().getString());
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        DamageSource source = event.getSource();
        LivingEntity entity = event.getEntity();
        CookiumMod.LOGGER.debug("{} was attacked by {}", entity.getName().getString(), source);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        float damage = event.getAmount();
        DamageSource source = event.getSource();
        
        if (damage > 5.0f) {
            CookiumMod.LOGGER.info("{} took heavy damage: {} from {}", 
                    entity.getName().getString(), damage, source);
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathDropsEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        CookiumMod.LOGGER.info("{} died from {}", entity.getName().getString(), source);
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        for (ItemEntity drop : event.getDrops()) {
            CookiumMod.LOGGER.debug("Dropped: {}", drop.getItem().getDisplayName().getString());
        }
    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        float amount = event.getAmount();
        if (entity instanceof Player player && amount > 0) {
            CookiumMod.LOGGER.debug("Player {} healed for {}", player.getName().getString(), amount);
        }
    }

    @SubscribeEvent
    public static void onMobEffectApplicable(MobEffectEvent.Applicable event) {
        LivingEntity entity = event.getEntity();
        MobEffect effect = event.getEffect();
        CookiumMod.LOGGER.debug("Effect {} applied to {}", effect.getName(), entity.getName().getString());
    }

    @SubscribeEvent
    public static void onMobEffectAdded(MobEffectEvent.Added event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance effectInstance = event.getEffectInstance();
        CookiumMod.LOGGER.info("Effect {} added to {}", 
                effectInstance.getEffect().getName(), entity.getName().getString());
    }

    @SubscribeEvent
    public static void onMobEffectRemoved(MobEffectEvent.Remove event) {
        LivingEntity entity = event.getEntity();
        MobEffect effect = event.getEffect();
        CookiumMod.LOGGER.info("Effect {} removed from {}", 
                effect.getName(), entity.getName().getString());
    }

    @SubscribeEvent
    public static void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack itemStack = event.getItemStack();
        CookiumMod.LOGGER.debug("Player {} right-clicked with {}", 
                player.getName().getString(), itemStack.getDisplayName().getString());
    }

    @SubscribeEvent
    public static void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        CookiumMod.LOGGER.debug("Player {} broke block at {}", 
                player.getName().getString(), event.getPos());
    }

    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event) {
        Player player = event.getEntity();
        CookiumMod.LOGGER.info("Player {} caught {} items", 
                player.getName().getString(), event.getDrops().size());
    }
}
