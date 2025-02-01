package net.diemond_player.unidye.component;

import com.google.common.collect.ImmutableList;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

public record CustomBannerPatternsComponent(List<CustomBannerPatternsComponent.Layer> layers) {
    static final Logger LOGGER = LogUtils.getLogger();
    public static final CustomBannerPatternsComponent DEFAULT = new CustomBannerPatternsComponent(List.of());
    public static final Codec<CustomBannerPatternsComponent> CODEC = CustomBannerPatternsComponent.Layer.CODEC
            .listOf()
            .xmap(CustomBannerPatternsComponent::new, CustomBannerPatternsComponent::layers);
    public static final PacketCodec<RegistryByteBuf, CustomBannerPatternsComponent> PACKET_CODEC = CustomBannerPatternsComponent.Layer.PACKET_CODEC
            .collect(PacketCodecs.toList())
            .xmap(CustomBannerPatternsComponent::new, CustomBannerPatternsComponent::layers);

    public CustomBannerPatternsComponent withoutTopLayer() {
        return new CustomBannerPatternsComponent(List.copyOf(this.layers.subList(0, this.layers.size() - 1)));
    }

    public static class Builder {
        private final ImmutableList.Builder<CustomBannerPatternsComponent.Layer> entries = ImmutableList.builder();

        @Deprecated
        public CustomBannerPatternsComponent.Builder add(RegistryEntryLookup<BannerPattern> patternLookup, RegistryKey<BannerPattern> pattern, int color) {
            Optional<RegistryEntry.Reference<BannerPattern>> optional = patternLookup.getOptional(pattern);
            if (optional.isEmpty()) {
                CustomBannerPatternsComponent.LOGGER.warn("Unable to find banner pattern with id: '{}'", pattern.getValue());
                return this;
            } else {
                return this.add((RegistryEntry<BannerPattern>)optional.get(), color);
            }
        }

        public CustomBannerPatternsComponent.Builder add(RegistryEntry<BannerPattern> pattern, int color) {
            return this.add(new CustomBannerPatternsComponent.Layer(pattern, color));
        }

        public CustomBannerPatternsComponent.Builder add(CustomBannerPatternsComponent.Layer layer) {
            this.entries.add(layer);
            return this;
        }

        public CustomBannerPatternsComponent.Builder addAll(CustomBannerPatternsComponent patterns) {
            this.entries.addAll(patterns.layers);
            return this;
        }

        public CustomBannerPatternsComponent build() {
            return new CustomBannerPatternsComponent(this.entries.build());
        }
    }

    public static record Layer(RegistryEntry<BannerPattern> pattern, int color) {
        public static final Codec<CustomBannerPatternsComponent.Layer> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                BannerPattern.ENTRY_CODEC.fieldOf("pattern").forGetter(CustomBannerPatternsComponent.Layer::pattern),
                                Codec.intRange(0, 0xFFFFFF).fieldOf("color").forGetter(CustomBannerPatternsComponent.Layer::color)
                        )
                        .apply(instance, CustomBannerPatternsComponent.Layer::new)
        );
        public static final PacketCodec<RegistryByteBuf, CustomBannerPatternsComponent.Layer> PACKET_CODEC = PacketCodec.tuple(
                BannerPattern.ENTRY_PACKET_CODEC,
                CustomBannerPatternsComponent.Layer::pattern,
                PacketCodecs.codec(Codec.intRange(0, 0xFFFFFF)),
                CustomBannerPatternsComponent.Layer::color,
                CustomBannerPatternsComponent.Layer::new
        );

        public MutableText getTooltipText() {
            String string = this.pattern.value().translationKey();
            return Text.translatable(String.format("#%06X", (0xFFFFFF & color)) + " " + string);
        }
    }
}
