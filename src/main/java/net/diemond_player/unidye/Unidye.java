package net.diemond_player.unidye;

import com.google.common.collect.Lists;
import net.diemond_player.unidye.block.UnidyeBlocks;
import net.diemond_player.unidye.block.entity.UnidyeBlockEntities;
import net.diemond_player.unidye.command.UnidyeDyesCommand;
import net.diemond_player.unidye.command.UnidyeRandomCommand;
import net.diemond_player.unidye.item.UnidyeItemGroups;
import net.diemond_player.unidye.item.UnidyeItems;
import net.diemond_player.unidye.recipes.UnidyeSpecialRecipes;
import net.diemond_player.unidye.util.UnidyeCauldronBehaviors;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Unidye implements ModInitializer {

    public static final ArrayList<Item> STONECUTTER_PRESERVE_COLOR = Lists.newArrayList();

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

        CommandRegistrationCallback.EVENT.register(UnidyeDyesCommand::register);
        CommandRegistrationCallback.EVENT.register(UnidyeRandomCommand::register);
    }
}