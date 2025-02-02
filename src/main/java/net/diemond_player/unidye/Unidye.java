package net.diemond_player.unidye;

import net.diemond_player.unidye.block.UnidyeBlocks;
import net.diemond_player.unidye.block.entity.UnidyeBlockEntities;
import net.diemond_player.unidye.component.UnidyeDataComponentTypes;
import net.diemond_player.unidye.entity.UnidyeEntities;
import net.diemond_player.unidye.item.UnidyeItemGroups;
import net.diemond_player.unidye.item.UnidyeItems;
import net.diemond_player.unidye.recipes.UnidyeSpecialRecipes;
import net.diemond_player.unidye.util.UnidyeCauldronBehaviors;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Unidye implements ModInitializer {

    public static final boolean POLYMORPH = FabricLoader.getInstance().isModLoaded("polymorph");
    public static final boolean SIMPLE_CONCRETE = FabricLoader.getInstance().isModLoaded("simpleconcrete");
    public static final String MOD_ID = "unidye";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        UnidyeItems.registerModItems();
        UnidyeItemGroups.registerItemGroups();
        UnidyeBlocks.registerModBlocks();
        UnidyeBlockEntities.registerBlockEntities();
        UnidyeCauldronBehaviors.registerCauldronBehaviors();
        UnidyeSpecialRecipes.registerSpecialRecipes();
        UnidyeDataComponentTypes.registerModDataComponentTypes();
        UnidyeEntities.registerModEntities();
    }
}