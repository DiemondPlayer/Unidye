package net.diemond_player.unidye.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.diemond_player.unidye.block.DyeableGlassBlock;
import net.diemond_player.unidye.block.DyeablePaneBlock;
import net.diemond_player.unidye.registry.UnidyeBlockEntities;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Stainable;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin {
    @ModifyVariable(method = "tick", at = @At(value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/util/DyeColor;getColorComponents()[F"), ordinal = 0)
    private static float[] unidye$tick(float[] fs, World world, BlockPos BlockPos, BlockState state, BeaconBlockEntity blockEntity, @Local(ordinal = 1) BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        if (block instanceof DyeableGlassBlock || block instanceof DyeablePaneBlock) {
            if (UnidyeBlockEntities.DYEABLE_LEATHERY_BE.get(world, pos) != null) {
                return UnidyeUtils.getColorArray(UnidyeBlockEntities.DYEABLE_LEATHERY_BE.get(world, pos).leatherColor);
            }
        }
        return ((Stainable) ((Object) block)).getColor().getColorComponents();
    }
}
