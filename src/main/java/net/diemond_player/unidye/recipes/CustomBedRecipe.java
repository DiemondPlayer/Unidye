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
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.world.World;

public class CustomBedRecipe extends SpecialCraftingRecipe {
    public CustomBedRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        ItemStack itemStack = ItemStack.EMPTY;
        int woolCount = 0;
        int planksCount = 0;
        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack2 = inventory.getStack(i);
            if (itemStack2.isEmpty()) continue;
            if (itemStack2.isIn(ItemTags.PLANKS) && woolCount == 3) {
                if (planksCount == 3) return false;
                planksCount++;
                continue;
            }
            if (itemStack2.getItem() == UnidyeBlocks.CUSTOM_WOOL.asItem()) {
                if (woolCount == 3) return false;
                woolCount++;
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
        if (!((inventory.getStack(0).isEmpty()
                && inventory.getStack(1).isEmpty()
                && inventory.getStack(2).isEmpty())
                || (inventory.getStack(6).isEmpty()
                && inventory.getStack(7).isEmpty()
                && inventory.getStack(8).isEmpty()))) {
            return false;
        }
        return !itemStack.isEmpty() && planksCount == 3;
    }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, RegistryWrapper.WrapperLookup lookup) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack1 = new ItemStack(UnidyeBlocks.CUSTOM_BED.asItem());
        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack2 = inventory.getStack(i);
            if (itemStack2.getItem() == UnidyeBlocks.CUSTOM_WOOL.asItem()) {
                itemStack = itemStack2;
                break;
            }
        }
        UnidyeUtils.setColor(itemStack1, DyeableLeatheryBlockItem.getLeatherColor(itemStack));
        return itemStack1;
    }

    @Override
    public boolean fits(int width, int height) {
        return width == 3 && height == 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return UnidyeSpecialRecipes.CUSTOM_BED;
    }
}
