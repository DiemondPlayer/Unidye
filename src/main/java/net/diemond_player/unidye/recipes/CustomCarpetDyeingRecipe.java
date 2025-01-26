package net.diemond_player.unidye.recipes;

import net.diemond_player.unidye.block.UnidyeBlocks;
import net.diemond_player.unidye.item.UnidyeItems;
import net.diemond_player.unidye.item.custom.CustomDyeItem;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.world.World;

public class CustomCarpetDyeingRecipe extends SpecialCraftingRecipe {
    public CustomCarpetDyeingRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack2 = inventory.getStack(i);
            if ((itemStack2.isIn(ItemTags.WOOL_CARPETS) && i != 4)
                    || (itemStack2.getItem() == UnidyeItems.CUSTOM_DYE && i == 4)) {
                continue;
            }
            return false;
        }
        return true;
    }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, RegistryWrapper.WrapperLookup lookup) {
        ItemStack itemStack1 = new ItemStack(UnidyeBlocks.CUSTOM_CARPET.asItem());
        UnidyeUtils.setColor(itemStack1, CustomDyeItem.getMaterialColor(inventory.getStack(4), "wool"));
        itemStack1.setCount(8);
        return itemStack1;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return UnidyeSpecialRecipes.CUSTOM_CARPET_DYEING;
    }
}
