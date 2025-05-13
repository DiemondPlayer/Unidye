package net.diemond_player.unidye.entity.client.renderer;

import net.diemond_player.unidye.block.DyeableBannerBlock;
import net.diemond_player.unidye.block.DyeableBedBlock;
import net.diemond_player.unidye.block.DyeableShulkerBoxBlock;
import net.diemond_player.unidye.block.DyeableWallBannerBlock;
import net.diemond_player.unidye.block.entity.DyeableBannerBlockEntity;
import net.diemond_player.unidye.block.entity.DyeableBedBlockEntity;
import net.diemond_player.unidye.block.entity.DyeableShulkerBoxBlockEntity;
import net.diemond_player.unidye.registry.UnidyeBlocks;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.math.BlockPos;

public class UnidyeBuiltinModelItemRenderer extends BuiltinModelItemRenderer implements SynchronousResourceReloader {
    private final DyeableBedBlockEntity renderDyeableBed;
    private final DyeableShulkerBoxBlockEntity renderDyeableShulkerBox;
    private final DyeableBannerBlockEntity renderDyeableBanner;

    public UnidyeBuiltinModelItemRenderer() {
        super(MinecraftClient.getInstance().getBlockEntityRenderDispatcher(), MinecraftClient.getInstance().getEntityModelLoader());
        renderDyeableBed = new DyeableBedBlockEntity(BlockPos.ORIGIN, UnidyeBlocks.CUSTOM_BED.getDefaultState());
        renderDyeableShulkerBox = new DyeableShulkerBoxBlockEntity(BlockPos.ORIGIN, UnidyeBlocks.CUSTOM_SHULKER_BOX.getDefaultState());
        renderDyeableBanner = new DyeableBannerBlockEntity(BlockPos.ORIGIN, UnidyeBlocks.CUSTOM_BANNER.getDefaultState());
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockEntityRenderDispatcher blockEntityRenderDispatcher = MinecraftClient.getInstance().getBlockEntityRenderDispatcher();
        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            Object blockEntity = null;
            Block block = ((BlockItem) item).getBlock();
            if (block instanceof DyeableBedBlock) {
                this.renderDyeableBed.setColor(UnidyeUtils.getColor(stack));
                blockEntity = this.renderDyeableBed;
            }
            if (block instanceof DyeableShulkerBoxBlock) {
                this.renderDyeableShulkerBox.setColor(UnidyeUtils.getColor(stack));
                blockEntity = this.renderDyeableShulkerBox;
            }
            if (block instanceof DyeableBannerBlock || block instanceof DyeableWallBannerBlock) {
                this.renderDyeableBanner.setColor(UnidyeUtils.getColor(stack));
                this.renderDyeableBanner.readFrom(stack);
                blockEntity = this.renderDyeableBanner;
            }
            blockEntityRenderDispatcher.renderEntity((BlockEntity) blockEntity, matrices, vertexConsumers, light, overlay);
        }
//        else if (item instanceof CustomDyeItem) {
//            RenderLayer renderLayer = RenderLayers.getItemLayer(stack, true);
//            VertexConsumer vertexConsumer;
//            if (usesDynamicDisplay(stack) && stack.hasGlint()) {
//                MatrixStack.Entry entry = matrices.peek().copy();
//                if (mode == ModelTransformationMode.GUI) {
//                    MatrixUtil.scale(entry.getPositionMatrix(), 0.5F);
//                } else if (mode.isFirstPerson()) {
//                    MatrixUtil.scale(entry.getPositionMatrix(), 0.75F);
//                }
//
//                vertexConsumer = getDynamicDisplayGlintConsumer(vertexConsumers, renderLayer, entry);
//            } else {
//                vertexConsumer = getDirectItemGlintConsumer(vertexConsumers, renderLayer, true, stack.hasGlint());
//            }
//
//            ItemRenderer.renderBakedItemModel(model, stack, light, overlay, matrices, vertexConsumer);
//        }
    }
}
