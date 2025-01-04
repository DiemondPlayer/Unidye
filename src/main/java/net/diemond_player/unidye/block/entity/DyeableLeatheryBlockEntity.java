package net.diemond_player.unidye.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class DyeableLeatheryBlockEntity extends BlockEntity {
    public DyeableLeatheryBlockEntity(BlockPos pos, BlockState state) {
        super(UnidyeBlockEntities.DYEABLE_LEATHERY_BE, pos, state);
    }

    public static final int DEFAULT_COLOR = 16777215;
    public int color = DEFAULT_COLOR;
    public int leatherColor = DEFAULT_COLOR;

    @Override
    public void writeNbt(NbtCompound nbt) {
        if (color != DEFAULT_COLOR) {
            nbt.putInt("color", color);
        }
        if (leatherColor != DEFAULT_COLOR) {
            nbt.putInt("leather", leatherColor);
        }
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        if (nbt.getInt("leather") == 0) {
            leatherColor = DEFAULT_COLOR;
        } else {
            leatherColor = nbt.getInt("leather");
        }
        if (nbt.getInt("color") == 0) {
            color = DEFAULT_COLOR;
        } else {
            color = nbt.getInt("color");
        }
    }

    @Override
    public void markDirty() {
        if (this.world != null) {
            markDirty(this.world, this.pos, this.getCachedState());
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public static int getColor(BlockView world, BlockPos pos) {
        if (world == null) {
            return DyeableBlockEntity.DEFAULT_COLOR;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof DyeableLeatheryBlockEntity dyeableBlockEntity) {
            return dyeableBlockEntity.color;
        } else {
            return DyeableBlockEntity.DEFAULT_COLOR;
        }
    }
}
