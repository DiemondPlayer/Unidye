package net.diemond_player.unidye.recipe;

import net.diemond_player.unidye.block.entity.DyeableBannerBlockEntity;
import net.diemond_player.unidye.item.DyeableBannerItem;
import net.diemond_player.unidye.registry.UnidyeSpecialRecipes;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class MixedBannerDuplicateRecipe extends SpecialCraftingRecipe {
    public MixedBannerDuplicateRecipe(Identifier identifier, CraftingRecipeCategory craftingRecipeCategory) {
        super(identifier, craftingRecipeCategory);
    }

    public boolean matches(RecipeInputInventory recipeInputInventory, World world) {
        boolean bl = false;
        ItemStack itemStack = null;
        ItemStack itemStack2 = null;
        for (int i = 0; i < recipeInputInventory.size(); ++i) {
            ItemStack itemStack3 = recipeInputInventory.getStack(i);
            if (itemStack3.isEmpty()) continue;
            Item item = itemStack3.getItem();
            if (item instanceof DyeableBannerItem && itemStack == null) {
                itemStack = itemStack3;
                int j = DyeableBannerBlockEntity.getPatternCount(itemStack3);
                if (j > 6 || j == 0) {
                    return false;
                }
                continue;
            } else if (item instanceof DyeableBannerItem) {
                return false;
            }

            if (item instanceof BannerItem && itemStack2 == null) {
                int j = DyeableBannerBlockEntity.getPatternCount(itemStack3);
                if (j > 0) {
                    return false;
                }
                itemStack2 = itemStack3;
                continue;
            } else if (item instanceof BannerItem) {
                return false;
            }

            return false;
        }
        if (itemStack != null && itemStack2 != null) {
            int color = UnidyeUtils.getColor(itemStack);
            float[] a = ((BannerItem) itemStack2.getItem()).getColor().getColorComponents();
            int n = (int) (a[0] * 255);
            n = (n << 8) + (int) (a[1] * 255);
            n = (n << 8) + (int) (a[2] * 255);
            bl = (n == color);
        }
        return bl;
    }

    public ItemStack craft(RecipeInputInventory recipeInputInventory, DynamicRegistryManager dynamicRegistryManager) {
        for (int i = 0; i < recipeInputInventory.size(); ++i) {
            ItemStack itemStack = recipeInputInventory.getStack(i);
            if (!itemStack.isEmpty()) {
                int j = DyeableBannerBlockEntity.getPatternCount(itemStack);
                if (j > 0 && j <= 6) {
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
        return UnidyeSpecialRecipes.MIXED_BANNER_DUPLICATE;
    }

    public boolean fits(int width, int height) {
        return width * height >= 2;
    }
}
