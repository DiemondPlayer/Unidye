package net.diemond_player.unidye.entity.client.renderer;

import com.mojang.datafixers.util.Pair;
import net.diemond_player.unidye.block.custom.DyeableBannerBlock;
import net.diemond_player.unidye.block.custom.DyeableWallBannerBlock;
import net.diemond_player.unidye.block.entity.DyeableBannerBlockEntity;
import net.diemond_player.unidye.entity.layer.UnidyeModelLayers;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BannerPattern;
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
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.RotationPropertyHelper;

import java.util.List;

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
        List<Pair<RegistryEntry<BannerPattern>, ?>> list = bannerBlockEntity.getPatterns();
        boolean bl = bannerBlockEntity.getWorld() == null;
        matrixStack.push();
        if (bl) {
            l = 0L;
            matrixStack.translate(0.5f, 0.5f, 0.5f);
            this.pillar.visible = true;
        } else {
            l = bannerBlockEntity.getWorld().getTime();
            BlockState blockState = bannerBlockEntity.getCachedState();
            float h;
            if (blockState.getBlock() instanceof DyeableBannerBlock) {
                matrixStack.translate(0.5f, 0.5f, 0.5f);
                h = -RotationPropertyHelper.toDegrees(blockState.get(DyeableBannerBlock.ROTATION));
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(h));
                this.pillar.visible = true;
            } else {
                matrixStack.translate(0.5f, -0.16666667f, 0.5f);
                h = -blockState.get(DyeableWallBannerBlock.FACING).asRotation();
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(h));
                matrixStack.translate(0.0f, -0.3125f, -0.4375f);
                this.pillar.visible = false;
            }
        }
        matrixStack.push();
        matrixStack.scale(0.6666667f, -0.6666667f, -0.6666667f);
        VertexConsumer vertexConsumer = ModelLoader.BANNER_BASE.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntitySolid);
        this.pillar.render(matrixStack, vertexConsumer, i, j);
        this.crossbar.render(matrixStack, vertexConsumer, i, j);
        BlockPos blockPos = bannerBlockEntity.getPos();
        float k = ((float) Math.floorMod((long) (blockPos.getX() * 7 + blockPos.getY() * 9 + blockPos.getZ() * 13) + l, 100L) + f) / 100.0f;
        this.banner.pitch = (-0.0125f + 0.01f * MathHelper.cos((float) Math.PI * 2 * k)) * (float) Math.PI;
        this.banner.pivotY = -32.0f;
        DyeableBannerBlockEntityRenderer.renderCanvas(matrixStack, vertexConsumerProvider, i, j, this.banner, ModelLoader.BANNER_BASE, true, list);
        matrixStack.pop();
        matrixStack.pop();
    }

    public static void renderCanvas(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ModelPart canvas, SpriteIdentifier baseSprite, boolean isBanner, List<Pair<RegistryEntry<BannerPattern>, ?>> patterns) {
        DyeableBannerBlockEntityRenderer.renderCanvas(matrices, vertexConsumers, light, overlay, canvas, baseSprite, isBanner, patterns, false);
    }

    public static void renderCanvas(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ModelPart canvas, SpriteIdentifier baseSprite, boolean isBanner, List<Pair<RegistryEntry<BannerPattern>, ?>> patterns, boolean glint) {
        canvas.render(matrices, baseSprite.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid, glint), light, overlay);
        for (int i = 0; i < 17 && i < patterns.size(); ++i) {
            Pair<RegistryEntry<BannerPattern>, ?> pair = patterns.get(i);
            if (pair.getSecond() instanceof DyeColor dyeColor) {
                float[] fs = dyeColor.getColorComponents();
                pair.getFirst().getKey().map(key -> isBanner ? TexturedRenderLayers.getBannerPatternTextureId(key) : TexturedRenderLayers.getShieldPatternTextureId(key)).ifPresent(sprite -> canvas.render(matrices, sprite.getVertexConsumer(vertexConsumers, RenderLayer::getEntityNoOutline), light, overlay, fs[0], fs[1], fs[2], 1.0f));
            } else {
                float[] fs = UnidyeUtils.getColorArray((int) pair.getSecond());
                pair.getFirst().getKey().map(key -> isBanner ? TexturedRenderLayers.getBannerPatternTextureId(key) : TexturedRenderLayers.getShieldPatternTextureId(key)).ifPresent(sprite -> canvas.render(matrices, sprite.getVertexConsumer(vertexConsumers, RenderLayer::getEntityNoOutline), light, overlay, fs[0], fs[1], fs[2], 1.0f));
            }
        }
    }
}
