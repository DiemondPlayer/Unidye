package net.diemond_player.unidye.block;

import net.diemond_player.unidye.block.custom.*;
import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.item.custom.DyeableBlockItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block CUSTOM_WOOL = registerDyeableBlock("custom_wool",
            new DyeableBlock(FabricBlockSettings.copyOf(Blocks.WHITE_WOOL)));
    public static final Block CUSTOM_CONCRETE = registerDyeableBlock("custom_concrete",
            new DyeableBlock(FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE)));
    public static final Block CUSTOM_TERRACOTTA = registerDyeableBlock("custom_terracotta",
            new DyeableBlock(FabricBlockSettings.copyOf(Blocks.TERRACOTTA)));
    public static final Block CUSTOM_STAINED_GLASS = registerDyeableBlock("custom_stained_glass",
            new DyeableGlassBlock(FabricBlockSettings.copyOf(Blocks.GLASS)));
    public static final Block CUSTOM_CONCRETE_POWDER = registerDyeableBlock("custom_concrete_powder",
            new DyeableConcretePowderBlock(CUSTOM_CONCRETE, FabricBlockSettings.copyOf(Blocks.WHITE_CONCRETE_POWDER)));
    public static final Block CUSTOM_CARPET = registerDyeableBlock("custom_carpet",
            new DyeableCarpetBlock(FabricBlockSettings.copyOf(Blocks.WHITE_CARPET)));
    public static final Block CUSTOM_STAINED_GLASS_PANE = registerDyeableBlock("custom_stained_glass_pane",
            new DyeablePaneBlock(FabricBlockSettings.copyOf(Blocks.WHITE_STAINED_GLASS_PANE)));
    public static final Block CUSTOM_CANDLE = registerDyeableBlock("custom_candle",
            new DyeableCandleBlock(FabricBlockSettings.copyOf(Blocks.WHITE_CANDLE)));
    public static final Block CUSTOM_SHULKER_BOX = registerDyeableBlock("custom_shulker_box",
            new DyeableShulkerBoxBlock(null, FabricBlockSettings.copyOf(Blocks.WHITE_SHULKER_BOX)));
    public static final Block CUSTOM_BED = registerDyeableBlock("custom_bed",
            new DyeableBedBlock(FabricBlockSettings.copyOf(Blocks.WHITE_SHULKER_BOX)));
    public static final Block TEST_BLOCK = registerDyeableBlock("test_block",
            new TestBlock(FabricBlockSettings.copyOf(Blocks.MUD_BRICKS)));

    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(Unidye.MOD_ID, name), block);
    }

    private static Block registerBlockWithoutItem(String name, Block block){
        return Registry.register(Registries.BLOCK, new Identifier(Unidye.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block){
        return Registry.register(Registries.ITEM, new Identifier(Unidye.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks(){
        Unidye.LOGGER.info("Registering Mod Blocks for" + Unidye.MOD_ID);
}
    private static Block registerDyeableBlock(String name, Block block) {
        registerDyeableBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(Unidye.MOD_ID, name), block);
    }
    private static void registerDyeableBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, new Identifier(Unidye.MOD_ID, name), new DyeableBlockItem(block, new FabricItemSettings()));
    }
}
