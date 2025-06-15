package net.diemond_player.unidye.block;

import net.diemond_player.unidye.block.entity.IDyeableBlockEntity;
import net.diemond_player.unidye.entity.DyeableFallingBlockEntity;
import net.diemond_player.unidye.registry.UnidyeBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ConcretePowderBlock;
import net.minecraft.block.FallingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import static net.diemond_player.unidye.component.MaterialColorsComponent.DEFAULT_WHITE_COLOR;

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
        NbtCompound blockEntityData = world.getBlockEntity(pos) != null ? world.getBlockEntity(pos).createNbt(world.getRegistryManager()) : new NbtCompound();
        DyeableFallingBlockEntity dyeableFallingBlockEntity = DyeableFallingBlockEntity.spawnFromBlock(world, pos, state);
        this.configureFallingBlockEntity(dyeableFallingBlockEntity, color, blockEntityData);
    }

    protected void configureFallingBlockEntity(DyeableFallingBlockEntity entity, int color, NbtCompound blockEntityData) {
        entity.setCustomColor(color);
        entity.blockEntityData = blockEntityData;
    }

    @Override
    public int getColor(BlockState state, BlockView world, BlockPos pos) {
        return MinecraftClient.getInstance().getBlockColors().getColor(state, (BlockRenderView) world, pos.up(), 0);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if(!newState.isOf(UnidyeBlocks.CUSTOM_CONCRETE)){
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}
