package net.diemond_player.unidye.mixin.compat.supplementaries;

import com.llamalad7.mixinextras.sugar.Local;
import net.diemond_player.unidye.item.DyeableBannerItem;
import net.mehvahdjukaar.supplementaries.common.items.crafting.FlagFromBannerRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FlagFromBannerRecipe.class, remap = false)
public abstract class FlagFromBannerRecipeMixin {

    @Inject(method = "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z", at = @At(value = "HEAD"), cancellable = true)
    private void unidye$matches(CraftingRecipeInput inv, World world, CallbackInfoReturnable<Boolean> cir) {
        if (!checkForUnidyeItems(inv, world)) {
            cir.setReturnValue(false);
        }
    }

    @Unique
    private boolean checkForUnidyeItems(CraftingRecipeInput craftingRecipeInput, World world) {
        for (int i = 0; i < craftingRecipeInput.getSize(); ++i) {
            ItemStack itemStack3 = craftingRecipeInput.getStackInSlot(i);
            if (itemStack3.isEmpty()) continue;
            Item item = itemStack3.getItem();
            if (item instanceof DyeableBannerItem) {
                return false;
            }
        }
        return true;
    }
}
