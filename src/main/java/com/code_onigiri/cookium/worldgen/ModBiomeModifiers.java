package com.code_onigiri.cookium.worldgen;

import com.code_onigiri.cookium.CookiumMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BiomeModifierProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BiomeLoadingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBiomeModifiers {
    public static final DeferredRegister<net.minecraft.core.registries.RegistryEntryInfo> BIOME_MODIFIERS = 
            DeferredRegister.create(Registries.BIOME_MODIFIER, CookiumMod.MODID);

    @SubscribeEvent
    public static void onBiomeLoad(BiomeLoadingEvent event) {
        if (event.getCategory() == Biome.BiomeCategory.UNDERGROUND) {
            event.getGeneration().getFeatures(net.minecraft.world.level.biome.BiomeGenerationSettings.Step.UNDERGROUND_ORES)
                    .add(ModFeatures.COOKIUM_ORE_PLACED_KEY);
        }
    }
}
