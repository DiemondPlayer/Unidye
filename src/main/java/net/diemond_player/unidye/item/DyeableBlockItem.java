package net.diemond_player.unidye.item;

import net.diemond_player.unidye.block.entity.IDyeableBlockEntity;
import net.diemond_player.unidye.component.ItemNamePrefixComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.registry.UnidyeMaterialTypes;
import net.diemond_player.unidye.util.UnidyeMaterialType;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

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

    @Override
    public Text getName(ItemStack stack) {
        Text text = ItemNamePrefixComponent.getName(stack, this.getTranslationKey());
        return text != null ? text : super.getName(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(stack.getOrDefault(UnidyeDataComponentTypes.ITEM_NAME_PREFIX, ItemNamePrefixComponent.DEFAULT).toText());
    }
}
