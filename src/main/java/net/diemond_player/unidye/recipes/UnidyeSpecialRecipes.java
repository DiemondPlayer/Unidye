package net.diemond_player.unidye.recipes;

import net.diemond_player.unidye.Unidye;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class UnidyeSpecialRecipes {
    public static final RecipeSerializer<CustomCarpetRecipe> CUSTOM_CARPET = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Unidye.MOD_ID, "custom_carpet"),
            new SpecialRecipeSerializer<>(CustomCarpetRecipe::new));
    public static final RecipeSerializer<CustomStainedGlassPaneRecipe> CUSTOM_STAINED_GLASS_PANE = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Unidye.MOD_ID, "custom_stained_glass_pane"),
            new SpecialRecipeSerializer<>(CustomStainedGlassPaneRecipe::new));
    public static final RecipeSerializer<CustomTerracottaDyeingRecipe> CUSTOM_TERRACOTTA_DYEING = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Unidye.MOD_ID, "custom_terracotta_dyeing"),
            new SpecialRecipeSerializer<>(CustomTerracottaDyeingRecipe::new));
    public static final RecipeSerializer<CustomStainedGlassDyeingRecipe> CUSTOM_STAINED_GLASS_DYEING = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Unidye.MOD_ID, "custom_stained_glass_dyeing"),
            new SpecialRecipeSerializer<>(CustomStainedGlassDyeingRecipe::new));
    public static final RecipeSerializer<CustomStainedGlassPaneDyeingRecipe> CUSTOM_STAINED_GLASS_PANE_DYEING = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Unidye.MOD_ID, "custom_stained_glass_pane_dyeing"),
            new SpecialRecipeSerializer<>(CustomStainedGlassPaneDyeingRecipe::new));
    public static final RecipeSerializer<CustomCandleDyeingRecipe> CUSTOM_CANDLE_DYEING = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Unidye.MOD_ID, "custom_candle_dyeing"),
            new SpecialRecipeSerializer<>(CustomCandleDyeingRecipe::new));
    public static final RecipeSerializer<CustomCarpetDyeingRecipe> CUSTOM_CARPET_DYEING = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Unidye.MOD_ID, "custom_carpet_dyeing"),
            new SpecialRecipeSerializer<>(CustomCarpetDyeingRecipe::new));
    public static final RecipeSerializer<CustomWoolDyeingRecipe> CUSTOM_WOOL_DYEING = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Unidye.MOD_ID, "custom_wool_dyeing"),
            new SpecialRecipeSerializer<>(CustomWoolDyeingRecipe::new));
    public static final RecipeSerializer<CustomConcretePowderRecipe> CUSTOM_CONCRETE_POWDER = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Unidye.MOD_ID, "custom_concrete_powder"),
            new SpecialRecipeSerializer<>(CustomConcretePowderRecipe::new));
    public static final RecipeSerializer<CustomDyeRecipe> CUSTOM_DYE = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Unidye.MOD_ID, "custom_dye"),
            new SpecialRecipeSerializer<>(CustomDyeRecipe::new));
    public static final RecipeSerializer<CustomBannerRecipe> CUSTOM_BANNER = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Unidye.MOD_ID, "custom_banner"),
            new SpecialRecipeSerializer<>(CustomBannerRecipe::new));
    public static final RecipeSerializer<CustomBedRecipe> CUSTOM_BED = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Unidye.MOD_ID, "custom_bed"),
            new SpecialRecipeSerializer<>(CustomBedRecipe::new));
    public static final RecipeSerializer<CustomBedDyeingRecipe> CUSTOM_BED_DYEING = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Unidye.MOD_ID, "custom_bed_dyeing"),
            new SpecialRecipeSerializer<>(CustomBedDyeingRecipe::new));
    public static final RecipeSerializer<CustomShulkerBoxDyeingRecipe> CUSTOM_SHULKER_BOX_DYEING = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Unidye.MOD_ID, "custom_shulker_box_dyeing"),
            new SpecialRecipeSerializer<>(CustomShulkerBoxDyeingRecipe::new));
    public static final RecipeSerializer<CustomShieldDecorationRecipe> CUSTOM_SHIELD_DECORATION = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Unidye.MOD_ID, "custom_shield_decoration"),
            new SpecialRecipeSerializer<>(CustomShieldDecorationRecipe::new));
    public static final RecipeSerializer<CustomBannerDuplicateRecipe> CUSTOM_BANNER_DUPLICATE = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Unidye.MOD_ID, "custom_banner_duplicate"),
            new SpecialRecipeSerializer<>(CustomBannerDuplicateRecipe::new));
    public static final RecipeSerializer<MixedBannerDuplicateRecipe> MIXED_BANNER_DUPLICATE = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Unidye.MOD_ID, "mixed_banner_duplicate"),
            new SpecialRecipeSerializer<>(MixedBannerDuplicateRecipe::new));
    public static final RecipeSerializer<CustomFireworkStarRecipe> CUSTOM_FIREWORK_STAR = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Unidye.MOD_ID, "custom_firework_star"),
            new SpecialRecipeSerializer<>(CustomFireworkStarRecipe::new));
    public static final RecipeSerializer<CustomFireworkStarFadeRecipe> CUSTOM_FIREWORK_STAR_FADE = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(Unidye.MOD_ID, "custom_firework_star_fade"),
            new SpecialRecipeSerializer<>(CustomFireworkStarFadeRecipe::new));

    public static void registerSpecialRecipes() {
    }
}
