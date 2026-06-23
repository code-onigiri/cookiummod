package com.code_onigiri.cookium.datagen;

import com.code_onigiri.cookium.CookiumMod;
import com.code_onigiri.cookium.item.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    private final PackOutput output;
    private final ExistingFileHelper existingFileHelper;

    public ModRecipeProvider(CompletableFuture<net.minecraft.core.HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(lookupProvider, RecipeOutput.create((id, advancement) -> {}));
        this.output = null;
        this.existingFileHelper = existingFileHelper;
    }

    @Override
    protected net.minecraft.data.recipes.RecipeOutput createOutputHolder(net.minecraft.resources.ResourceLocation id, net.minecraft.advancements.Advancement.Builder advancement) {
        return RecipeOutput.create((rId, adv) -> {});
    }

    @Override
    public void buildRecipes() {
        shapeless(RecipeCategory.MISC, ModItems.COOKIUM_INGOT.get())
                .requires(Ingredient.of(ModItems.COOKIUM_NUGGET.get()), 9)
                .unlockedBy("has_nugget", has(ModItems.COOKIUM_NUGGET.get()))
                .save(withName("cookium_ingot_from_nuggets"));

        shapeless(RecipeCategory.MISC, ModItems.COOKIUM_NUGGET.get(), 9)
                .requires(Ingredient.of(ModItems.COOKIUM_INGOT.get()))
                .unlockedBy("has_ingot", has(ModItems.COOKIUM_INGOT.get()))
                .save(withName("cookium_nuggets_from_ingot"));

        smithing(
                Ingredient.of(net.minecraft.world.item.Items.DIAMOND_SWORD),
                Ingredient.of(ModItems.COOKIUM_CORE.get()),
                RecipeCategory.COMBAT,
                ModItems.COOKIUM_DAGGER.get()
        ).unlockedBy("has_core", has(ModItems.COOKIUM_CORE.get())).save(withName("cookium_dagger_smithing"));

        smelting(
                Ingredient.of(ModItems.COOKIUM_NUGGET.get()),
                RecipeCategory.MISC,
                ModItems.COOKIUM_INGOT.get(),
                0.7f,
                200
        ).unlockedBy("has_nugget", has(ModItems.COOKIUM_NUGGET.get())).save(withName("cookium_ingot_smelting"));

        blasting(
                Ingredient.of(ModItems.COOKIUM_NUGGET.get()),
                RecipeCategory.MISC,
                ModItems.COOKIUM_INGOT.get(),
                0.7f,
                100
        ).unlockedBy("has_nugget", has(ModItems.COOKIUM_NUGGET.get())).save(withName("cookium_ingot_blasting"));
    }

    private String withName(String name) {
        return name;
    }
}
