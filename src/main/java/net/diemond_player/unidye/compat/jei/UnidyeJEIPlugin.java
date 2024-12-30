package net.diemond_player.unidye.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.registration.IRecipeRegistration;
import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.block.UnidyeBlocks;
import net.diemond_player.unidye.item.UnidyeItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.NotNull;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

@JeiPlugin
public class UnidyeJEIPlugin implements IModPlugin {
    @Override
    public @NotNull Identifier getPluginUid() {
        return Identifier.of(Unidye.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if(Unidye.POLYMORPH) {
            registration.addIngredientInfo(UnidyeItems.CUSTOM_DYE.getDefaultStack(),
                    VanillaTypes.ITEM_STACK,
                    Text.translatable("jei_description.unidye.custom_dye_polymorph"));
        }else{
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
    }
}
