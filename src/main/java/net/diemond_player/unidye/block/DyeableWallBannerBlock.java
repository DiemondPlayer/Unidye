package net.diemond_player.unidye.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.diemond_player.unidye.block.entity.DyeableBannerBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Map;

public class DyeableWallBannerBlock extends WallBannerBlock {

    public DyeableWallBannerBlock(AbstractBlock.Settings settings) {
        super(DyeColor.WHITE, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == state.get(FACING).getOpposite() && !state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = this.getDefaultState();
        World worldView = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        for (Direction direction : ctx.getPlacementDirections()) {
            if (!direction.getAxis().isHorizontal() || !(blockState = blockState.with(FACING, direction.getOpposite())).canPlaceAt(worldView, blockPos))
                continue;
            return blockState;
        }
        return null;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DyeableBannerBlockEntity(pos, state);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof DyeableBannerBlockEntity) {
            return ((DyeableBannerBlockEntity) blockEntity).getPickStack();
        }
        return super.getPickStack(world, pos, state);
    }

    @Override
    public DyeColor getColor() {
        //try {
//            Unidye.LOGGER.warn("{} returns DyeColor.WHITE as a requirement rather than an actual color, calling the {} method is not recommended.", this.getClass().getName(), this.getClass().getMethod("getColor").getName());
//       } catch (NoSuchMethodException ignored) {
//     }
        return super.getColor();
    }
}