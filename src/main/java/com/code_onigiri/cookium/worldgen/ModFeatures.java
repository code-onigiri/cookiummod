package com.code_onigiri.cookium.worldgen;

import com.code_onigiri.cookium.CookiumMod;
import com.code_onigiri.cookium.block.myblock.MyBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> COOKIUM_ORE_KEY = FeatureUtils.createKey("cookium_ore");
    public static final ResourceKey<PlacedFeature> COOKIUM_ORE_PLACED_KEY = PlacedFeatures.createKey("cookium_ore_placed");

    public static void bootstrapConfigured(BootstrapContext<net.minecraft.world.level.levelgen.feature.ConfiguredFeature<?, ?>> context) {
        RuleTest stoneOreReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateOreReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> cookiumOreTargets = List.of(
                OreConfiguration.target(stoneOreReplaceables, MyBlock.BLOCK.defaultBlockState()),
                OreConfiguration.target(deepslateOreReplaceables, MyBlock.BLOCK.defaultBlockState())
        );

        FeatureUtils.register(
                context,
                COOKIUM_ORE_KEY,
                Feature.ORE,
                new OreConfiguration(cookiumOreTargets, 9)
        );
    }

    public static void bootstrapPlaced(BootstrapContext<PlacedFeature> context) {
        FeatureUtils.register(
                context,
                COOKIUM_ORE_PLACED_KEY,
                new PlacedFeature(
                        context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(COOKIUM_ORE_KEY),
                        List.of(
                                CountPlacement.of(5),
                                HeightRangePlacement.of(UniformHeight.of(-16, 48)),
                                PlacementFilter.forModded()
                        )
                )
        );
    }
}
