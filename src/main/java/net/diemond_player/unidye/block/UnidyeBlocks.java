package net.diemond_player.unidye.block;

import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.block.custom.*;
import net.diemond_player.unidye.item.custom.DyeableBlockItem;
import net.diemond_player.unidye.item.custom.DyeableLeatheryBlockItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class UnidyeBlocks {
    //unlikely
    //TODOO dye blocks second layer! (umhhhhh)
    //FIXMEE sheep do not drop colored wool on death
    //FIXMEE when dropped on blocks; wrong particles
    //FIXMEE middlemouse + ctrl works not like intended
    //FIXMEE proper map markers check MapIcon.Type FilledMapItem map_icons MapRenderer
    //FIXMEE map colors

    //post-1.0
    //TODO jeb_ dye
    //TODO dye naming system (?)
    //TODO dye recipe saving system (?)
    //TODO add Llama carpets
    //TODO add Glazed Terracotta
    /*TODO integration with:
    DyeDepot
    El & L's Dyes
    Create
    Arts&Crafts
    Supplementaries
    SupplementariesSquared
    ...
     */

    //1.1.0 changelog
    //colorful tooltips
    // for vanilla dyes too!
    //villager bed fix
    //multiplayer crash
    //langs
    //sign display issue
    //shulker box and bed middle mouse issue
    //shulker box piston interaction
    //bed piston interaction


    public static final Block CUSTOM_WOOL = registerDyeableLeatheryBlock("custom_wool",
            new DyeableWoolBlock(AbstractBlock.Settings.copy(Blocks.WHITE_WOOL)));

    public static final Block CUSTOM_CONCRETE = registerDyeableBlock("custom_concrete",
            new DyeableBlock(AbstractBlock.Settings.copy(Blocks.WHITE_CONCRETE)));

    public static final Block CUSTOM_TERRACOTTA = registerDyeableBlock("custom_terracotta",
            new DyeableBlock(AbstractBlock.Settings.copy(Blocks.TERRACOTTA)));

    public static final Block CUSTOM_STAINED_GLASS = registerDyeableLeatheryBlock("custom_stained_glass",
            new DyeableGlassBlock(AbstractBlock.Settings.copy(Blocks.GLASS)));

    public static final Block CUSTOM_CONCRETE_POWDER = registerDyeableBlock("custom_concrete_powder",
            new DyeableConcretePowderBlock(CUSTOM_CONCRETE, AbstractBlock.Settings.copy(Blocks.WHITE_CONCRETE_POWDER)));

    public static final Block CUSTOM_CARPET = registerDyeableBlock("custom_carpet",
            new DyeableCarpetBlock(AbstractBlock.Settings.copy(Blocks.WHITE_CARPET)));

    public static final Block CUSTOM_STAINED_GLASS_PANE = registerDyeableLeatheryBlock("custom_stained_glass_pane",
            new DyeablePaneBlock(AbstractBlock.Settings.copy(Blocks.WHITE_STAINED_GLASS_PANE)));

    public static final Block CUSTOM_CANDLE = registerDyeableBlock("custom_candle",
            new DyeableCandleBlock(AbstractBlock.Settings.copy(Blocks.WHITE_CANDLE)));

    public static final Block CUSTOM_CANDLE_CAKE = registerBlockWithoutItem("custom_candle_cake",
            new DyeableCandleCakeBlock(CUSTOM_CANDLE, AbstractBlock.Settings.copy(Blocks.WHITE_CANDLE_CAKE)));

    public static final Block CUSTOM_SHULKER_BOX = registerDyeableBlock("custom_shulker_box",
            new DyeableShulkerBoxBlock(AbstractBlock.Settings.copy(Blocks.WHITE_SHULKER_BOX).pistonBehavior(PistonBehavior.DESTROY)), new Item.Settings().maxCount(1));

    public static final Block CUSTOM_BED = registerDyeableBlock("custom_bed",
            new DyeableBedBlock(AbstractBlock.Settings.copy(Blocks.WHITE_BED).pistonBehavior(PistonBehavior.DESTROY)), new Item.Settings().maxCount(1));

    public static final Block CUSTOM_BANNER = registerBlockWithoutItem("custom_banner",
            new DyeableBannerBlock(AbstractBlock.Settings.copy(Blocks.WHITE_BANNER)));

    public static final Block CUSTOM_WALL_BANNER = registerBlockWithoutItem("custom_wall_banner",
            new DyeableWallBannerBlock(AbstractBlock.Settings.copy(Blocks.WHITE_WALL_BANNER).dropsLike(CUSTOM_BANNER)));

    public static void registerModBlocks() {
//        Unidye.LOGGER.info("Registering Mod Blocks for " + Unidye.MOD_ID);
    }

    private static Block registerBlockWithoutItem(String name, Block block) {
        return Registry.register(Registries.BLOCK, Identifier.of(Unidye.MOD_ID, name), block);
    }

    private static Block registerDyeableBlock(String name, Block block) {
        registerDyeableBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Unidye.MOD_ID, name), block);
    }

    private static void registerDyeableBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Unidye.MOD_ID, name), new DyeableBlockItem(block, new Item.Settings()));
    }

    private static Block registerDyeableBlock(String name, Block block, Item.Settings fabricItemSettings) {
        registerDyeableBlockItem(name, block, fabricItemSettings);
        return Registry.register(Registries.BLOCK, Identifier.of(Unidye.MOD_ID, name), block);
    }

    private static void registerDyeableBlockItem(String name, Block block, Item.Settings fabricItemSettings) {
        Registry.register(Registries.ITEM, Identifier.of(Unidye.MOD_ID, name), new DyeableBlockItem(block, fabricItemSettings));
    }

    private static Block registerDyeableLeatheryBlock(String name, Block block) {
        registerDyeableLeatheryBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Unidye.MOD_ID, name), block);
    }

    private static void registerDyeableLeatheryBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Unidye.MOD_ID, name), new DyeableLeatheryBlockItem(block, new Item.Settings()));
    }
}
