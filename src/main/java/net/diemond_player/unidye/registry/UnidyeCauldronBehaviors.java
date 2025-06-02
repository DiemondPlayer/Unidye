package net.diemond_player.unidye.registry;

import com.google.common.collect.Lists;
import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.block.DyeableShulkerBoxBlock;
import net.diemond_player.unidye.component.CustomBannerPatternsComponent;
import net.diemond_player.unidye.util.EmiCauldronRecipeData;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.stat.Stats;
import net.minecraft.util.ItemActionResult;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.block.cauldron.CauldronBehavior.WATER_CAULDRON_BEHAVIOR;

public class UnidyeCauldronBehaviors {

    public static final ArrayList<Runnable> COMPAT_CAULDRON_BEHAVIOURS = Lists.newArrayList();
    public static final ArrayList<EmiCauldronRecipeData> EMI_CAULDRON_RECIPE_DATA = Lists.newArrayList(
            new EmiCauldronRecipeData("minecraft/wool", ItemTags.WOOL, Blocks.WHITE_WOOL),
            new EmiCauldronRecipeData("minecraft/candle", ItemTags.CANDLES, Items.CANDLE),
            new EmiCauldronRecipeData("minecraft/shulker_box", ConventionalItemTags.SHULKER_BOXES, Blocks.SHULKER_BOX),
            new EmiCauldronRecipeData("minecraft/terracotta", ItemTags.TERRACOTTA, Blocks.TERRACOTTA),
            new EmiCauldronRecipeData("minecraft/glass", ConventionalItemTags.GLASS_BLOCKS_CHEAP, Blocks.GLASS),
            new EmiCauldronRecipeData("minecraft/glass_pane", ConventionalItemTags.GLASS_PANES, Blocks.GLASS_PANE),
            new EmiCauldronRecipeData("minecraft/carpet", ItemTags.WOOL_CARPETS, Blocks.WHITE_CARPET),
            new EmiCauldronRecipeData("minecraft/concrete", UnidyeTags.CONCRETES, Blocks.WHITE_CONCRETE),
            new EmiCauldronRecipeData("minecraft/bed", ItemTags.BEDS, Blocks.WHITE_BED)
            //new EmiCauldronRecipeData("minecraft/axes", ItemTags.AXES, Items.DIAMOND_AXE)
    );

    public static void registerCauldronBehaviors() {

        //COMPAT_CAULDRON_BEHAVIOURS.add(() -> registerUndyeingBehavior(ItemTags.AXES, Items.DIAMOND_AXE));

        WATER_CAULDRON_BEHAVIOR.map().put(UnidyeItems.CUSTOM_BANNER, CLEAN_CUSTOM_BANNER);

        WATER_CAULDRON_BEHAVIOR.map().put(UnidyeBlocks.CUSTOM_SHULKER_BOX.asItem(), CLEAN_CUSTOM_SHULKER_BOX);

        registerUndyeingBehavior(ItemTags.TERRACOTTA, Blocks.TERRACOTTA);

        registerUndyeingBehavior(ItemTags.WOOL, Blocks.WHITE_WOOL);

        registerUndyeingBehavior(ConventionalItemTags.GLASS_BLOCKS_CHEAP, Blocks.GLASS);

        registerUndyeingBehavior(ConventionalItemTags.GLASS_PANES, Blocks.GLASS_PANE);

        registerUndyeingBehavior(ItemTags.WOOL_CARPETS, Blocks.WHITE_CARPET);

        registerUndyeingBehavior(ItemTags.CANDLES, Items.CANDLE);

        registerUndyeingBehavior(UnidyeTags.CONCRETES, Blocks.WHITE_CONCRETE);

        registerUndyeingBehavior(ItemTags.BEDS, Blocks.WHITE_BED);

        if (Unidye.SIMPLE_CONCRETE) {
            WATER_CAULDRON_BEHAVIOR.map().put(UnidyeBlocks.CUSTOM_CONCRETE_POWDER.asItem(), HARDEN_CUSTOM_CONCRETE_POWDER);
        }

        for(Runnable r : COMPAT_CAULDRON_BEHAVIOURS){
            r.run();
        }
    }

    public static final CauldronBehavior CLEAN_CUSTOM_BANNER = (state, world, pos, player, hand, stack) -> {
        CustomBannerPatternsComponent bannerPatternsComponent = stack.getOrDefault(UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS, CustomBannerPatternsComponent.DEFAULT);
        if (bannerPatternsComponent.layers().isEmpty()) {
            return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        } else {
            if (!world.isClient) {
                ItemStack itemStack = stack.copyWithCount(1);
                itemStack.set(UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS, bannerPatternsComponent.withoutTopLayer());
                stack.decrementUnlessCreative(1, player);
                if (stack.isEmpty()) {
                    player.setStackInHand(hand, itemStack);
                } else if (player.getInventory().insertStack(itemStack)) {
                    player.playerScreenHandler.syncState();
                } else {
                    player.dropItem(itemStack, false);
                }

                player.incrementStat(Stats.CLEAN_BANNER);
                LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
            }

            return ItemActionResult.success(world.isClient);
        }
    };

    public static final CauldronBehavior CLEAN_CUSTOM_SHULKER_BOX = (state, world, pos, player, hand, stack) -> {
        Block block = Block.getBlockFromItem(stack.getItem());
        if (!(block instanceof DyeableShulkerBoxBlock)) {
            return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (!world.isClient) {
            ItemStack itemStack = new ItemStack(Blocks.SHULKER_BOX);
            stack.remove(DataComponentTypes.DYED_COLOR);
            player.setStackInHand(hand, itemStack);
            player.incrementStat(Stats.CLEAN_SHULKER_BOX);
            LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
        }
        return ItemActionResult.success(world.isClient);
    };

    public static final CauldronBehavior HARDEN_CUSTOM_CONCRETE_POWDER = (state, world, pos, player, hand, stack) -> {
        if (!world.isClient) {
            ItemStack itemStack = new ItemStack(UnidyeBlocks.CUSTOM_CONCRETE);
            itemStack.setCount(stack.getCount());
            UnidyeUtils.setColor(itemStack, UnidyeUtils.getColor(stack));
            player.setStackInHand(hand, itemStack);
            player.incrementStat(Stats.USE_CAULDRON);
            LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
        }
        return ItemActionResult.success(world.isClient);
    };

    public static void registerUndyeingBehavior(ArrayList<Item> acceptedItems, ItemConvertible outputItem) {
        for (Item item : acceptedItems) {
            if (item == outputItem.asItem()) continue;
            WATER_CAULDRON_BEHAVIOR.map().put(item, (state, world, pos, player, hand, stack) -> {
                if (!world.isClient) {
                    ItemStack itemStack = new ItemStack(outputItem);
                    itemStack.setCount(stack.getCount());
                    player.setStackInHand(hand, itemStack);
                    player.incrementStat(Stats.USE_CAULDRON);
                    LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
                }
                return ItemActionResult.success(world.isClient);
            });
        }
    }

    public static void registerUndyeingBehavior(TagKey<Item> tag, ItemConvertible outputItem) {
        ArrayList<Item> acceptedItems = new ArrayList<>(Registries.ITEM.getOrCreateEntryList(tag).stream().map(RegistryEntry::value).toList());
        registerUndyeingBehavior(acceptedItems, outputItem);
    }
}
