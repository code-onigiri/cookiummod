package com.code_onigiri.cookium.recipe;

import com.code_onigiri.cookium.CookiumMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = 
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, CookiumMod.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<InfusionRecipe>> INFUSION = RECIPE_SERIALIZERS.register(
            "infusion",
            () -> InfusionRecipe.SERIALIZER
    );
}
