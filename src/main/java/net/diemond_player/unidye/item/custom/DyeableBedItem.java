package net.diemond_player.unidye.item.custom;

import net.diemond_player.unidye.block.entity.DyeableBedBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;

import static net.minecraft.block.HorizontalFacingBlock.FACING;

public class DyeableBedItem extends DyeableBlockItem{
    public DyeableBedItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ActionResult place(ItemPlacementContext context) {
        ActionResult result = super.place(context);
        BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
        if(result.isAccepted()) {
            if (blockEntity instanceof DyeableBedBlockEntity dyeableBedBlockEntity) {
                dyeableBedBlockEntity.setColor(getColor(context.getStack()));
                DyeableBedBlockEntity dyeableBedBlockEntity1 = (DyeableBedBlockEntity) context.getWorld().getBlockEntity(context.getBlockPos().offset(context.getWorld().getBlockState(context.getBlockPos()).get(FACING)));
                if (dyeableBedBlockEntity1 != null) {
                    dyeableBedBlockEntity1.setColor(getColor(context.getStack()));
                }
            }
        }
        return result;
    }
}
