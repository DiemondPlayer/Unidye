package net.diemond_player.unidye.recipes;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.diemond_player.unidye.item.custom.CustomDyeItem;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class CustomFireworkStarFadeRecipe extends SpecialCraftingRecipe {
    public CustomFireworkStarFadeRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    private static final Ingredient INPUT_STAR = Ingredient.ofItems(Items.FIREWORK_STAR);

    @Override
    public boolean matches(CraftingRecipeInput recipeInputInventory, World world) {
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;

        for (int i = 0; i < recipeInputInventory.getSize(); i++) {
            ItemStack itemStack = recipeInputInventory.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                if (itemStack.getItem() instanceof DyeItem) {
                    bl = true;
                    if(itemStack.getItem() instanceof CustomDyeItem){
                        bl3 = true;
                    }
                } else {
                    if (!INPUT_STAR.test(itemStack)) {
                        return false;
                    }

                    if (bl2) {
                        return false;
                    }

                    bl2 = true;
                }
            }
        }

        return bl2 && bl && bl3;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput recipeInputInventory, RegistryWrapper.WrapperLookup lookup) {
        IntList intList = new IntArrayList();
        ItemStack itemStack = null;

        for (int i = 0; i < recipeInputInventory.getSize(); i++) {
            ItemStack itemStack2 = recipeInputInventory.getStackInSlot(i);
            Item item = itemStack2.getItem();
            if (item instanceof CustomDyeItem){
                intList.add(CustomDyeItem.getMaterialColor(itemStack2, "firework").intValue());
            }else if (item instanceof DyeItem) {
                intList.add(((DyeItem)item).getColor().getFireworkColor());
            } else if (INPUT_STAR.test(itemStack2)) {
                itemStack = itemStack2.copyWithCount(1);
            }
        }

        if (itemStack != null && !intList.isEmpty()) {
            itemStack.apply(DataComponentTypes.FIREWORK_EXPLOSION, FireworkExplosionComponent.DEFAULT, intList, FireworkExplosionComponent::withFadeColors);
            return itemStack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return UnidyeSpecialRecipes.CUSTOM_FIREWORK_STAR_FADE;
    }
}
