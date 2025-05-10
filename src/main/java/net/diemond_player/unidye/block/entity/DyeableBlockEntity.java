package net.diemond_player.unidye.block.entity;

// Mostly taken with permission from Hecco's Bountiful Fares mod back when Unidye was still indev for 1.20.1
// Source: https://github.com/Heccology/Bountiful-Fares/blob/1.20.1/src/main/java/net/hecco/bountifulfares/block/entity/DyeableBlockEntity.java

import net.diemond_player.unidye.registry.UnidyeBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import static net.diemond_player.unidye.item.CustomDyeItem.DEFAULT_WHITE_COLOR;

public class DyeableBlockEntity extends BlockEntity implements IDyeableBlockEntity {
    public DyeableBlockEntity(BlockPos pos, BlockState state) {
        super(UnidyeBlockEntities.DYEABLE_BE, pos, state);
    }
    private int color = DEFAULT_WHITE_COLOR;

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (color != DEFAULT_WHITE_COLOR) {
            nbt.putInt("color", color);
        }
        super.writeNbt(nbt, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
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
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return this.createNbt(registryLookup);
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

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        this.color = components.getOrDefault(DataComponentTypes.DYED_COLOR, new DyedColorComponent(DyedColorComponent.DEFAULT_COLOR, true)).rgb();
    }

    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        componentMapBuilder.add(DataComponentTypes.DYED_COLOR, new DyedColorComponent(color, true));
    }
}
