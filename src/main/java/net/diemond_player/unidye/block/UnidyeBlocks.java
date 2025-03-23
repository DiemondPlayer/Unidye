package net.diemond_player.unidye.block;

import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.block.custom.*;
import net.diemond_player.unidye.item.custom.DyeableBedItem;
import net.diemond_player.unidye.item.custom.DyeableBlockItem;
import net.diemond_player.unidye.item.custom.DyeableLeatheryBlockItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

public class UnidyeBlocks {
    public static final Block CUSTOM_WOOL = registerBlock(Unidye.MOD_ID, "custom_wool",
            new DyeableWoolBlock(FabricBlockSettings.copyOf(Blocks.WHITE_WOOL)),
            DyeableLeatheryBlockItem::new);

    public static final Block CUSTOM_CONCRETE = registerBlock(Unidye.MOD_ID,
            "custom_concrete",
            new DyeableBlock(FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE)),
            DyeableBlockItem::new);

    public static final Block CUSTOM_TERRACOTTA = registerBlock(Unidye.MOD_ID,
            "custom_terracotta",
            new DyeableBlock(FabricBlockSettings.copyOf(Blocks.TERRACOTTA)),
            DyeableBlockItem::new);

    public static final Block CUSTOM_STAINED_GLASS = registerBlock(Unidye.MOD_ID,
            "custom_stained_glass",
            new DyeableGlassBlock(FabricBlockSettings.copyOf(Blocks.GLASS)),
            DyeableLeatheryBlockItem::new);

    public static final Block CUSTOM_CONCRETE_POWDER = registerBlock(Unidye.MOD_ID,
            "custom_concrete_powder",
            new DyeableConcretePowderBlock(CUSTOM_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE_POWDER)),
            DyeableBlockItem::new);

    public static final Block CUSTOM_CARPET = registerBlock(Unidye.MOD_ID, "custom_carpet",
            new DyeableCarpetBlock(FabricBlockSettings.copyOf(Blocks.WHITE_CARPET)),
            DyeableBlockItem::new);

    public static final Block CUSTOM_STAINED_GLASS_PANE = registerBlock(Unidye.MOD_ID,
            "custom_stained_glass_pane",
            new DyeablePaneBlock(FabricBlockSettings.copyOf(Blocks.WHITE_STAINED_GLASS_PANE)),
            DyeableLeatheryBlockItem::new);

    public static final Block CUSTOM_CANDLE = registerBlock(Unidye.MOD_ID, "custom_candle",
            new DyeableCandleBlock(FabricBlockSettings.copyOf(Blocks.WHITE_CANDLE)),
            DyeableBlockItem::new);

    public static final Block CUSTOM_CANDLE_CAKE = registerBlockWithoutItem(Unidye.MOD_ID,
            "custom_candle_cake",
            new DyeableCandleCakeBlock(CUSTOM_CANDLE, FabricBlockSettings.copyOf(Blocks.WHITE_CANDLE_CAKE)));

    public static final Block CUSTOM_SHULKER_BOX = registerBlock(Unidye.MOD_ID, "custom_shulker_box",
            new DyeableShulkerBoxBlock(FabricBlockSettings.copyOf(Blocks.WHITE_SHULKER_BOX).pistonBehavior(PistonBehavior.DESTROY)),
            new FabricItemSettings().maxCount(1), DyeableBlockItem::new);

    public static final Block CUSTOM_BED = registerBlock(Unidye.MOD_ID,"custom_bed",
            new DyeableBedBlock(FabricBlockSettings.copyOf(Blocks.WHITE_BED).pistonBehavior(PistonBehavior.DESTROY)),
            new FabricItemSettings().maxCount(1), DyeableBedItem::new);

    public static final Block CUSTOM_BANNER = registerBlockWithoutItem(Unidye.MOD_ID,
            "custom_banner",
            new DyeableBannerBlock(FabricBlockSettings.copyOf(Blocks.WHITE_BANNER)));

    public static final Block CUSTOM_WALL_BANNER = registerBlockWithoutItem(Unidye.MOD_ID,
            "custom_wall_banner",
            new DyeableWallBannerBlock(FabricBlockSettings.copyOf(Blocks.WHITE_WALL_BANNER).dropsLike(CUSTOM_BANNER)));

    public static void registerModBlocks() {
        //Unidye.LOGGER.info("Registering Mod Blocks for " + Unidye.MOD_ID);
    }

    public static Block registerBlockWithoutItem(String modId, String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(modId, name), block);
    }

    public static Block registerBlock(String modId, String name, Block block, FabricItemSettings fabricItemSettings, BiFunction<Block, FabricItemSettings, BlockItem> toItem) {
        registerBlockItem(modId, name, block, fabricItemSettings, toItem);
        return Registry.register(Registries.BLOCK, new Identifier(modId, name), block);
    }

    public static Block registerBlock(String modId, String name, Block block, FabricItemSettings fabricItemSettings) {
        registerBlockItem(modId, name, block, fabricItemSettings, BlockItem::new);
        return Registry.register(Registries.BLOCK, new Identifier(modId, name), block);
    }

    public static Block registerBlock(String modId, String name, Block block, BiFunction<Block, FabricItemSettings, BlockItem> toItem) {
        registerBlockItem(modId, name, block, new FabricItemSettings(), toItem);
        return Registry.register(Registries.BLOCK, new Identifier(modId, name), block);
    }

    public static Block registerBlock(String modId, String name, Block block) {
        return registerBlock(modId, name, block, new FabricItemSettings());
    }

    public static void registerBlockItem(String modId, String name, Block block, FabricItemSettings fabricItemSettings, BiFunction<Block, FabricItemSettings, BlockItem> toItem) {
        Registry.register(Registries.ITEM, new Identifier(modId, name), toItem.apply(block, fabricItemSettings));
    }
}
