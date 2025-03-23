package net.diemond_player.unidye.item.custom;

import net.diemond_player.unidye.block.entity.IDyeableBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.ActionResult;

public class DyeableBlockItem extends BlockItem implements DyeableItem {
    public DyeableBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ActionResult place(ItemPlacementContext context) {
        ActionResult result = super.place(context);
        BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
        if(result.isAccepted() && blockEntity instanceof IDyeableBlockEntity iDyeableBlockEntity) {
            iDyeableBlockEntity.setColor(getColor(context.getStack()));
        }
        return result;
    }

    @Override
    public int getColor(ItemStack stack) {
        NbtCompound nbtCompound = stack.getSubNbt(DISPLAY_KEY);
        if (nbtCompound != null && nbtCompound.contains(COLOR_KEY, NbtElement.NUMBER_TYPE)) {
            return nbtCompound.getInt(COLOR_KEY);
        }
        return CustomDyeItem.DEFAULT_WHITE_COLOR;
    }
}
