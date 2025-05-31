package net.diemond_player.unidye.compat.rei;

import com.google.common.collect.Lists;
import com.ibm.icu.impl.Pair;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.plugin.client.categories.crafting.filler.CraftingRecipeFiller;
import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.registry.UnidyeBlocks;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

import java.util.ArrayList;

public class UnidyeREIClientPlugin implements REIClientPlugin {

    public static final ArrayList<Pair<ArrayList<Item>, ItemConvertible>> REI_ARRAY_DRIVEN_CIRCLE_DYEING_RECIPE_PARAMETERS = Lists.newArrayList();
    public static final ArrayList<Pair<TagKey<Item>, ItemConvertible>> REI_TAG_DRIVEN_CIRCLE_DYEING_RECIPE_PARAMETERS = Lists.newArrayList(
            Pair.of(ConventionalItemTags.GLASS_BLOCKS, UnidyeBlocks.CUSTOM_STAINED_GLASS),
            Pair.of(ConventionalItemTags.GLASS_PANES, UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE),
            Pair.of(ItemTags.CANDLES, UnidyeBlocks.CUSTOM_CANDLE),
            Pair.of(ItemTags.WOOL_CARPETS, UnidyeBlocks.CUSTOM_CARPET),
            Pair.of(ItemTags.WOOL, UnidyeBlocks.CUSTOM_WOOL),
            Pair.of(ItemTags.TERRACOTTA, UnidyeBlocks.CUSTOM_TERRACOTTA)
    );

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
        for (Pair<TagKey<Item>, ItemConvertible> params : REI_TAG_DRIVEN_CIRCLE_DYEING_RECIPE_PARAMETERS){
            CraftingRecipeFiller<?> filler = new CustomCircleDyeingRecipeFiller(params.first, params.second);
            filler.registerDisplays(registry);
        }
        for (Pair<ArrayList<Item>, ItemConvertible> params : REI_ARRAY_DRIVEN_CIRCLE_DYEING_RECIPE_PARAMETERS){
            CraftingRecipeFiller<?> filler = new CustomCircleDyeingRecipeFiller(params.first, params.second);
            filler.registerDisplays(registry);
        }
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        for (CraftingRecipeFiller<?> filler : CRAFTING_RECIPE_FILLERS) {
            filler.registerCategories(registry);
        }
        for (Pair<TagKey<Item>, ItemConvertible> params : REI_TAG_DRIVEN_CIRCLE_DYEING_RECIPE_PARAMETERS){
            CraftingRecipeFiller<?> filler = new CustomCircleDyeingRecipeFiller(params.first, params.second);
            filler.registerCategories(registry);
        }
        for (Pair<ArrayList<Item>, ItemConvertible> params : REI_ARRAY_DRIVEN_CIRCLE_DYEING_RECIPE_PARAMETERS){
            CraftingRecipeFiller<?> filler = new CustomCircleDyeingRecipeFiller(params.first, params.second);
            filler.registerCategories(registry);
        }
    }
}
