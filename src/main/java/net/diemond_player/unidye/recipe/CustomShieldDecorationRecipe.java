package net.diemond_player.unidye.recipe;

import net.diemond_player.unidye.component.CustomBannerPatternsComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.item.DyeableBannerItem;
import net.diemond_player.unidye.registry.UnidyeSpecialRecipes;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;

import java.util.Optional;

public class CustomShieldDecorationRecipe extends SpecialCraftingRecipe {
    public CustomShieldDecorationRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput recipeInputInventory, World world) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < recipeInputInventory.getSize(); i++) {
            ItemStack itemStack3 = recipeInputInventory.getStackInSlot(i);
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
    public ItemStack craft(CraftingRecipeInput recipeInputInventory, RegistryWrapper.WrapperLookup lookup) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < recipeInputInventory.getSize(); i++) {
            ItemStack itemStack3 = recipeInputInventory.getStackInSlot(i);
            if (!itemStack3.isEmpty()) {
                if (itemStack3.getItem() instanceof DyeableBannerItem) {
                    itemStack = itemStack3;
                } else if (itemStack3.isOf(Items.SHIELD)) {
                    itemStack2 = itemStack3.copy();
                }
            }
        }

        if (!itemStack2.isEmpty()) {
            itemStack2.set(UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS, new CustomBannerPatternsComponent(itemStack.getOrDefault(UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS, CustomBannerPatternsComponent.DEFAULT).layers()));
            Optional<DyeColor> dyeColor = UnidyeUtils.findDyeColorByLeatherColor(UnidyeUtils.getColor(itemStack));
            itemStack2.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(UnidyeUtils.getColor(itemStack), dyeColor.isEmpty()));
            if(itemStack2.contains(DataComponentTypes.BASE_COLOR)) {
                itemStack2.remove(DataComponentTypes.BASE_COLOR);
            }
            if(itemStack2.contains(DataComponentTypes.BANNER_PATTERNS)) {
                itemStack2.remove(DataComponentTypes.BANNER_PATTERNS);
            }
        }
        return itemStack2;
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