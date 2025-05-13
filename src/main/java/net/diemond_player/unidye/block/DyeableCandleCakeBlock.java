package net.diemond_player.unidye.block;

import net.diemond_player.unidye.block.entity.IDyeableBlockEntity;
import net.diemond_player.unidye.registry.UnidyeBlocks;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CandleCakeBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
        int color = IDyeableBlockEntity.getColor(world, pos);
        ActionResult actionResult = tryEat(world, pos, Blocks.CAKE.getDefaultState(), player);
        if (actionResult.isAccepted()) {
            ItemStack itemStack1 = new ItemStack(UnidyeBlocks.CUSTOM_CANDLE);
            UnidyeUtils.setColor(itemStack1, color);
            dropStack(world, pos, itemStack1);
        }

        return actionResult;
    }

    public static ActionResult tryEat(WorldAccess world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!player.canConsume(false)) {
            return ActionResult.PASS;
        } else {
            player.incrementStat(Stats.EAT_CAKE_SLICE);
            player.getHungerManager().add(2, 0.1F);
            int i = state.get(BITES);
            world.emitGameEvent(player, GameEvent.EAT, pos);
            if (i < 6) {
                world.setBlockState(pos, state.with(BITES, i + 1), 3);
            } else {
                world.removeBlock(pos, false);
                world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            }
            return ActionResult.SUCCESS;
        }
    }
}
