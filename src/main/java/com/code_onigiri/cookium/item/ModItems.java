package com.code_onigiri.cookium.item;

import com.code_onigiri.cookium.CookiumMod;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * ModItems - Comprehensive examples of item registration in NeoForge 26.1
 *
 * Key NeoForge 26.1 Changes:
 * - SwordItem, PickaxeItem, ArmorItem classes are REMOVED
 * - Tools/Weapons are now configured via Item.Properties methods:
 *   - .sword(ToolMaterial, attackDamage, attackSpeed)
 *   - .pickaxe(ToolMaterial, attackDamage, attackSpeed)
 *   - .axe(ToolMaterial, attackDamage, attackSpeed)
 *   - .shovel(ToolMaterial, attackDamage, attackSpeed)
 *   - .hoe(ToolMaterial, attackDamage, attackSpeed)
 * - Armor is configured via .humanoidArmor(ArmorMaterial, ArmorType)
 * - Food uses .food(FoodProperties) or .food(FoodProperties, Consumable)
 * - DataComponents system replaces item-specific classes
 */
public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CookiumMod.MODID);

    // ==================== BASIC ITEMS ====================

    /**
     * Simple item with no special properties
     */
    public static final DeferredItem<Item> COOKIUM_INGOT = ITEMS.registerSimpleItem(
            "cookium_ingot"
    );

    /**
     * Item with custom properties (rarity, stack size)
     */
    public static final DeferredItem<Item> COOKIUM_NUGGET = ITEMS.registerItem(
            "cookium_nugget",
            props -> new Item(props.rarity(Rarity.UNCOMMON).stacksTo(32))
    );

    /**
     * Fire-resistant item (like Netherite)
     */
    public static final DeferredItem<Item> COOKIUM_CORE = ITEMS.registerItem(
            "cookium_core",
            props -> new Item(props.fireResistant().rarity(Rarity.RARE))
    );

    // ==================== FOOD ITEMS ====================

    /**
     * Simple food item with basic nutrition
     * - nutrition: hunger points restored (1 = half drumstick)
     * - saturationModifier: how long before you get hungry again (higher = longer)
     */
    public static final DeferredItem<Item> COOKIUM_APPLE = ITEMS.registerItem(
            "cookium_apple",
            props -> new Item(props.food(
                    new FoodProperties.Builder()
                            .nutrition(6)              // 3 full drumsticks
                            .saturationModifier(0.8F)  // High saturation
                            .build()
            ))
    );

    /**
     * Always-edible food (can eat even when not hungry, like Golden Apple)
     */
    public static final DeferredItem<Item> COOKIUM_BERRY = ITEMS.registerItem(
            "cookium_berry",
            props -> new Item(props.food(
                    new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationModifier(1.2F)
                            .alwaysEdible()  // Can eat when full
                            .build()
            ))
    );

    /**
     * Food with custom consumable effects (like Golden Apple with effects)
     * Uses the new .food(FoodProperties, Consumable) signature
     */
    public static final DeferredItem<Item> GOLDEN_COOKIUM_APPLE = ITEMS.registerItem(
            "golden_cookium_apple",
            props -> new Item(props
                    .rarity(Rarity.RARE)
                    .food(
                            new FoodProperties.Builder()
                                    .nutrition(4)
                                    .saturationModifier(1.2F)
                                    .alwaysEdible()
                                    .build(),
                            Consumables.GOLDEN_APPLE  // Applies same effects as Golden Apple
                    ))
    );

    // ==================== TOOLS (using Item.Properties methods) ====================

    /**
     * Custom Tool Material for Cookium tools
     *
     * ToolMaterial record parameters:
     * @param incorrectBlocksForDrops - Tag of blocks this tool can't properly mine
     * @param durability - Tool durability (uses before breaking)
     * @param speed - Mining speed multiplier
     * @param attackDamageBonus - Bonus damage added to attacks
     * @param enchantmentValue - How enchantable the item is
     * @param repairItems - Tag of items that can repair this tool
     */
    public static final ToolMaterial COOKIUM_TOOL_MATERIAL = new ToolMaterial(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,  // Same mining capability as diamond
            1800,                                   // Durability between diamond (1561) and netherite (2031)
            9.0F,                                   // Faster than diamond (8.0F)
            3.5F,                                   // Bonus damage between diamond (3.0F) and netherite (4.0F)
            12,                                     // Enchantability
            ItemTags.DIAMOND_TOOL_MATERIALS         // Repaired with diamonds (or create custom tag)
    );

    /**
     * Sword using .sword() method
     * Attack damage = attackDamageBaseline + material.attackDamageBonus
     * Attack speed = attackSpeedBaseline (negative values = slower)
     */
    public static final DeferredItem<Item> COOKIUM_SWORD = ITEMS.registerItem(
            "cookium_sword",
            props -> new Item(props.sword(COOKIUM_TOOL_MATERIAL, 3.0F, -2.4F))
    );

    /**
     * Pickaxe using .pickaxe() method
     */
    public static final DeferredItem<Item> COOKIUM_PICKAXE = ITEMS.registerItem(
            "cookium_pickaxe",
            props -> new Item(props.pickaxe(COOKIUM_TOOL_MATERIAL, 1.0F, -2.8F))
    );

    /**
     * Axe using .axe() method
     */
    public static final DeferredItem<Item> COOKIUM_AXE = ITEMS.registerItem(
            "cookium_axe",
            props -> new Item(props.axe(COOKIUM_TOOL_MATERIAL, 5.0F, -3.0F))
    );

    /**
     * Shovel using .shovel() method
     */
    public static final DeferredItem<Item> COOKIUM_SHOVEL = ITEMS.registerItem(
            "cookium_shovel",
            props -> new Item(props.shovel(COOKIUM_TOOL_MATERIAL, 1.5F, -3.0F))
    );

    /**
     * Hoe using .hoe() method
     */
    public static final DeferredItem<Item> COOKIUM_HOE = ITEMS.registerItem(
            "cookium_hoe",
            props -> new Item(props.hoe(COOKIUM_TOOL_MATERIAL, -3.0F, 0.0F))
    );

    // ==================== ARMOR (using Item.Properties methods) ====================

    /**
     * Armor using .humanoidArmor() method
     *
     * ArmorMaterials available: LEATHER, COPPER, CHAINMAIL, IRON, GOLD, DIAMOND, NETHERITE
     * ArmorType available: HELMET, CHESTPLATE, LEGGINGS, BOOTS, BODY (for horse/wolf armor)
     *
     * Note: For custom armor materials, create an ArmorMaterial record
     */
    public static final DeferredItem<Item> COOKIUM_HELMET = ITEMS.registerItem(
            "cookium_helmet",
            props -> new Item(props.humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.HELMET))
    );

    public static final DeferredItem<Item> COOKIUM_CHESTPLATE = ITEMS.registerItem(
            "cookium_chestplate",
            props -> new Item(props.humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.CHESTPLATE))
    );

    public static final DeferredItem<Item> COOKIUM_LEGGINGS = ITEMS.registerItem(
            "cookium_leggings",
            props -> new Item(props.humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.LEGGINGS))
    );

    public static final DeferredItem<Item> COOKIUM_BOOTS = ITEMS.registerItem(
            "cookium_boots",
            props -> new Item(props.humanoidArmor(ArmorMaterials.DIAMOND, ArmorType.BOOTS))
    );

    // ==================== SPECIAL ITEMS ====================

    /**
     * Item with custom weapon component (damage on hit)
     * Useful for items that deal damage but aren't traditional tools
     */
    public static final DeferredItem<Item> COOKIUM_DAGGER = ITEMS.registerItem(
            "cookium_dagger",
            props -> new Item(props
                    .durability(500)
                    .component(DataComponents.WEAPON, new Weapon(1))  // 1 damage per hit
                    .rarity(Rarity.UNCOMMON))
    );

    /**
     * Item that leaves a remainder when used in crafting (like buckets)
     */
    public static final DeferredItem<Item> COOKIUM_BOTTLE = ITEMS.registerItem(
            "cookium_bottle",
            props -> new Item(props
                    .stacksTo(16)
                    .craftRemainder(net.minecraft.world.item.Items.GLASS_BOTTLE))
    );

    /**
     * Item with use cooldown (like Ender Pearl)
     */
    public static final DeferredItem<Item> COOKIUM_PEARL = ITEMS.registerItem(
            "cookium_pearl",
            props -> new Item(props
                    .stacksTo(16)
                    .useCooldown(2.0F)  // 2 second cooldown
                    .rarity(Rarity.UNCOMMON))
    );
}
