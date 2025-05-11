package net.diemond_player.unidye.item;

import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.block.entity.DyeableBannerBlockEntity;
import net.diemond_player.unidye.component.CustomBannerPatternsComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;

import java.util.List;

public class DyeableBannerItem extends BannerItem {
    private static final String TRANSLATION_KEY_PREFIX = "block.minecraft.banner.";

    public DyeableBannerItem(Block bannerBlock, Block wallBannerBlock, Item.Settings settings) {
        super(bannerBlock, wallBannerBlock, settings);
    }

    @Override
    public ActionResult place(ItemPlacementContext context) {
        int color = UnidyeUtils.getColor(context.getStack());
        ActionResult result = super.place(context);
        BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
        if(result.isAccepted()) {
            if (blockEntity instanceof DyeableBannerBlockEntity dyeableBannerBlockEntity) {
                dyeableBannerBlockEntity.setColor(color);
            }
        }
        return result;
    }

    public static void appendBannerTooltip(ItemStack stack, List<Text> tooltip) {
        CustomBannerPatternsComponent bannerPatternsComponent = stack.get(UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS);
        if (bannerPatternsComponent != null) {
            for (int i = 0; i < Math.min(bannerPatternsComponent.layers().size(), 6); i++) {
                CustomBannerPatternsComponent.Layer layer = bannerPatternsComponent.layers().get(i);
                tooltip.add(layer.getTooltipText());
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        DyeableBannerItem.appendBannerTooltip(stack, tooltip);
    }

    @Override
    public DyeColor getColor() {
        //try {
//            Unidye.LOGGER.warn("{} returns DyeColor.WHITE as a requirement rather than an actual color, calling the {} method is not recommended.", this.getClass().getName(), this.getClass().getMethod("getColor").getName());
//       } catch (NoSuchMethodException ignored) {
//     }
        return super.getColor();
    }
}
