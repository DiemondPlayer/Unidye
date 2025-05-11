package net.diemond_player.unidye.block;

import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.block.entity.DyeableBlockEntity;
import net.diemond_player.unidye.block.entity.IDyeableBlockEntity;
import net.diemond_player.unidye.entity.DyeableFallingBlockEntity;
import net.diemond_player.unidye.registry.UnidyeBlockEntities;
import net.diemond_player.unidye.registry.UnidyeBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.block.FallingBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import static net.diemond_player.unidye.item.CustomDyeItem.DEFAULT_WHITE_COLOR;

public class DyeableConcretePowderBlock extends ConcretePowderBlock implements IDyeableBlock {

    public DyeableConcretePowderBlock(Block hardened, Settings settings) {
        super(hardened, settings);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        if (IDyeableBlockEntity.getColor(world, pos) != DEFAULT_WHITE_COLOR) {
            ItemStack stack = super.getPickStack(world, pos, state);
            return pickBlock(world, pos, stack);
        } else {
            return new ItemStack(this);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!FallingBlock.canFallThrough(world.getBlockState(pos.down())) || pos.getY() < world.getBottomY()) {
            return;
        }
        int color = IDyeableBlockEntity.getColor(world, pos);
        DyeableFallingBlockEntity dyeableFallingBlockEntity = DyeableFallingBlockEntity.spawnFromBlock(world, pos, state);
        this.configureFallingBlockEntity(dyeableFallingBlockEntity, color);
    }

    protected void configureFallingBlockEntity(DyeableFallingBlockEntity entity, int color) {
        entity.setCustomColor(color);
    }

    @Override
    public int getColor(BlockState state, BlockView world, BlockPos pos) {
        DyeableBlockEntity blockEntity = UnidyeBlockEntities.DYEABLE_BE.get(world, pos);
        if(blockEntity != null) {
            return blockEntity.getColor();
        } else {
            return DEFAULT_WHITE_COLOR;
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(!newState.isOf(UnidyeBlocks.CUSTOM_CONCRETE)){
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}
