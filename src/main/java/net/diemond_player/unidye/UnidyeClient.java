package net.diemond_player.unidye;

import net.diemond_player.unidye.block.UnidyeBlocks;
import net.diemond_player.unidye.block.entity.IDyeableBlockEntity;
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
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;

import java.util.HashMap;

public class UnidyeClient implements ClientModInitializer {
    public static final HashMap<Block, Integer> DYEABLE_BLOCKS_ADJUST = new HashMap<>() {{
            put(UnidyeBlocks.CUSTOM_CONCRETE_POWDER, 15);
    }};

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

        registerBlockColor(UnidyeBlocks.CUSTOM_WOOL);
        registerBlockColor(UnidyeBlocks.CUSTOM_STAINED_GLASS);
        registerBlockColor(UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE);
        registerBlockColor(UnidyeBlocks.CUSTOM_SHULKER_BOX);

        UnidyeBlockEntities.DYEABLE_BE_BLOCKS.forEach(block -> {
            if (DYEABLE_BLOCKS_ADJUST.containsKey(block)){
                registerBlockColor(block, DYEABLE_BLOCKS_ADJUST.get(block));
            } else {
                registerBlockColor(block);
            }
        });

        UnidyeModelPredicateProvider.registerModModels();
    }

    private static void registerItemColor(Item item) {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : ((DyeableItem) stack.getItem()).getColor(stack), item);
    }

    private static void registerItemColor(Item item, int adjust) {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex > 0 ? -1 : adjust(((DyeableItem) stack.getItem()).getColor(stack), adjust), item);
    }

    public static void registerBlockColor(Block block) {
        registerItemColor(block.asItem());
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> IDyeableBlockEntity.getColor(world, pos), block);
    }

    public static void registerBlockColor(Block block, int adjust) {
        registerItemColor(block.asItem(), adjust);
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> adjust(IDyeableBlockEntity.getColor(world, pos), adjust), block);
    }

    public static int adjust(int color, int i) {
        int j = Math.min(((color & 0xFF0000) >> 16) + i, 255);
        int k = Math.min(((color & 0xFF00) >> 8) + i, 255);
        int l = Math.min(((color & 0xFF)) + i, 255);
        int res = j;
        res = (res << 8) + k;
        res = (res << 8) + l;
        return res;
    }
}
