package net.diemond_player.unidye.block.custom;

import net.diemond_player.unidye.block.entity.DyeableLeatheryBlockEntity;
import net.diemond_player.unidye.block.entity.IDyeableBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.Stainable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import static net.diemond_player.unidye.item.custom.CustomDyeItem.DEFAULT_WHITE_COLOR;


public class DyeableGlassBlock extends GlassBlock implements IDyeableBlock, Stainable {
    public DyeableGlassBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        if (IDyeableBlockEntity.getColor(world, pos) != DEFAULT_WHITE_COLOR) {
            ItemStack stack = super.getPickStack(world, pos, state);
            return pickBlock(world, pos, stack);
        } else {
            return new ItemStack(this);
        }
    }

    @Override
    public ItemStack pickBlock(BlockView world, BlockPos pos, ItemStack stack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        int color = DEFAULT_WHITE_COLOR;
        int beaconColor = DEFAULT_WHITE_COLOR;
        if (blockEntity instanceof DyeableLeatheryBlockEntity dyeableLeatheryBlockEntity) {
            color = dyeableLeatheryBlockEntity.getColor();
            beaconColor = dyeableLeatheryBlockEntity.leatherColor;
        }
        NbtCompound subNbt = stack.getOrCreateSubNbt("display");
        subNbt.putInt("color", color);
        if(stack.getNbt()!=null) {
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
