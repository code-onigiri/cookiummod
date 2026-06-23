package com.code_onigiri.cookium.recipe;

import com.code_onigiri.cookium.CookiumMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = 
            DeferredRegister.create(Registries.RECIPE_TYPE, CookiumMod.MODID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<InfusionRecipe>> INFUSION = RECIPE_TYPES.register(
            "infusion",
            () -> RecipeType.simple(net.minecraft.resources.Identifier.fromNamespaceAndPath(CookiumMod.MODID, "infusion"))
    );
}
