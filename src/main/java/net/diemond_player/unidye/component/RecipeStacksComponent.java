package net.diemond_player.unidye.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static net.minecraft.item.ItemStack.ITEM_CODEC;

public record RecipeStacksComponent(List<Stack> stacks, int outputAmount, boolean shapeless) {
    public static final RecipeStacksComponent DEFAULT = new RecipeStacksComponent(List.of(), 1, false);
    public static final Codec<RecipeStacksComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                            Stack.CODEC.listOf().fieldOf("stacks").forGetter(RecipeStacksComponent::stacks),
                            Codec.intRange(0, 64).fieldOf("outputAmount").forGetter(RecipeStacksComponent::outputAmount),
                            Codec.BOOL.fieldOf("shapeless").forGetter(RecipeStacksComponent::shapeless)
                    )
                    .apply(instance, RecipeStacksComponent::new));
    public static final PacketCodec<RegistryByteBuf, RecipeStacksComponent> PACKET_CODEC = PacketCodec.tuple(
            Stack.PACKET_CODEC.collect(PacketCodecs.toList()),
            RecipeStacksComponent::stacks,
            PacketCodecs.codec(Codec.intRange(0, 64)),
            RecipeStacksComponent::outputAmount,
            PacketCodecs.BOOL,
            RecipeStacksComponent::shapeless,
            RecipeStacksComponent::new
    );

    public List<ItemStack> toItemStacks(){
        return stacks.stream().map(i -> {
            ItemStack itemStack1 = new ItemStack(i.item());
            itemStack1.applyChanges(i.componentChanges());
            return itemStack1;
        }).toList();
    }

    public static RecipeStacksComponent fromItemStacks(List<ItemStack> itemStacks, int outputAmount){
        return fromItemStacks(itemStacks, outputAmount, false);
    }

    public static RecipeStacksComponent fromItemStacks(List<ItemStack> itemStacks, int outputAmount, boolean shapeless){
        List<Stack> stackList = new ArrayList<>(List.of());
        for(ItemStack itemStack : itemStacks){
            if(itemStack.isEmpty()) continue;
            stackList.add(new Stack(itemStack.getRegistryEntry(), itemStack.getComponentChanges()));
        }
        if (shapeless) stackList.sort(Comparator.comparing(stack -> stack.item().getIdAsString()));
        return new RecipeStacksComponent(stackList, outputAmount, shapeless);
    }

    public record Stack(RegistryEntry<Item> item, ComponentChanges componentChanges) {
        public static final Codec<RecipeStacksComponent.Stack> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                ITEM_CODEC.fieldOf("id").forGetter(RecipeStacksComponent.Stack::item),
                                ComponentChanges.CODEC.optionalFieldOf("components", ComponentChanges.EMPTY).forGetter(RecipeStacksComponent.Stack::componentChanges)
                        )
                        .apply(instance, RecipeStacksComponent.Stack::new)
        );
        public static final PacketCodec<RegistryByteBuf, RecipeStacksComponent.Stack> PACKET_CODEC = PacketCodec.tuple(
                PacketCodecs.registryEntry(RegistryKeys.ITEM),
                RecipeStacksComponent.Stack::item,
                ComponentChanges.PACKET_CODEC,
                RecipeStacksComponent.Stack::componentChanges,
                RecipeStacksComponent.Stack::new
        );
    }
}
