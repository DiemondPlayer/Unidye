package net.diemond_player.unidye.item.custom;

import net.diemond_player.unidye.block.UnidyeBlocks;
import net.diemond_player.unidye.block.entity.DyeableBannerBlockEntity;
import net.diemond_player.unidye.component.CustomBannerPatternsComponent;
import net.diemond_player.unidye.component.UnidyeDataComponentTypes;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipType;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import java.util.List;

public class DyeableBannerItem extends BannerItem{
    public static final int DEFAULT_COLOR = 16777215;

    public DyeableBannerItem(Block bannerBlock, Block wallBannerBlock, Item.Settings settings) {
        super(bannerBlock, wallBannerBlock, settings);
    }

    @Override
    public ActionResult place(ItemPlacementContext context) {
        BlockState blockstate = context.getWorld().getBlockState(context.getBlockPos());
        ActionResult result = super.place(context);
        BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
        if (blockEntity instanceof DyeableBannerBlockEntity dyeableBannerBlockEntity
                && !blockstate.isOf(UnidyeBlocks.CUSTOM_BANNER)
                && !blockstate.isOf(UnidyeBlocks.CUSTOM_WALL_BANNER)) {
            dyeableBannerBlockEntity.color = UnidyeUtils.getColor(context.getStack());
        }
        return result;
    }

    public static void appendBannerTooltip(ItemStack stack, List<Text> tooltip) {
        CustomBannerPatternsComponent bannerPatternsComponent = stack.get(UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS);
        if (bannerPatternsComponent != null) {
            for (int i = 0; i < Math.min(bannerPatternsComponent.layers().size(), 6); i++) {
                CustomBannerPatternsComponent.Layer layer = (CustomBannerPatternsComponent.Layer)bannerPatternsComponent.layers().get(i);
                tooltip.add(layer.getTooltipText());
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        DyeableBannerItem.appendBannerTooltip(stack, tooltip);
    }
}
