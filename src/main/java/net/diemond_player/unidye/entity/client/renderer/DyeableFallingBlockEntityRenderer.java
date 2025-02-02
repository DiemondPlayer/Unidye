package net.diemond_player.unidye.entity.client.renderer;

import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.UnidyeClient;
import net.diemond_player.unidye.entity.DyeableFallingBlockEntity;
import net.diemond_player.unidye.util.UnidyeAccessor;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class DyeableFallingBlockEntityRenderer extends EntityRenderer<DyeableFallingBlockEntity> {
    private final BlockRenderManager blockRenderManager;

    public DyeableFallingBlockEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0.5f;
        this.blockRenderManager = context.getBlockRenderManager();
    }

    @Override
    public void render(DyeableFallingBlockEntity fallingBlockEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        BlockState blockState = fallingBlockEntity.getBlockState();
        if (blockState.getRenderType() == BlockRenderType.MODEL) {
            World world = fallingBlockEntity.getWorld();
            if (blockState != world.getBlockState(fallingBlockEntity.getBlockPos()) && blockState.getRenderType() != BlockRenderType.INVISIBLE) {
                matrixStack.push();
                BlockPos blockPos = BlockPos.ofFloored(fallingBlockEntity.getX(), fallingBlockEntity.getBoundingBox().maxY, fallingBlockEntity.getZ());
                matrixStack.translate(-0.5, 0.0, -0.5);
                ((UnidyeAccessor) this.blockRenderManager.getModelRenderer()).unidye$render(world, this.blockRenderManager.getModel(blockState), blockState, blockPos, matrixStack, vertexConsumerProvider.getBuffer(RenderLayers.getMovingBlockLayer(blockState)), false, Random.create(), blockState.getRenderingSeed(fallingBlockEntity.getFallingBlockPos()), OverlayTexture.DEFAULT_UV, UnidyeClient.adjust(fallingBlockEntity.getCustomColor(), 15));
                matrixStack.pop();
                super.render(fallingBlockEntity, f, g, matrixStack, vertexConsumerProvider, i);
            }
        }
    }

    @Override
    public Identifier getTexture(DyeableFallingBlockEntity fallingBlockEntity) {
        return Identifier.of(Unidye.MOD_ID, "custom_concrete_powder");
    }
}
