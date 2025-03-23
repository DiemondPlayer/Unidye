package net.diemond_player.unidye.mixin;

import net.diemond_player.unidye.util.UnidyeAccessor;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.BitSet;
import java.util.List;

import static net.minecraft.client.render.WorldRenderer.DIRECTIONS;

@Mixin(BlockModelRenderer.class)
public abstract class BlockModelRendererMixin implements UnidyeAccessor {
    @SuppressWarnings("target")
    @Shadow
    private void getQuadDimensions(BlockRenderView world, BlockState state, BlockPos pos, int[] vertexData, Direction face, float @Nullable [] box, BitSet flags){

    }

    @Override
    @Unique
    public void unidye$render(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, MatrixStack matrices, VertexConsumer vertexConsumer, boolean cull, Random random, long seed, int overlay, int color) {
        boolean bl = MinecraftClient.isAmbientOcclusionEnabled() && state.getLuminance() == 0 && model.useAmbientOcclusion();
        Vec3d vec3d = state.getModelOffset(world, pos);
        matrices.translate(vec3d.x, vec3d.y, vec3d.z);
        try {
            this.unidye$renderFlat(world, model, state, pos, matrices, vertexConsumer, cull, random, seed, overlay, color);
        } catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.create(throwable, "Tesselating block model");
            CrashReportSection crashReportSection = crashReport.addElement("Block model being tesselated");
            CrashReportSection.addBlockInfo(crashReportSection, world, pos, state);
            crashReportSection.add("Using AO", bl);
            throw new CrashException(crashReport);
        }
    }

    @Unique
    public void unidye$renderFlat(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, MatrixStack matrices, VertexConsumer vertexConsumer, boolean cull, Random random, long seed, int overlay, int color) {
        BitSet bitSet = new BitSet(3);
        BlockPos.Mutable mutable = pos.mutableCopy();
        for (Direction direction : DIRECTIONS) {
            random.setSeed(seed);
            List<BakedQuad> list = model.getQuads(state, direction, random);
            if (list.isEmpty()) continue;
            mutable.set(pos, direction);
            if (cull && !Block.shouldDrawSide(state, world, pos, direction, mutable)) continue;
            int i = WorldRenderer.getLightmapCoordinates(world, state, mutable);
            this.unidye$renderQuadsFlat(world, state, pos, i, overlay, false, matrices, vertexConsumer, list, bitSet, color);
        }
        random.setSeed(seed);
        List<BakedQuad> list2 = model.getQuads(state, null, random);
        if (!list2.isEmpty()) {
            this.unidye$renderQuadsFlat(world, state, pos, -1, overlay, true, matrices, vertexConsumer, list2, bitSet, color);
        }
    }

    @Unique
    private void unidye$renderQuadsFlat(BlockRenderView world, BlockState state, BlockPos pos, int light, int overlay, boolean useWorldLight, MatrixStack matrices, VertexConsumer vertexConsumer, List<BakedQuad> quads, BitSet flags, int color) {
        for (BakedQuad bakedQuad : quads) {
            if (useWorldLight) {
                this.getQuadDimensions(world, state, pos, bakedQuad.getVertexData(), bakedQuad.getFace(), null, flags);
                BlockPos blockPos = flags.get(0) ? pos.offset(bakedQuad.getFace()) : pos;
                light = WorldRenderer.getLightmapCoordinates(world, state, blockPos);
            }
            float f = world.getBrightness(bakedQuad.getFace(), bakedQuad.hasShade());
            this.unidye$renderQuad(world, state, pos, vertexConsumer, matrices.peek(), bakedQuad, f, f, f, f, light, light, light, light, overlay, color);
        }
    }

    @SuppressWarnings("unused")
    @Unique
    private void unidye$renderQuad(BlockRenderView world, BlockState state, BlockPos pos, VertexConsumer vertexConsumer, MatrixStack.Entry matrixEntry, BakedQuad quad, float brightness0, float brightness1, float brightness2, float brightness3, int light0, int light1, int light2, int light3, int overlay, int color) {
        float[] fs = UnidyeUtils.getColorArray(color);
        vertexConsumer.quad(matrixEntry, quad, new float[]{brightness0, brightness1, brightness2, brightness3}, fs[0], fs[1], fs[2], new int[]{light0, light1, light2, light3}, overlay, true);
    }
}
