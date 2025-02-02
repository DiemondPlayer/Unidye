package net.diemond_player.unidye.recipes;

import net.diemond_player.unidye.component.CustomBannerPatternsComponent;
import net.diemond_player.unidye.component.UnidyeDataComponentTypes;
import net.diemond_player.unidye.item.custom.DyeableBannerItem;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.recipe.input.CraftingRecipeInput;;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class CustomBannerDuplicateRecipe extends SpecialCraftingRecipe {
    public CustomBannerDuplicateRecipe(CraftingRecipeCategory craftingRecipeCategory) {
        super(craftingRecipeCategory);
    }

    @Override
    public boolean matches(CraftingRecipeInput recipeInputInventory, World world) {
        int color = -1;
        ItemStack itemStack = null;
        ItemStack itemStack2 = null;
        for (int i = 0; i < recipeInputInventory.getSize(); ++i) {
            ItemStack itemStack3 = recipeInputInventory.getStackInSlot(i);
            if (itemStack3.isEmpty()) continue;
            Item item = itemStack3.getItem();
            if (!(item instanceof DyeableBannerItem)) {
                return false;
            }
            if (color == -1) {
                color = UnidyeUtils.getColor(itemStack3);
            } else if (color != UnidyeUtils.getColor(itemStack3)) {
                return false;
            }
            int j = itemStack3.getOrDefault(UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS, CustomBannerPatternsComponent.DEFAULT).layers().size();
            if (j > 6) {
                return false;
            }
            if (j > 0) {
                if (itemStack == null) {
                    itemStack = itemStack3;
                    continue;
                }
                return false;
            }
            if (itemStack2 == null) {
                itemStack2 = itemStack3;
                continue;
            }
            return false;
        }
        return itemStack != null && itemStack2 != null;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput inventory, RegistryWrapper.WrapperLookup lookup) {
        for (int i = 0; i < inventory.getSize(); ++i) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                int j = itemStack.getOrDefault(UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS, CustomBannerPatternsComponent.DEFAULT).layers().size();
                if (j > 0 && j <= 6) {
                    return itemStack.copyWithCount(1);
                }
            }
        }

        return ItemStack.EMPTY;
    }


    @Override
    public DefaultedList<ItemStack> getRemainder(CraftingRecipeInput recipeInputInventory) {
        DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(recipeInputInventory.getSize(), ItemStack.EMPTY);

        for (int i = 0; i < defaultedList.size(); i++) {
            ItemStack itemStack = recipeInputInventory.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                if (itemStack.getItem().hasRecipeRemainder()) {
                    defaultedList.set(i, new ItemStack(itemStack.getItem().getRecipeRemainder()));
                } else if (!itemStack.getOrDefault(UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS, CustomBannerPatternsComponent.DEFAULT).layers().isEmpty()) {
                    defaultedList.set(i, itemStack.copyWithCount(1));
                }
            }
        }

        return defaultedList;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return UnidyeSpecialRecipes.CUSTOM_BANNER_DUPLICATE;
    }

    public boolean fits(int width, int height) {
        return width * height >= 2;
    }
}
