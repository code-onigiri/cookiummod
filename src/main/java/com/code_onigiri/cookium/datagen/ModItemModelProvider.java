package com.code_onigiri.cookium.datagen;

import com.code_onigiri.cookium.CookiumMod;
import com.code_onigiri.cookium.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.ItemModels;
import net.minecraft.data.client.model.BlockModelGenerators;
import net.minecraft.data.client.model.Models;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModItemModelProvider extends ItemModelGenerator {
    public ModItemModelProvider(PackOutput output, CompletableFuture<net.minecraft.core.HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, CookiumMod.MODID, existingFileHelper);
    }

    @Override
    public void run(net.minecraft.data.CachedOutput cache) {
        super.run(cache);
        generateItemModels();
    }

    private void generateItemModels() {
        handheld(ModItems.COOKIUM_SWORD.get());
        handheld(ModItems.COOKIUM_PICKAXE.get());
        handheld(ModItems.COOKIUM_AXE.get());
        handheld(ModItems.COOKIUM_SHOVEL.get());
        handheld(ModItems.COOKIUM_HOE.get());
        handheld(ModItems.COOKIUM_DAGGER.get());

        generated(ModItems.COOKIUM_INGOT.get());
        generated(ModItems.COOKIUM_NUGGET.get());
        generated(ModItems.COOKIUM_CORE.get());
        generated(ModItems.COOKIUM_APPLE.get());
        generated(ModItems.COOKIUM_BERRY.get());
        generated(ModItems.GOLDEN_COOKIUM_APPLE.get());
        generated(ModItems.COOKIUM_BOTTLE.get());
        generated(ModItems.COOKIUM_PEARL.get());

        handheld(ModItems.COOKIUM_HELMET.get());
        handheld(ModItems.COOKIUM_CHESTPLATE.get());
        handheld(ModItems.COOKIUM_LEGGINGS.get());
        handheld(ModItems.COOKIUM_BOOTS.get());
    }

    private ResourceLocation key(Item item) {
        return Registries.ITEM.getKey(item);
    }

    private void generated(Item item) {
        withExistingParent(key(item).toString(), 
                ResourceLocation.withDefaultNamespace("item/generated"))
                .override().model(Models.HANDHELD).predicate(
                        net.minecraft.world.item.ItemPropertiesComponent.getAccessor(net.minecraft.world.item.Items.FIREWORK_ROCKET),
                        1.0F
                ).end();
    }

    private void handheld(Item item) {
        withExistingParent(key(item).toString(), 
                ResourceLocation.withDefaultNamespace("item/handheld"));
    }
}
