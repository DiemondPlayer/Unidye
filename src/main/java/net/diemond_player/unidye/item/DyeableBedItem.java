package net.diemond_player.unidye.item;

import net.diemond_player.unidye.block.entity.DyeableBedBlockEntity;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

import static net.minecraft.block.HorizontalFacingBlock.FACING;

public class DyeableBedItem extends DyeableBlockItem{
    public DyeableBedItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ActionResult place(ItemPlacementContext context) {
        ItemStack itemStack = context.getStack().copy();
        ActionResult result = super.place(context);
        BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
        if(result.isAccepted()) {
            if (blockEntity instanceof DyeableBedBlockEntity) {
                DyeableBedBlockEntity dyeableBedBlockEntity1 = (DyeableBedBlockEntity) context.getWorld().getBlockEntity(context.getBlockPos().offset(context.getWorld().getBlockState(context.getBlockPos()).get(FACING)));
                if (dyeableBedBlockEntity1 != null) {
                    dyeableBedBlockEntity1.setColor(UnidyeUtils.getColor(itemStack));
                    if(itemStack.contains(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES)) dyeableBedBlockEntity1.setItemNameAffixes(itemStack.get(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES));
                    if(itemStack.contains(UnidyeDataComponentTypes.RECIPE_STACKS)) dyeableBedBlockEntity1.setRecipeStacks(itemStack.get(UnidyeDataComponentTypes.RECIPE_STACKS));
                    if(itemStack.contains(UnidyeDataComponentTypes.MATERIAL_COLORS)) dyeableBedBlockEntity1.setMaterialColors(itemStack.get(UnidyeDataComponentTypes.MATERIAL_COLORS));
                    if(itemStack.get(DataComponentTypes.PROFILE) != null) dyeableBedBlockEntity1.setProfile(itemStack.get(DataComponentTypes.PROFILE));
                }
            }
        }
        return result;
    }
}
