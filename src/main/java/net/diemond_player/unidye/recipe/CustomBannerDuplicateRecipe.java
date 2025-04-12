package net.diemond_player.unidye.recipe;

import net.diemond_player.unidye.block.entity.DyeableBannerBlockEntity;
import net.diemond_player.unidye.item.DyeableBannerItem;
import net.diemond_player.unidye.registry.UnidyeSpecialRecipes;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class CustomBannerDuplicateRecipe extends SpecialCraftingRecipe {
    public CustomBannerDuplicateRecipe(Identifier identifier, CraftingRecipeCategory craftingRecipeCategory) {
        super(identifier, craftingRecipeCategory);
    }

    public boolean matches(RecipeInputInventory recipeInputInventory, World world) {
        int color = -1;
        ItemStack itemStack = null;
        ItemStack itemStack2 = null;
        for (int i = 0; i < recipeInputInventory.size(); ++i) {
            ItemStack itemStack3 = recipeInputInventory.getStack(i);
            if (itemStack3.isEmpty()) continue;
            Item item = itemStack3.getItem();
            if (!(item instanceof DyeableBannerItem bannerItem)) {
                return false;
            }
            if (color == -1) {
                color = bannerItem.getColor(itemStack3);
            } else if (color != bannerItem.getColor(itemStack3)) {
                return false;
            }
            int j = DyeableBannerBlockEntity.getPatternCount(itemStack3);
            if (j > DyeableBannerBlockEntity.MAX_PATTERN_COUNT) {
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

    public ItemStack craft(RecipeInputInventory recipeInputInventory, DynamicRegistryManager dynamicRegistryManager) {
        for (int i = 0; i < recipeInputInventory.size(); ++i) {
            ItemStack itemStack = recipeInputInventory.getStack(i);
            if (!itemStack.isEmpty()) {
                int j = DyeableBannerBlockEntity.getPatternCount(itemStack);
                if (j > 0 && j <= DyeableBannerBlockEntity.MAX_PATTERN_COUNT) {
                    return itemStack.copyWithCount(1);
                }
            }
        }

        return ItemStack.EMPTY;
    }

    public DefaultedList<ItemStack> getRemainder(RecipeInputInventory recipeInputInventory) {
        DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(recipeInputInventory.size(), ItemStack.EMPTY);

        for (int i = 0; i < defaultedList.size(); ++i) {
            ItemStack itemStack = recipeInputInventory.getStack(i);
            if (!itemStack.isEmpty()) {
                if (itemStack.getItem().hasRecipeRemainder()) {
                    defaultedList.set(i, new ItemStack(itemStack.getItem().getRecipeRemainder()));
                } else if (itemStack.hasNbt() && DyeableBannerBlockEntity.getPatternCount(itemStack) > 0) {
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
