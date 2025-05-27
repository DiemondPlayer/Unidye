package net.diemond_player.unidye.mixin.misc;

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
        MutableText mutableText = Text.literal("■ ");
        mutableText.setStyle(mutableText.getStyle().withColor(color));
        if (dyeColor == null) {
            cir.setReturnValue(mutableText.append(Text.literal(String.format("#%06X", (0xFFFFFF & color))).formatted(Formatting.GRAY)));
        } else {
            cir.setReturnValue(mutableText.append(Text.translatable("item.minecraft.firework_star." + dyeColor.getName()).formatted(Formatting.GRAY)));
        }
    }
}
