package net.diemond_player.unidye.payload;

import net.diemond_player.unidye.Unidye;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record SetColorAndRerenderBlockPayload(BlockPos pos, int color) implements CustomPayload {
    public static final Id<SetColorAndRerenderBlockPayload> ID = new Id<>(Unidye.SET_COLOR_AND_RERENDER_BLOCK_PACKET_ID);

    public static final PacketCodec<RegistryByteBuf, SetColorAndRerenderBlockPayload> CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC, SetColorAndRerenderBlockPayload::pos,
            PacketCodecs.INTEGER, SetColorAndRerenderBlockPayload::color,
            SetColorAndRerenderBlockPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
