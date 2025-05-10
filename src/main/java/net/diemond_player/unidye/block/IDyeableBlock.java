package net.diemond_player.unidye.block;

// Mostly taken with permission from Hecco's Bountiful Fares mod back when Unidye was still indev for 1.20.1
// Source: https://github.com/Heccology/Bountiful-Fares/blob/1.20.1/src/main/java/net/hecco/bountifulfares/block/interfaces/DyeableCeramicBlockInterface.java

import net.diemond_player.unidye.block.entity.DyeableBlockEntity;
import net.diemond_player.unidye.block.entity.IDyeableBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import static net.diemond_player.unidye.item.CustomDyeItem.DEFAULT_WHITE_COLOR;

public interface IDyeableBlock extends BlockEntityProvider {

    @Override
    default BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DyeableBlockEntity(pos, state);
    }

    default ItemStack pickBlock(BlockView world, BlockPos pos, ItemStack stack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        int color = DEFAULT_WHITE_COLOR;
        if (blockEntity instanceof IDyeableBlockEntity iDyeableBlockEntity) {
            color = iDyeableBlockEntity.getColor();
        }
        stack.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(color, true));
        return stack;
    }
}
