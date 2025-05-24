package net.diemond_player.unidye.recipe;

import net.diemond_player.unidye.component.RecipeStacksComponent;
import net.diemond_player.unidye.item.CustomDyeItem;
import net.diemond_player.unidye.registry.*;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class CustomConcretePowderRecipe extends SpecialCraftingRecipe {
    public CustomConcretePowderRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput inventory, World world) {
        if(!fits(inventory.getWidth(), inventory.getHeight())){
            return false;
        }
        int gravelCount = 0;
        int sandCount = 0;
        boolean dye = false;
        for (int i = 0; i < inventory.getSize(); i++) {
            Item item = inventory.getStackInSlot(i).getItem();
            if (item == Items.SAND) {
                sandCount++;
                continue;
            } else if (item == Items.GRAVEL) {
                gravelCount++;
                continue;
            } else if (item == UnidyeItems.CUSTOM_DYE) {
                dye = true;
                continue;
            }
            return false;
        }
        return sandCount == 4 && gravelCount == 4 && dye;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput inventory, RegistryWrapper.WrapperLookup lookup) {
        ItemStack itemStack = ItemStack.EMPTY;
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getStackInSlot(i).getItem() == UnidyeItems.CUSTOM_DYE) {
                itemStack = inventory.getStackInSlot(i);
            }
        }
        ItemStack itemStack1 = new ItemStack(UnidyeBlocks.CUSTOM_CONCRETE_POWDER.asItem());
        UnidyeUtils.setColor(itemStack1, CustomDyeItem.getMaterialColor(itemStack, UnidyeMaterialTypes.CONCRETE));
        itemStack1.setCount(8);
        itemStack1.set(UnidyeDataComponentTypes.RECIPE_STACKS, RecipeStacksComponent.fromItemStacks(inventory.getStacks(), itemStack1.getCount(), true));
        return itemStack1;
    }

    @Override
    public boolean fits(int width, int height) {
        return width == 3 && height == 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return UnidyeSpecialRecipes.CUSTOM_CONCRETE_POWDER;
    }
}
