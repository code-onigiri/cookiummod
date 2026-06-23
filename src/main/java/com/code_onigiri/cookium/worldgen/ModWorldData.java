package com.code_onigiri.cookium.worldgen;

import com.code_onigiri.cookium.CookiumMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BiomeModifierProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = CookiumMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModWorldData {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(
                event.includeServer(),
                new BiomeModifierProvider(
                        event.getLookupProvider(),
                        event.getExistingFileHelper(),
                        CookiumMod.MODID,
                        builder -> builder.addBiomeModifier(
                                net.minecraft.core.registries.Registries.BIOME,
                                net.minecraft.core.registries.BuiltInRegistries.PLACED_FEATURE,
                                ModFeatures.COOKIUM_ORE_PLACED_KEY,
                                net.minecraft.world.level.biome.Biome.BiomeCategory.UNDERGROUND
                        )
                )
        );
    }
}
