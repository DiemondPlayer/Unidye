package net.diemond_player.unidye.mixin;

import net.diemond_player.unidye.util.UnidyeColor;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "appendTooltip", at = @At("HEAD"))
    public void unidye$appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
        if(stack.getItem() instanceof DyeItem) {
            if (UnidyeUtils.DYES.containsKey(stack.getItem())) {
                if (Screen.hasShiftDown()) {
                    UnidyeColor unidyeColor = UnidyeUtils.DYES.get(stack.getItem());
                    MutableText mutableText = Text.literal("■ ");
                    tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(unidyeColor.woolColor)).append(Text.translatable("tooltip.unidye.wool_color").append(String.format("#%06X", (0xFFFFFF & unidyeColor.woolColor))).formatted(Formatting.GRAY)));
                    mutableText = Text.literal("■ ");
                    tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(unidyeColor.signColor)).append(Text.translatable("tooltip.unidye.sign_color").append(String.format("#%06X", (0xFFFFFF & unidyeColor.signColor))).formatted(Formatting.GRAY)));
                    mutableText = Text.literal("■ ");
                    tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(unidyeColor.glassColor)).append(Text.translatable("tooltip.unidye.glass_color").append(String.format("#%06X", (0xFFFFFF & unidyeColor.glassColor))).formatted(Formatting.GRAY)));
                    mutableText = Text.literal("■ ");
                    tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(unidyeColor.candleColor)).append(Text.translatable("tooltip.unidye.candle_color").append(String.format("#%06X", (0xFFFFFF & unidyeColor.candleColor))).formatted(Formatting.GRAY)));
                    mutableText = Text.literal("■ ");
                    tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(unidyeColor.fireworkColor)).append(Text.translatable("tooltip.unidye.firework_color").append(String.format("#%06X", (0xFFFFFF & unidyeColor.fireworkColor))).formatted(Formatting.GRAY)));
                    mutableText = Text.literal("■ ");
                    tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(unidyeColor.concreteColor)).append(Text.translatable("tooltip.unidye.concrete_color").append(String.format("#%06X", (0xFFFFFF & unidyeColor.concreteColor))).formatted(Formatting.GRAY)));
                    mutableText = Text.literal("■ ");
                    tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(unidyeColor.terracottaColor)).append(Text.translatable("tooltip.unidye.terracotta_color").append(String.format("#%06X", (0xFFFFFF & unidyeColor.terracottaColor))).formatted(Formatting.GRAY)));
                    mutableText = Text.literal("■ ");
                    tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(unidyeColor.shulkerBoxColor)).append(Text.translatable("tooltip.unidye.shulker_box_color").append(String.format("#%06X", (0xFFFFFF & unidyeColor.shulkerBoxColor))).formatted(Formatting.GRAY)));
                    mutableText = Text.literal("■ ");
                    tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(unidyeColor.leatherColor)).append(Text.translatable("tooltip.unidye.leather_color").append(String.format("#%06X", (0xFFFFFF & unidyeColor.leatherColor))).formatted(Formatting.GRAY)));
                    mutableText = Text.literal("■ ");
                    tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(unidyeColor.dyeColor)).append(Text.translatable("tooltip.unidye.dye_color").append(String.format("#%06X", (0xFFFFFF & unidyeColor.dyeColor))).formatted(Formatting.GRAY)));
                } else {
                    tooltip.add(Text.translatable("tooltip.unidye.press_shift"));
                }
            }
        }
    }
}