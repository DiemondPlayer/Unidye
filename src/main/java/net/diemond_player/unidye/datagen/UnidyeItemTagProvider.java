package net.diemond_player.unidye.datagen;

import net.diemond_player.unidye.registry.UnidyeBlocks;
import net.diemond_player.unidye.registry.UnidyeItems;
import net.diemond_player.unidye.registry.UnidyeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;

import java.util.concurrent.CompletableFuture;

public class UnidyeItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public UnidyeItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(ItemTags.CANDLES)
                .add(UnidyeBlocks.CUSTOM_CANDLE.asItem());
        getOrCreateTagBuilder(ItemTags.DAMPENS_VIBRATIONS)
                .add(UnidyeBlocks.CUSTOM_CARPET.asItem(),
                        UnidyeBlocks.CUSTOM_WOOL.asItem());
        getOrCreateTagBuilder(ItemTags.TERRACOTTA)
                .add(UnidyeBlocks.CUSTOM_TERRACOTTA.asItem());
        getOrCreateTagBuilder(ItemTags.WOOL)
                .add(UnidyeBlocks.CUSTOM_WOOL.asItem());
        getOrCreateTagBuilder(ItemTags.BEDS)
                .add(UnidyeBlocks.CUSTOM_BED.asItem());
        getOrCreateTagBuilder(ItemTags.WOOL_CARPETS)
                .add(UnidyeBlocks.CUSTOM_CARPET.asItem());
        getOrCreateTagBuilder(ConventionalItemTags.GLASS_BLOCKS)
                .add(UnidyeBlocks.CUSTOM_STAINED_GLASS.asItem());
        getOrCreateTagBuilder(ConventionalItemTags.GLASS_BLOCKS_CHEAP)
                .add(UnidyeBlocks.CUSTOM_STAINED_GLASS.asItem());
        getOrCreateTagBuilder(ConventionalItemTags.GLASS_PANES)
                .add(UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE.asItem());
        getOrCreateTagBuilder(ConventionalItemTags.SHULKER_BOXES)
                .add(UnidyeBlocks.CUSTOM_SHULKER_BOX.asItem());
        getOrCreateTagBuilder(UnidyeTags.C_CONCRETES)
                .add(UnidyeBlocks.CUSTOM_CONCRETE.asItem())
                .add(Blocks.BLACK_CONCRETE.asItem())
                .add(Blocks.BROWN_CONCRETE.asItem())
                .add(Blocks.RED_CONCRETE.asItem())
                .add(Blocks.BLUE_CONCRETE.asItem())
                .add(Blocks.YELLOW_CONCRETE.asItem())
                .add(Blocks.LIGHT_GRAY_CONCRETE.asItem())
                .add(Blocks.GRAY_CONCRETE.asItem())
                .add(Blocks.PINK_CONCRETE.asItem())
                .add(Blocks.MAGENTA_CONCRETE.asItem())
                .add(Blocks.PURPLE_CONCRETE.asItem())
                .add(Blocks.LIGHT_BLUE_CONCRETE.asItem())
                .add(Blocks.CYAN_CONCRETE.asItem())
                .add(Blocks.ORANGE_CONCRETE.asItem())
                .add(Blocks.GREEN_CONCRETE.asItem())
                .add(Blocks.LIME_CONCRETE.asItem())
                .add(Blocks.WHITE_CONCRETE.asItem());
        getOrCreateTagBuilder(ItemTags.DYEABLE)
                .add(UnidyeBlocks.CUSTOM_SHULKER_BOX.asItem())
                .add(UnidyeBlocks.CUSTOM_CONCRETE_POWDER.asItem())
                .add(UnidyeBlocks.CUSTOM_CONCRETE.asItem())
                .add(UnidyeBlocks.CUSTOM_STAINED_GLASS.asItem())
                .add(UnidyeBlocks.CUSTOM_CANDLE.asItem())
                .add(UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE.asItem())
                .add(UnidyeBlocks.CUSTOM_TERRACOTTA.asItem())
                .add(UnidyeBlocks.CUSTOM_CARPET.asItem())
                .add(UnidyeBlocks.CUSTOM_WOOL.asItem())
                .add(UnidyeBlocks.CUSTOM_BED.asItem())
                .add(UnidyeItems.CUSTOM_BANNER)
                .add(UnidyeItems.CUSTOM_DYE);
    }
}
