package net.diemond_player.unidye.mixin.util;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ShulkerBoxBlock.class)
public interface ShulkerBoxBlockAccessor {
    @Accessor("SIDES_SHAPES")
    Map<Direction, VoxelShape> getSidesShapes();
}
