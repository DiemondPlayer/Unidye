package net.diemond_player.unidye.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import java.util.List;

public record MaterialColorsComponent(List<MaterialColor> materialColors){
    public static final Codec<MaterialColorsComponent> CODEC = MaterialColor.CODEC
            .listOf()
            .xmap(MaterialColorsComponent::new, MaterialColorsComponent::materialColors);
    public static final PacketCodec<RegistryByteBuf, MaterialColorsComponent> PACKET_CODEC = MaterialColor.PACKET_CODEC
            .collect(PacketCodecs.toList())
            .xmap(MaterialColorsComponent::new, MaterialColorsComponent::materialColors);

    public record MaterialColor(Identifier materialTypeId, int color) {
        public static final Codec<MaterialColor> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                Identifier.CODEC.fieldOf("materialType").forGetter(MaterialColor::materialTypeId),
                                Codecs.rangedInt(0, 0xFFFFFF).fieldOf("color").forGetter(MaterialColor::color)
                        )
                        .apply(instance, MaterialColor::new)
        );
        public static final PacketCodec<RegistryByteBuf, MaterialColor> PACKET_CODEC = PacketCodec.tuple(
                Identifier.PACKET_CODEC,
                MaterialColor::materialTypeId,
                PacketCodecs.codec(Codec.intRange(0, 0xFFFFFF)),
                MaterialColor::color,
                MaterialColor::new
        );
    }
}
