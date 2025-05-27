package net.diemond_player.unidye.block;

import net.diemond_player.unidye.block.entity.DyeableLeatheryBlockEntity;
import net.diemond_player.unidye.block.entity.IDyeableBlockEntity;
import net.diemond_player.unidye.registry.UnidyeBlockEntities;
import net.diemond_player.unidye.registry.UnidyeMaterialTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.Stainable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

import static net.diemond_player.unidye.item.CustomDyeItem.DEFAULT_WHITE_COLOR;

public class DyeablePaneBlock extends PaneBlock implements IDyeableBlock, Stainable {
    public DyeablePaneBlock(Settings settings) {
        super(settings);
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
    public ItemStack pickBlock(BlockView world, BlockPos pos, ItemStack stack) {
        DyeableLeatheryBlockEntity blockEntity = UnidyeBlockEntities.DYEABLE_LEATHERY_BE.get(world, pos);
        int color = DEFAULT_WHITE_COLOR;
        int beaconColor = DEFAULT_WHITE_COLOR;
        if (blockEntity != null) {
            color = blockEntity.getColor();
            beaconColor = blockEntity.leatherColor;
        }
        stack.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(color, true));
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putInt(UnidyeMaterialTypes.LEATHER.getId().toString(), beaconColor);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbtCompound));
        return stack;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DyeableLeatheryBlockEntity(pos, state);
    }

    @Override
    public DyeColor getColor() {
        //try {
//            Unidye.LOGGER.warn("{} returns DyeColor.WHITE as a requirement rather than an actual color, calling the {} method is not recommended.", this.getClass().getName(), this.getClass().getMethod("getColor").getName());
//       } catch (NoSuchMethodException ignored) {
//     }
        return DyeColor.WHITE;
    }
}
