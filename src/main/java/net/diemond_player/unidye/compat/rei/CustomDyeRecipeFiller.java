package net.diemond_player.unidye.compat.rei;

import com.google.common.collect.Lists;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.client.categories.crafting.filler.CraftingRecipeFiller;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomShapelessDisplay;
import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.item.UnidyeItems;
import net.diemond_player.unidye.recipes.CustomDyeRecipe;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DyeColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class CustomDyeRecipeFiller implements CraftingRecipeFiller<CustomDyeRecipe> {
    @Override
    public Collection<Display> apply(CustomDyeRecipe recipe) {
        List<Display> displays = new ArrayList<>();
        DyeColor[] colors = DyeColor.values();
        for (int i = 0; i < 9; i++) {
            int dyes = new Random().nextInt(2) + 2;
            List<EntryIngredient> inputs = new ArrayList<>();
            List<DyeItem> dyeItems = new ArrayList<>();
            if (!Unidye.POLYMORPH) {
                inputs.add(EntryIngredients.of(Items.STICK));
            }
            for (int j = 0; j < dyes; j++) {
                DyeColor color = colors[new Random().nextInt(colors.length)];
                DyeItem dyeItem = DyeItem.byColor(color);
                if (dyeItem == UnidyeItems.CUSTOM_DYE) {
                    dyeItem = (DyeItem) Items.WHITE_DYE;
                }
                if (dyeItems.contains(dyeItem)) {
                    j--;
                } else {
                    dyeItems.add(dyeItem);
                    inputs.add(EntryIngredients.of(dyeItem));
                }
            }
            ItemStack output = UnidyeUtils.blendAndSetColor(new ItemStack(UnidyeItems.CUSTOM_DYE), dyeItems, Lists.newArrayList());
            output.setCount(dyeItems.size());
            displays.add(new DefaultCustomShapelessDisplay(recipe,
                    inputs, List.of(EntryIngredients.of(output))));
        }

        return displays;
    }

    @Override
    public Class<CustomDyeRecipe> getRecipeClass() {
        return CustomDyeRecipe.class;
    }
}
