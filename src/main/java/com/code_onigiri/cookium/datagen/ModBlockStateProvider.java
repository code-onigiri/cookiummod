package com.code_onigiri.cookium.datagen;

import com.code_onigiri.cookium.CookiumMod;
import com.code_onigiri.cookium.block.myblock.MyBlock;
import com.code_onigiri.cookium.block.biggerchest.BiggerChestBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BlockStateProvider;
import net.minecraft.data.worldgen.BlockStateGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProviderModelUtils;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, CompletableFuture<net.minecraft.core.HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, CookiumMod.MODID, existingFileHelper);
    }

    @Override
    public void registerStatesAndModels() {
        simpleBlock(CookiumMod.MY_ENTITY_BLOCK.get());
        
        directionalBlock(CookiumMod.BIGGER_CHEST_BLOCK.get(), 
                state -> state.getValue(BlockStateProperties.FACING));
    }

    private ResourceLocation key(Block block) {
        return Registries.BLOCK.getKey(block);
    }
}
