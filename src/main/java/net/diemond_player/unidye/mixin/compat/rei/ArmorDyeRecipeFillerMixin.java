package net.diemond_player.unidye.mixin.compat.rei;

import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.plugin.client.categories.crafting.filler.ArmorDyeRecipeFiller;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomShapelessDisplay;
import net.diemond_player.unidye.registry.UnidyeItems;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.List;

@Mixin(value = ArmorDyeRecipeFiller.class, remap = false)
public abstract class ArmorDyeRecipeFillerMixin {
    @SuppressWarnings("unchecked")
    @ModifyArg(method = "apply(Lnet/minecraft/recipe/RecipeEntry;)Ljava/util/Collection;", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    public <E> E unidye$apply(E e){
        if(e instanceof DefaultCustomShapelessDisplay recipe) {
            EntryStack<?> entryStack = recipe.getOutputEntries().get(0).get(0);
            if(entryStack.getValue() instanceof ItemStack itemStack) {
                if(itemStack.isOf(UnidyeItems.CUSTOM_DYE)) {
                    itemStack = itemStack.copyWithCount(recipe.getInputEntries().size());
                    return (E) new DefaultCustomShapelessDisplay(recipe.getOptionalRecipe().get(),
                            recipe.getInputEntries(), List.of(EntryIngredients.of(itemStack)));
                }
            }
        }
        return e;
    }
}
