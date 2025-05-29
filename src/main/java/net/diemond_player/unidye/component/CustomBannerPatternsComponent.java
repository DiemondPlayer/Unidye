package net.diemond_player.unidye.component;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Optional;

public record CustomBannerPatternsComponent(List<CustomBannerPatternsComponent.Layer> layers) {
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
                Unidye.LOGGER.warn("Unable to find banner pattern with id: '{}'", pattern.getValue());
                return this;
            } else {
                return this.add(optional.get(), color);
            }
        }

        public CustomBannerPatternsComponent.Builder add(RegistryEntry<BannerPattern> pattern, int color) {
            return this.add(new CustomBannerPatternsComponent.Layer(pattern, color, ItemNameAffixesComponent.DEFAULT));
        }

        public CustomBannerPatternsComponent.Builder add(RegistryEntry<BannerPattern> pattern, int color, ItemNameAffixesComponent itemNameAffixesComponent) {
            return this.add(new CustomBannerPatternsComponent.Layer(pattern, color, itemNameAffixesComponent));
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

    public record Layer(RegistryEntry<BannerPattern> pattern, int color, ItemNameAffixesComponent itemNameAffixesComponent) {
        public static final Codec<CustomBannerPatternsComponent.Layer> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                BannerPattern.ENTRY_CODEC.fieldOf("pattern").forGetter(CustomBannerPatternsComponent.Layer::pattern),
                                Codec.intRange(0, 0xFFFFFF).fieldOf("color").forGetter(CustomBannerPatternsComponent.Layer::color),
                                ItemNameAffixesComponent.CODEC.fieldOf("itemNameAffixesComponent").forGetter(CustomBannerPatternsComponent.Layer::itemNameAffixesComponent)
                                )
                        .apply(instance, CustomBannerPatternsComponent.Layer::new)
        );
        public static final PacketCodec<RegistryByteBuf, CustomBannerPatternsComponent.Layer> PACKET_CODEC = PacketCodec.tuple(
                BannerPattern.ENTRY_PACKET_CODEC,
                CustomBannerPatternsComponent.Layer::pattern,
                PacketCodecs.codec(Codec.intRange(0, 0xFFFFFF)),
                CustomBannerPatternsComponent.Layer::color,
                ItemNameAffixesComponent.PACKET_CODEC,
                CustomBannerPatternsComponent.Layer::itemNameAffixesComponent,
                CustomBannerPatternsComponent.Layer::new
        );

        public MutableText getTooltipText() {
            String string = this.pattern.value().translationKey();
            Optional<DyeColor> dyeColor = UnidyeUtils.findDyeColorByLeatherColor(this.color);
            MutableText mutableText = Text.literal("■ ");
            mutableText.setStyle(mutableText.getStyle().withColor(this.color));
            if (dyeColor.isEmpty()) {
                MutableText prefix = this.itemNameAffixesComponent().prefix().copy();
                MutableText suffix = this.itemNameAffixesComponent().suffix().copy();
                boolean isPrefixEmpty = prefix.equals(Text.empty());
                boolean isSuffixEmpty = suffix.equals(Text.empty());
                MutableText name = Text.empty();
                if(isPrefixEmpty && isSuffixEmpty) return mutableText.append(Text.literal("§7#" + Integer.toString(this.color, 16).toUpperCase() + " ").append(Text.translatable(string).formatted(Formatting.GRAY)));
                if(!isPrefixEmpty) name.append(prefix).append(Text.literal(" "));
                name.append(Text.translatable(string));
                if(!isSuffixEmpty) name.append(Text.literal(" ")).append(suffix);
                return mutableText.append(name.formatted(Formatting.GRAY));
            } else {
                return mutableText.append(Text.translatable(string + "." + dyeColor.get().getName()).formatted(Formatting.GRAY));
            }
        }
    }
}
