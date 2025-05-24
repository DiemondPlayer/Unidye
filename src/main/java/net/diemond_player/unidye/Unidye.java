package net.diemond_player.unidye;

import com.google.common.collect.Lists;
import com.ibm.icu.impl.Pair;
import net.diemond_player.unidye.command.*;
import net.diemond_player.unidye.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public class Unidye implements ModInitializer {

    public static final ArrayList<Item> STONECUTTER_PRESERVE_COLOR = Lists.newArrayList();
    public static final ArrayList<Pair<ArrayList<Item>, ItemConvertible>> REI_ARRAY_DRIVEN_CIRCLE_DYEING_RECIPE_PARAMETERS = Lists.newArrayList();
    public static final ArrayList<Pair<TagKey<Item>, ItemConvertible>> REI_TAG_DRIVEN_CIRCLE_DYEING_RECIPE_PARAMETERS = Lists.newArrayList(
            Pair.of(ConventionalItemTags.GLASS_BLOCKS, UnidyeBlocks.CUSTOM_STAINED_GLASS),
            Pair.of(ConventionalItemTags.GLASS_PANES, UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE),
            Pair.of(ItemTags.CANDLES, UnidyeBlocks.CUSTOM_CANDLE),
            Pair.of(ItemTags.WOOL_CARPETS, UnidyeBlocks.CUSTOM_CARPET),
            Pair.of(ItemTags.WOOL, UnidyeBlocks.CUSTOM_WOOL),
            Pair.of(ItemTags.TERRACOTTA, UnidyeBlocks.CUSTOM_TERRACOTTA)
    );

    public static final boolean POLYMORPH = isModLoaded("polymorph");
    public static final boolean SIMPLE_CONCRETE = isModLoaded("simpleconcrete");
    public static final String MOD_ID = "unidye";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        UnidyeItems.registerModItems();
        UnidyeItemGroups.registerItemGroups();
        UnidyeBlocks.registerModBlocks();
        UnidyeBlockEntities.registerBlockEntities();
//        UnidyeCauldronBehaviors.registerCauldronBehaviors();
        UnidyeSpecialRecipes.registerSpecialRecipes();
        UnidyeMaterialTypes.registerMaterialTypes();
        UnidyeDataComponentTypes.registerModDataComponentTypes();

        addItemsToMaterialTypes();
        registerCommands();
    }

    private void addItemsToMaterialTypes() {
        UnidyeMaterialTypes.addItemToMaterialType(UnidyeBlocks.CUSTOM_WOOL.asItem(), UnidyeMaterialTypes.WOOL);
        UnidyeMaterialTypes.addItemToMaterialType(UnidyeBlocks.CUSTOM_CARPET.asItem(), UnidyeMaterialTypes.WOOL);
        UnidyeMaterialTypes.addItemToMaterialType(UnidyeBlocks.CUSTOM_TERRACOTTA.asItem(), UnidyeMaterialTypes.TERRACOTTA);
        UnidyeMaterialTypes.addItemToMaterialType(UnidyeBlocks.CUSTOM_CONCRETE.asItem(), UnidyeMaterialTypes.CONCRETE);
        UnidyeMaterialTypes.addItemToMaterialType(UnidyeBlocks.CUSTOM_CONCRETE_POWDER.asItem(), UnidyeMaterialTypes.CONCRETE);
        UnidyeMaterialTypes.addItemToMaterialType(UnidyeBlocks.CUSTOM_STAINED_GLASS.asItem(), UnidyeMaterialTypes.GLASS);
        UnidyeMaterialTypes.addItemToMaterialType(UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE.asItem(), UnidyeMaterialTypes.GLASS);
        UnidyeMaterialTypes.addItemToMaterialType(UnidyeItems.CUSTOM_DYE, UnidyeMaterialTypes.DYE);
        UnidyeMaterialTypes.addItemToMaterialType(UnidyeBlocks.CUSTOM_SHULKER_BOX.asItem(), UnidyeMaterialTypes.SHULKER_BOX);
        UnidyeMaterialTypes.addItemToMaterialType(UnidyeBlocks.CUSTOM_CANDLE.asItem(), UnidyeMaterialTypes.CANDLE);
    }

    private void registerCommands(){
        CommandRegistrationCallback.EVENT.register(UnidyeDyesCommand::register);
        CommandRegistrationCallback.EVENT.register(UnidyeRandomCommand::register);
        CommandRegistrationCallback.EVENT.register(UnidyeColorizeCommand::register);
        CommandRegistrationCallback.EVENT.register(UnidyeVanillifyCommand::register);
        CommandRegistrationCallback.EVENT.register(UnidyeNameCommand::register);
    }

    public static boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }
}