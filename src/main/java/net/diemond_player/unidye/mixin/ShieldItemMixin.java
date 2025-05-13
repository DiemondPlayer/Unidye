package net.diemond_player.unidye.mixin;

import net.diemond_player.unidye.item.DyeableBannerItem;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(ShieldItem.class)
public abstract class ShieldItemMixin extends Item {

    public ShieldItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "getTranslationKey", at = @At(value = "HEAD"), cancellable = true)
    private void unidye$getTranslationKey(ItemStack stack, CallbackInfoReturnable<String> cir) {
        if(stack.contains(DataComponentTypes.DYED_COLOR)) {
            Optional<DyeColor> dyeColor = UnidyeUtils.findDyeColorByLeatherColor(stack.get(DataComponentTypes.DYED_COLOR).rgb());
            if(dyeColor.isPresent()){
                cir.setReturnValue(this.getTranslationKey() + "." + dyeColor.get().getName());
            }else {
                cir.setReturnValue("item.unidye.shield_custom_color");
            }
        }
    }

    @Redirect(method = "appendTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BannerItem;appendBannerTooltip(Lnet/minecraft/item/ItemStack;Ljava/util/List;)V"))
    private void unidye$appendTooltip(ItemStack stack, List<Text> tooltip) {
        if(stack.contains(DataComponentTypes.DYED_COLOR)) {
            DyeableBannerItem.appendBannerTooltip(stack, tooltip);
        } else {
            BannerItem.appendBannerTooltip(stack, tooltip);
        }
    }
}
