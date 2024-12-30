package net.diemond_player.unidye.entity.client.renderer;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.diemond_player.unidye.block.custom.DyeableBedBlock;
import net.diemond_player.unidye.block.entity.DyeableBedBlockEntity;
import net.diemond_player.unidye.block.entity.UnidyeBlockEntities;
import net.diemond_player.unidye.entity.layer.UnidyeModelLayers;
import net.diemond_player.unidye.util.UnidyeAccessor;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.DoubleBlockProperties;
import net.minecraft.block.enums.BedPart;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.LightmapCoordinatesRetriever;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

public class DyeableBedBlockEntityRenderer
        implements BlockEntityRenderer<DyeableBedBlockEntity> {
    private final ModelPart bedHead;
    private final ModelPart bedFoot;

    public DyeableBedBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.bedHead = ctx.getLayerModelPart(UnidyeModelLayers.CUSTOM_BED_HEAD);
        this.bedFoot = ctx.getLayerModelPart(UnidyeModelLayers.CUSTOM_BED_FOOT);
    }

    public static TexturedModelData getHeadTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("main", ModelPartBuilder.create().uv(2, 10).cuboid(0.0f, 8.0f, 0.0f, 16.0f, 8.0f, 4.0f), ModelTransform.NONE);
        modelPartData.addChild("main1", ModelPartBuilder.create().uv(0, 0).cuboid(0.0f, 0.0f, 0.0f, 16.0f, 8.0f, 6.0f), ModelTransform.NONE);
        modelPartData.addChild("main2", ModelPartBuilder.create().uv(0, 44).cuboid(0.0f, 8.0f, 4.0f, 16.0f, 8.0f, 2.0f), ModelTransform.NONE);
        modelPartData.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create().uv(50, 6).cuboid(0.0f, 6.0f, 0.0f, 3.0f, 3.0f, 3.0f), ModelTransform.rotation(1.5707964f, 0.0f, 1.5707964f));
        modelPartData.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create().uv(50, 18).cuboid(-16.0f, 6.0f, 0.0f, 3.0f, 3.0f, 3.0f), ModelTransform.rotation(1.5707964f, 0.0f, (float) Math.PI));
        return TexturedModelData.of(modelData, 128, 128);
    }

    public static TexturedModelData getFootTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("main", ModelPartBuilder.create().uv(2, 24).cuboid(0.0f, 0.0f, 0.0f, 16.0f, 16.0f, 4.0f), ModelTransform.NONE);
        modelPartData.addChild("main1", ModelPartBuilder.create().uv(0, 54).cuboid(0.0f, 0.0f, 4.0f, 16.0f, 16.0f, 2.0f), ModelTransform.NONE);
        modelPartData.addChild(EntityModelPartNames.LEFT_LEG, ModelPartBuilder.create().uv(50, 0).cuboid(0.0f, 6.0f, -16.0f, 3.0f, 3.0f, 3.0f), ModelTransform.rotation(1.5707964f, 0.0f, 0.0f));
        modelPartData.addChild(EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create().uv(50, 12).cuboid(-16.0f, 6.0f, -16.0f, 3.0f, 3.0f, 3.0f), ModelTransform.rotation(1.5707964f, 0.0f, 4.712389f));
        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void render(DyeableBedBlockEntity bedBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider
            vertexConsumerProvider, int i, int j) {
        SpriteIdentifier spriteIdentifier = new SpriteIdentifier(TexturedRenderLayers.BEDS_ATLAS_TEXTURE, new Identifier("entity/bed/custom"));
        World world2 = bedBlockEntity.getWorld();
        if (world2 != null) {
            BlockState blockState = bedBlockEntity.getCachedState();
            DoubleBlockProperties.PropertySource<DyeableBedBlockEntity> propertySource = DoubleBlockProperties.toPropertySource(UnidyeBlockEntities.DYEABLE_BED_BE, DyeableBedBlock::getBedPart, DyeableBedBlock::getOppositePartDirection, ChestBlock.FACING, blockState, world2, bedBlockEntity.getPos(), (world, pos) -> false);
            int k = ((Int2IntFunction) propertySource.apply(new LightmapCoordinatesRetriever())).get(i);
            this.renderPart(matrixStack, vertexConsumerProvider, blockState.get(DyeableBedBlock.PART) == BedPart.HEAD ? this.bedHead : this.bedFoot, blockState.get(DyeableBedBlock.FACING), spriteIdentifier, k, j, false, bedBlockEntity);
        } else {
            this.renderPart(matrixStack, vertexConsumerProvider, this.bedHead, Direction.SOUTH, spriteIdentifier, i, j, false, bedBlockEntity);
            this.renderPart(matrixStack, vertexConsumerProvider, this.bedFoot, Direction.SOUTH, spriteIdentifier, i, j, true, bedBlockEntity);
        }
    }

    private void renderPart(net.minecraft.client.util.math.MatrixStack matrices, VertexConsumerProvider vertexConsumers, ModelPart part, Direction direction, SpriteIdentifier sprite, int light, int overlay, boolean isFoot, DyeableBedBlockEntity dyeableBedBlockEntity) {
        matrices.push();
        matrices.translate(0.0f, 0.5625f, isFoot ? -1.0f : 0.0f);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f));
        matrices.translate(0.5f, 0.5f, 0.5f);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0f + direction.asRotation()));
        matrices.translate(-0.5f, -0.5f, -0.5f);
        VertexConsumer vertexConsumer = sprite.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
        float[] fs = UnidyeUtils.getColorArray(dyeableBedBlockEntity.color);
        ((UnidyeAccessor) (Object) part).unidye$renderCustomBed(matrices, vertexConsumer, light, overlay, fs[0], fs[1], fs[2], 1.0f);
        matrices.pop();
    }
}
