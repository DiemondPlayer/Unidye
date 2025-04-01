package net.diemond_player.unidye.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import static net.diemond_player.unidye.item.custom.CustomDyeItem.DEFAULT_WHITE_COLOR;

public class DyeableLeatheryBlockEntity extends BlockEntity implements IDyeableBlockEntity{
    public DyeableLeatheryBlockEntity(BlockPos pos, BlockState state) {
        super(UnidyeBlockEntities.DYEABLE_LEATHERY_BE, pos, state);
    }
    private int color = DEFAULT_WHITE_COLOR;
    public int leatherColor = DEFAULT_WHITE_COLOR;

    @Override
    public void writeNbt(NbtCompound nbt) {
        if (color != DEFAULT_WHITE_COLOR) {
            nbt.putInt("color", color);
        }
        if (leatherColor != DEFAULT_WHITE_COLOR) {
            nbt.putInt("leather", leatherColor);
        }
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        if (nbt.getInt("leather") == 0) {
            leatherColor = DEFAULT_WHITE_COLOR;
        } else {
            leatherColor = nbt.getInt("leather");
        }
        if (nbt.getInt("color") == 0) {
            color = DEFAULT_WHITE_COLOR;
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

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
        this.markDirty();
    }
}
