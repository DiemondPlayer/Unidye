package net.diemond_player.unidye.compat.emi;

import dev.emi.emi.EmiPort;
import dev.emi.emi.EmiUtil;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.config.FluidUnit;
import dev.emi.emi.runtime.EmiReloadLog;
import net.diemond_player.unidye.recipe.*;
import net.diemond_player.unidye.registry.UnidyeBlocks;
import net.diemond_player.unidye.registry.UnidyeCauldronBehaviors;
import net.diemond_player.unidye.util.EmiCauldronRecipeData;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("SameParameterValue")
public class UnidyeEmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {

        registry.removeRecipes(emiRecipe -> {
            if(emiRecipe.getCategory() != VanillaEmiRecipeCategories.WORLD_INTERACTION) return false;
            if(emiRecipe.getId() == null) return false;
            return emiRecipe.getId().getPath().contains("/world/cauldron_washing/unidye/custom_");
        });

        for (CraftingRecipe recipe : getRecipes(registry, RecipeType.CRAFTING)) {
            Identifier id = EmiPort.getId(recipe);
            if (recipe instanceof CustomDyeRecipe) {
                addRecipeSafe(registry, () -> new EmiCustomDyeRecipe(id), recipe);
            } else if(recipe instanceof CustomCircleDyeingRecipe customCircleDyeingRecipe){
                if(customCircleDyeingRecipe.itemTag != null) {
                    addRecipeSafe(registry, () -> new EmiCustomCircleDyeingRecipe(customCircleDyeingRecipe.itemTag, customCircleDyeingRecipe.outputItem, id), recipe);
                }else if(customCircleDyeingRecipe.acceptedItems != null){
                    addRecipeSafe(registry, () -> new EmiCustomCircleDyeingRecipe(customCircleDyeingRecipe.acceptedItems, customCircleDyeingRecipe.outputItem, id), recipe);
                }
            } else if(recipe instanceof CustomStainedGlassPaneRecipe) {
                addRecipeSafe(registry, () -> new EmiCustomStainedGlassPaneRecipe(id), recipe);
            } else if(recipe instanceof CustomCarpetRecipe) {
                addRecipeSafe(registry, () -> new EmiCustomCarpetRecipe(id), recipe);
            } else if(recipe instanceof CustomBedRecipe) {
                addRecipeSafe(registry, () -> new EmiCustomBedRecipe(id), recipe);
            } else if(recipe instanceof CustomConcretePowderRecipe) {
                addRecipeSafe(registry, () -> new EmiCustomConcretePowderRecipe(id), recipe);
            } else if(recipe instanceof CustomBannerRecipe) {
                addRecipeSafe(registry, () -> new EmiCustomBannerRecipe(id), recipe);
            } else if (recipe instanceof CustomBannerDuplicateRecipe) {
                addRecipeSafe(registry, () -> new EmiCustomBannerDuplicateRecipe(id), recipe);
            } else if (recipe instanceof CustomBedDyeingRecipe) {
                addRecipeSafe(registry, () -> new EmiCustomSingleDyeingRecipe(ItemTags.BEDS, UnidyeBlocks.CUSTOM_BED.asItem(), id), recipe);
            } else if (recipe instanceof CustomShulkerBoxDyeingRecipe) {
                addRecipeSafe(registry, () -> new EmiCustomSingleDyeingRecipe(ConventionalItemTags.SHULKER_BOXES, UnidyeBlocks.CUSTOM_SHULKER_BOX.asItem(), id), recipe);
            } else if (recipe instanceof CustomShieldDecorationRecipe) {
                addRecipeSafe(registry, () -> new EmiCustomShieldDecorationRecipe(id), recipe);
            }
        }
        addConcreteRecipe(registry, UnidyeBlocks.CUSTOM_CONCRETE_POWDER, EmiStack.of(Fluids.WATER), UnidyeBlocks.CUSTOM_CONCRETE);
        for(EmiCauldronRecipeData emiCauldronRecipeData : UnidyeCauldronBehaviors.EMI_CAULDRON_RECIPE_DATA){
            if(emiCauldronRecipeData.tag != null) {
                addRecipeSafe(registry, () -> EmiWorldInteractionRecipe.builder()
                        .id(synthetic("world/cauldron_washing", emiCauldronRecipeData.name))
                        .leftInput(EmiIngredient.of(emiCauldronRecipeData.tag))
                        .rightInput(EmiStack.of(Items.CAULDRON), true)
                        .rightInput(EmiStack.of(Fluids.WATER, FluidUnit.BOTTLE), false)
                        .output(EmiStack.of(emiCauldronRecipeData.outputItem))
                        .supportsRecipeTree(false)
                        .build());
            }else{
                addRecipeSafe(registry, () -> EmiWorldInteractionRecipe.builder()
                        .id(synthetic("world/cauldron_washing", emiCauldronRecipeData.name))
                        .leftInput(EmiIngredient.of(emiCauldronRecipeData.acceptedItems.stream().filter(item -> item != emiCauldronRecipeData.outputItem.asItem()).map(EmiStack::of).toList()))
                        .rightInput(EmiStack.of(Items.CAULDRON), true)
                        .rightInput(EmiStack.of(Fluids.WATER, FluidUnit.BOTTLE), false)
                        .output(EmiStack.of(emiCauldronRecipeData.outputItem))
                        .supportsRecipeTree(false)
                        .build());
            }
        }
    }

    private static <C extends RecipeInput, T extends Recipe<C>> Iterable<T> getRecipes(EmiRegistry registry, RecipeType<T> type) {
        return registry.getRecipeManager().listAllOfType(type).stream().map(RecipeEntry::value)::iterator;
    }
    private static void addRecipeSafe(EmiRegistry registry, Supplier<EmiRecipe> supplier, Recipe<?> recipe) {
        try {
            registry.addRecipe(supplier.get());
        } catch (Throwable e) {
            EmiReloadLog.warn("Exception thrown when parsing unidye recipe " + EmiPort.getId(recipe));
            EmiReloadLog.error(e);
        }
    }
    private static void addConcreteRecipe(EmiRegistry registry, Block powder, EmiStack water, Block result) {
        addRecipeSafe(registry, () -> basicWorld(EmiStack.of(powder), water, EmiStack.of(result),
                synthetic("world/concrete", EmiUtil.subId(result))));
    }
    private static Identifier synthetic(String type, String name) {
        return EmiPort.id("unidye", "/" + type + "/" + name);
    }
    private static EmiRecipe basicWorld(EmiIngredient left, EmiIngredient right, EmiStack output, Identifier id) {
        return basicWorld(left, right, output, id, true);
    }

    private static EmiRecipe basicWorld(EmiIngredient left, EmiIngredient right, EmiStack output, Identifier id, boolean catalyst) {
        return EmiWorldInteractionRecipe.builder()
                .id(id)
                .leftInput(left)
                .rightInput(right, catalyst)
                .output(output)
                .build();
    }
    private static void addRecipeSafe(EmiRegistry registry, Supplier<EmiRecipe> supplier) {
        try {
            registry.addRecipe(supplier.get());
        } catch (Throwable e) {
            EmiReloadLog.warn("Exception thrown when parsing EMI recipe (no ID available)");
            EmiReloadLog.error(e);
        }
    }
}
