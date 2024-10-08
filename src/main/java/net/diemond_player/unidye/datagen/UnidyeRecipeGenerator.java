package net.diemond_player.unidye.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class UnidyeRecipeGenerator extends FabricRecipeProvider {
    public UnidyeRecipeGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.RED_WOOL, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL)
                .input('D', Items.RED_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.RED_DYE))
                .group("wool_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.RED_WOOL) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.ORANGE_WOOL, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL)
                .input('D', Items.ORANGE_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.ORANGE_DYE))
                .group("wool_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.ORANGE_WOOL) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.YELLOW_WOOL, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL)
                .input('D', Items.YELLOW_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.YELLOW_DYE))
                .group("wool_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.YELLOW_WOOL) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.GREEN_WOOL, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL)
                .input('D', Items.GREEN_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.GREEN_DYE))
                .group("wool_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.GREEN_WOOL) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.LIME_WOOL, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL)
                .input('D', Items.LIME_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.LIME_DYE))
                .group("wool_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.LIME_WOOL) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.LIGHT_BLUE_WOOL, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL)
                .input('D', Items.LIGHT_BLUE_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.LIGHT_BLUE_DYE))
                .group("wool_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.LIGHT_BLUE_WOOL) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.BLUE_WOOL, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL)
                .input('D', Items.BLUE_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.BLUE_DYE))
                .group("wool_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.BLUE_WOOL) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.PURPLE_WOOL, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL)
                .input('D', Items.PURPLE_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.PURPLE_DYE))
                .group("wool_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.PURPLE_WOOL) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.MAGENTA_WOOL, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL)
                .input('D', Items.MAGENTA_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.MAGENTA_DYE))
                .group("wool_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.MAGENTA_WOOL) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.PINK_WOOL, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL)
                .input('D', Items.PINK_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.PINK_DYE))
                .group("wool_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.PINK_WOOL) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.CYAN_WOOL, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL)
                .input('D', Items.CYAN_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.CYAN_DYE))
                .group("wool_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.CYAN_WOOL) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.BROWN_WOOL, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL)
                .input('D', Items.BROWN_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.BROWN_DYE))
                .group("wool_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.BROWN_WOOL) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.BLACK_WOOL, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL)
                .input('D', Items.BLACK_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.BLACK_DYE))
                .group("wool_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.BLACK_WOOL) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.GRAY_WOOL, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL)
                .input('D', Items.GRAY_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.GRAY_DYE))
                .group("wool_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.GRAY_WOOL) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.LIGHT_GRAY_WOOL, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL)
                .input('D', Items.LIGHT_GRAY_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.LIGHT_GRAY_DYE))
                .group("wool_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.LIGHT_GRAY_WOOL) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.WHITE_WOOL, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL)
                .input('D', Items.WHITE_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.WHITE_DYE))
                .group("wool_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.WHITE_WOOL) + "_from_circle_dyeing"));


        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, Blocks.WHITE_CANDLE, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.CANDLES)
                .input('D', Items.WHITE_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.WHITE_DYE))
                .group("candle_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.WHITE_CANDLE) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, Blocks.LIGHT_GRAY_CANDLE, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.CANDLES)
                .input('D', Items.LIGHT_GRAY_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.LIGHT_GRAY_DYE))
                .group("candle_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.LIGHT_GRAY_CANDLE) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, Blocks.GRAY_CANDLE, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.CANDLES)
                .input('D', Items.GRAY_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.GRAY_DYE))
                .group("candle_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.GRAY_CANDLE) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, Blocks.BLACK_CANDLE, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.CANDLES)
                .input('D', Items.BLACK_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.BLACK_DYE))
                .group("candle_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.BLACK_CANDLE) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, Blocks.BROWN_CANDLE, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.CANDLES)
                .input('D', Items.BROWN_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.BROWN_DYE))
                .group("candle_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.BROWN_CANDLE) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, Blocks.PINK_CANDLE, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.CANDLES)
                .input('D', Items.PINK_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.PINK_DYE))
                .group("candle_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.PINK_CANDLE) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, Blocks.RED_CANDLE, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.CANDLES)
                .input('D', Items.RED_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.RED_DYE))
                .group("candle_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.RED_CANDLE) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, Blocks.ORANGE_CANDLE, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.CANDLES)
                .input('D', Items.ORANGE_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.ORANGE_DYE))
                .group("candle_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.ORANGE_CANDLE) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, Blocks.YELLOW_CANDLE, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.CANDLES)
                .input('D', Items.YELLOW_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.YELLOW_DYE))
                .group("candle_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.YELLOW_CANDLE) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, Blocks.LIME_CANDLE, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.CANDLES)
                .input('D', Items.LIME_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.LIME_DYE))
                .group("candle_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.LIME_CANDLE) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, Blocks.GREEN_CANDLE, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.CANDLES)
                .input('D', Items.GREEN_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.GREEN_DYE))
                .group("candle_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.GREEN_CANDLE) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, Blocks.CYAN_CANDLE, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.CANDLES)
                .input('D', Items.CYAN_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.CYAN_DYE))
                .group("candle_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.CYAN_CANDLE) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, Blocks.BLUE_CANDLE, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.CANDLES)
                .input('D', Items.BLUE_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.BLUE_DYE))
                .group("candle_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.BLUE_CANDLE) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, Blocks.LIGHT_BLUE_CANDLE, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.CANDLES)
                .input('D', Items.LIGHT_BLUE_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.LIGHT_BLUE_DYE))
                .group("candle_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.LIGHT_BLUE_CANDLE) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, Blocks.PURPLE_CANDLE, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.CANDLES)
                .input('D', Items.PURPLE_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.PURPLE_DYE))
                .group("candle_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.PURPLE_CANDLE) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, Blocks.MAGENTA_CANDLE, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.CANDLES)
                .input('D', Items.MAGENTA_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.MAGENTA_DYE))
                .group("candle_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.MAGENTA_CANDLE) + "_from_circle_dyeing"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.RED_CARPET, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL_CARPETS)
                .input('D', Items.RED_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.RED_DYE))
                .group("carpet_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.RED_CARPET) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.ORANGE_CARPET, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL_CARPETS)
                .input('D', Items.ORANGE_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.ORANGE_DYE))
                .group("carpet_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.ORANGE_CARPET) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.YELLOW_CARPET, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL_CARPETS)
                .input('D', Items.YELLOW_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.YELLOW_DYE))
                .group("carpet_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.YELLOW_CARPET) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.GREEN_CARPET, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL_CARPETS)
                .input('D', Items.GREEN_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.GREEN_DYE))
                .group("carpet_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.GREEN_CARPET) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.LIME_CARPET, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL_CARPETS)
                .input('D', Items.LIME_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.LIME_DYE))
                .group("carpet_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.LIME_CARPET) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.LIGHT_BLUE_CARPET, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL_CARPETS)
                .input('D', Items.LIGHT_BLUE_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.LIGHT_BLUE_DYE))
                .group("carpet_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.LIGHT_BLUE_CARPET) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.BLUE_CARPET, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL_CARPETS)
                .input('D', Items.BLUE_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.BLUE_DYE))
                .group("carpet_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.BLUE_CARPET) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.PURPLE_CARPET, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL_CARPETS)
                .input('D', Items.PURPLE_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.PURPLE_DYE))
                .group("carpet_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.PURPLE_CARPET) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.MAGENTA_CARPET, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL_CARPETS)
                .input('D', Items.MAGENTA_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.MAGENTA_DYE))
                .group("carpet_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.MAGENTA_CARPET) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.PINK_CARPET, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL_CARPETS)
                .input('D', Items.PINK_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.PINK_DYE))
                .group("carpet_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.PINK_CARPET) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.CYAN_CARPET, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL_CARPETS)
                .input('D', Items.CYAN_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.CYAN_DYE))
                .group("carpet_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.CYAN_CARPET) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.BROWN_CARPET, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL_CARPETS)
                .input('D', Items.BROWN_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.BROWN_DYE))
                .group("carpet_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.BROWN_CARPET) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.BLACK_CARPET, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL_CARPETS)
                .input('D', Items.BLACK_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.BLACK_DYE))
                .group("carpet_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.BLACK_CARPET) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.GRAY_CARPET, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL_CARPETS)
                .input('D', Items.GRAY_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.GRAY_DYE))
                .group("carpet_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.GRAY_CARPET) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.LIGHT_GRAY_CARPET, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL_CARPETS)
                .input('D', Items.LIGHT_GRAY_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.LIGHT_GRAY_DYE))
                .group("carpet_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.LIGHT_GRAY_CARPET) + "_from_circle_dyeing"));
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, Blocks.WHITE_CARPET, 8)
                .pattern("WWW")
                .pattern("WDW")
                .pattern("WWW")
                .input('W', ItemTags.WOOL_CARPETS)
                .input('D', Items.WHITE_DYE)
                .criterion("has_needed_dye", conditionsFromItem(Items.WHITE_DYE))
                .group("carpet_circle_dyeing")
                .showNotification(true)
                .offerTo(exporter, new Identifier(getRecipeName(Items.WHITE_CARPET) + "_from_circle_dyeing"));
    }
}
