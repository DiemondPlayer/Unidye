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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.ActionResult;

import static net.minecraft.block.HorizontalFacingBlock.FACING;

public class DyeableBlockItem extends BlockItem{
    public DyeableBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    public static final int DEFAULT_COLOR = 16777215;

    @Override
    public ActionResult place(ItemPlacementContext context) {
        BlockState blockstate = context.getWorld().getBlockState(context.getBlockPos());
        ActionResult result = super.place(context);
        BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
        if (blockEntity instanceof DyeableBlockEntity dyeableBlockEntity
                && !blockstate.isOf(UnidyeBlocks.CUSTOM_CARPET)
                && !blockstate.isOf(UnidyeBlocks.CUSTOM_CANDLE)
                && !blockstate.isOf(UnidyeBlocks.CUSTOM_CANDLE_CAKE)) {
            dyeableBlockEntity.color = UnidyeUtils.getColor(context.getStack());
        }
        if (blockEntity instanceof DyeableShulkerBoxBlockEntity dyeableShulkerBoxBlockEntity
                && !blockstate.isOf(UnidyeBlocks.CUSTOM_SHULKER_BOX)) {
            dyeableShulkerBoxBlockEntity.color = UnidyeUtils.getColor(context.getStack());
        }
        if (blockEntity instanceof DyeableBedBlockEntity dyeableBedBlockEntity
                && !blockstate.isOf(UnidyeBlocks.CUSTOM_BED)) {
            dyeableBedBlockEntity.color = UnidyeUtils.getColor(context.getStack());
            DyeableBedBlockEntity dyeableBedBlockEntity1 = (DyeableBedBlockEntity) context.getWorld().getBlockEntity(context.getBlockPos().offset(context.getWorld().getBlockState(context.getBlockPos()).get(FACING)));
            if (dyeableBedBlockEntity1 != null) {
                dyeableBedBlockEntity1.color = UnidyeUtils.getColor(context.getStack());
            }
        }
        return result;
    }
}
