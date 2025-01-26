package net.diemond_player.unidye.recipes;

import net.diemond_player.unidye.component.CustomBannerPatternsComponent;
import net.diemond_player.unidye.component.UnidyeDataComponentTypes;
import net.diemond_player.unidye.item.custom.DyeableBannerItem;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class CustomShieldDecorationRecipe extends SpecialCraftingRecipe {
    public CustomShieldDecorationRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(RecipeInputInventory recipeInputInventory, World world) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < recipeInputInventory.size(); i++) {
            ItemStack itemStack3 = recipeInputInventory.getStack(i);
            if (!itemStack3.isEmpty()) {
                if (itemStack3.getItem() instanceof DyeableBannerItem) {
                    if (!itemStack2.isEmpty()) {
                        return false;
                    }

                    itemStack2 = itemStack3;
                } else {
                    if (!itemStack3.isOf(Items.SHIELD)) {
                        return false;
                    }

                    if (!itemStack.isEmpty()) {
                        return false;
                    }

                    CustomBannerPatternsComponent bannerPatternsComponent = itemStack3.getOrDefault(UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS, CustomBannerPatternsComponent.DEFAULT);
                    if (!bannerPatternsComponent.layers().isEmpty()) {
                        return false;
                    }

                    itemStack = itemStack3;
                }
            }
        }

        return !itemStack.isEmpty() && !itemStack2.isEmpty();
    }

    @Override
    public ItemStack craft(RecipeInputInventory recipeInputInventory, RegistryWrapper.WrapperLookup lookup) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < recipeInputInventory.size(); i++) {
            ItemStack itemStack3 = recipeInputInventory.getStack(i);
            if (!itemStack3.isEmpty()) {
                if (itemStack3.getItem() instanceof DyeableBannerItem) {
                    itemStack = itemStack3;
                } else if (itemStack3.isOf(Items.SHIELD)) {
                    itemStack2 = itemStack3.copy();
                }
            }
        }

        if (itemStack2.isEmpty()) {
            return itemStack2;
        } else {
            itemStack2.set(UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS, itemStack.get(UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS));
            itemStack2.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(UnidyeUtils.getColor(itemStack), true));
            itemStack2.remove(DataComponentTypes.BASE_COLOR);
            itemStack2.remove(DataComponentTypes.BANNER_PATTERNS);
            return itemStack2;
        }
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return UnidyeSpecialRecipes.CUSTOM_SHIELD_DECORATION;
    }
}
