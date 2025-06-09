package net.diemond_player.unidye.mixin.compat.emi;

import com.google.common.collect.Lists;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.recipe.special.EmiArmorDyeRecipe;
import net.diemond_player.unidye.registry.UnidyeItems;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(value = EmiArmorDyeRecipe.class, remap = false)
public abstract class EmiArmorDyeRecipeMixin {

    @Mutable
    @Final
    @Shadow
    private final Item armor;

    @Shadow
    private List<DyeItem> getDyes(Random random) {
        return null;
    }

    protected EmiArmorDyeRecipeMixin(Item armor) {
        this.armor = armor;
    }

    @Inject(method = "lambda$getOutputWidget$3", at = @At("HEAD"), cancellable = true)
    public void unidye$lambda$getOutputWidget$3(Random r, CallbackInfoReturnable<EmiIngredient> cir) {
        if(armor == UnidyeItems.CUSTOM_DYE){
            List<DyeItem> dyes = getDyes(r);
            cir.setReturnValue(EmiStack.of(DyedColorComponent.setColor(new ItemStack(armor), dyes)).setAmount(dyes.size()+1));
        }
    }
}
