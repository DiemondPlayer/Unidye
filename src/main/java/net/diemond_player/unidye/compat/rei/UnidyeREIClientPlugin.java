package net.diemond_player.unidye.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.plugin.client.categories.crafting.filler.CraftingRecipeFiller;
import net.diemond_player.unidye.registry.UnidyeBlocks;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.registry.tag.ItemTags;

public class UnidyeREIClientPlugin implements REIClientPlugin {
    public static final CraftingRecipeFiller<?>[] CRAFTING_RECIPE_FILLERS = new CraftingRecipeFiller[]{
            new CustomDyeRecipeFiller(),
            new CustomCircleDyeingRecipeFiller(ConventionalItemTags.GLASS_BLOCKS, UnidyeBlocks.CUSTOM_STAINED_GLASS),
            new CustomCircleDyeingRecipeFiller(ConventionalItemTags.GLASS_PANES, UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE),
            new CustomCircleDyeingRecipeFiller(ItemTags.CANDLES, UnidyeBlocks.CUSTOM_CANDLE),
            new CustomCircleDyeingRecipeFiller(ItemTags.WOOL_CARPETS, UnidyeBlocks.CUSTOM_CARPET),
            new CustomCircleDyeingRecipeFiller(ItemTags.WOOL, UnidyeBlocks.CUSTOM_WOOL),
            new CustomCircleDyeingRecipeFiller(ItemTags.TERRACOTTA, UnidyeBlocks.CUSTOM_TERRACOTTA),
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
