package net.diemond_player.unidye.mixin.render;

import net.diemond_player.unidye.block.entity.IDyeableBlockEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {

    @Inject(method = "getMapColor", at = @At(value = "HEAD"), cancellable = true)
    private void unidye$getMapColor(BlockView world, BlockPos pos, CallbackInfoReturnable<MapColor> cir) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof IDyeableBlockEntity) {
            cir.setReturnValue(unidye$findNearestMapColor(Integer.parseInt(String.format("%06X", IDyeableBlockEntity.getColor(world, pos)).substring(0, 6), 16)));
        }
    }

    @Unique
    private static MapColor unidye$findNearestMapColor(int color) {
        int r1 = (color >> 16) & 255;
        int g1 = (color >> 8) & 255;
        int b1 = color & 255;
        MapColor nearest = MapColor.CLEAR;
        double nearestDistance = -1;
        for (int i = 0; i < 64; i++) {
            MapColor mapColor = MapColor.get(i);
            int r2 = (mapColor.color >> 16) & 255;
            int g2 = (mapColor.color >> 8) & 255;
            int b2 = mapColor.color & 255;
            double distance = Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) + Math.pow(b1 - b2, 2);
            if (distance < nearestDistance || nearestDistance == -1) {
                nearest = mapColor;
                nearestDistance = distance;
            }
        }
        return nearest;
    }
}
