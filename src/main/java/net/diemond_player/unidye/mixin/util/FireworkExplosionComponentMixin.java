package net.diemond_player.unidye.mixin.util;

import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireworkExplosionComponent.class)
public abstract class FireworkExplosionComponentMixin {
    @Inject(method = "getColorText", at = @At(value = "HEAD"), cancellable = true)
    private static void unidye$getColorText(int color, CallbackInfoReturnable<Text> cir) {
        DyeColor dyeColor = DyeColor.byFireworkColor(color);
        if (dyeColor == null) {
            MutableText mutableText = Text.literal("■ ");
            mutableText.setStyle(mutableText.getStyle().withColor(color));
            cir.setReturnValue(mutableText.append(Text.literal(String.format("#%06X", (0xFFFFFF & color))).formatted(Formatting.GRAY)));
        }
    }
}
