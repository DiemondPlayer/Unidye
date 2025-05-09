package net.diemond_player.unidye.registry;

import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.recipe.*;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings({"unchecked", "unused"})
public class UnidyeSpecialRecipes {
    public static final RecipeSerializer<CustomCarpetRecipe> CUSTOM_CARPET = (RecipeSerializer<CustomCarpetRecipe>)
            registerSpecialRecipe("crafting_special_custom_carpet",
            CustomCarpetRecipe::new);

    public static final RecipeSerializer<CustomStainedGlassPaneRecipe> CUSTOM_STAINED_GLASS_PANE = (RecipeSerializer<CustomStainedGlassPaneRecipe>)
            registerSpecialRecipe("crafting_special_custom_stained_glass_pane",
            CustomStainedGlassPaneRecipe::new);

    public static final RecipeSerializer<CustomCircleDyeingRecipe> CUSTOM_TERRACOTTA_DYEING = registerSpecialCircleDyeingRecipe(
            "crafting_special_custom_terracotta_dyeing",
            ItemTags.TERRACOTTA, UnidyeBlocks.CUSTOM_TERRACOTTA);

    public static final RecipeSerializer<CustomCircleDyeingRecipe> CUSTOM_STAINED_GLASS_DYEING = registerSpecialCircleDyeingRecipe(
            "crafting_special_custom_stained_glass_dyeing",
            ConventionalItemTags.GLASS_BLOCKS, UnidyeBlocks.CUSTOM_STAINED_GLASS);

    public static final RecipeSerializer<CustomCircleDyeingRecipe> CUSTOM_STAINED_GLASS_PANE_DYEING = registerSpecialCircleDyeingRecipe(
            "crafting_special_custom_stained_glass_pane_dyeing",
            ConventionalItemTags.GLASS_PANES, UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE);

    public static final RecipeSerializer<CustomCircleDyeingRecipe> CUSTOM_CANDLE_DYEING = registerSpecialCircleDyeingRecipe(
            "crafting_special_custom_candle_dyeing",
            ItemTags.CANDLES, UnidyeBlocks.CUSTOM_CANDLE);

    public static final RecipeSerializer<CustomCircleDyeingRecipe> CUSTOM_CARPET_DYEING = registerSpecialCircleDyeingRecipe(
            "crafting_special_custom_carpet_dyeing",
            ItemTags.WOOL_CARPETS, UnidyeBlocks.CUSTOM_CARPET);

    public static final RecipeSerializer<CustomCircleDyeingRecipe> CUSTOM_WOOL_DYEING = registerSpecialCircleDyeingRecipe(
            "crafting_special_custom_wool_dyeing",
            ItemTags.WOOL, UnidyeBlocks.CUSTOM_WOOL);

    public static final RecipeSerializer<CustomConcretePowderRecipe> CUSTOM_CONCRETE_POWDER = (RecipeSerializer<CustomConcretePowderRecipe>)
            registerSpecialRecipe("crafting_special_custom_concrete_powder",
            CustomConcretePowderRecipe::new);

    public static final RecipeSerializer<CustomDyeRecipe> CUSTOM_DYE = (RecipeSerializer<CustomDyeRecipe>)
            registerSpecialRecipe("crafting_special_custom_dye",
            CustomDyeRecipe::new);

    public static final RecipeSerializer<CustomBannerRecipe> CUSTOM_BANNER = (RecipeSerializer<CustomBannerRecipe>)
            registerSpecialRecipe("crafting_special_custom_banner",
            CustomBannerRecipe::new);

    public static final RecipeSerializer<CustomBedRecipe> CUSTOM_BED = (RecipeSerializer<CustomBedRecipe>)
            registerSpecialRecipe("crafting_special_custom_bed",
            CustomBedRecipe::new);

    public static final RecipeSerializer<CustomBedDyeingRecipe> CUSTOM_BED_DYEING = (RecipeSerializer<CustomBedDyeingRecipe>)
            registerSpecialRecipe("crafting_special_custom_bed_dyeing",
            CustomBedDyeingRecipe::new);

    public static final RecipeSerializer<CustomShulkerBoxDyeingRecipe> CUSTOM_SHULKER_BOX_DYEING = (RecipeSerializer<CustomShulkerBoxDyeingRecipe>)
            registerSpecialRecipe("crafting_special_custom_shulker_box_dyeing",
            CustomShulkerBoxDyeingRecipe::new);

    public static final RecipeSerializer<CustomShieldDecorationRecipe> CUSTOM_SHIELD_DECORATION = (RecipeSerializer<CustomShieldDecorationRecipe>)
            registerSpecialRecipe("crafting_special_custom_shield_decoration",
            CustomShieldDecorationRecipe::new);

    public static final RecipeSerializer<CustomBannerDuplicateRecipe> CUSTOM_BANNER_DUPLICATE = (RecipeSerializer<CustomBannerDuplicateRecipe>)
            registerSpecialRecipe("crafting_special_custom_banner_duplicate",
            CustomBannerDuplicateRecipe::new);

    public static final RecipeSerializer<MixedBannerDuplicateRecipe> MIXED_BANNER_DUPLICATE = (RecipeSerializer<MixedBannerDuplicateRecipe>)
            registerSpecialRecipe("crafting_special_mixed_banner_duplicate",
            MixedBannerDuplicateRecipe::new);

    public static final RecipeSerializer<CustomFireworkStarRecipe> CUSTOM_FIREWORK_STAR = (RecipeSerializer<CustomFireworkStarRecipe>)
            registerSpecialRecipe("crafting_special_custom_firework_star",
            CustomFireworkStarRecipe::new);

    public static final RecipeSerializer<CustomFireworkStarFadeRecipe> CUSTOM_FIREWORK_STAR_FADE = (RecipeSerializer<CustomFireworkStarFadeRecipe>)
            registerSpecialRecipe("crafting_special_custom_firework_star_fade",
            CustomFireworkStarFadeRecipe::new);

    public static void registerSpecialRecipes() {
    }

    private static RecipeSerializer<CustomCircleDyeingRecipe> registerSpecialCircleDyeingRecipe(String name, ArrayList<Item> acceptedItems, ItemConvertible outputItem){
        return registerSpecialCircleDyeingRecipe(Unidye.MOD_ID, name, acceptedItems, outputItem);
    }

    private static RecipeSerializer<CustomCircleDyeingRecipe> registerSpecialCircleDyeingRecipe(String name, TagKey<Item> itemOrItemTag, ItemConvertible outputItem){
        return registerSpecialCircleDyeingRecipe(Unidye.MOD_ID, name, itemOrItemTag, outputItem);
    }

    private static RecipeSerializer<? extends SpecialCraftingRecipe> registerSpecialRecipe(String name, Function<CraftingRecipeCategory, SpecialCraftingRecipe> toRecipe){
        return registerSpecialRecipe(Unidye.MOD_ID, name, toRecipe);
    }

    public static RecipeSerializer<CustomCircleDyeingRecipe> registerSpecialCircleDyeingRecipe(String modId, String name, TagKey<Item> itemOrItemTag, ItemConvertible outputItem){
        Identifier identifier = Identifier.of(modId, name);
        return Registry.register(Registries.RECIPE_SERIALIZER, identifier,
                new SpecialRecipeSerializer<>(((category) ->
                        new CustomCircleDyeingRecipe(itemOrItemTag, outputItem, category, identifier))));
    }

    public static RecipeSerializer<CustomCircleDyeingRecipe> registerSpecialCircleDyeingRecipe(String modId, String name, ArrayList<Item> acceptedItems, ItemConvertible outputItem){
        Identifier identifier = Identifier.of(modId, name);
        return Registry.register(Registries.RECIPE_SERIALIZER, identifier,
                new SpecialRecipeSerializer<>((( category) ->
                        new CustomCircleDyeingRecipe(acceptedItems, outputItem, category, identifier))));
    }

    public static RecipeSerializer<? extends SpecialCraftingRecipe> registerSpecialRecipe(String modId, String name, Function<CraftingRecipeCategory, SpecialCraftingRecipe> toRecipe){
        return Registry.register(Registries.RECIPE_SERIALIZER, Identifier.of(modId, name),
                new SpecialRecipeSerializer<>(toRecipe::apply));
    }
}
