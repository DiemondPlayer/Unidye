package net.diemond_player.unidye.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static net.minecraft.item.ItemStack.ITEM_CODEC;

@SuppressWarnings("deprecation")
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
        return stacks.stream().map(Stack::toItemStack).toList();
    }

    public static RecipeStacksComponent fromItemStacks(List<ItemStack> itemStacks, int outputAmount){
        return fromItemStacks(itemStacks, outputAmount, false);
    }

    public static RecipeStacksComponent fromItemStacks(List<ItemStack> itemStacks, int outputAmount, boolean shapeless){
        List<Stack> stackList = new ArrayList<>(List.of());
        for(ItemStack itemStack : itemStacks){
            ItemStack itemStackCopy = itemStack.copy();
            if(itemStackCopy.isEmpty()) continue;
            if(itemStackCopy.isOf(Items.STICK) && Unidye.POLYMORPH) continue;
            if(itemStackCopy.contains(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES)) {
                ItemNameAffixesComponent itemNamePrefixComponent = itemStackCopy.get(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES);
                itemStackCopy.set(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, ItemNameAffixesComponent.noAffixes(itemNamePrefixComponent.sourceCustomDyeColor()));
            }
            stackList.add(new Stack(itemStackCopy.getRegistryEntry(), itemStackCopy.getComponentChanges()));
        }
        if (shapeless) {
            stackList.sort(Comparator.comparing(stack -> stack.item().getIdAsString()));
            stackList.sort(Comparator.comparing(stack -> {
                if(stack.componentChanges.get(DataComponentTypes.DYED_COLOR) == null) return 1;
                if(stack.componentChanges.get(DataComponentTypes.DYED_COLOR).isEmpty()) return 1;
                return stack.componentChanges.get(DataComponentTypes.DYED_COLOR).get().rgb();
            }));
        }
        return new RecipeStacksComponent(stackList, outputAmount, shapeless);
    }

    public RecipeStacksComponent optimizeTagToFallback(TagKey<Item> tag, ItemConvertible fallbackItem){
        List<Stack> optimizedStackList = Lists.newArrayList();
        for (Stack stack : stacks){
            if(stack.item().value().getDefaultStack().isIn(tag)) {
                optimizedStackList.add(new Stack(fallbackItem.asItem().getRegistryEntry(), ComponentChanges.EMPTY));
            }else{
                optimizedStackList.add(stack);
            }
        }
        return new RecipeStacksComponent(optimizedStackList, this.outputAmount(), this.shapeless());
    }

    public RecipeStacksComponent optimizeAcceptedItemsToFallback(ArrayList<Item> acceptedItems, ItemConvertible fallbackItem){
        List<Stack> optimizedStackList = Lists.newArrayList();
        for (Stack stack : stacks){
            if(acceptedItems.contains(stack.item().value())) {
                optimizedStackList.add(new Stack(fallbackItem.asItem().getRegistryEntry(), ComponentChanges.EMPTY));
            }else{
                optimizedStackList.add(stack);
            }
        }
        return new RecipeStacksComponent(optimizedStackList, this.outputAmount(), this.shapeless());
    }

    //should only be called when recipe stacks are sorted (aka shapeless recipe) and when ...
    public RecipeStacksComponent optimizeRecipeStacks(){
        Stack referenceStack = null;
        int referenceDenominator = 0;
        int currentDenominator = 0;
        for (Stack stack : stacks) {
            if (stack.item().value() == Items.STICK) continue;
            if (referenceStack == null || (referenceStack.item() == stack.item && referenceStack.componentChanges().equals(stack.componentChanges))) {
                referenceStack = stack;
                currentDenominator++;
            } else {
                referenceStack = stack;
                if (referenceDenominator == 0 || referenceDenominator == currentDenominator) {
                    referenceDenominator = currentDenominator;
                } else {
                    referenceDenominator = 0;
                    break;
                }
                currentDenominator = 1;
            }
        }
        if(referenceDenominator > 1 && outputAmount % referenceDenominator == 0){
            List<Stack> optimizedStackList = Lists.newArrayList();
            int run = 0;
            for (Stack stack : stacks) {
                if (stack.item().value() == Items.STICK) {
                    optimizedStackList.add(stack);
                    continue;
                }
                if (run == 0){
                    optimizedStackList.add(stack);
                }
                run++;
                if(run == referenceDenominator) run = 0;
            }
            return new RecipeStacksComponent(optimizedStackList, outputAmount/referenceDenominator, shapeless);
        }
        return this;
    }

    public record Stack(RegistryEntry<Item> item, ComponentChanges componentChanges) {
        public static final Codec<Stack> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                ITEM_CODEC.fieldOf("id").forGetter(Stack::item),
                                ComponentChanges.CODEC.optionalFieldOf("components", ComponentChanges.EMPTY).forGetter(Stack::componentChanges)
                        )
                        .apply(instance, Stack::new)
        );
        public static final PacketCodec<RegistryByteBuf, Stack> PACKET_CODEC = PacketCodec.tuple(
                PacketCodecs.registryEntry(RegistryKeys.ITEM),
                Stack::item,
                ComponentChanges.PACKET_CODEC,
                Stack::componentChanges,
                Stack::new
        );

        public ItemStack toItemStack(){
            ItemStack itemStack1 = new ItemStack(this.item());
            itemStack1.applyChanges(this.componentChanges());
            return itemStack1;
        }
    }
}
