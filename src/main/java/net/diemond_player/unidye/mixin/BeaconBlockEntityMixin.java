package net.diemond_player.unidye.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.diemond_player.unidye.block.custom.DyeableGlassBlock;
import net.diemond_player.unidye.block.custom.DyeablePaneBlock;
import net.diemond_player.unidye.block.entity.DyeableLeatheryBlockEntity;
import net.diemond_player.unidye.block.entity.UnidyeBlockEntities;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Stainable;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin {
    @Redirect(method = "tick", at = @At(value = "INVOKE", target="Lnet/minecraft/util/DyeColor;getEntityColor()I"))
    private static int unidye$tick(DyeColor instance, @Local(ordinal = 1) BlockPos pos, @Local(argsOnly = true) World world) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        if (block instanceof DyeableGlassBlock || block instanceof DyeablePaneBlock) {
            if (world.getBlockEntity(pos, UnidyeBlockEntities.DYEABLE_LEATHERY_BE).isPresent()) {
                return ColorHelper.Argb.fullAlpha(world.getBlockEntity(pos, UnidyeBlockEntities.DYEABLE_LEATHERY_BE).get().leatherColor);
            }
        }
        return instance.getEntityColor();
    }
}
