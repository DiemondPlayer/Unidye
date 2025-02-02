package net.diemond_player.unidye.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.diemond_player.unidye.item.custom.DyeableBannerItem;
import net.minecraft.recipe.input.CraftingRecipeInput;;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.BannerDuplicateRecipe;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BannerDuplicateRecipe.class)
public abstract class BannerDuplicationRecipeMixin {
    @ModifyReturnValue(method = "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z", at = @At(value = "RETURN"))
    private boolean unidye$matches(boolean original, @Local(argsOnly = true) CraftingRecipeInput craftingRecipeInput, @Local(argsOnly = true) World world) {
        if (original) {
            return checkForUnidyeItems(craftingRecipeInput, world);
        }
        return false;
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
