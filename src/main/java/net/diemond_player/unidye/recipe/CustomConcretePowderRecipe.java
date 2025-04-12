package net.diemond_player.unidye.recipe;

import net.diemond_player.unidye.registry.UnidyeBlocks;
import net.diemond_player.unidye.registry.UnidyeItems;
import net.diemond_player.unidye.item.CustomDyeItem;
import net.diemond_player.unidye.registry.UnidyeSpecialRecipes;
import net.diemond_player.unidye.registry.UnidyeMaterialTypes;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class CustomConcretePowderRecipe extends SpecialCraftingRecipe {
    public CustomConcretePowderRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        int gravelCount = 0;
        int sandCount = 0;
        boolean dye = false;
        for (int i = 0; i < inventory.size(); ++i) {
            Item item = inventory.getStack(i).getItem();
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
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        ItemStack itemStack = ItemStack.EMPTY;
        for (int i = 0; i < inventory.size(); ++i) {
            if (inventory.getStack(i).getItem() == UnidyeItems.CUSTOM_DYE) {
                itemStack = inventory.getStack(i);
            }
        }
        ItemStack itemStack1 = new ItemStack(UnidyeBlocks.CUSTOM_CONCRETE_POWDER.asItem());
        UnidyeUtils.setColor(itemStack1, CustomDyeItem.getMaterialColor(itemStack, UnidyeMaterialTypes.CONCRETE));
        itemStack1.setCount(8);
        return itemStack1;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return UnidyeSpecialRecipes.CUSTOM_CONCRETE_POWDER;
    }
}
