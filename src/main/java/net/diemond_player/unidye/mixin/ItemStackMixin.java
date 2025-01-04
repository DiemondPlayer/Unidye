package net.diemond_player.unidye.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Locale;

import static net.minecraft.item.ItemStack.COLOR_KEY;
import static net.minecraft.item.ItemStack.DISPLAY_KEY;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    @Nullable
    private NbtCompound nbt;

    @ModifyReturnValue(method = "getTooltip", at = @At(value = "TAIL"))
    private List<Text> unidye$getTooltip(List<Text> original) {
        if (this.nbt != null) {
            if (original.contains(Text.translatable("item.color", String.format(Locale.ROOT, "#%06X", this.nbt.getCompound(DISPLAY_KEY).getInt(COLOR_KEY))).formatted(Formatting.GRAY))) {
                MutableText mutableText = Text.literal("■ ");
                mutableText.setStyle(mutableText.getStyle().withColor(this.nbt.getCompound(DISPLAY_KEY).getInt(COLOR_KEY)));
                original.set(original.indexOf(Text.translatable("item.color", String.format(Locale.ROOT, "#%06X", this.nbt.getCompound(DISPLAY_KEY).getInt(COLOR_KEY))).formatted(Formatting.GRAY)), mutableText.append(Text.translatable("item.color", String.format(Locale.ROOT, "#%06X", this.nbt.getCompound(DISPLAY_KEY).getInt(COLOR_KEY))).formatted(Formatting.GRAY)));
            }
        }
        return original;
    }
}
