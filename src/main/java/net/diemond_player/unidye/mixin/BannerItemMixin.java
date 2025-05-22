package net.diemond_player.unidye.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BannerItem.class)
public class BannerItemMixin {
    //Note to self: may need a rewrite because you may run into issues with tooltip mods
    @Inject(method = "appendBannerTooltip", at = @At(value = "RETURN"))
    private static void appendBannerTooltip(ItemStack stack, List<Text> tooltip, CallbackInfo ci) {
        BannerPatternsComponent bannerPatternsComponent = stack.get(DataComponentTypes.BANNER_PATTERNS);
        if (bannerPatternsComponent != null) {
            for (int i = 0; i < Math.min(bannerPatternsComponent.layers().size(), 6); i++) {
                BannerPatternsComponent.Layer layer = bannerPatternsComponent.layers().get(i);
                Text originalTooltipText = tooltip.get(i+1);
                MutableText mutableText = Text.literal("■ ");
                mutableText.setStyle(mutableText.getStyle().withColor(ColorHelper.Argb.withAlpha(0, layer.color().getEntityColor())));
                mutableText.append(originalTooltipText);
                tooltip.set(i+1, mutableText);
            }
        }
    }
}
