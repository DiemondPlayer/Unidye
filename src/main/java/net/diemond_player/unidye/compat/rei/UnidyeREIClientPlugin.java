package net.diemond_player.unidye.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.plugin.client.categories.crafting.filler.*;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCraftingDisplay;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomShapelessDisplay;
import net.diemond_player.unidye.item.custom.CustomDyeItem;
import net.diemond_player.unidye.recipes.CustomDyeRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SpecialCraftingRecipe;

public class UnidyeREIClientPlugin implements REIClientPlugin {
    private static final CraftingRecipeFiller<?>[] CRAFTING_RECIPE_FILLERS = new CraftingRecipeFiller[]{
            new CustomDyeRecipeFiller(),
            new CustomWoolDyeingRecipeFiller(),
            new CustomTerracottaDyeingRecipeFiller(),
            new CustomCandleDyeingRecipeFiller(),
            new CustomCarpetDyeingRecipeFiller(),
            new CustomStainedGlassDyeingRecipeFiller(),
            new CustomStainedGlassPaneDyeingRecipeFiller(),
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
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        for (CraftingRecipeFiller<?> filler : CRAFTING_RECIPE_FILLERS) {
            filler.registerCategories(registry);
        }
    }
}
