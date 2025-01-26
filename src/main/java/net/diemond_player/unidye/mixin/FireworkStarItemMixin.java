package net.diemond_player.unidye.mixin;

import net.minecraft.item.FireworkStarItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FireworkStarItem.class)
public abstract class FireworkStarItemMixin {
    //TODO change that completely
//    @Inject(method = "getColorText", at = @At(value = "HEAD"), cancellable = true)
//    private static void unidye$getColorText(int color, CallbackInfoReturnable<Text> cir) {
//        DyeColor dyeColor = DyeColor.byFireworkColor(color);
//        if (dyeColor == null) {
//            MutableText mutableText = Text.literal("■ ");
//            mutableText.setStyle(mutableText.getStyle().withColor(color));
//            cir.setReturnValue(mutableText.append(Text.literal(String.format("#%06X", (0xFFFFFF & color))).formatted(Formatting.GRAY)));
//        }
//    }
}
