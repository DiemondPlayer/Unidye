package net.diemond_player.unidye.block.custom;

import net.diemond_player.unidye.block.entity.IDyeableBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

@SuppressWarnings("unused")
//Isn't used by Unidye, but may be useful for compat
public class DyeableWallBlock extends WallBlock implements IDyeableBlock {

    public DyeableWallBlock(Settings settings) {
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
}