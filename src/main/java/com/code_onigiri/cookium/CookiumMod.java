package com.code_onigiri.cookium;

import com.code_onigiri.cookium.block_entity.biggerchest.BiggerChestBE;
import com.code_onigiri.cookium.block_entity.biggerchest.BiggerChestBlock;
import com.code_onigiri.cookium.block_entity.biggerchest.BiggerChestMenu;
import com.code_onigiri.cookium.block_entity.myblock.MyBlockEntity;
import com.code_onigiri.cookium.block_entity.myblock.MyBlockMenu;
import com.code_onigiri.cookium.block_entity.myblock.MyEntityBlock;
import com.code_onigiri.cookium.entity.ModEntities;
import com.code_onigiri.cookium.entity.ModEntityEvents;
import com.code_onigiri.cookium.item.ModItems;
import com.code_onigiri.cookium.recipe.ModRecipeSerializers;
import com.code_onigiri.cookium.recipe.ModRecipeTypes;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import java.util.function.Supplier;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(CookiumMod.MODID)
public class CookiumMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "cookiummod";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "cookiummod" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "cookiummod" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "cookiummod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredBlock<MyEntityBlock> MY_ENTITY_BLOCK = BLOCKS.registerBlock(
            "my_entity_block",
            MyEntityBlock::new
    );

    public static final DeferredBlock<BiggerChestBlock> BIGGER_CHEST_BLOCK = BLOCKS.registerBlock(
            "bigger_chest_block",
            BiggerChestBlock::new
    );

    public static final DeferredItem<BlockItem> MY_BLOCK_ENTITY_ITEM = ITEMS.registerSimpleBlockItem(MY_ENTITY_BLOCK);
    public static final DeferredItem<BlockItem> BIGGER_CHEST_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(BIGGER_CHEST_BLOCK);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.cookiummod"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.COOKIUM_INGOT.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(MY_BLOCK_ENTITY_ITEM);
                output.accept(BIGGER_CHEST_BLOCK_ITEM);
                output.accept(ModItems.COOKIUM_INGOT);
                output.accept(ModItems.COOKIUM_NUGGET);
                output.accept(ModItems.COOKIUM_CORE);
                output.accept(ModItems.COOKIUM_APPLE);
                output.accept(ModItems.COOKIUM_BERRY);
                output.accept(ModItems.GOLDEN_COOKIUM_APPLE);
                output.accept(ModItems.COOKIUM_SWORD);
                output.accept(ModItems.COOKIUM_PICKAXE);
                output.accept(ModItems.COOKIUM_AXE);
                output.accept(ModItems.COOKIUM_SHOVEL);
                output.accept(ModItems.COOKIUM_HOE);
                output.accept(ModItems.COOKIUM_HELMET);
                output.accept(ModItems.COOKIUM_CHESTPLATE);
                output.accept(ModItems.COOKIUM_LEGGINGS);
                output.accept(ModItems.COOKIUM_BOOTS);
                output.accept(ModItems.COOKIUM_DAGGER);
                output.accept(ModItems.COOKIUM_BOTTLE);
                output.accept(ModItems.COOKIUM_PEARL);
            }).build());


    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);

    public static final Supplier<BlockEntityType<MyBlockEntity>> MY_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register(
            "my_block_entity",
            () -> new BlockEntityType<>(
                    MyBlockEntity::new,
                    false,
                    MY_ENTITY_BLOCK.get()
            )
    );

    public static final Supplier<BlockEntityType<BiggerChestBE>> BIGGER_CHEST_BE = BLOCK_ENTITY_TYPES.register(
            "bigger_chest_be",
            () -> new BlockEntityType<>(
                    BiggerChestBE::new,
                    false,
                    BIGGER_CHEST_BLOCK.get()
            )
    );

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, MODID);

    public static final Supplier<MenuType<MyBlockMenu>> MY_MENU = MENU_TYPES.register(
            "my_menu",
            () -> new MenuType<>(MyBlockMenu::new, FeatureFlags.VANILLA_SET)
    );

    public static final Supplier<MenuType<BiggerChestMenu>> BIGGER_CHEST_MENU = MENU_TYPES.register(
            "bigger_chest_menu",
            () -> new MenuType<>(BiggerChestMenu::new, FeatureFlags.VANILLA_SET)
    );


    public CookiumMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(RegisterCapabilitiesEvent.class, MyBlockEntity::registerCapabilities);
        modEventBus.addListener(RegisterCapabilitiesEvent.class, BiggerChestBE::registerCapabilities);

        modEventBus.addListener(this::commonSetup);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        BLOCK_ENTITY_TYPES.register(modEventBus);
        MENU_TYPES.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModEntityEvents.register(modEventBus);
        ModRecipeTypes.RECIPE_TYPES.register(modEventBus);
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
