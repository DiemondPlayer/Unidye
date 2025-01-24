package net.diemond_player.unidye;

import net.diemond_player.unidye.block.UnidyeBlocks;
import net.diemond_player.unidye.block.entity.DyeableBlockEntity;
import net.diemond_player.unidye.block.entity.DyeableLeatheryBlockEntity;
import net.diemond_player.unidye.block.entity.DyeableShulkerBoxBlockEntity;
import net.diemond_player.unidye.block.entity.UnidyeBlockEntities;
import net.diemond_player.unidye.entity.UnidyeEntities;
import net.diemond_player.unidye.entity.client.model.DyeableShulkerEntityModel;
import net.diemond_player.unidye.entity.client.renderer.DyeableBannerBlockEntityRenderer;
import net.diemond_player.unidye.entity.client.renderer.DyeableBedBlockEntityRenderer;
import net.diemond_player.unidye.entity.client.renderer.DyeableFallingBlockEntityRenderer;
import net.diemond_player.unidye.entity.client.renderer.DyeableShulkerBoxBlockEntityRenderer;
import net.diemond_player.unidye.entity.layer.UnidyeModelLayers;
import net.diemond_player.unidye.item.UnidyeItems;
import net.diemond_player.unidye.util.UnidyeModelPredicateProvider;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.item.Item;

public class UnidyeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(UnidyeEntities.DYEABLE_FALLING_BLOCK_ENTITY, DyeableFallingBlockEntityRenderer::new);

        BlockEntityRendererFactories.register(UnidyeBlockEntities.DYEABLE_SHULKER_BOX_BE, DyeableShulkerBoxBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(UnidyeBlockEntities.DYEABLE_BED_BE, DyeableBedBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(UnidyeBlockEntities.DYEABLE_BANNER_BE, DyeableBannerBlockEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(UnidyeModelLayers.CUSTOM_SHULKER, DyeableShulkerEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(UnidyeModelLayers.CUSTOM_BED_HEAD, DyeableBedBlockEntityRenderer::getHeadTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(UnidyeModelLayers.CUSTOM_BED_FOOT, DyeableBedBlockEntityRenderer::getFootTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(UnidyeModelLayers.CUSTOM_BANNER, DyeableBannerBlockEntityRenderer::getTexturedModelData);

        BlockRenderLayerMap.INSTANCE.putBlock(UnidyeBlocks.CUSTOM_STAINED_GLASS, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE, RenderLayer.getTranslucent());

        registerItemColor(UnidyeItems.CUSTOM_DYE);
        registerItemColor(UnidyeBlocks.CUSTOM_BANNER.asItem());

        registerLeatheryBlockColor(UnidyeBlocks.CUSTOM_WOOL);
        registerBlockColor(UnidyeBlocks.CUSTOM_CONCRETE);
        registerBlockColor(UnidyeBlocks.CUSTOM_TERRACOTTA);
        registerLeatheryBlockColor(UnidyeBlocks.CUSTOM_STAINED_GLASS);
        registerBlockColor(UnidyeBlocks.CUSTOM_CONCRETE_POWDER, 15);
        registerBlockColor(UnidyeBlocks.CUSTOM_CARPET);
        registerLeatheryBlockColor(UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE);
        registerBlockColor(UnidyeBlocks.CUSTOM_CANDLE);
        registerBlockColor(UnidyeBlocks.CUSTOM_CANDLE_CAKE);
        registerCustomShulkerBoxColor(UnidyeBlocks.CUSTOM_SHULKER_BOX);

        UnidyeModelPredicateProvider.registerModModels();

    }

    private void registerItemColor(Item item) {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : UnidyeUtils.getColor(stack), item);
    }

    private void registerItemColor(Item item, int adjust) {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : adjust(UnidyeUtils.getColor(stack), adjust), item);
    }

    private void registerBlockColor(Block block) {
        registerItemColor(block.asItem());
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> DyeableBlockEntity.getColor(world, pos), block);
    }

    private void registerCustomShulkerBoxColor(Block block) {
        registerItemColor(block.asItem());
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> DyeableShulkerBoxBlockEntity.getColor(world, pos), block);
    }

    private void registerLeatheryBlockColor(Block block) {
        registerItemColor(block.asItem());
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> DyeableLeatheryBlockEntity.getColor(world, pos), block);
    }

    private void registerBlockColor(Block block, int adjust) {
        registerItemColor(block.asItem(), adjust);
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> adjust(DyeableBlockEntity.getColor(world, pos), adjust), block);
    }

    public static int adjust(int color, int i) {
        int j = Math.min(((color & 0xFF0000) >> 16) + i, 255);
        int k = Math.min(((color & 0xFF00) >> 8) + i, 255);
        int l = Math.min(((color & 0xFF) >> 0) + i, 255);
        int res = j;
        res = (res << 8) + k;
        res = (res << 8) + l;
        return res;
    }
}
