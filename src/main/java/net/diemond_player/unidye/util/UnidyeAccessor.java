package net.diemond_player.unidye.util;


import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

public interface UnidyeAccessor {

    int unidye$getCustomColor();

    void unidye$setCustomColor(int customColor);

    int unidye$getSecondaryCustomColor();

    void unidye$setSecondaryCustomColor(int customColor);

    void unidye$render(BlockRenderView world, BakedModel model, BlockState state, BlockPos pos, MatrixStack matrices, VertexConsumer vertexConsumer, boolean cull, Random random, long seed, int overlay, int color);

    void unidye$renderCustomBed(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color);

    ItemStack unidye$getCustomDyeItemStack();

    void unidye$setCustomDyeItemStack(ItemStack itemStack);

}