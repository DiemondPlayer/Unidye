package net.diemond_player.unidye.item.custom;

import net.diemond_player.unidye.block.UnidyeBlocks;
import net.diemond_player.unidye.block.entity.DyeableLeatheryBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;

import java.util.List;

import static net.diemond_player.unidye.util.UnidyeUtils.getColor;

public class DyeableLeatheryBlockItem extends DyeableBlockItem {
    public DyeableLeatheryBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    public static int getLeatherColor(ItemStack stack) {
        NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(new NbtCompound()));
        if (nbtComponent.contains("leather")) {
            return nbtComponent.copyNbt().getInt("leather");
        }
        return DEFAULT_COLOR;
    }

    public String getLeatherHexColor(ItemStack stack) {
        int color = getLeatherColor(stack);
        return String.format("#%06X", (0xFFFFFF & color));
    }

    public static void setLeatherColor(ItemStack itemStack, int n) {
        NbtComponent nbtComponent = itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(new NbtCompound()));
        NbtCompound nbtCompound = nbtComponent.copyNbt();
        nbtCompound.putInt("leather", n);
        itemStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbtCompound));
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        MutableText mutableText = Text.literal("■ ");
        if (stack.isOf(UnidyeBlocks.CUSTOM_WOOL.asItem())) {
            tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(getLeatherColor(stack))).append(Text.translatable("tooltip.unidye.banner_color").append(getLeatherHexColor(stack)).formatted(Formatting.GRAY)));
        } else {
            tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(getLeatherColor(stack))).append(Text.translatable("tooltip.unidye.beacon_color").append(getLeatherHexColor(stack)).formatted(Formatting.GRAY)));
        }
    }

    @Override
    public ActionResult place(ItemPlacementContext context) {
        int color = getColor(context.getStack());
        int leather_color = getLeatherColor(context.getStack());
        ActionResult result = super.place(context);
        BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
        if(result.isAccepted()) {
            if (blockEntity instanceof DyeableLeatheryBlockEntity dyeableBlockEntity) {
                dyeableBlockEntity.color = color;
                dyeableBlockEntity.leatherColor = leather_color;
            }
        }
        return result;
    }
}
