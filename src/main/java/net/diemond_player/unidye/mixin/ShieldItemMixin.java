package net.diemond_player.unidye.mixin;

import net.diemond_player.unidye.item.DyeableBannerItem;
import net.minecraft.item.BannerItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ShieldItem.class)
public abstract class ShieldItemMixin {
    @Inject(method = "getTranslationKey", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private void unidye$getTranslationKey(ItemStack stack, CallbackInfoReturnable<String> cir) {
        if(BlockItem.getBlockEntityNbt(stack) != null) {
            if (BlockItem.getBlockEntityNbt(stack).contains("CustomColored")) {
                cir.setReturnValue("item.unidye.shield_custom_color");
            }
        }
    }

    @Redirect(method = "appendTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BannerItem;appendBannerTooltip(Lnet/minecraft/item/ItemStack;Ljava/util/List;)V"))
    private void unidye$appendTooltip(ItemStack stack, List<Text> tooltip) {
        if (BlockItem.getBlockEntityNbt(stack) != null && BlockItem.getBlockEntityNbt(stack).contains("CustomColored")) {
            DyeableBannerItem.appendBannerTooltip(stack, tooltip);
        } else {
            BannerItem.appendBannerTooltip(stack, tooltip);
        }
    }
}
