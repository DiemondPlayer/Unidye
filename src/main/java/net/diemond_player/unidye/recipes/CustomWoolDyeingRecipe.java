package net.diemond_player.unidye.recipes;

import net.diemond_player.unidye.block.UnidyeBlocks;
import net.diemond_player.unidye.item.UnidyeItems;
import net.diemond_player.unidye.item.custom.CustomDyeItem;
import net.diemond_player.unidye.item.custom.DyeableLeatheryBlockItem;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.world.World;

public class CustomWoolDyeingRecipe extends SpecialCraftingRecipe {
    public CustomWoolDyeingRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingRecipeInput inventory, World world) {
        if(!fits(inventory.getWidth(), inventory.getHeight())){
            return false;
        }
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack2 = inventory.getStackInSlot(i);
            if ((itemStack2.isIn(ItemTags.WOOL) && i != 4)
                    || (itemStack2.getItem() == UnidyeItems.CUSTOM_DYE && i == 4)) {
                continue;
            }
            return false;
        }
        return true;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput inventory, RegistryWrapper.WrapperLookup lookup) {
        ItemStack itemStack1 = new ItemStack(UnidyeBlocks.CUSTOM_WOOL.asItem());
        UnidyeUtils.setColor(itemStack1, CustomDyeItem.getMaterialColor(inventory.getStackInSlot(4), "wool"));
        DyeableLeatheryBlockItem.setLeatherColor(itemStack1, CustomDyeItem.getMaterialColor(inventory.getStackInSlot(4), "leather"));
        itemStack1.setCount(8);
        return itemStack1;
    }

    @Override
    public boolean fits(int width, int height) {
        return width == 3 && height == 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return UnidyeSpecialRecipes.CUSTOM_WOOL_DYEING;
    }
}
