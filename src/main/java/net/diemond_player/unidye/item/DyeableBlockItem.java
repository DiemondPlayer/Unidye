package net.diemond_player.unidye.item;

import net.diemond_player.unidye.block.entity.IDyeableBlockEntity;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

public class DyeableBlockItem extends BlockItem {
    public DyeableBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ActionResult place(ItemPlacementContext context) {
        ItemStack itemStack = context.getStack().copy();
        ActionResult result = super.place(context);
        BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
        if(result.isAccepted() && blockEntity instanceof IDyeableBlockEntity iDyeableBlockEntity) {
            iDyeableBlockEntity.setColor(UnidyeUtils.getColor(itemStack));
        }
        return result;
    }
}
