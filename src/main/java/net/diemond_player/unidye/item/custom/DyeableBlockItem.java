package net.diemond_player.unidye.item.custom;

import net.diemond_player.unidye.block.UnidyeBlocks;
import net.diemond_player.unidye.block.entity.DyeableBedBlockEntity;
import net.diemond_player.unidye.block.entity.DyeableBlockEntity;
import net.diemond_player.unidye.block.entity.DyeableShulkerBoxBlockEntity;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;

import static net.diemond_player.unidye.util.UnidyeUtils.getColor;
import static net.minecraft.block.HorizontalFacingBlock.FACING;

public class DyeableBlockItem extends BlockItem{
    public DyeableBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    public static final int DEFAULT_COLOR = 16777215;

    @Override
    public ActionResult place(ItemPlacementContext context) {
        int color = getColor(context.getStack());
        ActionResult result = super.place(context);
        BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
        if(result.isAccepted()) {
            if (blockEntity instanceof DyeableBlockEntity dyeableBlockEntity) {
                dyeableBlockEntity.color = color;
            }
            if (blockEntity instanceof DyeableShulkerBoxBlockEntity dyeableShulkerBoxBlockEntity) {
                dyeableShulkerBoxBlockEntity.color = color;
            }
            if (blockEntity instanceof DyeableBedBlockEntity dyeableBedBlockEntity) {
                dyeableBedBlockEntity.color = color;
                DyeableBedBlockEntity dyeableBedBlockEntity1 = (DyeableBedBlockEntity) context.getWorld().getBlockEntity(context.getBlockPos().offset(context.getWorld().getBlockState(context.getBlockPos()).get(FACING)));
                if (dyeableBedBlockEntity1 != null) {
                    dyeableBedBlockEntity1.color = color;
                }
            }
        }
        return result;
    }
}
