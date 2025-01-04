package net.diemond_player.unidye.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CandleCakeBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class DyeableCandleCakeBlock extends CandleCakeBlock implements IDyeableBlock {
    public DyeableCandleCakeBlock(Block candle, Settings settings) {
        super(candle, settings);
    }

}
