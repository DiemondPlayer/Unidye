package net.diemond_player.unidye.recipe;

import net.diemond_player.unidye.component.MaterialColorsComponent;
import net.diemond_player.unidye.component.RecipeStacksComponent;
import net.diemond_player.unidye.registry.UnidyeBlocks;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.registry.UnidyeMaterialTypes;
import net.diemond_player.unidye.registry.UnidyeSpecialRecipes;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class CustomCarpetRecipe extends SpecialCraftingRecipe {
    public CustomCarpetRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput inventory, World world) {
        if(!fits(inventory.getWidth(), inventory.getHeight())){
            return false;
        }
        ItemStack itemStack = ItemStack.EMPTY;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack2 = inventory.getStackInSlot(i);
            if (itemStack2.getItem() == UnidyeBlocks.CUSTOM_WOOL.asItem()) {
                if (!itemStack.isEmpty()) {
                    if (UnidyeUtils.getColor(itemStack2) == UnidyeUtils.getColor(itemStack)
                            && MaterialColorsComponent.getMaterialColor(itemStack2, UnidyeMaterialTypes.LEATHER) == MaterialColorsComponent.getMaterialColor(itemStack, UnidyeMaterialTypes.LEATHER)) {
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
        return !itemStack.isEmpty();
    }

    @Override
    public ItemStack craft(CraftingRecipeInput inventory, RegistryWrapper.WrapperLookup lookup) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack1 = new ItemStack(UnidyeBlocks.CUSTOM_CARPET.asItem());
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack2 = inventory.getStackInSlot(i);
            if (itemStack2.getItem() == UnidyeBlocks.CUSTOM_WOOL.asItem()) {
                itemStack = itemStack2;
                break;
            }
        }
        UnidyeUtils.setColor(itemStack1, UnidyeUtils.getColor(itemStack));
        itemStack1.setCount(3);
        itemStack1.set(UnidyeDataComponentTypes.RECIPE_STACKS, RecipeStacksComponent.fromItemStacks(inventory.getStacks(), itemStack1.getCount()));
        if(itemStack.contains(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES)){
            itemStack1.set(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, itemStack.get(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES));
        }
        return itemStack1;
    }

    @Override
    public boolean fits(int width, int height) {
        return width == 2 && height == 1;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return UnidyeSpecialRecipes.CUSTOM_CARPET;
    }
}
