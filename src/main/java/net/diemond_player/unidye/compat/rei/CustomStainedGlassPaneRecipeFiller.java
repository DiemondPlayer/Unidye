package net.diemond_player.unidye.compat.rei;

import com.google.common.collect.Lists;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.client.categories.crafting.filler.CraftingRecipeFiller;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomShapedDisplay;
import net.diemond_player.unidye.recipe.CustomStainedGlassPaneRecipe;
import net.diemond_player.unidye.registry.UnidyeBlocks;
import net.diemond_player.unidye.registry.UnidyeItems;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DyeColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class CustomStainedGlassPaneRecipeFiller implements CraftingRecipeFiller<CustomStainedGlassPaneRecipe> {
    @Override
    public Collection<Display> apply(CustomStainedGlassPaneRecipe recipe) {
        List<Display> displays = new ArrayList<>();
        DyeColor[] colors = DyeColor.values();
        for (int i = 0; i < 9; i++) {
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
            ItemStack glassStack = UnidyeUtils.blendAndSetColor(new ItemStack(UnidyeBlocks.CUSTOM_STAINED_GLASS), dyeItems, Lists.newArrayList());
            inputs.add(EntryIngredients.of(glassStack));
            inputs.add(EntryIngredients.of(glassStack));
            inputs.add(EntryIngredients.of(glassStack));
            inputs.add(EntryIngredients.of(glassStack));
            inputs.add(EntryIngredients.of(glassStack));
            inputs.add(EntryIngredients.of(glassStack));
            ItemStack output = UnidyeUtils.blendAndSetColor(new ItemStack(UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE), dyeItems, Lists.newArrayList());
            output.setCount(16);
            displays.add(new DefaultCustomShapedDisplay(recipe,
                    inputs, List.of(EntryIngredients.of(output)), 3, 2));
        }

        return displays;
    }
    @Override
    public Class<CustomStainedGlassPaneRecipe> getRecipeClass() {
        return CustomStainedGlassPaneRecipe.class;
    }
}
