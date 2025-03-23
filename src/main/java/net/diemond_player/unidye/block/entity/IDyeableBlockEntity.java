package net.diemond_player.unidye.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import static net.diemond_player.unidye.item.custom.CustomDyeItem.DEFAULT_WHITE_COLOR;

public interface IDyeableBlockEntity {
    int getColor();
    void setColor(int color);
    static int getColor(BlockView world, BlockPos pos) {
        if (world == null) {
            return DEFAULT_WHITE_COLOR;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof IDyeableBlockEntity dyeableBlockEntity) {
            return dyeableBlockEntity.getColor();
        } else {
            return DEFAULT_WHITE_COLOR;
        }
    }
}
