package net.diemond_player.unidye.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.view.ViewSearchBuilder;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.impl.client.ClientHelperImpl;
import me.shedaniel.rei.impl.common.entry.TypedEntryStack;
import me.shedaniel.rei.impl.display.DisplaySpec;
import me.shedaniel.rei.plugin.client.categories.crafting.DefaultCraftingCategory;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCraftingDisplay;
import net.diemond_player.unidye.component.RecipeStacksComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mixin(value = ClientHelperImpl.class, remap = false)
public abstract class ClientHelperImplMixin {
    @Inject(method = "openView", at = @At(value = "INVOKE", target = "Lme/shedaniel/rei/api/client/config/ConfigObject;getInstance()Lme/shedaniel/rei/api/client/config/ConfigObject;", ordinal = 1), cancellable = true)
    public void unidye$openView(ViewSearchBuilder builder, CallbackInfoReturnable<Boolean> cir, @Local Map<DisplayCategory<?>, List<DisplaySpec>> map) {
        for(EntryStack<?> entryStack : builder.getRecipesFor()){
            if(entryStack instanceof TypedEntryStack<?> typedEntryStack){
                if(typedEntryStack.getValue() instanceof ItemStack itemStack){
                    if(itemStack.contains(UnidyeDataComponentTypes.RECIPE_STACKS)){
                        RecipeStacksComponent recipeStacksComponent = itemStack.get(UnidyeDataComponentTypes.RECIPE_STACKS);

                        List<DisplaySpec> craftingDisplays = map.get(new DefaultCraftingCategory());
                        List<EntryIngredient> recipeInputs = new ArrayList<>(List.of());
                        for(ItemStack itemStack1 : recipeStacksComponent.toItemStacks()){
                            recipeInputs.add(EntryIngredient.of(EntryStack.of(VanillaEntryTypes.ITEM,itemStack1)));
                        }
                        DefaultCraftingDisplay artificialCraftingRecipe = new DefaultCraftingDisplay<>(recipeInputs, List.of(EntryIngredient.of(EntryStack.of(VanillaEntryTypes.ITEM, itemStack.copyWithCount(recipeStacksComponent.outputAmount())))), Optional.empty()) {
                            @Override
                            public boolean isShapeless() {
                                return recipeStacksComponent.shapeless();
                            }

                            @Override
                            public int getWidth() {
                                return 3;
                            }

                            @Override
                            public int getHeight() {
                                return 3;
                            }
                        };
                        craftingDisplays.add(0, artificialCraftingRecipe);
                    }
                }
            }
        }
    }
}
