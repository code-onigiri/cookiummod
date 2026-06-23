package com.code_onigiri.cookium.datagen;

import com.code_onigiri.cookium.CookiumMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = CookiumMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModDataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(
                event.includeServer(),
                new ModRecipeProvider(event.getLookupProvider(), event.getExistingFileHelper())
        );
        
        event.getGenerator().addProvider(
                event.includeServer(),
                new ModLootTableProvider(event.getLookupProvider(), event.getExistingFileHelper())
        );
        
        event.getGenerator().addProvider(
                event.includeClient(),
                new ModBlockStateProvider(event.getLookupProvider(), event.getExistingFileHelper())
        );
        
        event.getGenerator().addProvider(
                event.includeClient(),
                new ModItemModelProvider(event.getLookupProvider(), event.getExistingFileHelper())
        );
    }
}
