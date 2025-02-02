package net.diemond_player.unidye.mixin;

import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

@Mixin(DyedColorComponent.class)
public abstract class DyedColorComponentMixin implements TooltipAppender {
    /**
     * @author Diemond_Player
     * @reason redirects to Unidye algorithm
     */
    @Overwrite
    public static ItemStack setColor(ItemStack stack, List<DyeItem> dyes) {
        return UnidyeUtils.blendAndSetColor(stack, dyes, new ArrayList<>());
    }

    @Final
    @Shadow
    private int rgb;
    @Inject(method = "appendTooltip", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"), cancellable = true)
    private void unidye$appendTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type, CallbackInfo ci) {
        if (type.isAdvanced()) {
            MutableText mutableText = Text.literal("■ ");
            mutableText.setStyle(mutableText.getStyle().withColor(this.rgb));
            tooltip.accept(mutableText.append(Text.translatable("item.color", String.format(Locale.ROOT, "#%06X", this.rgb)).formatted(Formatting.GRAY)));
            ci.cancel();
        }
    }
}
