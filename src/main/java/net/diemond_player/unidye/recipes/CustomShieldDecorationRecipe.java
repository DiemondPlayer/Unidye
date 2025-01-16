package net.diemond_player.unidye.recipes;

import net.diemond_player.unidye.block.entity.UnidyeBlockEntities;
import net.diemond_player.unidye.item.custom.DyeableBannerItem;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.world.World;

public class CustomShieldDecorationRecipe extends SpecialCraftingRecipe {
    public CustomShieldDecorationRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(RecipeInputInventory recipeInputInventory, World world) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;
        for (int i = 0; i < recipeInputInventory.size(); ++i) {
            ItemStack itemStack3 = recipeInputInventory.getStack(i);
            if (itemStack3.isEmpty()) continue;
            if (itemStack3.getItem() instanceof DyeableBannerItem) {
                if (!itemStack2.isEmpty()) {
                    return false;
                }
                itemStack2 = itemStack3;
                continue;
            }
            if (itemStack3.isOf(Items.SHIELD)) {
                if (!itemStack.isEmpty()) {
                    return false;
                }
                if (BlockItem.getBlockEntityNbt(itemStack3) != null) {
                    return false;
                }
                itemStack = itemStack3;
                continue;
            }
            return false;
        }
        return !itemStack.isEmpty() && !itemStack2.isEmpty();
    }

    @Override
    public ItemStack craft(RecipeInputInventory recipeInputInventory, DynamicRegistryManager dynamicRegistryManager) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;
        for (int i = 0; i < recipeInputInventory.size(); ++i) {
            ItemStack itemStack3 = recipeInputInventory.getStack(i);
            if (itemStack3.isEmpty()) continue;
            if (itemStack3.getItem() instanceof DyeableBannerItem) {
                itemStack = itemStack3;
                continue;
            }
            if (!itemStack3.isOf(Items.SHIELD)) continue;
            itemStack2 = itemStack3.copy();
        }
        if (itemStack2.isEmpty()) {
            return itemStack2;
        }
        NbtCompound nbtCompound = BlockItem.getBlockEntityNbt(itemStack);
        NbtCompound nbtCompound2 = nbtCompound == null ? new NbtCompound() : nbtCompound.copy();
        nbtCompound2.putInt("Base", UnidyeUtils.getColor(itemStack));
        nbtCompound2.putBoolean("CustomColored", true);
        BlockItem.setBlockEntityNbt(itemStack2, UnidyeBlockEntities.DYEABLE_BANNER_BE, nbtCompound2);
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
