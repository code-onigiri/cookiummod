package com.code_onigiri.cookium.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class InfusionRecipe implements net.minecraft.world.item.crafting.Recipe<InfusionRecipe.InfusionRecipeInput> {
    public static final MapCodec<InfusionRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(InfusionRecipe::getIngredients),
            Ingredient.CODEC.fieldOf("catalyst").forGetter(InfusionRecipe::getCatalyst),
            ItemStack.CODEC.fieldOf("result").forGetter(InfusionRecipe::getResult),
            Codec.FLOAT.fieldOf("experience").orElse(0.0f).forGetter(r -> r.experience),
            Codec.INT.fieldOf("cookingtime").orElse(200).forGetter(r -> r.cookingTime)
    ).apply(inst, InfusionRecipe::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, InfusionRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), InfusionRecipe::getIngredients,
            Ingredient.CONTENTS_STREAM_CODEC, InfusionRecipe::getCatalyst,
            ItemStack.STREAM_CODEC, InfusionRecipe::getResult,
            ByteBufCodecs.FLOAT, r -> r.experience,
            ByteBufCodecs.INT, r -> r.cookingTime,
            InfusionRecipe::new
    );

    public static final RecipeSerializer<InfusionRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

    private final NonNullList<Ingredient> ingredients;
    private final Ingredient catalyst;
    private final ItemStack result;
    private final float experience;
    private final int cookingTime;

    public InfusionRecipe(java.util.List<Ingredient> ingredients, Ingredient catalyst, ItemStack result, float experience, int cookingTime) {
        this.ingredients = NonNullList.create();
        this.ingredients.addAll(ingredients);
        this.catalyst = catalyst;
        this.result = result;
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    @Override
    public boolean matches(InfusionRecipeInput input, Level level) {
        if (level.isClientSide()) return false;
        
        if (!catalyst.test(input.getCatalyst())) return false;
        
        int matched = 0;
        for (Ingredient ingredient : ingredients) {
            boolean found = false;
            for (int i = 0; i < input.size(); i++) {
                if (ingredient.test(input.getItem(i))) {
                    found = true;
                    break;
                }
            }
            if (found) matched++;
        }
        
        return matched == ingredients.size();
    }

    @Override
    public ItemStack assemble(InfusionRecipeInput input) {
        return result.copy();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public boolean showNotification() {
        return true;
    }

    @Override
    public String group() {
        return "";
    }

    @Override
    public RecipeSerializer<InfusionRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<InfusionRecipe> getType() {
        return ModRecipeTypes.INFUSION.get();
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return net.minecraft.world.item.crafting.RecipeBookCategories.CRAFTING_MISC;
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    public Ingredient getCatalyst() {
        return catalyst;
    }

    public ItemStack getResult() {
        return result;
    }

    public float getExperience() {
        return experience;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public static class InfusionRecipeInput implements RecipeInput {
        private final NonNullList<ItemStack> items;
        private final ItemStack catalyst;

        public InfusionRecipeInput(NonNullList<ItemStack> items, ItemStack catalyst) {
            this.items = items;
            this.catalyst = catalyst;
        }

        public ItemStack getCatalyst() {
            return catalyst;
        }

        @Override
        public int size() {
            return items.size();
        }

        @Override
        public ItemStack getItem(int slot) {
            return items.get(slot);
        }
    }
}
