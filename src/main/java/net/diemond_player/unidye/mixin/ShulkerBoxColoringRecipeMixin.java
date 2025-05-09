package net.diemond_player.unidye.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.diemond_player.unidye.item.CustomDyeItem;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShulkerBoxColoringRecipe;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShulkerBoxColoringRecipe.class)
public abstract class ShulkerBoxColoringRecipeMixin {
    @ModifyReturnValue(method = "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z", at = @At(value = "RETURN"))
    private boolean unidye$matches(boolean original, @Local(argsOnly = true) CraftingRecipeInput recipeInputInventory, @Local(argsOnly = true) World world) {
        if (original) {
            return checkForUnidyeItems(recipeInputInventory, world);
        }
        return false;
    }

    @Unique
    private boolean checkForUnidyeItems(CraftingRecipeInput recipeInputInventory, World world) {
        for (int i = 0; i < recipeInputInventory.getSize(); ++i) {
            ItemStack itemStack3 = recipeInputInventory.getStackInSlot(i);
            if (itemStack3.isEmpty()) continue;
            Item item = itemStack3.getItem();
            if (item instanceof CustomDyeItem || Block.getBlockFromItem(item) instanceof ShulkerBoxBlock) {
                return false;
            }
        }
        return true;
    }
}
