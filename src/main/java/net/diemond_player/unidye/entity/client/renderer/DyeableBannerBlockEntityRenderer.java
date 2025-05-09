package net.diemond_player.unidye.entity.client.renderer;

import net.diemond_player.unidye.block.DyeableBannerBlock;
import net.diemond_player.unidye.block.DyeableWallBannerBlock;
import net.diemond_player.unidye.block.entity.DyeableBannerBlockEntity;
import net.diemond_player.unidye.component.CustomBannerPatternsComponent;
import net.diemond_player.unidye.registry.UnidyeModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.*;

@Environment(value = EnvType.CLIENT)
public class DyeableBannerBlockEntityRenderer
        implements BlockEntityRenderer<DyeableBannerBlockEntity> {
    public static final String BANNER = "flag";
    private static final String PILLAR = "pole";
    private static final String CROSSBAR = "bar";
    private final ModelPart banner;
    private final ModelPart pillar;
    private final ModelPart crossbar;

    public DyeableBannerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        ModelPart modelPart = ctx.getLayerModelPart(UnidyeModelLayers.CUSTOM_BANNER);
        this.banner = modelPart.getChild(BANNER);
        this.pillar = modelPart.getChild(PILLAR);
        this.crossbar = modelPart.getChild(CROSSBAR);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(BANNER, ModelPartBuilder.create().uv(0, 0).cuboid(-10.0f, 0.0f, -2.0f, 20.0f, 40.0f, 1.0f), ModelTransform.NONE);
        modelPartData.addChild(PILLAR, ModelPartBuilder.create().uv(44, 0).cuboid(-1.0f, -30.0f, -1.0f, 2.0f, 42.0f, 2.0f), ModelTransform.NONE);
        modelPartData.addChild(CROSSBAR, ModelPartBuilder.create().uv(0, 42).cuboid(-10.0f, -32.0f, -1.0f, 20.0f, 2.0f, 2.0f), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void render(DyeableBannerBlockEntity bannerBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        long l;
        boolean bl = bannerBlockEntity.getWorld() == null;
        matrixStack.push();
        if (bl) {
            l = 0L;
            matrixStack.translate(0.5F, 0.5F, 0.5F);
            this.pillar.visible = true;
        } else {
            l = bannerBlockEntity.getWorld().getTime();
            BlockState blockState = bannerBlockEntity.getCachedState();
            if (blockState.getBlock() instanceof DyeableBannerBlock) {
                matrixStack.translate(0.5F, 0.5F, 0.5F);
                float h = -RotationPropertyHelper.toDegrees(blockState.get(DyeableBannerBlock.ROTATION));
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(h));
                this.pillar.visible = true;
            } else {
                matrixStack.translate(0.5F, -0.16666667F, 0.5F);
                float h = -blockState.get(DyeableWallBannerBlock.FACING).asRotation();
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(h));
                matrixStack.translate(0.0F, -0.3125F, -0.4375F);
                this.pillar.visible = false;
            }
        }

        matrixStack.push();
        matrixStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
        VertexConsumer vertexConsumer = ModelLoader.BANNER_BASE.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntitySolid);
        this.pillar.render(matrixStack, vertexConsumer, i, j);
        this.crossbar.render(matrixStack, vertexConsumer, i, j);
        BlockPos blockPos = bannerBlockEntity.getPos();
        float k = ((float)Math.floorMod(blockPos.getX() * 7L + blockPos.getY() * 9L + blockPos.getZ() * 13L + l, 100L) + f) / 100.0F;
        this.banner.pitch = (-0.0125F + 0.01F * MathHelper.cos((float) (Math.PI * 2) * k)) * (float) Math.PI;
        this.banner.pivotY = -32.0F;
        renderCanvas(
                matrixStack, vertexConsumerProvider, i, j, this.banner, ModelLoader.BANNER_BASE, true, bannerBlockEntity.getColor(), bannerBlockEntity.getPatterns()
        );
        matrixStack.pop();
        matrixStack.pop();
    }

    public static void renderCanvas(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            int overlay,
            ModelPart canvas,
            SpriteIdentifier baseSprite,
            boolean isBanner,
            int color,
            CustomBannerPatternsComponent patterns
    ) {
        renderCanvas(matrices, vertexConsumers, light, overlay, canvas, baseSprite, isBanner, color, patterns, false);
    }

    public static void renderCanvas(
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            int overlay,
            ModelPart canvas,
            SpriteIdentifier baseSprite,
            boolean isBanner,
            int color,
            CustomBannerPatternsComponent patterns,
            boolean glint
    ) {
        canvas.render(matrices, baseSprite.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid, glint), light, overlay);
        renderLayer(matrices, vertexConsumers, light, overlay, canvas, isBanner ? TexturedRenderLayers.BANNER_BASE : TexturedRenderLayers.SHIELD_BASE, color);

        for (int i = 0; i < 16 && i < patterns.layers().size(); i++) {
            CustomBannerPatternsComponent.Layer layer = patterns.layers().get(i);
            SpriteIdentifier spriteIdentifier = isBanner
                    ? TexturedRenderLayers.getBannerPatternTextureId(layer.pattern())
                    : TexturedRenderLayers.getShieldPatternTextureId(layer.pattern());
            renderLayer(matrices, vertexConsumers, light, overlay, canvas, spriteIdentifier, layer.color());
        }
    }

    private static void renderLayer(
            MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ModelPart canvas, SpriteIdentifier textureId, int color
    ) {
        canvas.render(matrices, textureId.getVertexConsumer(vertexConsumers, RenderLayer::getEntityNoOutline), light, overlay, ColorHelper.Argb.fullAlpha(color));
    }
}
