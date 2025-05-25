package net.diemond_player.unidye.mixin.gameplay;

import com.llamalad7.mixinextras.sugar.Local;
import net.diemond_player.unidye.Unidye;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.StonecutterScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StonecutterScreenHandler.class)
public abstract class StonecutterScreenHandlerMixin {

    @Shadow
    @Final
    Slot inputSlot;

    @Inject(method = "populateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isItemEnabled(Lnet/minecraft/resource/featuretoggle/FeatureSet;)Z"))
    public void unidye$populateResult(CallbackInfo ci, @Local ItemStack itemStack) {
        if(Unidye.STONECUTTER_PRESERVE_COLOR.contains(itemStack.getItem())){
            itemStack.set(DataComponentTypes.DYED_COLOR, inputSlot.getStack().get(DataComponentTypes.DYED_COLOR));
        }
    }
}