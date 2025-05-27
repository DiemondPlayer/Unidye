package net.diemond_player.unidye.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.util.DyeNameDatabaseSaverAndLoader;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.world.World;

public record ItemNamePrefixComponent(Text prefix, int sourceCustomDyeColor) {
    public static final ItemNamePrefixComponent DEFAULT = new ItemNamePrefixComponent(Text.empty(), 0xFFFFFF);
    public static final Codec<ItemNamePrefixComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            TextCodecs.STRINGIFIED_CODEC.fieldOf("prefix").forGetter(ItemNamePrefixComponent::prefix),
                            Codec.intRange(0, 0xFFFFFF).fieldOf("sourceCustomDyeColor").forGetter(ItemNamePrefixComponent::sourceCustomDyeColor)
                    )
                    .apply(instance, ItemNamePrefixComponent::new));
    public static final PacketCodec<RegistryByteBuf, ItemNamePrefixComponent> PACKET_CODEC = PacketCodec.tuple(
            TextCodecs.REGISTRY_PACKET_CODEC,
            ItemNamePrefixComponent::prefix,
            PacketCodecs.codec(Codec.intRange(0, 0xFFFFFF)),
            ItemNamePrefixComponent::sourceCustomDyeColor,
            ItemNamePrefixComponent::new
    );

    public static ItemNamePrefixComponent noPrefix(int sourceCustomDyeColor){
        return new ItemNamePrefixComponent(Text.empty(), sourceCustomDyeColor);
    }

    public static void updateCustomDyePrefix(ItemStack itemStack, World world){
        if(!world.isClient) {
            int color = itemStack.getOrDefault(DataComponentTypes.DYED_COLOR, new DyedColorComponent(DyedColorComponent.DEFAULT_COLOR, true)).rgb();
            DyeNameDatabaseSaverAndLoader serverState = DyeNameDatabaseSaverAndLoader.getServerState(((ServerWorld) world).getServer());
            //Unidye.LOGGER.info(serverState.database.toString());
            if (serverState.database.containsKey(color)) {
                Text text = Text.literal(serverState.database.get(color));
                if(!itemStack.getOrDefault(UnidyeDataComponentTypes.ITEM_NAME_PREFIX, ItemNamePrefixComponent.DEFAULT).prefix().equals(text)) {
                    itemStack.set(UnidyeDataComponentTypes.ITEM_NAME_PREFIX, new ItemNamePrefixComponent(text, color));
                }
            }else{
                itemStack.set(UnidyeDataComponentTypes.ITEM_NAME_PREFIX, noPrefix(color));
            }
        }
    }

    public static void updatePrefix(ItemStack itemStack, World world){
        if(!world.isClient && itemStack.contains(UnidyeDataComponentTypes.ITEM_NAME_PREFIX)) {
            int color = itemStack.get(UnidyeDataComponentTypes.ITEM_NAME_PREFIX).sourceCustomDyeColor();
            DyeNameDatabaseSaverAndLoader serverState = DyeNameDatabaseSaverAndLoader.getServerState(((ServerWorld) world).getServer());
            //Unidye.LOGGER.info(serverState.database.toString());
            if (serverState.database.containsKey(color)) {
                Text text = Text.literal(serverState.database.get(color));
                if(!itemStack.get(UnidyeDataComponentTypes.ITEM_NAME_PREFIX).prefix().equals(text)) {
                    itemStack.set(UnidyeDataComponentTypes.ITEM_NAME_PREFIX, new ItemNamePrefixComponent(text, color));
                }
            }else{
                itemStack.set(UnidyeDataComponentTypes.ITEM_NAME_PREFIX, noPrefix(color));
            }
        }
    }

    public static MutableText getName(ItemStack itemStack, String translationKey){
        if(itemStack.contains(UnidyeDataComponentTypes.ITEM_NAME_PREFIX)) {
            MutableText text = itemStack.get(UnidyeDataComponentTypes.ITEM_NAME_PREFIX).prefix().copy();
            return text.equals(Text.empty()) ? null : text.append(Text.translatable(translationKey + ".suffix"));
        }
        return null;
    }

    public Text toText(){
        return this.prefix().equals(Text.empty()) ? Text.literal("No Prefix").append(Text.literal(" ")).append(Text.literal(String.valueOf(this.sourceCustomDyeColor()))) : this.prefix().copy().append(Text.literal(" ")).append(Text.literal(String.valueOf(this.sourceCustomDyeColor())));
    }
}
