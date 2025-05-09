package net.diemond_player.unidye.registry;

import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.block.*;
import net.diemond_player.unidye.item.DyeableBedItem;
import net.diemond_player.unidye.item.DyeableBlockItem;
import net.diemond_player.unidye.item.DyeableLeatheryBlockItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.BiFunction;

@SuppressWarnings("unused")
public class UnidyeBlocks {
    public static final Block CUSTOM_WOOL = registerBlock(Unidye.MOD_ID, "custom_wool",
            new DyeableWoolBlock(AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)),
            DyeableLeatheryBlockItem::new);

    public static final Block CUSTOM_CONCRETE = registerBlock(Unidye.MOD_ID,
            "custom_concrete",
            new DyeableBlock(AbstractBlock.Settings.copy(Blocks.WHITE_CONCRETE)),
            DyeableBlockItem::new);

    public static final Block CUSTOM_TERRACOTTA = registerBlock(Unidye.MOD_ID,
            "custom_terracotta",
            new DyeableBlock(AbstractBlock.Settings.copy(Blocks.TERRACOTTA)),
            DyeableBlockItem::new);

    public static final Block CUSTOM_STAINED_GLASS = registerBlock(Unidye.MOD_ID,
            "custom_stained_glass",
            new DyeableGlassBlock(AbstractBlock.Settings.copy(Blocks.GLASS)),
            DyeableLeatheryBlockItem::new);

    public static final Block CUSTOM_CONCRETE_POWDER = registerBlock(Unidye.MOD_ID,
            "custom_concrete_powder",
            new DyeableConcretePowderBlock(CUSTOM_CONCRETE, AbstractBlock.Settings.copy(Blocks.WHITE_CONCRETE_POWDER)),
            DyeableBlockItem::new);

    public static final Block CUSTOM_CARPET = registerBlock(Unidye.MOD_ID, "custom_carpet",
            new DyeableCarpetBlock(AbstractBlock.Settings.copy(Blocks.WHITE_CARPET)),
            DyeableBlockItem::new);

    public static final Block CUSTOM_STAINED_GLASS_PANE = registerBlock(Unidye.MOD_ID,
            "custom_stained_glass_pane",
            new DyeablePaneBlock(AbstractBlock.Settings.copy(Blocks.WHITE_STAINED_GLASS_PANE)),
            DyeableLeatheryBlockItem::new);

    public static final Block CUSTOM_CANDLE = registerBlock(Unidye.MOD_ID, "custom_candle",
            new DyeableCandleBlock(AbstractBlock.Settings.copy(Blocks.WHITE_CANDLE)),
            DyeableBlockItem::new);

    public static final Block CUSTOM_CANDLE_CAKE = registerBlockWithoutItem(Unidye.MOD_ID,
            "custom_candle_cake",
            new DyeableCandleCakeBlock(CUSTOM_CANDLE, AbstractBlock.Settings.copy(Blocks.WHITE_CANDLE_CAKE)));

    public static final Block CUSTOM_SHULKER_BOX = registerBlock(Unidye.MOD_ID, "custom_shulker_box",
            new DyeableShulkerBoxBlock(AbstractBlock.Settings.copy(Blocks.WHITE_SHULKER_BOX).pistonBehavior(PistonBehavior.DESTROY)),
            new Item.Settings().maxCount(1), DyeableBlockItem::new);

    public static final Block CUSTOM_BED = registerBlock(Unidye.MOD_ID,"custom_bed",
            new DyeableBedBlock(AbstractBlock.Settings.copy(Blocks.WHITE_BED).pistonBehavior(PistonBehavior.DESTROY)),
            new Item.Settings().maxCount(1), DyeableBedItem::new);

    public static final Block CUSTOM_BANNER = registerBlockWithoutItem(Unidye.MOD_ID,
            "custom_banner",
            new DyeableBannerBlock(AbstractBlock.Settings.copy(Blocks.WHITE_BANNER)));

    public static final Block CUSTOM_WALL_BANNER = registerBlockWithoutItem(Unidye.MOD_ID,
            "custom_wall_banner",
            new DyeableWallBannerBlock(AbstractBlock.Settings.copy(Blocks.WHITE_WALL_BANNER).dropsLike(CUSTOM_BANNER)));

    public static void registerModBlocks() {
        //Unidye.LOGGER.info("Registering Mod Blocks for " + Unidye.MOD_ID);
    }

    public static Block registerBlockWithoutItem(String modId, String name, Block block) {
        return Registry.register(Registries.BLOCK, Identifier.of(modId, name), block);
    }

    public static Block registerBlock(String modId, String name, Block block, Item.Settings fabricItemSettings, BiFunction<Block, Item.Settings, BlockItem> toItem) {
        registerBlockItem(modId, name, block, fabricItemSettings, toItem);
        return Registry.register(Registries.BLOCK, Identifier.of(modId, name), block);
    }

    public static Block registerBlock(String modId, String name, Block block, Item.Settings fabricItemSettings) {
        registerBlockItem(modId, name, block, fabricItemSettings, BlockItem::new);
        return Registry.register(Registries.BLOCK, Identifier.of(modId, name), block);
    }

    public static Block registerBlock(String modId, String name, Block block, BiFunction<Block, Item.Settings, BlockItem> toItem) {
        registerBlockItem(modId, name, block, new Item.Settings(), toItem);
        return Registry.register(Registries.BLOCK, Identifier.of(modId, name), block);
    }

    public static Block registerBlock(String modId, String name, Block block) {
        return registerBlock(modId, name, block, new Item.Settings());
    }

    public static void registerBlockItem(String modId, String name, Block block, Item.Settings fabricItemSettings, BiFunction<Block, Item.Settings, BlockItem> toItem) {
        Registry.register(Registries.ITEM, Identifier.of(modId, name), toItem.apply(block, fabricItemSettings));
    }
}
