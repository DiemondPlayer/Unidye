package net.diemond_player.unidye.mixin.compat.supplementaries;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.diemond_player.unidye.item.DyeableBannerItem;
import net.diemond_player.unidye.registry.UnidyeItemGroups;
import net.mehvahdjukaar.supplementaries.common.items.crafting.FlagFromBannerRecipe;
import net.mehvahdjukaar.supplementaries.common.items.crafting.SoapClearRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = SoapClearRecipe.class, remap = false)
public abstract class SoapClearRecipeMixin {

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
            if (UnidyeItemGroups.UNIDYE_ITEM_GROUP_ITEMS.stream().map(ItemConvertible::asItem).toList().contains(item)) {
                return false;
            }
        }
        return true;
    }
}
