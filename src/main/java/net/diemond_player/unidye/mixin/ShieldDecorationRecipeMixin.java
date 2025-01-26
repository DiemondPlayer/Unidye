package net.diemond_player.unidye.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.diemond_player.unidye.component.UnidyeDataComponentTypes;
import net.diemond_player.unidye.item.custom.DyeableBannerItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShieldDecorationRecipe;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShieldDecorationRecipe.class)
public abstract class ShieldDecorationRecipeMixin {
    @ModifyReturnValue(method = "matches(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/world/World;)Z", at = @At(value = "RETURN"))
    private boolean unidye$matches(boolean original, @Local(argsOnly = true) RecipeInputInventory recipeInputInventory, @Local(argsOnly = true) World world) {
        if (original) {
            return checkForUnidyeItems(recipeInputInventory, world);
        }
        return false;
    }

    @ModifyReturnValue(method = "craft(Lnet/minecraft/inventory/RecipeInputInventory;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;", at = @At("TAIL"))
    public ItemStack unidye$craft(ItemStack original) {
        if(!original.isEmpty()) {
            original.remove(UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS);
            original.remove(DataComponentTypes.DYED_COLOR);
        }
        return original;
    }

        @Unique
    private boolean checkForUnidyeItems(RecipeInputInventory recipeInputInventory, World world) {
        for (int i = 0; i < recipeInputInventory.size(); ++i) {
            ItemStack itemStack3 = recipeInputInventory.getStack(i);
            if (itemStack3.isEmpty()) continue;
            Item item = itemStack3.getItem();
            if (item instanceof DyeableBannerItem) {
                return false;
            }
        }
        return true;
    }
}
