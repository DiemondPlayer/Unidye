package net.diemond_player.unidye.block.custom;

import com.google.common.collect.Maps;
import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.block.UnidyeBlocks;
import net.diemond_player.unidye.block.entity.DyeableShulkerBoxBlockEntity;
import net.diemond_player.unidye.block.entity.UnidyeBlockEntities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class DyeableShulkerBoxBlock extends ShulkerBoxBlock implements IDyeableBlock {
    private static final VoxelShape UP_SHAPE = Block.createCuboidShape(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape DOWN_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    private static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
    private static final VoxelShape EAST_SHAPE = Block.createCuboidShape(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
    private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
    private static final Map<Direction, VoxelShape> SIDES_SHAPES = Util.make(Maps.newEnumMap(Direction.class), map -> {
        map.put(Direction.NORTH, NORTH_SHAPE);
        map.put(Direction.EAST, EAST_SHAPE);
        map.put(Direction.SOUTH, SOUTH_SHAPE);
        map.put(Direction.WEST, WEST_SHAPE);
        map.put(Direction.UP, UP_SHAPE);
        map.put(Direction.DOWN, DOWN_SHAPE);
    });
    public static final EnumProperty<Direction> FACING = FacingBlock.FACING;
    public static final Identifier CONTENTS_DYNAMIC_DROP_ID = new Identifier("minecraft", "contents");

    public DyeableShulkerBoxBlock(AbstractBlock.Settings settings) {
        super(DyeColor.CYAN, settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DyeableShulkerBoxBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return DyeableShulkerBoxBlock.checkType(type, UnidyeBlockEntities.DYEABLE_SHULKER_BOX_BE, DyeableShulkerBoxBlockEntity::tick);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        if (player.isSpectator()) {
            return ActionResult.CONSUME;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof DyeableShulkerBoxBlockEntity dyeableDyeableShulkerBoxBlockEntity) {
            if (DyeableShulkerBoxBlock.canOpen(state, world, pos, dyeableDyeableShulkerBoxBlockEntity)) {
                player.openHandledScreen(dyeableDyeableShulkerBoxBlockEntity);
                player.incrementStat(Stats.OPEN_SHULKER_BOX);
                PiglinBrain.onGuardedBlockInteracted(player, true);
            }
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }

    private static boolean canOpen(BlockState state, World world, BlockPos pos, DyeableShulkerBoxBlockEntity entity) {
        if (entity.getAnimationStage() != DyeableShulkerBoxBlockEntity.AnimationStage.CLOSED) {
            return true;
        }
        Box box = ShulkerEntity.calculateBoundingBox(state.get(FACING), 0.0f, 0.5f).offset(pos).contract(1.0E-6);
        return world.isSpaceEmpty(box);
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof DyeableShulkerBoxBlockEntity dyeableShulkerBoxBlockEntity) {
            if (!world.isClient && player.isCreative() && !dyeableShulkerBoxBlockEntity.isEmpty()) {
                ItemStack itemStack = new ItemStack(UnidyeBlocks.CUSTOM_SHULKER_BOX);
                blockEntity.setStackNbt(itemStack);
                if (dyeableShulkerBoxBlockEntity.hasCustomName()) {
                    itemStack.setCustomName(dyeableShulkerBoxBlockEntity.getCustomName());
                }
                if (dyeableShulkerBoxBlockEntity.color != DyeableShulkerBoxBlockEntity.DEFAULT_COLOR) {
                    DyeableItem dyeableItem = (DyeableItem) ((Object) itemStack.getItem());
                    dyeableItem.setColor(itemStack, dyeableShulkerBoxBlockEntity.color);
                }
                ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, itemStack);
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
            } else {
                dyeableShulkerBoxBlockEntity.checkLootInteraction(player);
            }
        }
        super.onBreak(world, pos, state, player);
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
        if (blockEntity instanceof DyeableShulkerBoxBlockEntity dyeableShulkerBoxBlockEntity) {
            builder = builder.addDynamicDrop(CONTENTS_DYNAMIC_DROP_ID, lootConsumer -> {
                for (int i = 0; i < dyeableShulkerBoxBlockEntity.size(); ++i) {
                    lootConsumer.accept(dyeableShulkerBoxBlockEntity.getStack(i));
                }
            });
        }
        return super.getDroppedStacks(state, builder);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity;
        if (itemStack.hasCustomName() && (blockEntity = world.getBlockEntity(pos)) instanceof DyeableShulkerBoxBlockEntity) {
            ((DyeableShulkerBoxBlockEntity) blockEntity).setCustomName(itemStack.getName());
        }
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof DyeableShulkerBoxBlockEntity) {
            world.updateComparators(pos, state.getBlock());
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        DyeableShulkerBoxBlockEntity dyeableShulkerBoxBlockEntity;
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof DyeableShulkerBoxBlockEntity && !(dyeableShulkerBoxBlockEntity = (DyeableShulkerBoxBlockEntity) blockEntity).suffocates()) {
            return SIDES_SHAPES.get(state.get(FACING).getOpposite());
        }
        return VoxelShapes.fullCube();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof DyeableShulkerBoxBlockEntity) {
            return VoxelShapes.cuboid(((DyeableShulkerBoxBlockEntity) blockEntity).getBoundingBox(state));
        }
        return VoxelShapes.fullCube();
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        ItemStack itemStack = super.getPickStack(world, pos, state);
        world.getBlockEntity(pos, UnidyeBlockEntities.DYEABLE_SHULKER_BOX_BE).ifPresent(blockEntity -> blockEntity.setStackNbt(itemStack));
        if (DyeableShulkerBoxBlockEntity.getColor(world, pos) != DyeableShulkerBoxBlockEntity.DEFAULT_COLOR) {
            return pickBlock(world, pos, itemStack);
        }
        return itemStack;
    }

    @Override
    public ItemStack pickBlock(BlockView world, BlockPos pos, ItemStack stack) {
        DyeableShulkerBoxBlockEntity blockEntity = UnidyeBlockEntities.DYEABLE_SHULKER_BOX_BE.get(world, pos);
        int color = DyeableShulkerBoxBlockEntity.DEFAULT_COLOR;
        if (blockEntity != null) {
            color = blockEntity.color;
        }
        NbtCompound subNbt = stack.getOrCreateSubNbt("display");
        subNbt.putInt("color", color);
        return stack;
    }
}
