package net.diemond_player.unidye.compat.rei;

import com.google.common.collect.Lists;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.client.categories.crafting.filler.CraftingRecipeFiller;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomShapedDisplay;
import net.diemond_player.unidye.recipe.CustomCircleDyeingRecipe;
import net.diemond_player.unidye.registry.UnidyeItems;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.item.*;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.DyeColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class CustomCircleDyeingRecipeFiller implements CraftingRecipeFiller<CustomCircleDyeingRecipe> {

    private final ItemConvertible itemOutput;
    private final TagKey<Item> itemTag;
    private final ArrayList<Item> acceptedItems;

    public CustomCircleDyeingRecipeFiller(TagKey<Item> itemTag, ItemConvertible itemOutput) {
        this.itemOutput = itemOutput;
        this.itemTag = itemTag;
        this.acceptedItems = null;
    }

    @SuppressWarnings("unused")
    public CustomCircleDyeingRecipeFiller(ArrayList<Item> acceptedItems, ItemConvertible itemOutput) {
        this.itemOutput = itemOutput;
        this.itemTag = null;
        this.acceptedItems = acceptedItems;
    }

    @Override
    public Collection<Display> apply(RecipeEntry<CustomCircleDyeingRecipe> recipe) {
        List<Display> displays = new ArrayList<>();
        DyeColor[] colors = DyeColor.values();
        for (int i = 0; i < 3; i++) {
            int dyes = new Random().nextInt(2) + 2;
            List<EntryIngredient> inputs = new ArrayList<>();
            List<DyeItem> dyeItems = new ArrayList<>();
            for (int j = 0; j < dyes; j++) {
                DyeColor color = colors[new Random().nextInt(colors.length)];
                DyeItem dyeItem = DyeItem.byColor(color);
                if(dyeItem == UnidyeItems.CUSTOM_DYE){
                    dyeItem = (DyeItem) Items.WHITE_DYE;
                }
                if (dyeItems.contains(dyeItem)) {
                    j--;
                }else{
                    dyeItems.add(dyeItem);
                }
            }
            ItemStack customDyeStack = UnidyeUtils.blendAndSetColor(new ItemStack(UnidyeItems.CUSTOM_DYE), dyeItems, Lists.newArrayList());
            ItemStack output = UnidyeUtils.blendAndSetColor(new ItemStack(itemOutput), dyeItems, Lists.newArrayList());
            output.setCount(8);
            for(int k = 0; k < 9; k++) {
                if(k != 4) {
                    if(itemTag !=null) {
                        inputs.add(EntryIngredients.ofItemTag(itemTag));
                    }else if(acceptedItems != null){
                        inputs.add(EntryIngredient.of(
                                acceptedItems.stream().map((item ->
                                        EntryStack.of(VanillaEntryTypes.ITEM, new ItemStack(item))))
                                        .toList()));
                    }
                }else{
                    inputs.add(EntryIngredient.of(EntryStack.of(VanillaEntryTypes.ITEM, customDyeStack)));
                }
            }
            displays.add(new DefaultCustomShapedDisplay(recipe,
                    inputs, List.of(EntryIngredients.of(output)), 3, 3));
        }

        return displays;
    }
    @Override
    public Class<CustomCircleDyeingRecipe> getRecipeClass() {
        return CustomCircleDyeingRecipe.class;
    }
}
