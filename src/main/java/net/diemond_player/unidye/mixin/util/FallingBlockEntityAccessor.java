package net.diemond_player.unidye.mixin.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FallingBlockEntity.class)
public interface FallingBlockEntityAccessor {
    @Accessor("destroyedOnLanding")
    boolean getDestroyedOnLanding();

    @Accessor("block")
    void setBlock(BlockState block);

    @Accessor("block")
    BlockState getBlock();
}
