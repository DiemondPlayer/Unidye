package net.diemond_player.unidye.component;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

import static net.minecraft.item.ItemStack.ITEM_CODEC;

public record RecipeStacksComponent(List<Stack> stacks) {
    public static final RecipeStacksComponent DEFAULT = new RecipeStacksComponent(List.of());
    public static final Codec<RecipeStacksComponent> CODEC = Stack.CODEC
            .listOf()
            .xmap(RecipeStacksComponent::new, RecipeStacksComponent::stacks);
    public static final PacketCodec<RegistryByteBuf, RecipeStacksComponent> PACKET_CODEC = Stack.PACKET_CODEC
            .collect(PacketCodecs.toList())
            .xmap(RecipeStacksComponent::new, RecipeStacksComponent::stacks);

    public List<ItemStack> toItemStacks(){
        return stacks.stream().map(i -> {
            ItemStack itemStack1 = new ItemStack(i.item());
            itemStack1.applyChanges(i.componentChanges());
            return itemStack1;
        }).toList();
    }

    public static RecipeStacksComponent fromItemStacks(List<ItemStack> itemStacks){
        return fromItemStacks(itemStacks, false);
    }

    public static RecipeStacksComponent fromItemStacks(List<ItemStack> itemStacks, boolean shapeless){
        List<Stack> stackList = new ArrayList<>(List.of());
        for(ItemStack itemStack : itemStacks){
            if(itemStack.isEmpty()) continue;
            stackList.add(new Stack(itemStack.getRegistryEntry(), itemStack.getComponentChanges()));
        }
        if (shapeless) stackList.sort(Comparator.comparing(stack -> stack.item().getIdAsString()));
        return new RecipeStacksComponent(stackList);
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
