package net.diemond_player.unidye.payload;

import net.diemond_player.unidye.Unidye;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record RequestDatabasePayload(boolean hi) implements CustomPayload{
    public static final CustomPayload.Id<RequestDatabasePayload> ID = new CustomPayload.Id<>(Unidye.REQUEST_DATABASE_PACKET_ID);

    public static final PacketCodec<RegistryByteBuf, RequestDatabasePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, RequestDatabasePayload::hi,
            RequestDatabasePayload::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
