package net.diemond_player.unidye.item;

import net.diemond_player.unidye.block.entity.IDyeableBlockEntity;
import net.diemond_player.unidye.component.ItemNameAffixesComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import java.util.Objects;

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
            iDyeableBlockEntity.setItemNameAffixes(itemStack.get(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES));
            iDyeableBlockEntity.setRecipeStacks(itemStack.get(UnidyeDataComponentTypes.RECIPE_STACKS));
            if(itemStack.get(DataComponentTypes.PROFILE) != null) iDyeableBlockEntity.setProfile(itemStack.get(DataComponentTypes.PROFILE));
        }
        return result;
    }

    @Override
    public Text getName(ItemStack stack) {
        Text text = ItemNameAffixesComponent.getName(stack, this.getTranslationKey());
        return !Objects.equals(text, Text.empty()) ? text : super.getName(stack);
    }
}
