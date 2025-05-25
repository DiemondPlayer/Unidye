package net.diemond_player.unidye.mixin;

import dev.emi.emi.EmiPort;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.registry.EmiRecipes;
import net.diemond_player.unidye.component.RecipeStacksComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(value = EmiApi.class, remap = false)
public abstract class EmiApiMixin{
    @Shadow @Final private static MinecraftClient client;

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
        if(!map.equals(recipes)) {
            ItemStack itemStack = zero.getItemStack();
            RecipeStacksComponent recipeStacksComponent = itemStack.getOrDefault(UnidyeDataComponentTypes.RECIPE_STACKS, RecipeStacksComponent.DEFAULT);
            if (recipeStacksComponent != RecipeStacksComponent.DEFAULT) {
                push();
                List<EmiRecipe> craftingRecipeList = recipes.get(VanillaEmiRecipeCategories.CRAFTING);
                craftingRecipeList.add(0, new EmiCraftingRecipe(recipeStacksComponent.toItemStacks().stream().map(i -> (EmiIngredient) EmiStack.of(i)).collect(Collectors.toList()),
                        EmiStack.of(itemStack, recipeStacksComponent.outputAmount()),
                        EmiPort.id("unidye", "/component_saved_recipe"), recipeStacksComponent.shapeless()));
                recipes.replace(VanillaEmiRecipeCategories.CRAFTING, craftingRecipeList);
            }
        }
    }
}