package net.diemond_player.unidye.registry;

import com.google.common.collect.Lists;
import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.block.DyeableShulkerBoxBlock;
import net.diemond_player.unidye.block.entity.DyeableBannerBlockEntity;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;

import java.util.ArrayList;

import static net.minecraft.block.cauldron.CauldronBehavior.WATER_CAULDRON_BEHAVIOR;
import static net.minecraft.block.cauldron.CauldronBehavior.registerBucketBehavior;

public class UnidyeCauldronBehaviors {

    public static final ArrayList<Runnable> COMPAT_CAULDRON_BEHAVIOURS = Lists.newArrayList();

    public static void registerCauldronBehaviors() {

//        COMPAT_CAULDRON_BEHAVIOURS.add(() -> registerUndyeingBehavior(ItemTags.AXES, Items.DIAMOND_AXE));

        WATER_CAULDRON_BEHAVIOR.put(UnidyeItems.CUSTOM_BANNER, CLEAN_CUSTOM_BANNER);

        WATER_CAULDRON_BEHAVIOR.put(UnidyeBlocks.CUSTOM_SHULKER_BOX.asItem(), CLEAN_CUSTOM_SHULKER_BOX);

        registerUndyeingBehavior(ItemTags.TERRACOTTA, Blocks.TERRACOTTA);

        registerUndyeingBehavior(ItemTags.WOOL, Blocks.WHITE_WOOL);

        registerUndyeingBehavior(ConventionalItemTags.GLASS_BLOCKS, Blocks.GLASS);

        registerUndyeingBehavior(ConventionalItemTags.GLASS_PANES, Blocks.GLASS_PANE);

        registerUndyeingBehavior(ItemTags.WOOL_CARPETS, Blocks.WHITE_CARPET);

        registerUndyeingBehavior(ItemTags.CANDLES, Items.CANDLE);

        registerUndyeingBehavior(
                Lists.newArrayList(UnidyeBlocks.CUSTOM_CONCRETE.asItem(),
                        Blocks.BLACK_CONCRETE.asItem(), Blocks.BROWN_CONCRETE.asItem(),
                        Blocks.RED_CONCRETE.asItem(), Blocks.BLUE_CONCRETE.asItem(),
                        Blocks.YELLOW_CONCRETE.asItem(), Blocks.LIGHT_GRAY_CONCRETE.asItem(),
                        Blocks.GRAY_CONCRETE.asItem(), Blocks.PINK_CONCRETE.asItem(),
                        Blocks.MAGENTA_CONCRETE.asItem(), Blocks.PURPLE_CONCRETE.asItem(),
                        Blocks.LIGHT_BLUE_CONCRETE.asItem(), Blocks.CYAN_CONCRETE.asItem(),
                        Blocks.ORANGE_CONCRETE.asItem(), Blocks.GREEN_CONCRETE.asItem(),
                        Blocks.LIME_CONCRETE.asItem()
                ), Blocks.WHITE_CONCRETE);

        registerUndyeingBehavior(ItemTags.BEDS, Blocks.WHITE_BED);

        if (Unidye.SIMPLE_CONCRETE) {
            WATER_CAULDRON_BEHAVIOR.put(UnidyeBlocks.CUSTOM_CONCRETE_POWDER.asItem(), HARDEN_CUSTOM_CONCRETE_POWDER);
        }

        WATER_CAULDRON_BEHAVIOR.remove(Items.TINTED_GLASS);

        for(Runnable r : COMPAT_CAULDRON_BEHAVIOURS){
            r.run();
        }
    }

    public static final CauldronBehavior CLEAN_CUSTOM_BANNER = (state, world, pos, player, hand, stack) -> {
        if (DyeableBannerBlockEntity.getPatternCount(stack) <= 0) {
            return ActionResult.PASS;
        }
        if (!world.isClient) {
            ItemStack itemStack = stack.copyWithCount(1);
            DyeableBannerBlockEntity.loadFromItemStack(itemStack);
            if (!player.getAbilities().creativeMode) {
                stack.decrement(1);
            }
            if (stack.isEmpty()) {
                player.setStackInHand(hand, itemStack);
                player.getStackInHand(player.getActiveHand());
            } else if (player.getInventory().insertStack(itemStack)) {
                player.playerScreenHandler.syncState();
            } else {
                player.dropItem(itemStack, false);
            }
            player.incrementStat(Stats.CLEAN_BANNER);
            LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
        }
        return ActionResult.success(world.isClient);
    };

    public static final CauldronBehavior CLEAN_CUSTOM_SHULKER_BOX = (state, world, pos, player, hand, stack) -> {
        Block block = Block.getBlockFromItem(stack.getItem());
        if (!(block instanceof DyeableShulkerBoxBlock)) {
            return ActionResult.PASS;
        }
        if (!world.isClient) {
            ItemStack itemStack = new ItemStack(Blocks.SHULKER_BOX);
            if (stack.hasNbt()) {
                stack.removeSubNbt("display");
                itemStack.setNbt(stack.getNbt().copy());
            }
            player.setStackInHand(hand, itemStack);
            player.incrementStat(Stats.CLEAN_SHULKER_BOX);
            LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
        }
        return ActionResult.success(world.isClient);
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
        return ActionResult.success(world.isClient);
    };

    public static void registerUndyeingBehavior(ArrayList<Item> acceptedItems, ItemConvertible outputItem) {
        for (Item item : acceptedItems) {
            if (item == outputItem.asItem()) continue;
            WATER_CAULDRON_BEHAVIOR.put(item, (state, world, pos, player, hand, stack) -> {
                if (!world.isClient) {
                    ItemStack itemStack = new ItemStack(outputItem);
                    itemStack.setCount(stack.getCount());
                    player.setStackInHand(hand, itemStack);
                    player.incrementStat(Stats.USE_CAULDRON);
                    LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
                }
                return ActionResult.success(world.isClient);
            });
        }
    }

    public static void registerUndyeingBehavior(TagKey<Item> tag, ItemConvertible outputItem) {
        ArrayList<Item> acceptedItems = new ArrayList<>(Registries.ITEM.getOrCreateEntryList(tag).stream().map(RegistryEntry::value).toList());
        registerUndyeingBehavior(acceptedItems, outputItem);
    }
}
