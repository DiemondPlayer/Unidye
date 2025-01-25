package net.diemond_player.unidye.util;

import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.block.UnidyeBlocks;
import net.diemond_player.unidye.block.custom.DyeableShulkerBoxBlock;
import net.diemond_player.unidye.block.entity.DyeableBannerBlockEntity;
import net.diemond_player.unidye.component.CustomBannerPatternsComponent;
import net.diemond_player.unidye.component.UnidyeDataComponentTypes;
import net.diemond_player.unidye.item.UnidyeItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemActionResult;

import static net.minecraft.block.cauldron.CauldronBehavior.WATER_CAULDRON_BEHAVIOR;

public class UnidyeCauldronBehaviors {
    public static void registerCauldronBehaviors() {
        WATER_CAULDRON_BEHAVIOR.map().put(UnidyeItems.CUSTOM_BANNER, CLEAN_CUSTOM_BANNER);

        WATER_CAULDRON_BEHAVIOR.map().put(UnidyeBlocks.CUSTOM_SHULKER_BOX.asItem(), CLEAN_CUSTOM_SHULKER_BOX);

        WATER_CAULDRON_BEHAVIOR.map().put(UnidyeBlocks.CUSTOM_TERRACOTTA.asItem(), CLEAN_TERRACOTTA);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BLACK_TERRACOTTA.asItem(), CLEAN_TERRACOTTA);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BROWN_TERRACOTTA.asItem(), CLEAN_TERRACOTTA);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.RED_TERRACOTTA.asItem(), CLEAN_TERRACOTTA);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BLUE_TERRACOTTA.asItem(), CLEAN_TERRACOTTA);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.YELLOW_TERRACOTTA.asItem(), CLEAN_TERRACOTTA);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.WHITE_TERRACOTTA.asItem(), CLEAN_TERRACOTTA);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIGHT_GRAY_TERRACOTTA.asItem(), CLEAN_TERRACOTTA);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.GRAY_TERRACOTTA.asItem(), CLEAN_TERRACOTTA);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.PINK_TERRACOTTA.asItem(), CLEAN_TERRACOTTA);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.MAGENTA_TERRACOTTA.asItem(), CLEAN_TERRACOTTA);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.PURPLE_TERRACOTTA.asItem(), CLEAN_TERRACOTTA);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIGHT_BLUE_TERRACOTTA.asItem(), CLEAN_TERRACOTTA);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.CYAN_TERRACOTTA.asItem(), CLEAN_TERRACOTTA);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.ORANGE_TERRACOTTA.asItem(), CLEAN_TERRACOTTA);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.GREEN_TERRACOTTA.asItem(), CLEAN_TERRACOTTA);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIME_TERRACOTTA.asItem(), CLEAN_TERRACOTTA);

        WATER_CAULDRON_BEHAVIOR.map().put(UnidyeBlocks.CUSTOM_WOOL.asItem(), CLEAN_WOOL);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BLACK_WOOL.asItem(), CLEAN_WOOL);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BROWN_WOOL.asItem(), CLEAN_WOOL);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.RED_WOOL.asItem(), CLEAN_WOOL);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BLUE_WOOL.asItem(), CLEAN_WOOL);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.YELLOW_WOOL.asItem(), CLEAN_WOOL);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIGHT_GRAY_WOOL.asItem(), CLEAN_WOOL);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.GRAY_WOOL.asItem(), CLEAN_WOOL);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.PINK_WOOL.asItem(), CLEAN_WOOL);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.MAGENTA_WOOL.asItem(), CLEAN_WOOL);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.PURPLE_WOOL.asItem(), CLEAN_WOOL);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIGHT_BLUE_WOOL.asItem(), CLEAN_WOOL);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.CYAN_WOOL.asItem(), CLEAN_WOOL);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.ORANGE_WOOL.asItem(), CLEAN_WOOL);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.GREEN_WOOL.asItem(), CLEAN_WOOL);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIME_WOOL.asItem(), CLEAN_WOOL);

        WATER_CAULDRON_BEHAVIOR.map().put(UnidyeBlocks.CUSTOM_STAINED_GLASS.asItem(), CLEAN_GLASS);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BLACK_STAINED_GLASS.asItem(), CLEAN_GLASS);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BROWN_STAINED_GLASS.asItem(), CLEAN_GLASS);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.RED_STAINED_GLASS.asItem(), CLEAN_GLASS);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BLUE_STAINED_GLASS.asItem(), CLEAN_GLASS);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.YELLOW_STAINED_GLASS.asItem(), CLEAN_GLASS);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.WHITE_STAINED_GLASS.asItem(), CLEAN_GLASS);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIGHT_GRAY_STAINED_GLASS.asItem(), CLEAN_GLASS);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.GRAY_STAINED_GLASS.asItem(), CLEAN_GLASS);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.PINK_STAINED_GLASS.asItem(), CLEAN_GLASS);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.MAGENTA_STAINED_GLASS.asItem(), CLEAN_GLASS);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.PURPLE_STAINED_GLASS.asItem(), CLEAN_GLASS);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIGHT_BLUE_STAINED_GLASS.asItem(), CLEAN_GLASS);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.CYAN_STAINED_GLASS.asItem(), CLEAN_GLASS);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.ORANGE_STAINED_GLASS.asItem(), CLEAN_GLASS);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.GREEN_STAINED_GLASS.asItem(), CLEAN_GLASS);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIME_STAINED_GLASS.asItem(), CLEAN_GLASS);

        WATER_CAULDRON_BEHAVIOR.map().put(UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE.asItem(), CLEAN_GLASS_PANE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BLACK_STAINED_GLASS_PANE.asItem(), CLEAN_GLASS_PANE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BROWN_STAINED_GLASS_PANE.asItem(), CLEAN_GLASS_PANE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.RED_STAINED_GLASS_PANE.asItem(), CLEAN_GLASS_PANE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BLUE_STAINED_GLASS_PANE.asItem(), CLEAN_GLASS_PANE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.YELLOW_STAINED_GLASS_PANE.asItem(), CLEAN_GLASS_PANE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.WHITE_STAINED_GLASS_PANE.asItem(), CLEAN_GLASS_PANE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIGHT_GRAY_STAINED_GLASS_PANE.asItem(), CLEAN_GLASS_PANE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.GRAY_STAINED_GLASS_PANE.asItem(), CLEAN_GLASS_PANE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.PINK_STAINED_GLASS_PANE.asItem(), CLEAN_GLASS_PANE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.MAGENTA_STAINED_GLASS_PANE.asItem(), CLEAN_GLASS_PANE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.PURPLE_STAINED_GLASS_PANE.asItem(), CLEAN_GLASS_PANE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIGHT_BLUE_STAINED_GLASS_PANE.asItem(), CLEAN_GLASS_PANE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.CYAN_STAINED_GLASS_PANE.asItem(), CLEAN_GLASS_PANE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.ORANGE_STAINED_GLASS_PANE.asItem(), CLEAN_GLASS_PANE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.GREEN_STAINED_GLASS_PANE.asItem(), CLEAN_GLASS_PANE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIME_STAINED_GLASS_PANE.asItem(), CLEAN_GLASS_PANE);

        WATER_CAULDRON_BEHAVIOR.map().put(UnidyeBlocks.CUSTOM_CARPET.asItem(), CLEAN_CARPET);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BLACK_CARPET.asItem(), CLEAN_CARPET);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BROWN_CARPET.asItem(), CLEAN_CARPET);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.RED_CARPET.asItem(), CLEAN_CARPET);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BLUE_CARPET.asItem(), CLEAN_CARPET);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.YELLOW_CARPET.asItem(), CLEAN_CARPET);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIGHT_GRAY_CARPET.asItem(), CLEAN_CARPET);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.GRAY_CARPET.asItem(), CLEAN_CARPET);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.PINK_CARPET.asItem(), CLEAN_CARPET);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.MAGENTA_CARPET.asItem(), CLEAN_CARPET);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.PURPLE_CARPET.asItem(), CLEAN_CARPET);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIGHT_BLUE_CARPET.asItem(), CLEAN_CARPET);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.CYAN_CARPET.asItem(), CLEAN_CARPET);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.ORANGE_CARPET.asItem(), CLEAN_CARPET);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.GREEN_CARPET.asItem(), CLEAN_CARPET);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIME_CARPET.asItem(), CLEAN_CARPET);

        WATER_CAULDRON_BEHAVIOR.map().put(UnidyeBlocks.CUSTOM_CANDLE.asItem(), CLEAN_CANDLE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BLACK_CANDLE.asItem(), CLEAN_CANDLE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BROWN_CANDLE.asItem(), CLEAN_CANDLE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.RED_CANDLE.asItem(), CLEAN_CANDLE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BLUE_CANDLE.asItem(), CLEAN_CANDLE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.YELLOW_CANDLE.asItem(), CLEAN_CANDLE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.WHITE_CANDLE.asItem(), CLEAN_CANDLE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIGHT_GRAY_CANDLE.asItem(), CLEAN_CANDLE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.GRAY_CANDLE.asItem(), CLEAN_CANDLE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.PINK_CANDLE.asItem(), CLEAN_CANDLE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.MAGENTA_CANDLE.asItem(), CLEAN_CANDLE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.PURPLE_CANDLE.asItem(), CLEAN_CANDLE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIGHT_BLUE_CANDLE.asItem(), CLEAN_CANDLE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.CYAN_CANDLE.asItem(), CLEAN_CANDLE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.ORANGE_CANDLE.asItem(), CLEAN_CANDLE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.GREEN_CANDLE.asItem(), CLEAN_CANDLE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIME_CANDLE.asItem(), CLEAN_CANDLE);

        WATER_CAULDRON_BEHAVIOR.map().put(UnidyeBlocks.CUSTOM_CONCRETE.asItem(), CLEAN_CONCRETE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BLACK_CONCRETE.asItem(), CLEAN_CONCRETE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BROWN_CONCRETE.asItem(), CLEAN_CONCRETE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.RED_CONCRETE.asItem(), CLEAN_CONCRETE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BLUE_CONCRETE.asItem(), CLEAN_CONCRETE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.YELLOW_CONCRETE.asItem(), CLEAN_CONCRETE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIGHT_GRAY_CONCRETE.asItem(), CLEAN_CONCRETE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.GRAY_CONCRETE.asItem(), CLEAN_CONCRETE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.PINK_CONCRETE.asItem(), CLEAN_CONCRETE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.MAGENTA_CONCRETE.asItem(), CLEAN_CONCRETE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.PURPLE_CONCRETE.asItem(), CLEAN_CONCRETE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIGHT_BLUE_CONCRETE.asItem(), CLEAN_CONCRETE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.CYAN_CONCRETE.asItem(), CLEAN_CONCRETE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.ORANGE_CONCRETE.asItem(), CLEAN_CONCRETE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.GREEN_CONCRETE.asItem(), CLEAN_CONCRETE);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIME_CONCRETE.asItem(), CLEAN_CONCRETE);

        WATER_CAULDRON_BEHAVIOR.map().put(UnidyeBlocks.CUSTOM_BED.asItem(), CLEAN_BED);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BLACK_BED.asItem(), CLEAN_BED);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BROWN_BED.asItem(), CLEAN_BED);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.RED_BED.asItem(), CLEAN_BED);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.BLUE_BED.asItem(), CLEAN_BED);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.YELLOW_BED.asItem(), CLEAN_BED);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIGHT_GRAY_BED.asItem(), CLEAN_BED);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.GRAY_BED.asItem(), CLEAN_BED);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.PINK_BED.asItem(), CLEAN_BED);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.MAGENTA_BED.asItem(), CLEAN_BED);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.PURPLE_BED.asItem(), CLEAN_BED);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIGHT_BLUE_BED.asItem(), CLEAN_BED);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.CYAN_BED.asItem(), CLEAN_BED);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.ORANGE_BED.asItem(), CLEAN_BED);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.GREEN_BED.asItem(), CLEAN_BED);
        WATER_CAULDRON_BEHAVIOR.map().put(Blocks.LIME_BED.asItem(), CLEAN_BED);

        if (Unidye.SIMPLE_CONCRETE) {
            WATER_CAULDRON_BEHAVIOR.map().put(UnidyeBlocks.CUSTOM_CONCRETE_POWDER.asItem(), HARDEN_CUSTOM_CONCRETE_POWDER);
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

    public static final CauldronBehavior CLEAN_TERRACOTTA = (state, world, pos, player, hand, stack) -> {
        if (!world.isClient) {
            ItemStack itemStack = new ItemStack(Blocks.TERRACOTTA);
            itemStack.setCount(stack.getCount());
            player.setStackInHand(hand, itemStack);
            player.incrementStat(Stats.USE_CAULDRON);
            LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
        }
        return ItemActionResult.success(world.isClient);
    };

    public static final CauldronBehavior CLEAN_WOOL = (state, world, pos, player, hand, stack) -> {
        if (!world.isClient) {
            ItemStack itemStack = new ItemStack(Blocks.WHITE_WOOL);
            itemStack.setCount(stack.getCount());
            player.setStackInHand(hand, itemStack);
            player.incrementStat(Stats.USE_CAULDRON);
            LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
        }
        return ItemActionResult.success(world.isClient);
    };

    public static final CauldronBehavior CLEAN_GLASS = (state, world, pos, player, hand, stack) -> {
        if (!world.isClient) {
            ItemStack itemStack = new ItemStack(Blocks.GLASS);
            itemStack.setCount(stack.getCount());
            player.setStackInHand(hand, itemStack);
            player.incrementStat(Stats.USE_CAULDRON);
            LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
        }
        return ItemActionResult.success(world.isClient);
    };

    public static final CauldronBehavior CLEAN_GLASS_PANE = (state, world, pos, player, hand, stack) -> {
        if (!world.isClient) {
            ItemStack itemStack = new ItemStack(Blocks.GLASS_PANE);
            itemStack.setCount(stack.getCount());
            player.setStackInHand(hand, itemStack);
            player.incrementStat(Stats.USE_CAULDRON);
            LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
        }
        return ItemActionResult.success(world.isClient);
    };

    public static final CauldronBehavior CLEAN_CARPET = (state, world, pos, player, hand, stack) -> {
        if (!world.isClient) {
            ItemStack itemStack = new ItemStack(Blocks.WHITE_CARPET);
            itemStack.setCount(stack.getCount());
            player.setStackInHand(hand, itemStack);
            player.incrementStat(Stats.USE_CAULDRON);
            LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
        }
        return ItemActionResult.success(world.isClient);
    };

    public static final CauldronBehavior CLEAN_CANDLE = (state, world, pos, player, hand, stack) -> {
        if (!world.isClient) {
            ItemStack itemStack = new ItemStack(Blocks.CANDLE);
            itemStack.setCount(stack.getCount());
            player.setStackInHand(hand, itemStack);
            player.incrementStat(Stats.USE_CAULDRON);
            LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
        }
        return ItemActionResult.success(world.isClient);
    };

    public static final CauldronBehavior CLEAN_CONCRETE = (state, world, pos, player, hand, stack) -> {
        if (!world.isClient) {
            ItemStack itemStack = new ItemStack(Blocks.WHITE_CONCRETE);
            itemStack.setCount(stack.getCount());
            player.setStackInHand(hand, itemStack);
            player.incrementStat(Stats.USE_CAULDRON);
            LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
        }
        return ItemActionResult.success(world.isClient);
    };

    public static final CauldronBehavior CLEAN_BED = (state, world, pos, player, hand, stack) -> {
        if (!world.isClient) {
            ItemStack itemStack = new ItemStack(Blocks.WHITE_BED);
            player.setStackInHand(hand, itemStack);
            player.incrementStat(Stats.USE_CAULDRON);
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
}
