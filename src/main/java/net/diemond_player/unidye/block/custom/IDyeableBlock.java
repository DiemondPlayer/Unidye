package net.diemond_player.unidye.block.custom;

import net.diemond_player.unidye.block.entity.DyeableBlockEntity;
import net.diemond_player.unidye.block.entity.UnidyeBlockEntities;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public interface IDyeableBlock extends BlockEntityProvider {

    @Override
    default BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DyeableBlockEntity(pos, state);
    }

    default ItemStack pickBlock(BlockView world, BlockPos pos, ItemStack stack) {
        DyeableBlockEntity blockEntity = UnidyeBlockEntities.DYEABLE_BE.get(world, pos);
        int color = DyeableBlockEntity.DEFAULT_COLOR;
        if (blockEntity != null) {
            color = blockEntity.color;
        }
        NbtCompound subNbt = stack.getOrCreateSubNbt("display");
        subNbt.putInt("color", color);
        return stack;
    }

    //TODO wth is up with someone extending this
}
