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
import net.minecraft.world.World;

import java.util.Objects;

public class CustomStainedGlassPaneRecipe extends SpecialCraftingRecipe {
    public CustomStainedGlassPaneRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        ItemStack itemStack = ItemStack.EMPTY;
        int count = 0;
        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack2 = inventory.getStack(i);
            if (itemStack2.isEmpty()) continue;
            if (itemStack2.getItem() == UnidyeBlocks.CUSTOM_STAINED_GLASS.asItem()) {
                if (count == 6) return false;
                count++;
                if (!itemStack.isEmpty()) {
                    if (UnidyeUtils.getColor(itemStack2) == UnidyeUtils.getColor(itemStack)
                            && Objects.equals(DyeableLeatheryBlockItem.getLeatherColor(itemStack2), DyeableLeatheryBlockItem.getLeatherColor(itemStack))) {
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
        return !itemStack.isEmpty() && count == 6;
    }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, RegistryWrapper.WrapperLookup lookup) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack1 = new ItemStack(UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE.asItem());
        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack2 = inventory.getStack(i);
            if (itemStack2.getItem() == UnidyeBlocks.CUSTOM_STAINED_GLASS.asItem()) {
                itemStack = itemStack2;
            }
        }
        UnidyeUtils.setColor(itemStack1, UnidyeUtils.getColor(itemStack));
        DyeableLeatheryBlockItem.setLeatherColor(itemStack1, DyeableLeatheryBlockItem.getLeatherColor(itemStack));
        itemStack1.setCount(16);
        return itemStack1;
    }

    @Override
    public boolean fits(int width, int height) {
        return width == 3 && height == 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return UnidyeSpecialRecipes.CUSTOM_STAINED_GLASS_PANE;
    }
}
