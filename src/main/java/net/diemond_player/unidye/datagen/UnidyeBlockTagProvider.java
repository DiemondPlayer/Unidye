package net.diemond_player.unidye.datagen;

import net.diemond_player.unidye.registry.UnidyeBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class UnidyeBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public UnidyeBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(BlockTags.CANDLES)
                .add(UnidyeBlocks.CUSTOM_CANDLE);
        getOrCreateTagBuilder(BlockTags.TERRACOTTA)
                .add(UnidyeBlocks.CUSTOM_TERRACOTTA);
        getOrCreateTagBuilder(BlockTags.WOOL)
                .add(UnidyeBlocks.CUSTOM_WOOL);
        getOrCreateTagBuilder(BlockTags.WOOL_CARPETS)
                .add(UnidyeBlocks.CUSTOM_CARPET);
        getOrCreateTagBuilder(BlockTags.BEDS)
                .add(UnidyeBlocks.CUSTOM_BED);
        getOrCreateTagBuilder(BlockTags.SHULKER_BOXES)
                .add(UnidyeBlocks.CUSTOM_SHULKER_BOX);
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(UnidyeBlocks.CUSTOM_CONCRETE,
                        UnidyeBlocks.CUSTOM_TERRACOTTA,
                        UnidyeBlocks.CUSTOM_SHULKER_BOX);
        getOrCreateTagBuilder(BlockTags.SHOVEL_MINEABLE)
                .add(UnidyeBlocks.CUSTOM_CONCRETE_POWDER);
        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
                .add(UnidyeBlocks.CUSTOM_WALL_BANNER,
                        UnidyeBlocks.CUSTOM_BANNER);
        getOrCreateTagBuilder(BlockTags.BANNERS)
                .add(UnidyeBlocks.CUSTOM_WALL_BANNER,
                        UnidyeBlocks.CUSTOM_BANNER);
        getOrCreateTagBuilder(BlockTags.CANDLE_CAKES)
                .add(UnidyeBlocks.CUSTOM_CANDLE_CAKE);
        getOrCreateTagBuilder(net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.GLASS_BLOCKS)
                .add(UnidyeBlocks.CUSTOM_STAINED_GLASS);
        getOrCreateTagBuilder(net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags.GLASS_PANES)
                .add(UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE);
    }
}
