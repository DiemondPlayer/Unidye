package net.diemond_player.unidye.mixin.misc;

import com.llamalad7.mixinextras.sugar.Local;
import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
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
            ItemStack inputStack = inputSlot.getStack();
            if(itemStack.contains(DataComponentTypes.DYED_COLOR)) itemStack.set(DataComponentTypes.DYED_COLOR, inputStack.get(DataComponentTypes.DYED_COLOR));
            if(itemStack.contains(UnidyeDataComponentTypes.MATERIAL_COLORS)) itemStack.set(UnidyeDataComponentTypes.MATERIAL_COLORS, inputStack.get(UnidyeDataComponentTypes.MATERIAL_COLORS));
            if(itemStack.contains(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES)) itemStack.set(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, inputStack.get(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES));
            if(itemStack.contains(DataComponentTypes.PROFILE)) itemStack.set(DataComponentTypes.PROFILE, inputStack.get(DataComponentTypes.PROFILE));
        }
    }
}