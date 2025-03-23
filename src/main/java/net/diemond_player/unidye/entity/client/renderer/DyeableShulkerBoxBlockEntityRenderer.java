package net.diemond_player.unidye.entity.client.renderer;

import net.diemond_player.unidye.block.custom.DyeableShulkerBoxBlock;
import net.diemond_player.unidye.block.entity.DyeableShulkerBoxBlockEntity;
import net.diemond_player.unidye.entity.client.model.DyeableShulkerEntityModel;
import net.diemond_player.unidye.entity.layer.UnidyeModelLayers;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

@Environment(value = EnvType.CLIENT)
public class DyeableShulkerBoxBlockEntityRenderer
        implements BlockEntityRenderer<DyeableShulkerBoxBlockEntity> {
    private final DyeableShulkerEntityModel<?> model;

    public DyeableShulkerBoxBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.model = new DyeableShulkerEntityModel<>(ctx.getLayerModelPart(UnidyeModelLayers.CUSTOM_SHULKER));
    }

    @Override
    public void render(DyeableShulkerBoxBlockEntity shulkerBoxBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        BlockState blockState;
        Direction direction = Direction.UP;
        if (shulkerBoxBlockEntity.hasWorld() && (blockState = shulkerBoxBlockEntity.getWorld().getBlockState(shulkerBoxBlockEntity.getPos())).getBlock() instanceof DyeableShulkerBoxBlock) {
            direction = blockState.get(DyeableShulkerBoxBlock.FACING);
        }
        SpriteIdentifier spriteIdentifier = new SpriteIdentifier(TexturedRenderLayers.SHULKER_BOXES_ATLAS_TEXTURE, new Identifier("entity/shulker/shulker_custom"));
        matrixStack.push();
        matrixStack.translate(0.5f, 0.5f, 0.5f);
        float g = 0.9995f;
        matrixStack.scale(g, g, g);
        matrixStack.multiply(direction.getRotationQuaternion());
        matrixStack.scale(1.0f, -1.0f, -1.0f);
        matrixStack.translate(0.0f, -1.0f, 0.0f);
        ModelPart modelPart = this.model.getLid();
        modelPart.setPivot(0.0f, 24.0f - shulkerBoxBlockEntity.getAnimationProgress(f) * 0.5f * 16.0f, 0.0f);
        modelPart.yaw = 270.0f * shulkerBoxBlockEntity.getAnimationProgress(f) * ((float) Math.PI / 180);
        VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntityCutoutNoCull);
        float[] fs = UnidyeUtils.getColorArray(shulkerBoxBlockEntity.getColor());
        this.model.render(matrixStack, vertexConsumer, i, j, fs[0], fs[1], fs[2], 1.0f);
        matrixStack.pop();
    }
}
