package net.diemond_player.unidye.mixin.compat.emi;

import dev.emi.emi.EmiPort;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.registry.EmiRecipes;
import net.diemond_player.unidye.UnidyeClient;
import net.diemond_player.unidye.component.ItemNameAffixesComponent;
import net.diemond_player.unidye.component.RecipeStacksComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.util.DyeData;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(value = EmiApi.class, remap = false)
public abstract class EmiApiMixin{

    @Shadow
    private static void push() {
    }

    @Shadow
    private static Map<EmiRecipeCategory, List<EmiRecipe>> mapRecipes(List<EmiRecipe> list) {
        return Map.of();
    }

    @Shadow
    private static List<EmiRecipe> pruneUses(List<EmiRecipe> list, EmiIngredient context) {
        return list;
    }

    @Shadow
    public static EmiRecipeManager getRecipeManager() {
        return null;
    }

    @Inject(method = "setPages", at = @At(value = "INVOKE", target = "Ldev/emi/emi/runtime/EmiSidebars;lookup(Ldev/emi/emi/api/stack/EmiIngredient;)V"))
    private static void unidye$displayRecipes(Map<EmiRecipeCategory, List<EmiRecipe>> recipes, EmiIngredient stack, CallbackInfo ci) {
        EmiStack zero = stack.getEmiStacks().get(0);
        Map<EmiRecipeCategory, List<EmiRecipe>> map
                = mapRecipes(Stream.concat(
                pruneUses(getRecipeManager().getRecipesByInput(zero), stack).stream(),
                EmiRecipes.byWorkstation.getOrDefault(zero, List.of()).stream()).distinct().toList());
//        ClientPlayNetworking.send(new RequestDatabasePayload(true));
//        Unidye.LOGGER.info("sent a request");
        if(!map.equals(recipes)) {
            ItemStack itemStack = zero.getItemStack();
            RecipeStacksComponent recipeStacksComponent = itemStack.getOrDefault(UnidyeDataComponentTypes.RECIPE_STACKS, RecipeStacksComponent.DEFAULT);
            if (recipeStacksComponent != RecipeStacksComponent.DEFAULT) {
                push();
                List<EmiRecipe> craftingRecipeList = recipes.get(VanillaEmiRecipeCategories.CRAFTING);
                List<ItemStack> itemStacks = recipeStacksComponent.toItemStacks();
                HashMap<Integer, DyeData> database = UnidyeClient.database;
//                Unidye.LOGGER.info("tried accessing database");
                if (database != null) {
                    for (ItemStack itemStack1 : itemStacks) {
                        if (!itemStack1.contains(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES)) continue;
                        ItemNameAffixesComponent itemNameAffixesComponent = itemStack1.get(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES);
                        int color = itemNameAffixesComponent.sourceCustomDyeColor();
                        //Unidye.LOGGER.info(serverState.database.toString());
                        if (database.containsKey(color)) {
                            DyeData dyeData = database.get(color);
                            Text prefix = Text.literal(dyeData.getPrefix());
                            Text suffix = Text.literal(dyeData.getSuffix());
                            if(!itemNameAffixesComponent.prefix().equals(prefix) || !itemNameAffixesComponent.suffix().equals(suffix)) {
                                itemStack1.set(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, new ItemNameAffixesComponent(prefix, suffix, color));
                            }
                            itemStack.set(DataComponentTypes.PROFILE, dyeData.getProfileComponent());
                        } else {
                            itemStack1.set(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, itemNameAffixesComponent.noAffixes());
                        }
                    }
                } else {
//                    Unidye.LOGGER.info("it's null");
                }
                craftingRecipeList.add(0, new EmiCraftingRecipe(itemStacks.stream().map(i -> (EmiIngredient) EmiStack.of(i)).collect(Collectors.toList()),
                        EmiStack.of(itemStack, recipeStacksComponent.outputAmount()),
                        EmiPort.id("unidye", "/component_saved_recipe"), recipeStacksComponent.shapeless()));
                recipes.replace(VanillaEmiRecipeCategories.CRAFTING, craftingRecipeList);
            }
        }
    }
}