package net.diemond_player.unidye.block;

import net.diemond_player.unidye.block.entity.IDyeableBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.Stainable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import static net.diemond_player.unidye.component.MaterialColorsComponent.DEFAULT_WHITE_COLOR;

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
    public DyeColor getColor() {
        //try {
//            Unidye.LOGGER.warn("{} returns DyeColor.WHITE as a requirement rather than an actual color, calling the {} method is not recommended.", this.getClass().getName(), this.getClass().getMethod("getColor").getName());
//       } catch (NoSuchMethodException ignored) {
//     }
        return DyeColor.WHITE;
    }
}
