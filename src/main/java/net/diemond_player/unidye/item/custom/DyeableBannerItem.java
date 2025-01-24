package net.diemond_player.unidye.item.custom;

import net.diemond_player.unidye.block.UnidyeBlocks;
import net.diemond_player.unidye.block.entity.DyeableBannerBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DyeableBannerItem extends BannerItem implements DyeableItem {
    public static final int DEFAULT_COLOR = 16777215;
    private static final String TRANSLATION_KEY_PREFIX = "block.minecraft.banner.";

    public DyeableBannerItem(Block bannerBlock, Block wallBannerBlock, Item.Settings settings) {
        super(bannerBlock, wallBannerBlock, settings);
    }

    @Override
    public ActionResult place(ItemPlacementContext context) {
        BlockState blockstate = context.getWorld().getBlockState(context.getBlockPos());
        ActionResult result = super.place(context);
        BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
        if(result.isAccepted()) {
            if (blockEntity instanceof DyeableBannerBlockEntity dyeableBannerBlockEntity) {
                dyeableBannerBlockEntity.color = getColor(context.getStack());
            }
        }
        return result;
    }

    public static void appendBannerTooltip(ItemStack stack, List<Text> tooltip) {
        NbtCompound nbtCompound = BlockItem.getBlockEntityNbt(stack);
        if (nbtCompound == null || !nbtCompound.contains("Patterns")) {
            return;
        }
        NbtList nbtList = nbtCompound.getList("Patterns", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < nbtList.size() && i < 6; ++i) {
            NbtCompound nbtCompound2 = nbtList.getCompound(i);
            int n = nbtCompound2.getInt("Color");
            DyeColor dyeColor = DyeColor.byId(n);
            RegistryEntry<BannerPattern> registryEntry = BannerPattern.byId(nbtCompound2.getString("Pattern"));
            if (registryEntry == null) continue;
            if (DyeColor.WHITE == dyeColor && n != 0) {
                MutableText mutableText = Text.literal("■ ");
                mutableText.setStyle(mutableText.getStyle().withColor(n));
                registryEntry.getKey().map(key -> key.getValue().toShortTranslationKey()).ifPresent(translationKey -> tooltip.add(mutableText.append(Text.literal("§7#" + Integer.toString(n, 16).toUpperCase() + " ").append(Text.translatable(TRANSLATION_KEY_PREFIX + translationKey).formatted(Formatting.GRAY)))));
            } else {
                registryEntry.getKey().map(key -> key.getValue().toShortTranslationKey()).ifPresent(translationKey -> tooltip.add(Text.translatable(TRANSLATION_KEY_PREFIX + translationKey + "." + dyeColor.getName()).formatted(Formatting.GRAY)));
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        DyeableBannerItem.appendBannerTooltip(stack, tooltip);
    }

    @Override
    public int getColor(ItemStack stack) {
        NbtCompound nbtCompound = stack.getSubNbt(DISPLAY_KEY);
        if (nbtCompound != null && nbtCompound.contains(COLOR_KEY, NbtElement.NUMBER_TYPE)) {
            return nbtCompound.getInt(COLOR_KEY);
        }
        return DEFAULT_COLOR;
    }
}
