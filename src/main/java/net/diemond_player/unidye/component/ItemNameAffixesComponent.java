package net.diemond_player.unidye.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.util.DyeData;
import net.diemond_player.unidye.util.DyeDatabaseSaverAndLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.world.World;

public record ItemNameAffixesComponent(Text prefix, Text suffix, int sourceCustomDyeColor) {
    public static final ItemNameAffixesComponent DEFAULT = new ItemNameAffixesComponent(Text.empty(), Text.empty(), 0xFFFFFF);
    public static final Codec<ItemNameAffixesComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            TextCodecs.STRINGIFIED_CODEC.fieldOf("prefix").forGetter(ItemNameAffixesComponent::prefix),
                            TextCodecs.STRINGIFIED_CODEC.fieldOf("suffix").forGetter(ItemNameAffixesComponent::suffix),
                            Codec.intRange(0, 0xFFFFFF).fieldOf("sourceCustomDyeColor").forGetter(ItemNameAffixesComponent::sourceCustomDyeColor)
                    )
                    .apply(instance, ItemNameAffixesComponent::new));
    public static final PacketCodec<RegistryByteBuf, ItemNameAffixesComponent> PACKET_CODEC = PacketCodec.tuple(
            TextCodecs.REGISTRY_PACKET_CODEC,
            ItemNameAffixesComponent::prefix,
            TextCodecs.REGISTRY_PACKET_CODEC,
            ItemNameAffixesComponent::suffix,
            PacketCodecs.codec(Codec.intRange(0, 0xFFFFFF)),
            ItemNameAffixesComponent::sourceCustomDyeColor,
            ItemNameAffixesComponent::new
    );

    public static ItemNameAffixesComponent noAffixes(int sourceCustomDyeColor){
        return new ItemNameAffixesComponent(Text.empty(), Text.empty(), sourceCustomDyeColor);
    }

    public static ItemNameAffixesComponent noPrefix(Text suffix, int sourceCustomDyeColor){
        return new ItemNameAffixesComponent(Text.empty(), suffix, sourceCustomDyeColor);
    }

    public static ItemNameAffixesComponent noSuffix(Text prefix, int sourceCustomDyeColor){
        return new ItemNameAffixesComponent(prefix, Text.empty(), sourceCustomDyeColor);
    }


    public static void updateAffixes(ItemStack itemStack, World world){
        if(!world.isClient && itemStack.contains(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES)) {
            int color = itemStack.get(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES).sourceCustomDyeColor();
            DyeDatabaseSaverAndLoader serverState = DyeDatabaseSaverAndLoader.getServerState(((ServerWorld) world).getServer());
            Unidye.LOGGER.info(serverState.database.toString());
            if (serverState.database.containsKey(color)) {
                DyeData dyeData = serverState.database.get(color);
                Item item = itemStack.getItem();
                Text prefix = dyeData.getPrefixExclusions().containsKey(item) ?  Text.literal(dyeData.getPrefixExclusions().get(item)) : Text.literal(dyeData.getPrefix());
                Text suffix = dyeData.getSuffixExclusions().containsKey(item) ?  Text.literal(dyeData.getSuffixExclusions().get(item)) : Text.literal(dyeData.getSuffix());
                if(!itemStack.get(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES).prefix().equals(prefix) || !itemStack.get(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES).suffix().equals(suffix)) {
                    itemStack.set(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, new ItemNameAffixesComponent(prefix, suffix, color));
                }
            }else{
                itemStack.set(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, noAffixes(color));
            }
        }
    }

    public ItemNameAffixesComponent updateAffixes(World world){
        int color = this.sourceCustomDyeColor();
        if(!world.isClient) {
            DyeDatabaseSaverAndLoader serverState = DyeDatabaseSaverAndLoader.getServerState(((ServerWorld) world).getServer());
            Unidye.LOGGER.info(serverState.database.toString());
            if (serverState.database.containsKey(color)) {
                DyeData dyeData = serverState.database.get(color);
                Text prefix = Text.literal(dyeData.getPrefix());
                Text suffix = Text.literal(dyeData.getSuffix());
                if(!this.prefix().equals(prefix) || !this.suffix().equals(suffix)) {
                    return new ItemNameAffixesComponent(prefix, suffix, color);
                }else{
                    return this;
                }
            }
        }
        return noAffixes(color);
    }

    public static MutableText getName(ItemStack itemStack, String translationKey){
        if(itemStack.contains(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES)) {
            MutableText prefix = itemStack.get(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES).prefix().copy();
            MutableText suffix = itemStack.get(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES).suffix().copy();
            boolean isPrefixEmpty = prefix.equals(Text.empty());
            boolean isSuffixEmpty = suffix.equals(Text.empty());
            MutableText name = Text.empty();
            if(isPrefixEmpty && isSuffixEmpty) return null;
            if(!isPrefixEmpty) name.append(prefix).append(Text.literal(" "));
            name.append(Text.translatable(isPrefixEmpty ? translationKey + ".root_capitalized" : translationKey + ".root"));
            if(!isSuffixEmpty) name.append(Text.literal(" ")).append(suffix);
            return name;
        }
        return null;
    }

    public Text toText(){
        boolean isPrefixEmpty = prefix.equals(Text.empty());
        boolean isSuffixEmpty = suffix.equals(Text.empty());
        MutableText name = Text.empty();
        if(isPrefixEmpty) name.append("No Prefix");
        if(!isPrefixEmpty) name.append(prefix);
        name.append(Text.literal(" ")).append(Text.literal(String.valueOf(this.sourceCustomDyeColor()))).append(Text.literal(" "));
        if(!isSuffixEmpty) name.append(suffix);
        if(isSuffixEmpty) name.append("No Suffix");
        return name;
    }
}
