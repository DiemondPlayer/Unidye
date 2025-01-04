package net.diemond_player.unidye.block.custom;

import net.diemond_player.unidye.block.entity.DyeableLeatheryBlockEntity;
import net.diemond_player.unidye.block.entity.UnidyeBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Stainable;
import net.minecraft.block.TransparentBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

import static net.diemond_player.unidye.block.entity.DyeableLeatheryBlockEntity.DEFAULT_COLOR;

public class DyeableGlassBlock extends TransparentBlock implements IDyeableBlock, Stainable {
    public DyeableGlassBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        if (DyeableLeatheryBlockEntity.getColor(world, pos) != DyeableLeatheryBlockEntity.DEFAULT_COLOR) {
            ItemStack stack = super.getPickStack(world, pos, state);
            return pickBlock(world, pos, stack);
        } else {
            return new ItemStack(this);
        }
    }

    @Override
    public ItemStack pickBlock(BlockView world, BlockPos pos, ItemStack stack) {
        DyeableLeatheryBlockEntity blockEntity = UnidyeBlockEntities.DYEABLE_LEATHERY_BE.get(world, pos);
        int color = DEFAULT_COLOR;
        int beaconColor = DEFAULT_COLOR;
        if (blockEntity != null) {
            color = blockEntity.color;
            beaconColor = blockEntity.leatherColor;
        }
        NbtCompound subNbt = stack.getOrCreateSubNbt("display");
        subNbt.putInt("color", color);
        if (stack.getNbt() != null) {
            stack.getNbt().putInt("leather", beaconColor);
        }
        return stack;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DyeableLeatheryBlockEntity(pos, state);
    }

    @Override
    public DyeColor getColor() {
        return DyeColor.CYAN;
    }
}
