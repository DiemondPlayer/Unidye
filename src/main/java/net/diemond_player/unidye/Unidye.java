package net.diemond_player.unidye;

import com.google.common.collect.Lists;
import net.diemond_player.unidye.command.*;
import net.diemond_player.unidye.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


public class Unidye implements ModInitializer {

    public static final ArrayList<Item> STONECUTTER_PRESERVE_COLOR = Lists.newArrayList();

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