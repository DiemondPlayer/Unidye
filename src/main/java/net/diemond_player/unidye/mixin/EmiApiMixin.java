package net.diemond_player.unidye.mixin;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.llamalad7.mixinextras.sugar.Local;
import dev.emi.emi.EmiPort;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.recipe.*;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.emi.emi.registry.EmiRecipes;
import dev.emi.emi.runtime.EmiHistory;
import dev.emi.emi.screen.BoMScreen;
import dev.emi.emi.screen.RecipeScreen;
import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.component.RecipeStacksComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(value = EmiApi.class, remap = false)
public abstract class EmiApiMixin {
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

    //FIXME EMI knows no diff between custom dyes
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
                        EmiStack.of(itemStack, countAmount(recipeStacksComponent)),
                        EmiPort.id("unidye", "/component_saved_recipe"), true));
                recipes.replace(VanillaEmiRecipeCategories.CRAFTING, craftingRecipeList);
            }
        }
    }

    //TODO counting for diff recipes?
    @Unique
    private static long countAmount(RecipeStacksComponent recipeStacksComponent) {
        long amount = 0;
        for(RecipeStacksComponent.Stack stack1 : recipeStacksComponent.stacks()){
            if(stack1.item().value() != Items.STICK){
                amount++;
            }
        }
        return amount;
    }
}