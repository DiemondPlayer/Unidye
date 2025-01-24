package net.diemond_player.unidye.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.fabric.api.item.v1.FabricItemStack;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.DataComponentTypes;
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


@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ComponentHolder, FabricItemStack {

    @ModifyReturnValue(method = "getTooltip", at = @At(value = "TAIL"))
    private List<Text> unidye$getTooltip(List<Text> original) {
        if (this.contains(DataComponentTypes.DYED_COLOR)) {
            if (original.contains(Text.translatable("item.color", String.format(Locale.ROOT, "#%06X", this.get(DataComponentTypes.DYED_COLOR).rgb())).formatted(Formatting.GRAY))) {
                MutableText mutableText = Text.literal("■ ");
                mutableText.setStyle(mutableText.getStyle().withColor(this.get(DataComponentTypes.DYED_COLOR).rgb()));
                original.set(original.indexOf(Text.translatable("item.color", String.format(Locale.ROOT, "#%06X", this.get(DataComponentTypes.DYED_COLOR).rgb()).formatted(Formatting.GRAY))), mutableText.append(Text.translatable("item.color", String.format(Locale.ROOT, "#%06X", this.get(DataComponentTypes.DYED_COLOR).rgb()).formatted(Formatting.GRAY))));
            }
        }
        return original;
    }
}
