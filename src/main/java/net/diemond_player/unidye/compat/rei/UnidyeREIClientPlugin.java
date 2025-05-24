package net.diemond_player.unidye.compat.rei;

import com.ibm.icu.impl.Pair;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.plugin.client.categories.crafting.filler.CraftingRecipeFiller;
import net.diemond_player.unidye.Unidye;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.tag.TagKey;

import java.util.ArrayList;

public class UnidyeREIClientPlugin implements REIClientPlugin {
    public static final CraftingRecipeFiller<?>[] CRAFTING_RECIPE_FILLERS = new CraftingRecipeFiller[]{
            new CustomDyeRecipeFiller(),
            new CustomCarpetRecipeFiller(),
            new CustomBedRecipeFiller(),
            new CustomBannerRecipeFiller(),
            new CustomBannerDuplicateRecipeFiller(),
            new CustomStainedGlassPaneRecipeFiller(),
            new CustomConcretePowderRecipeFiller(),
            new CustomShulkerBoxDyeingRecipeFiller(),
            new CustomBedDyeingRecipeFiller()
    };

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        for (CraftingRecipeFiller<?> filler : CRAFTING_RECIPE_FILLERS) {
            filler.registerDisplays(registry);
        }
        for (Pair<TagKey<Item>, ItemConvertible> params : Unidye.REI_TAG_DRIVEN_CIRCLE_DYEING_RECIPE_PARAMETERS){
            CraftingRecipeFiller<?> filler = new CustomCircleDyeingRecipeFiller(params.first, params.second);
            filler.registerDisplays(registry);
        }
        for (Pair<ArrayList<Item>, ItemConvertible> params : Unidye.REI_ARRAY_DRIVEN_CIRCLE_DYEING_RECIPE_PARAMETERS){
            CraftingRecipeFiller<?> filler = new CustomCircleDyeingRecipeFiller(params.first, params.second);
            filler.registerDisplays(registry);
        }
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        for (CraftingRecipeFiller<?> filler : CRAFTING_RECIPE_FILLERS) {
            filler.registerCategories(registry);
        }
        for (Pair<TagKey<Item>, ItemConvertible> params : Unidye.REI_TAG_DRIVEN_CIRCLE_DYEING_RECIPE_PARAMETERS){
            CraftingRecipeFiller<?> filler = new CustomCircleDyeingRecipeFiller(params.first, params.second);
            filler.registerCategories(registry);
        }
        for (Pair<ArrayList<Item>, ItemConvertible> params : Unidye.REI_ARRAY_DRIVEN_CIRCLE_DYEING_RECIPE_PARAMETERS){
            CraftingRecipeFiller<?> filler = new CustomCircleDyeingRecipeFiller(params.first, params.second);
            filler.registerCategories(registry);
        }
    }
}
