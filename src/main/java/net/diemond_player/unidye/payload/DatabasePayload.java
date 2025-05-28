package net.diemond_player.unidye.payload;

import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.util.DyeData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

import java.util.HashMap;

public record DatabasePayload(HashMap<Integer, DyeData> database) implements CustomPayload{
    public static final CustomPayload.Id<DatabasePayload> ID = new CustomPayload.Id<>(Unidye.DATABASE_PACKET_ID);

    public static final PacketCodec<RegistryByteBuf, DatabasePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.map(HashMap::new, PacketCodecs.INTEGER, PacketCodec.tuple(
                    PacketCodecs.STRING, DyeData::getPrefix,
                    //add exclusions
                    DyeData::new
            )), DatabasePayload::database,
            DatabasePayload::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
