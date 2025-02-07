package net.diemond_player.unidye.item.custom;

import net.diemond_player.unidye.block.UnidyeBlocks;
import net.diemond_player.unidye.block.entity.DyeableLeatheryBlockEntity;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DyeableLeatheryBlockItem extends DyeableBlockItem {
    public DyeableLeatheryBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    public static int getLeatherColor(ItemStack stack) {
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound != null && nbtCompound.contains("leather", NbtElement.NUMBER_TYPE)) {
            return nbtCompound.getInt("leather");
        }
        return DEFAULT_COLOR;
    }

    public String getLeatherHexColor(ItemStack stack) {
        int color = getLeatherColor(stack);
        return String.format("#%06X", (0xFFFFFF & color));
    }

    public static void setLeatherColor(ItemStack itemStack, int n) {
        itemStack.getOrCreateNbt().putInt("leather", n);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        MutableText mutableText = Text.literal("■ ");
        if (stack.isOf(UnidyeBlocks.CUSTOM_WOOL.asItem())) {
            tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(getLeatherColor(stack))).append(Text.translatable("tooltip.unidye.banner_color").append(getLeatherHexColor(stack)).formatted(Formatting.GRAY)));
        } else {
            tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(getLeatherColor(stack))).append(Text.translatable("tooltip.unidye.beacon_color").append(getLeatherHexColor(stack)).formatted(Formatting.GRAY)));
        }
    }

    @Override
    public ActionResult place(ItemPlacementContext context) {
        ActionResult result = super.place(context);
        BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
        if(result.isAccepted()) {
            if (blockEntity instanceof DyeableLeatheryBlockEntity dyeableBlockEntity) {
                dyeableBlockEntity.color = UnidyeUtils.getColor(context.getStack());
                dyeableBlockEntity.leatherColor = getLeatherColor(context.getStack());
            }
        }
        return result;
    }
}
