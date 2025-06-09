package net.diemond_player.unidye.util;

import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.block.DyeableBannerBlock;
import net.diemond_player.unidye.block.DyeableBedBlock;
import net.diemond_player.unidye.block.DyeableShulkerBoxBlock;
import net.diemond_player.unidye.block.DyeableWallBannerBlock;
import net.diemond_player.unidye.block.entity.DyeableBannerBlockEntity;
import net.diemond_player.unidye.block.entity.DyeableBedBlockEntity;
import net.diemond_player.unidye.block.entity.DyeableShulkerBoxBlockEntity;
import net.diemond_player.unidye.item.CustomDyeItem;
import net.diemond_player.unidye.registry.UnidyeBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class UnidyeBuiltinModelItemRenderer{

    private final DyeableBedBlockEntity renderDyeableBed = new DyeableBedBlockEntity(BlockPos.ORIGIN, UnidyeBlocks.CUSTOM_BED.getDefaultState());
    private final DyeableShulkerBoxBlockEntity renderDyeableShulkerBox = new DyeableShulkerBoxBlockEntity(BlockPos.ORIGIN, UnidyeBlocks.CUSTOM_SHULKER_BOX.getDefaultState());;
    private final DyeableBannerBlockEntity renderDyeableBanner = new DyeableBannerBlockEntity(BlockPos.ORIGIN, UnidyeBlocks.CUSTOM_BANNER.getDefaultState());;

    public void renderBlockEntities(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
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
    }

    public void renderCustomDye(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Item item = stack.getItem();
        if (item instanceof CustomDyeItem) {
            ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
            boolean leftHanded = mode == ModelTransformationMode.FIRST_PERSON_LEFT_HAND || mode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND;
            String colorHex = String.format("%06X", (0xFFFFFF & UnidyeUtils.getColor(stack))).toLowerCase();
            Identifier baseModel = Identifier.of(Unidye.MOD_ID, "item/custom_" + CustomDyeItem.getDyeShape(stack) + "_dye");
            Identifier patternModel = Identifier.of(Unidye.MOD_ID, "item/custom_dye_special/" + colorHex);
            BakedModel loadedBaseModel = MinecraftClient.getInstance().getBakedModelManager().getModel(baseModel);
            BakedModel model = MinecraftClient.getInstance().getBakedModelManager().getModel(patternModel);

            matrices.push();
            matrices.translate(0.5, 0.5, 0.5);

            if (model == null) {
                itemRenderer.renderItem(stack, mode, leftHanded, matrices, vertexConsumers, light, overlay, loadedBaseModel);
            } else {
//                Unidye.LOGGER.info(String.valueOf(loadedPatternModel.isSideLit()));
                itemRenderer.renderItem(stack, mode, leftHanded, matrices, vertexConsumers, light, overlay, model);
            }
            matrices.pop();
        }
    }
}
