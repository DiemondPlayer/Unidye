package net.diemond_player.unidye.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.common.util.ErrorUtil;
import mezz.jei.library.plugins.vanilla.crafting.CraftingRecipeCategory;
import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.registry.UnidyeBlocks;
import net.diemond_player.unidye.registry.UnidyeItems;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JeiPlugin @SuppressWarnings("unused")
public class UnidyeJEIPlugin implements IModPlugin {

    private @Nullable CraftingRecipeCategory craftingCategory;

    public UnidyeJEIPlugin() {
    }

    @Override
    public @NotNull Identifier getPluginUid() {
        return Identifier.of(Unidye.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        craftingCategory = new CraftingRecipeCategory(guiHelper);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

        ErrorUtil.checkNotNull(this.craftingCategory, "craftingCategory");

        if (Unidye.POLYMORPH) {
            registration.addIngredientInfo(UnidyeItems.CUSTOM_DYE.getDefaultStack(),
                    VanillaTypes.ITEM_STACK,
                    Text.translatable("jei_description.unidye.custom_dye_polymorph"));
        } else {
            registration.addIngredientInfo(UnidyeItems.CUSTOM_DYE.getDefaultStack(),
                    VanillaTypes.ITEM_STACK,
                    Text.translatable("jei_description.unidye.custom_dye"));
        }
        registration.addIngredientInfo(UnidyeBlocks.CUSTOM_WOOL.asItem().getDefaultStack(),
                VanillaTypes.ITEM_STACK,
                Text.translatable("jei_description.unidye.custom_wool"));
        registration.addIngredientInfo(UnidyeBlocks.CUSTOM_SHULKER_BOX.asItem().getDefaultStack(),
                VanillaTypes.ITEM_STACK,
                Text.translatable("jei_description.unidye.custom_shulker_box"));
        registration.addIngredientInfo(UnidyeBlocks.CUSTOM_TERRACOTTA.asItem().getDefaultStack(),
                VanillaTypes.ITEM_STACK,
                Text.translatable("jei_description.unidye.custom_terracotta"));
        registration.addIngredientInfo(UnidyeBlocks.CUSTOM_CANDLE.asItem().getDefaultStack(),
                VanillaTypes.ITEM_STACK,
                Text.translatable("jei_description.unidye.custom_candle"));
        registration.addIngredientInfo(UnidyeBlocks.CUSTOM_CARPET.asItem().getDefaultStack(),
                VanillaTypes.ITEM_STACK,
                Text.translatable("jei_description.unidye.custom_carpet"));
        registration.addIngredientInfo(UnidyeBlocks.CUSTOM_BED.asItem().getDefaultStack(),
                VanillaTypes.ITEM_STACK,
                Text.translatable("jei_description.unidye.custom_bed"));
        registration.addIngredientInfo(UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE.asItem().getDefaultStack(),
                VanillaTypes.ITEM_STACK,
                Text.translatable("jei_description.unidye.custom_stained_glass_pane"));
        registration.addIngredientInfo(UnidyeBlocks.CUSTOM_STAINED_GLASS.asItem().getDefaultStack(),
                VanillaTypes.ITEM_STACK,
                Text.translatable("jei_description.unidye.custom_stained_glass"));
        registration.addIngredientInfo(UnidyeBlocks.CUSTOM_CONCRETE_POWDER.asItem().getDefaultStack(),
                VanillaTypes.ITEM_STACK,
                Text.translatable("jei_description.unidye.custom_concrete_powder"));
        registration.addIngredientInfo(UnidyeBlocks.CUSTOM_CONCRETE.asItem().getDefaultStack(),
                VanillaTypes.ITEM_STACK,
                Text.translatable("jei_description.unidye.custom_concrete"));
        registration.addIngredientInfo(UnidyeItems.CUSTOM_BANNER.asItem().getDefaultStack(),
                VanillaTypes.ITEM_STACK,
                Text.translatable("jei_description.unidye.custom_banner"));

//        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
//        IStackHelper stackHelper = jeiHelpers.getStackHelper();
//        IIngredientManager ingredientManager = registration.getIngredientManager();
//        VanillaRecipes vanillaRecipes = new VanillaRecipes(ingredientManager);
//        var craftingRecipes = vanillaRecipes.getCraftingRecipes(this.craftingCategory);
//        var unhandledCraftingRecipes = craftingRecipes.get(false);
//        var specialCraftingRecipes = replaceSpecialCraftingRecipes(unhandledCraftingRecipes, stackHelper);
//        registration.addRecipes(RecipeTypes.CRAFTING, specialCraftingRecipes);
    }

//    private static List replaceSpecialCraftingRecipes(List unhandledCraftingRecipes, IStackHelper stackHelper) {
//        var replacers = new IdentityHashMap<>();
//        replacers.put(CustomDyeRecipe.class, () -> CustomDyeRecipeMaker.createRecipes(stackHelper));
//
//        return unhandledCraftingRecipes.stream()
//                .map(CraftingRecipe::getClass)
//                .filter(replacers::containsKey)
//                .distinct()
//                // distinct + this limit will ensure we stop iterating early if we find all the recipes we're looking for.
//                .limit(replacers.size())
//                .flatMap(recipeClass -> {
//                    var supplier = replacers.get(recipeClass);
//                    try {
//                        return supplier.get()
//                                .stream();
//                    } catch (RuntimeException e) {
//                        Unidye.LOGGER.error("Failed to create JEI recipes for {}", recipeClass, e);
//                        return Stream.of();
//                    }
//                })
//                .toList();
//    }
}
