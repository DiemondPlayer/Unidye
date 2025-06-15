package net.diemond_player.unidye.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.registry.UnidyeMaterialTypes;
import net.diemond_player.unidye.util.UnidyeMaterialType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import org.apache.commons.compress.utils.Lists;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import static net.diemond_player.unidye.item.CustomDyeItem.DYE_SHAPE;

public record MaterialColorsComponent(List<MaterialColor> materialColors){
    public static final MaterialColorsComponent DEFAULT = new MaterialColorsComponent(List.of());
    public static final Codec<MaterialColorsComponent> CODEC = MaterialColor.CODEC
            .listOf()
            .xmap(MaterialColorsComponent::new, MaterialColorsComponent::materialColors);
    public static final PacketCodec<RegistryByteBuf, MaterialColorsComponent> PACKET_CODEC = MaterialColor.PACKET_CODEC
            .collect(PacketCodecs.toList())
            .xmap(MaterialColorsComponent::new, MaterialColorsComponent::materialColors);

    public static final int DEFAULT_WHITE_COLOR = 16777215;

    public static void convertFromCustomDataComponent(ItemStack itemStack){
        //This if statement converts old nbt to new one so that world made before 2.0.0 don't get corrupted
        if(itemStack.contains(DataComponentTypes.CUSTOM_DATA)) {
            NbtComponent nbtComponent = itemStack.get(DataComponentTypes.CUSTOM_DATA);
            if (nbtComponent == null) return;
            NbtCompound nbtCompoundTest = nbtComponent.copyNbt();
            nbtCompoundTest.remove(DYE_SHAPE);
            if(nbtCompoundTest.isEmpty()) return;
            List<MaterialColor> materialColorList = Lists.newArrayList();
            NbtCompound nbtCompound = nbtComponent.copyNbt();
            for (UnidyeMaterialType materialType : UnidyeMaterialTypes.MATERIAL_TYPE.stream().toList()) {
                int n = -1;
                if (nbtComponent.contains(materialType.getId().getPath())) {
                    n = nbtCompound.getInt(materialType.getId().getPath());
                    nbtCompound.remove(materialType.getId().getPath());
                } else if (nbtComponent.contains(materialType.getId().toString())) {
                    n = nbtCompound.getInt(materialType.getId().toString());
                    nbtCompound.remove(materialType.getId().toString());
                }
                if (n != -1) {
                    materialColorList.add(new MaterialColor(materialType.getId(), n));
                }
            }
            itemStack.set(UnidyeDataComponentTypes.MATERIAL_COLORS, new MaterialColorsComponent(materialColorList));
            itemStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbtCompound));
        }
    }

    public static int getMaterialColor(ItemStack stack, UnidyeMaterialType materialType) {
        return getMaterialColor(stack, materialType.getId());
    }

    public static int getMaterialColor(ItemStack stack, Identifier materialTypeId) {
        convertFromCustomDataComponent(stack);
        if(stack.contains(UnidyeDataComponentTypes.MATERIAL_COLORS)) {
            return stack.get(UnidyeDataComponentTypes.MATERIAL_COLORS).materialColors()
                    .stream().filter((materialColor ->
                            materialColor.materialTypeId().equals(materialTypeId)))
                    .findFirst().map(MaterialColor::color).orElse(DEFAULT_WHITE_COLOR);
        }
        return DEFAULT_WHITE_COLOR;
    }

    public static String getMaterialHexColor(ItemStack stack, UnidyeMaterialType materialType) {
        return getMaterialHexColor(stack, materialType.getId());
    }

    public static String getMaterialHexColor(ItemStack stack, Identifier materialTypeId) {
        return String.format("#%06X", (0xFFFFFF & getMaterialColor(stack, materialTypeId)));
    }

    public static void setMaterialColor(ItemStack itemStack, int color, UnidyeMaterialType materialType) {
        List<MaterialColor> materialColors = itemStack.getOrDefault(UnidyeDataComponentTypes.MATERIAL_COLORS, DEFAULT).materialColors();
        materialColors.remove(new MaterialColor(materialType.getId(), getMaterialColor(itemStack, materialType)));
        materialColors.add(new MaterialColor(materialType.getId(), color));
        itemStack.set(UnidyeDataComponentTypes.MATERIAL_COLORS, new MaterialColorsComponent(materialColors));
    }

    public static void appendTooltip(ItemStack itemStack, Consumer<Text> tooltip) {
        convertFromCustomDataComponent(itemStack);
        List<MaterialColor> materialColors = itemStack.getOrDefault(UnidyeDataComponentTypes.MATERIAL_COLORS, DEFAULT).materialColors();
        if(materialColors.isEmpty()) return;
        materialColors.sort(Comparator.comparing(materialColor -> materialColor.materialTypeId().toString()));
        if (Screen.hasShiftDown() || materialColors.size() < 3) {
            for(MaterialColor materialColor : materialColors){
                int color = materialColor.color();
                if(color != 0xFFFFFF) {
                    Identifier id = materialColor.materialTypeId();
                    if (!id.equals(UnidyeMaterialTypes.getMaterialType(itemStack.getItem()).getId())) {
                        MutableText mutableText = Text.literal("■ ");
                        tooltip.accept(mutableText.setStyle(mutableText.getStyle().withColor(color))
                                .append(Text.translatable("tooltip." + id.getNamespace() + "." + id.getPath() + "_color")
                                        .append(getMaterialHexColor(itemStack, id)).formatted(Formatting.GRAY)));
                    }
                }
            }
        } else {
            tooltip.accept(Text.translatable("tooltip.unidye.press_shift"));
        }
    }

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
