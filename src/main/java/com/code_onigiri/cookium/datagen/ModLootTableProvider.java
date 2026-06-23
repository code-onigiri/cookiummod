package com.code_onigiri.cookium.datagen;

import com.code_onigiri.cookium.CookiumMod;
import com.code_onigiri.cookium.block.myblock.MyBlock;
import com.code_onigiri.cookium.block.biggerchest.BiggerChestBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.LootTableProvider;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider extends LootTableProvider {
    private final PackOutput output;
    private final CompletableFuture<Set<ResourceLocation>> lookupProvider;
    private final ExistingFileHelper existingFileHelper;

    public ModLootTableProvider(PackOutput output, CompletableFuture<Set<ResourceLocation>> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(
                output,
                Set.of(),
                List.of(
                        new LootTableProvider.SubProviderEntry(
                                LootTableSubProvider::new,
                                LootContextParamSets.BLOCK
                        )
                ),
                lookupProvider
        );
        this.output = output;
        this.lookupProvider = lookupProvider;
        this.existingFileHelper = existingFileHelper;
    }

    @Override
    public void generate() {
        generate(LootTableSubProvider.class, subProvider -> {
            subProvider.addDrop(CookiumMod.MY_ENTITY_BLOCK.get());
            subProvider.addDrop(CookiumMod.BIGGER_CHEST_BLOCK.get());
        });
    }

    private static class LootTableSubProvider implements LootTableSubProvider {
        @Override
        public void generate() {
        }
    }
}
