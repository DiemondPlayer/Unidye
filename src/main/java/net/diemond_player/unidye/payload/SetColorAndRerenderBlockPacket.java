package net.diemond_player.unidye.payload;

import net.diemond_player.unidye.Unidye;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;

public record SetColorAndRerenderBlockPacket(BlockPos pos, int color) implements CustomPayload {
    public static final Id<SetColorAndRerenderBlockPacket> ID = new Id<>(Unidye.SET_COLOR_AND_RERENDER_BLOCK_PACKET_ID);

    public static final PacketCodec<RegistryByteBuf, SetColorAndRerenderBlockPacket> CODEC = PacketCodec.tuple(
            BlockPos.PACKET_CODEC, SetColorAndRerenderBlockPacket::pos,
            PacketCodecs.INTEGER, SetColorAndRerenderBlockPacket::color,
            SetColorAndRerenderBlockPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
