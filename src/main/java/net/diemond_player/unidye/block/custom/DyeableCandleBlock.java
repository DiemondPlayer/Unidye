package net.diemond_player.unidye.block.custom;

import net.diemond_player.unidye.block.entity.DyeableBlockEntity;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.CandleBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

public class DyeableCandleBlock extends CandleBlock implements IDyeableBlock {

    public DyeableCandleBlock(Settings settings) {
        super(settings);
    }


    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        if (UnidyeUtils.getColor(context.getStack())
                != UnidyeUtils.getColor(getPickStack(context.getWorld(), context.getBlockPos(), state))) {
            return false;
        }
        return super.canReplace(state, context);
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
