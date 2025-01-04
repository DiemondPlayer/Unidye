package net.diemond_player.unidye.block.custom;

import net.diemond_player.unidye.block.entity.DyeableBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.CarpetBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class DyeableCarpetBlock extends CarpetBlock implements IDyeableBlock {
    public DyeableCarpetBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        if (DyeableBlockEntity.getColor(world, pos) != DyeableBlockEntity.DEFAULT_COLOR) {
            ItemStack stack = super.getPickStack(world, pos, state);
            return pickBlock(world, pos, stack);
        } else {
            return new ItemStack(this);
        }
    }
}
