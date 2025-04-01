package net.diemond_player.unidye.block.custom;

import net.diemond_player.unidye.block.entity.IDyeableBlockEntity;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

@SuppressWarnings("unused")
//Isn't used by Unidye, but may be useful for compat
public class DyeableSlabBlock extends SlabBlock implements IDyeableBlock {
    public DyeableSlabBlock(Settings settings) {
        super(settings);
    }

    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        if (IDyeableBlockEntity.getColor(world, pos) != 16777215) {
            ItemStack stack = super.getPickStack(world, pos, state);
            return this.pickBlock(world, pos, stack);
        } else {
            return new ItemStack(this);
        }
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return super.canReplace(state, context) && UnidyeUtils.getColor(context.getStack()) == IDyeableBlockEntity.getColor(context.getWorld(), context.getBlockPos());
    }
}
