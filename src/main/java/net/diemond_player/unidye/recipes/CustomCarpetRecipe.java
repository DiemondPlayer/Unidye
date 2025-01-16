package net.diemond_player.unidye.recipes;

import net.diemond_player.unidye.block.UnidyeBlocks;
import net.diemond_player.unidye.item.custom.DyeableLeatheryBlockItem;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.world.World;

public class CustomCarpetRecipe extends SpecialCraftingRecipe {
    public CustomCarpetRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        ItemStack itemStack = ItemStack.EMPTY;
        int count = 0;
        int[] positions = new int[2];
        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack2 = inventory.getStack(i);
            if (itemStack2.isEmpty()) continue;
            if (itemStack2.getItem() == UnidyeBlocks.CUSTOM_WOOL.asItem()) {
                if (count == 2) return false;
                positions[count] = i;
                count++;
                if (!itemStack.isEmpty()) {
                    if (UnidyeUtils.getColor(itemStack2) == UnidyeUtils.getColor(itemStack)
                            && DyeableLeatheryBlockItem.getLeatherColor(itemStack2) == DyeableLeatheryBlockItem.getLeatherColor(itemStack)) {
                        continue;
                    } else {
                        return false;
                    }
                }
                itemStack = itemStack2;
                continue;
            }
            return false;
        }
        if (inventory.size() == 9) {
            if (positions[0] == 2 || positions[0] == 5) {
                return false;
            }
        }
        if (inventory.size() == 4) {
            if (positions[0] == 1) {
                return false;
            }
        }
        return !itemStack.isEmpty() && positions[0] + 1 == positions[1];
    }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack1 = new ItemStack(UnidyeBlocks.CUSTOM_CARPET.asItem());
        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack2 = inventory.getStack(i);
            if (itemStack2.getItem() == UnidyeBlocks.CUSTOM_WOOL.asItem()) {
                itemStack = itemStack2;
            }
        }
        UnidyeUtils.setColor(itemStack1, UnidyeUtils.getColor(itemStack));
        itemStack1.setCount(3);
        return itemStack1;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return UnidyeSpecialRecipes.CUSTOM_CARPET;
    }
}
