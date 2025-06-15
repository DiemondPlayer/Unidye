package net.diemond_player.unidye.block;

// Mostly taken with permission from Hecco's Bountiful Fares mod back when Unidye was still indev for 1.20.1
// Source: https://github.com/Heccology/Bountiful-Fares/blob/1.20.1/src/main/java/net/hecco/bountifulfares/block/custom/DyeableCeramicBlock.java

import net.diemond_player.unidye.block.entity.IDyeableBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import static net.diemond_player.unidye.component.MaterialColorsComponent.DEFAULT_WHITE_COLOR;


public class DyeableBlock extends Block implements IDyeableBlock {
    public DyeableBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        if (IDyeableBlockEntity.getColor(world, pos) != DEFAULT_WHITE_COLOR) {
            ItemStack stack = super.getPickStack(world, pos, state);
            return pickBlock(world, pos, stack);
        } else {
            return new ItemStack(this);
        }
    }
}
