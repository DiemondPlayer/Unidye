package net.diemond_player.unidye.block.custom;

import net.diemond_player.unidye.block.entity.DyeableBedBlockEntity;
import net.diemond_player.unidye.block.entity.UnidyeBlockEntities;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

public class DyeableBedBlock extends BedBlock
        implements IDyeableBlock {
    public DyeableBedBlock(AbstractBlock.Settings settings) {
        super(DyeColor.CYAN, settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DyeableBedBlockEntity(pos, state);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        if (DyeableBedBlockEntity.getColor(world, pos) != DyeableBedBlockEntity.DEFAULT_COLOR) {
            ItemStack stack = super.getPickStack(world, pos, state);
            return pickBlock(world, pos, stack);
        } else {
            return new ItemStack(this);
        }
    }

    @Override
    public ItemStack pickBlock(BlockView world, BlockPos pos, ItemStack stack) {
        DyeableBedBlockEntity blockEntity = UnidyeBlockEntities.DYEABLE_BED_BE.get(world, pos);
        int color = DyeableBedBlockEntity.DEFAULT_COLOR;
        if (blockEntity != null) {
            color = DyeableBedBlockEntity.getColor(world, pos);
        }
        NbtCompound subNbt = stack.getOrCreateSubNbt("display");
        subNbt.putInt("color", color);
        return stack;
    }
}
