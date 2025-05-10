package net.diemond_player.unidye.recipe;

import net.diemond_player.unidye.component.CustomBannerPatternsComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.item.DyeableBannerItem;
import net.diemond_player.unidye.registry.UnidyeSpecialRecipes;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.World;

public class MixedBannerDuplicateRecipe extends SpecialCraftingRecipe {
    public MixedBannerDuplicateRecipe(CraftingRecipeCategory craftingRecipeCategory) {
        super(craftingRecipeCategory);
    }

    @Override
    public boolean matches(CraftingRecipeInput recipeInputInventory, World world) {
        boolean bl = false;
        ItemStack itemStack = null;
        ItemStack itemStack2 = null;
        for (int i = 0; i < recipeInputInventory.getSize(); i++) {
            ItemStack itemStack3 = recipeInputInventory.getStackInSlot(i);
            if (itemStack3.isEmpty()) continue;
            Item item = itemStack3.getItem();
            if (item instanceof DyeableBannerItem && itemStack == null) {
                itemStack = itemStack3;
                int j = itemStack3.getOrDefault(UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS, CustomBannerPatternsComponent.DEFAULT).layers().size();
                if (j > 6 || j == 0) {
                    return false;
                }
                continue;
            } else if (item instanceof DyeableBannerItem) {
                return false;
            }

            if (item instanceof BannerItem && itemStack2 == null) {
                int j = itemStack3.getOrDefault(DataComponentTypes.BANNER_PATTERNS, BannerPatternsComponent.DEFAULT).layers().size();
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
            int n = ColorHelper.Argb.withAlpha(0, ((BannerItem) itemStack2.getItem()).getColor().getEntityColor());
            bl = (n == color);
        }
        return bl;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput recipeInputInventory, RegistryWrapper.WrapperLookup lookup) {
        for (int i = 0; i < recipeInputInventory.getSize(); i++) {
            ItemStack itemStack = recipeInputInventory.getStackInSlot(i);
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
        return UnidyeSpecialRecipes.MIXED_BANNER_DUPLICATE;
    }

    public boolean fits(int width, int height) {
        return width * height >= 2;
    }
}
