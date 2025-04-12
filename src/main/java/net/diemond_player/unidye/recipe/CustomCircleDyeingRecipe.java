package net.diemond_player.unidye.recipe;

import com.google.common.collect.Lists;
import net.diemond_player.unidye.registry.UnidyeItems;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;

public class CustomCircleDyeingRecipe extends SpecialCraftingRecipe {

    public final TagKey<Item> itemTag;
    public final ArrayList<Item> acceptedItems;
    public final ItemConvertible outputItem;
    private final Identifier identifier;

    public CustomCircleDyeingRecipe(TagKey<Item> itemTag, ItemConvertible outputItem, Identifier id, CraftingRecipeCategory category, Identifier identifier) {
        super(id, category);
        this.outputItem = outputItem;
        this.identifier = identifier;
        this.itemTag = itemTag;
        this.acceptedItems = null;
    }

    public CustomCircleDyeingRecipe(ArrayList<Item> acceptedItems, ItemConvertible outputItem, Identifier id, CraftingRecipeCategory category, Identifier identifier) {
        super(id, category);
        this.outputItem = outputItem;
        this.identifier = identifier;
        this.itemTag = null;
        this.acceptedItems = acceptedItems;
    }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        for (int i = 0; i < inventory.size(); ++i) {
            ItemStack itemStack2 = inventory.getStack(i);
            if(itemStack2.getItem() == UnidyeItems.CUSTOM_DYE && i == 4){
                continue;
            }
            if(i != 4) {
                if(itemTag != null) {
                    if (itemStack2.isIn(itemTag)) {
                        continue;
                    }
                } else if (acceptedItems != null) {
                    if (acceptedItems.contains(itemStack2.getItem())) {
                        continue;
                    }
                }
            }
            return false;
        }
        return true;
    }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        ItemStack itemStack1 = UnidyeUtils.blendAndSetColor(new ItemStack(outputItem), Lists.newArrayList(), new ArrayList<>(){{
            add(inventory.getStack(4));
        }});
        itemStack1.setCount(8);
        return itemStack1;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Registries.RECIPE_SERIALIZER.get(identifier);
    }
}