package net.diemond_player.unidye.datagen;

import net.diemond_player.unidye.block.UnidyeBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
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
        getOrCreateTagBuilder(net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.GLASS_BLOCKS)
                .add(UnidyeBlocks.CUSTOM_STAINED_GLASS.asItem());
        getOrCreateTagBuilder(net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.GLASS_PANES)
                .add(UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE.asItem());
        getOrCreateTagBuilder(net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags.SHULKER_BOXES)
                .add(UnidyeBlocks.CUSTOM_SHULKER_BOX.asItem());
    }
}
