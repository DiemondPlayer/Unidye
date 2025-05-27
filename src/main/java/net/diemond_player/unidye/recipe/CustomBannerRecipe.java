package net.diemond_player.unidye.recipe;

import net.diemond_player.unidye.component.RecipeStacksComponent;
import net.diemond_player.unidye.item.DyeableLeatheryBlockItem;
import net.diemond_player.unidye.registry.UnidyeBlocks;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.registry.UnidyeSpecialRecipes;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class CustomBannerRecipe extends SpecialCraftingRecipe {
    public CustomBannerRecipe(CraftingRecipeCategory category) {
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
            if (itemStack2.isEmpty() && (i == 8 || i == 6)) continue;
            if (itemStack2.getItem() == Items.STICK && i == 7) continue;
            if (itemStack2.getItem() == UnidyeBlocks.CUSTOM_WOOL.asItem() && i <= 5) {
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
        return true;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput inventory, RegistryWrapper.WrapperLookup lookup) {
        ItemStack itemStack1 = new ItemStack(UnidyeBlocks.CUSTOM_BANNER.asItem());
        ItemStack itemStack = inventory.getStackInSlot(0);
        UnidyeUtils.setColor(itemStack1, DyeableLeatheryBlockItem.getLeatherColor(itemStack));
        itemStack1.set(UnidyeDataComponentTypes.RECIPE_STACKS, RecipeStacksComponent.fromItemStacks(inventory.getStacks(), itemStack1.getCount()));
        if(itemStack.contains(UnidyeDataComponentTypes.ITEM_NAME_PREFIX)){
            itemStack1.set(UnidyeDataComponentTypes.ITEM_NAME_PREFIX, itemStack.get(UnidyeDataComponentTypes.ITEM_NAME_PREFIX));
        }
        return itemStack1;
    }

    @Override
    public boolean fits(int width, int height) {
        return width == 3 && height == 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return UnidyeSpecialRecipes.CUSTOM_BANNER;
    }
}
