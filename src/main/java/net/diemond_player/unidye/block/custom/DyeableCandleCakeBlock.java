package net.diemond_player.unidye.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.CandleCakeBlock;

public class DyeableCandleCakeBlock extends CandleCakeBlock implements IDyeableBlock {
    public DyeableCandleCakeBlock(Block candle, Settings settings) {
        super(candle, settings);
    }

}
