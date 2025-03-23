package net.diemond_player.unidye.compat.emi;

import dev.emi.emi.EmiPort;
import dev.emi.emi.EmiUtil;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.config.FluidUnit;
import dev.emi.emi.registry.EmiTags;
import dev.emi.emi.runtime.EmiReloadLog;
import net.diemond_player.unidye.block.UnidyeBlocks;
import net.diemond_player.unidye.recipes.*;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UnidyeEmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry registry) {
        Set<Item> hiddenItems = Stream.concat(
                EmiUtil.values(TagKey.of(EmiPort.getItemRegistry().getKey(), EmiTags.HIDDEN_FROM_RECIPE_VIEWERS)).map(RegistryEntry::value),
                EmiPort.getDisabledItems()
        ).collect(Collectors.toSet());

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
        addRecipeSafe(registry, () -> EmiWorldInteractionRecipe.builder()
                .id(synthetic("world/cauldron_washing", "wool"))
                .leftInput(EmiIngredient.of(ItemTags.WOOL))
                .rightInput(EmiStack.of(Items.CAULDRON), true)
                .rightInput(EmiStack.of(Fluids.WATER, FluidUnit.BOTTLE), false)
                .output(EmiStack.of(Blocks.WHITE_WOOL))
                .supportsRecipeTree(false)
                .build());
        addRecipeSafe(registry, () -> EmiWorldInteractionRecipe.builder()
                .id(synthetic("world/cauldron_washing", "candle"))
                .leftInput(EmiIngredient.of(ItemTags.CANDLES))
                .rightInput(EmiStack.of(Items.CAULDRON), true)
                .rightInput(EmiStack.of(Fluids.WATER, FluidUnit.BOTTLE), false)
                .output(EmiStack.of(Blocks.CANDLE))
                .supportsRecipeTree(false)
                .build());
        addRecipeSafe(registry, () -> EmiWorldInteractionRecipe.builder()
                .id(synthetic("world/cauldron_washing", "shulker_box"))
                .leftInput(EmiIngredient.of(ConventionalItemTags.SHULKER_BOXES))
                .rightInput(EmiStack.of(Items.CAULDRON), true)
                .rightInput(EmiStack.of(Fluids.WATER, FluidUnit.BOTTLE), false)
                .output(EmiStack.of(Blocks.SHULKER_BOX))
                .supportsRecipeTree(false)
                .build());
        addRecipeSafe(registry, () -> EmiWorldInteractionRecipe.builder()
                .id(synthetic("world/cauldron_washing", "terracotta"))
                .leftInput(EmiIngredient.of(ItemTags.TERRACOTTA))
                .rightInput(EmiStack.of(Items.CAULDRON), true)
                .rightInput(EmiStack.of(Fluids.WATER, FluidUnit.BOTTLE), false)
                .output(EmiStack.of(Blocks.TERRACOTTA))
                .supportsRecipeTree(false)
                .build());
        addRecipeSafe(registry, () -> EmiWorldInteractionRecipe.builder()
                .id(synthetic("world/cauldron_washing", "glass"))
                .leftInput(EmiIngredient.of(ConventionalItemTags.GLASS_BLOCKS))
                .rightInput(EmiStack.of(Items.CAULDRON), true)
                .rightInput(EmiStack.of(Fluids.WATER, FluidUnit.BOTTLE), false)
                .output(EmiStack.of(Blocks.GLASS))
                .supportsRecipeTree(false)
                .build());
        addRecipeSafe(registry, () -> EmiWorldInteractionRecipe.builder()
                .id(synthetic("world/cauldron_washing", "glass_pane"))
                .leftInput(EmiIngredient.of(ConventionalItemTags.GLASS_PANES))
                .rightInput(EmiStack.of(Items.CAULDRON), true)
                .rightInput(EmiStack.of(Fluids.WATER, FluidUnit.BOTTLE), false)
                .output(EmiStack.of(Blocks.GLASS_PANE))
                .supportsRecipeTree(false)
                .build());
        addRecipeSafe(registry, () -> EmiWorldInteractionRecipe.builder()
                .id(synthetic("world/cauldron_washing", "carpet"))
                .leftInput(EmiIngredient.of(ItemTags.WOOL_CARPETS))
                .rightInput(EmiStack.of(Items.CAULDRON), true)
                .rightInput(EmiStack.of(Fluids.WATER, FluidUnit.BOTTLE), false)
                .output(EmiStack.of(Blocks.WHITE_CARPET))
                .supportsRecipeTree(false)
                .build());
        addRecipeSafe(registry, () -> EmiWorldInteractionRecipe.builder()
                .id(synthetic("world/cauldron_washing", "concrete"))
                .leftInput(EmiIngredient.of(List.of(
                        EmiStack.of(Blocks.BLACK_CONCRETE),
                        EmiStack.of(Blocks.BROWN_CONCRETE),
                        EmiStack.of(Blocks.RED_CONCRETE),
                        EmiStack.of(Blocks.BLUE_CONCRETE),
                        EmiStack.of(Blocks.YELLOW_CONCRETE),
                        EmiStack.of(Blocks.LIGHT_GRAY_CONCRETE),
                        EmiStack.of(Blocks.GRAY_CONCRETE),
                        EmiStack.of(Blocks.PINK_CONCRETE),
                        EmiStack.of(Blocks.MAGENTA_CONCRETE),
                        EmiStack.of(Blocks.PURPLE_CONCRETE),
                        EmiStack.of(Blocks.LIGHT_BLUE_CONCRETE),
                        EmiStack.of(Blocks.CYAN_CONCRETE),
                        EmiStack.of(Blocks.ORANGE_CONCRETE),
                        EmiStack.of(Blocks.GREEN_CONCRETE),
                        EmiStack.of(Blocks.LIME_CONCRETE),
                        EmiStack.of(UnidyeBlocks.CUSTOM_CONCRETE)
                )))
                .rightInput(EmiStack.of(Items.CAULDRON), true)
                .rightInput(EmiStack.of(Fluids.WATER, FluidUnit.BOTTLE), false)
                .output(EmiStack.of(Blocks.WHITE_CONCRETE))
                .supportsRecipeTree(false)
                .build());
        addRecipeSafe(registry, () -> EmiWorldInteractionRecipe.builder()
                .id(synthetic("world/cauldron_washing", "bed"))
                .leftInput(EmiIngredient.of(ItemTags.BEDS))
                .rightInput(EmiStack.of(Items.CAULDRON), true)
                .rightInput(EmiStack.of(Fluids.WATER, FluidUnit.BOTTLE), false)
                .output(EmiStack.of(Blocks.WHITE_BED))
                .supportsRecipeTree(false)
                .build());
    }
    private static <C extends Inventory, T extends Recipe<C>> Iterable<T> getRecipes(EmiRegistry registry, RecipeType<T> type) {
        return registry.getRecipeManager().listAllOfType(type).stream()::iterator;
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
