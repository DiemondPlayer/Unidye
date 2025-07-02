package net.diemond_player.unidye.recipe;

import com.google.common.collect.Lists;
import net.diemond_player.unidye.component.RecipeStacksComponent;
import net.diemond_player.unidye.item.CustomDyeItem;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;

public class CustomSingleDyeingRecipe extends SpecialCraftingRecipe {
    public final TagKey<Item> itemTag;
    public final ItemConvertible inputFallback;
    public final ArrayList<Item> acceptedItems;
    public final ItemConvertible outputItem;
    private final Identifier identifier;

    public CustomSingleDyeingRecipe(TagKey<Item> itemTag, ItemConvertible inputFallback, ItemConvertible outputItem, CraftingRecipeCategory category, Identifier identifier) {
        super(category);
        this.outputItem = outputItem;
        this.identifier = identifier;
        this.itemTag = itemTag;
        this.inputFallback = inputFallback;
        this.acceptedItems = null;
    }

    public CustomSingleDyeingRecipe(ArrayList<Item> acceptedItems, ItemConvertible inputFallback, ItemConvertible outputItem, CraftingRecipeCategory category, Identifier identifier) {
        super(category);
        this.outputItem = outputItem;
        this.identifier = identifier;
        this.itemTag = null;
        this.inputFallback = inputFallback;
        this.acceptedItems = acceptedItems;
    }

    @Override
    public boolean matches(CraftingRecipeInput inventory, World world) {
        boolean keyItem = false;
        boolean difference = false;
        int count = 0;
        Item item = null;
        ItemStack itemStack = null;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack2 = inventory.getStackInSlot(i);
            if (itemStack2.isEmpty()) {
                continue;
            }
            if (itemStack2.getItem() instanceof CustomDyeItem) {
                count++;
                if (item == null) {
                    item = itemStack2.getItem();
                } else if (item != itemStack2.getItem()) {
                    difference = true;
                }
                if (itemStack == null) {
                    itemStack = itemStack2;
                } else if (UnidyeUtils.getColor(itemStack) != UnidyeUtils.getColor(itemStack2)) {
                    difference = true;
                }
                continue;
            }
            if (itemStack2.getItem() instanceof DyeItem) {
                if (item == null) {
                    item = itemStack2.getItem();
                } else if (item != itemStack2.getItem()) {
                    difference = true;
                }
                continue;
            }
            if(itemTag != null) {
                if (itemStack2.isIn(itemTag) && itemStack2.getItem() != outputItem.asItem()) {
                    if (keyItem) {
                        return false;
                    }
                    keyItem = true;
                    continue;
                }
            } else if (acceptedItems != null) {
                if (acceptedItems.contains(itemStack2.getItem()) && itemStack2.getItem() != outputItem.asItem()) {
                    if (keyItem) {
                        return false;
                    }
                    keyItem = true;
                    continue;
                }
            }
            return false;
        }
        return keyItem && (difference || count == 1);
    }

    @Override
    public ItemStack craft(CraftingRecipeInput inventory, RegistryWrapper.WrapperLookup lookup) {
        ArrayList<DyeItem> dyeList = Lists.newArrayList();
        ArrayList<ItemStack> customDyeList = Lists.newArrayList();
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack2 = inventory.getStackInSlot(i);
            if (itemStack2.isEmpty()) continue;
            Item item = itemStack2.getItem();
            if (item instanceof CustomDyeItem) {
                customDyeList.add(itemStack2);
                continue;
            }
            if (item instanceof DyeItem) {
                dyeList.add((DyeItem) item);
            }
        }
        if (customDyeList.isEmpty() && dyeList.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack itemStack = UnidyeUtils.blendAndSetColor(new ItemStack(outputItem), dyeList, customDyeList);
        RecipeStacksComponent recipeStacksComponent = RecipeStacksComponent.fromItemStacks(inventory.getStacks(), itemStack.getCount());
        if(itemTag != null){
            recipeStacksComponent = recipeStacksComponent.optimizeTagToFallback(itemTag, inputFallback);
        }else if (acceptedItems != null) {
            recipeStacksComponent = recipeStacksComponent.optimizeAcceptedItemsToFallback(acceptedItems, inputFallback);
        }
        itemStack.set(UnidyeDataComponentTypes.RECIPE_STACKS, recipeStacksComponent);
        return itemStack;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Registries.RECIPE_SERIALIZER.get(identifier);
    }
}