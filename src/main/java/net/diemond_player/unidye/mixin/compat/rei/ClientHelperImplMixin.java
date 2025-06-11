package net.diemond_player.unidye.mixin.compat.rei;

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
import net.diemond_player.unidye.UnidyeClient;
import net.diemond_player.unidye.component.ItemNameAffixesComponent;
import net.diemond_player.unidye.component.RecipeStacksComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.util.DyeData;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

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
                        HashMap<Integer, DyeData> database = UnidyeClient.database;
//                        Unidye.LOGGER.info("tried accessing database");
                        for(ItemStack itemStack1 : recipeStacksComponent.toItemStacks()){
                            if (database != null) {
                                if (itemStack1.contains(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES)) {
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
//                                Unidye.LOGGER.info("it's null");
                            }
                            recipeInputs.add(EntryIngredient.of(EntryStack.of(VanillaEntryTypes.ITEM,itemStack1)));
                        }
                        DefaultCraftingDisplay<Recipe<?>> artificialCraftingRecipe = new DefaultCraftingDisplay<>(recipeInputs, List.of(EntryIngredient.of(EntryStack.of(VanillaEntryTypes.ITEM, itemStack.copyWithCount(recipeStacksComponent.outputAmount())))), Optional.empty()) {
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
