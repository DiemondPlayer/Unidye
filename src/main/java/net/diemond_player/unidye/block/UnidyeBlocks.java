package net.diemond_player.unidye.block;

import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.block.custom.*;
import net.diemond_player.unidye.item.custom.DyeableBlockItem;
import net.diemond_player.unidye.item.custom.DyeableLeatheryBlockItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class UnidyeBlocks {
    //post-1.0
    //TODO jeb_ dye
    //TODO dye naming system (?)
    //TODO dye recipe saving system (?)
    //TODO add Llama carpets
    //TODO add Glazed Terracotta
    //FIXME villagers do not use beds
    //FIXME sheep do not drop colored wool on death
    //FIXME when dropped on blocks; wrong particles
    //FIXME middlemouse + ctrl works not like intended
    //FIXME proper map markers check MapIcon.Type FilledMapItem map_icons MapRenderer
    //FIXME map colors
    //TODO dye blocks second layer! (umhhhhh)
    //FIXME bed and shulker middlemouse click
    //TODO en_gb and ru_ru translations
    /*TODO integration with:
    DyeDepot
    Create
    Arts&Crafts
    Supplementaries
    SupplementariesSquared
    ...
     */

    //1.1.0 changelog
    //colorful tooltips
    //villager bed fix
    //langs


    public static final Block CUSTOM_WOOL = registerDyeableLeatheryBlock("custom_wool",
            new DyeableWoolBlock(FabricBlockSettings.copyOf(Blocks.WHITE_WOOL)));

    public static final Block CUSTOM_CONCRETE = registerDyeableBlock("custom_concrete",
            new DyeableBlock(FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE)));

    public static final Block CUSTOM_TERRACOTTA = registerDyeableBlock("custom_terracotta",
            new DyeableBlock(FabricBlockSettings.copyOf(Blocks.TERRACOTTA)));

    public static final Block CUSTOM_STAINED_GLASS = registerDyeableLeatheryBlock("custom_stained_glass",
            new DyeableGlassBlock(FabricBlockSettings.copyOf(Blocks.GLASS)));

    public static final Block CUSTOM_CONCRETE_POWDER = registerDyeableBlock("custom_concrete_powder",
            new DyeableConcretePowderBlock(CUSTOM_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE_POWDER)));

    public static final Block CUSTOM_CARPET = registerDyeableBlock("custom_carpet",
            new DyeableCarpetBlock(FabricBlockSettings.copyOf(Blocks.WHITE_CARPET)));

    public static final Block CUSTOM_STAINED_GLASS_PANE = registerDyeableLeatheryBlock("custom_stained_glass_pane",
            new DyeablePaneBlock(FabricBlockSettings.copyOf(Blocks.WHITE_STAINED_GLASS_PANE)));

    public static final Block CUSTOM_CANDLE = registerDyeableBlock("custom_candle",
            new DyeableCandleBlock(FabricBlockSettings.copyOf(Blocks.WHITE_CANDLE)));

    public static final Block CUSTOM_CANDLE_CAKE = registerBlockWithoutItem("custom_candle_cake",
            new DyeableCandleCakeBlock(CUSTOM_CANDLE, FabricBlockSettings.copyOf(Blocks.WHITE_CANDLE_CAKE)));

    public static final Block CUSTOM_SHULKER_BOX = registerDyeableBlock("custom_shulker_box",
            new DyeableShulkerBoxBlock(FabricBlockSettings.copyOf(Blocks.WHITE_SHULKER_BOX)), new FabricItemSettings().maxCount(1));

    public static final Block CUSTOM_BED = registerDyeableBlock("custom_bed",
            new DyeableBedBlock(FabricBlockSettings.copyOf(Blocks.WHITE_BED)), new FabricItemSettings().maxCount(1));

    public static final Block CUSTOM_BANNER = registerBlockWithoutItem("custom_banner",
            new DyeableBannerBlock(FabricBlockSettings.copyOf(Blocks.WHITE_BANNER)));

    public static final Block CUSTOM_WALL_BANNER = registerBlockWithoutItem("custom_wall_banner",
            new DyeableWallBannerBlock(FabricBlockSettings.copyOf(Blocks.WHITE_WALL_BANNER).dropsLike(CUSTOM_BANNER)));

    public static void registerModBlocks() {
        Unidye.LOGGER.info("Registering Mod Blocks for " + Unidye.MOD_ID);
    }

    private static Block registerBlockWithoutItem(String name, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(Unidye.MOD_ID, name), block);
    }

    private static Block registerDyeableBlock(String name, Block block) {
        registerDyeableBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(Unidye.MOD_ID, name), block);
    }

    private static void registerDyeableBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, new Identifier(Unidye.MOD_ID, name), new DyeableBlockItem(block, new FabricItemSettings()));
    }

    private static Block registerDyeableBlock(String name, Block block, FabricItemSettings fabricItemSettings) {
        registerDyeableBlockItem(name, block, fabricItemSettings);
        return Registry.register(Registries.BLOCK, new Identifier(Unidye.MOD_ID, name), block);
    }

    private static void registerDyeableBlockItem(String name, Block block, FabricItemSettings fabricItemSettings) {
        Registry.register(Registries.ITEM, new Identifier(Unidye.MOD_ID, name), new DyeableBlockItem(block, fabricItemSettings));
    }

    private static Block registerDyeableLeatheryBlock(String name, Block block) {
        registerDyeableLeatheryBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(Unidye.MOD_ID, name), block);
    }

    private static void registerDyeableLeatheryBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, new Identifier(Unidye.MOD_ID, name), new DyeableLeatheryBlockItem(block, new FabricItemSettings()));
    }
}
