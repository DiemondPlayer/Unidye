package net.diemond_player.unidye.block.custom;

import net.diemond_player.unidye.block.UnidyeBlocks;
import net.diemond_player.unidye.block.entity.DyeableBlockEntity;
import net.minecraft.block.*;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;

import static net.minecraft.block.CakeBlock.BITES;

public class DyeableCandleCakeBlock extends CandleCakeBlock implements IDyeableBlock {
    public DyeableCandleCakeBlock(Block candle, Settings settings) {
        super(candle, settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        int color = DyeableBlockEntity.getColor(world, pos);
        ActionResult actionResult = tryEat(world, pos, Blocks.CAKE.getDefaultState(), player);
        if (actionResult.isAccepted()) {
            if (world instanceof ServerWorld) {
                ItemStack itemStack = new ItemStack(UnidyeBlocks.CUSTOM_CANDLE);
                itemStack.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(color, true));
                dropStack(world, pos, itemStack);
                state.onStacksDropped((ServerWorld)world, pos, ItemStack.EMPTY, true);
            }
        }

        return actionResult;
    }

    public static ActionResult tryEat(WorldAccess world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!player.canConsume(false)) {
            return ActionResult.PASS;
        } else {
            player.incrementStat(Stats.EAT_CAKE_SLICE);
            player.getHungerManager().add(2, 0.1F);
            int i = (Integer)state.get(BITES);
            world.emitGameEvent(player, GameEvent.EAT, pos);
            if (i < 6) {
                world.setBlockState(pos, state.with(BITES, i + 1), Block.NOTIFY_ALL);
            } else {
                world.removeBlock(pos, false);
                world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            }

            return ActionResult.SUCCESS;
        }
    }
}
