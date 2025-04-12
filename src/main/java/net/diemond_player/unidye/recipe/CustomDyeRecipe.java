package net.diemond_player.unidye.recipe;

import com.google.common.collect.Lists;
import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.registry.UnidyeItems;
import net.diemond_player.unidye.item.CustomDyeItem;
import net.diemond_player.unidye.registry.UnidyeSpecialRecipes;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.ArrayList;

public class CustomDyeRecipe extends SpecialCraftingRecipe {
    public CustomDyeRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        boolean stick = false;
        boolean difference = false;
        boolean custom = false;
        Item item = null;
        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack2 = inventory.getStack(i);
            if (itemStack2.isEmpty()) {
                continue;
            }
            if (itemStack2.getItem() instanceof DyeItem) {
                if (item == null) {
                    item = itemStack2.getItem();
                } else if (item != itemStack2.getItem()) {
                    difference = true;
                }
                if(itemStack2.getItem() instanceof CustomDyeItem){
                    custom = true;
                }
                continue;
            }
            if (itemStack2.getItem() == Items.STICK) {
                if (stick) {
                    return false;
                }
                stick = true;
                continue;
            }
            return false;
        }
        if(!custom) {
            return (stick || Unidye.POLYMORPH) && difference;
        }else{
            return difference && stick;
        }
    }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        ArrayList<DyeItem> list = Lists.newArrayList();
        ArrayList<ItemStack> customList = Lists.newArrayList();
        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack2 = inventory.getStack(i);
            if (itemStack2.isEmpty()) continue;
            Item item = itemStack2.getItem();
            if (item instanceof DyeItem && !(itemStack2.getItem() instanceof CustomDyeItem)) {
                list.add((DyeItem) item);
            }
            if(item instanceof CustomDyeItem){
                customList.add(itemStack2);
            }
        }
        if (list.isEmpty() && customList.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack itemStack = UnidyeUtils.blendAndSetColor(new ItemStack(UnidyeItems.CUSTOM_DYE), list, customList);
        itemStack.setCount(list.size() + customList.size());
        return itemStack;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return UnidyeSpecialRecipes.CUSTOM_DYE;
    }

    @Override
    public DefaultedList<ItemStack> getRemainder(RecipeInputInventory inventory) {
        DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);
        for (int i = 0; i < defaultedList.size(); ++i) {
            Item item = inventory.getStack(i).getItem();
            if (item == Items.STICK) {
                defaultedList.set(i, new ItemStack(Items.STICK));
            }
        }
        return defaultedList;
    }
}
