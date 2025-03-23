package net.diemond_player.unidye.compat.emi;

import com.google.common.collect.Lists;
import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.GeneratedSlotWidget;
import dev.emi.emi.api.widget.SlotWidget;
import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.item.UnidyeItems;
import net.diemond_player.unidye.item.custom.CustomDyeItem;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmiCustomCircleDyeingRecipe extends EmiPatternCraftingRecipe {
    private static final List<DyeItem> DYES = Stream.of(DyeColor.values()).map(DyeItem::byColor).filter(c -> !(c instanceof CustomDyeItem)).toList();
    private final ItemConvertible itemOutput;
    private final TagKey<Item> itemTag;
    private final ArrayList<Item> acceptedItems;

    public EmiCustomCircleDyeingRecipe(TagKey<Item> itemTag, ItemConvertible itemOutput, Identifier id) {
        super(List.of(EmiIngredient.of(Arrays.stream(Ingredient.fromTag(itemTag).getMatchingStacks()).map(i -> (EmiIngredient) EmiStack.of(i)).collect(Collectors.toList())),
                EmiStack.of(UnidyeItems.CUSTOM_DYE)), EmiStack.of(itemOutput), id, false);
        this.itemOutput = itemOutput;
        this.itemTag = itemTag;
        this.acceptedItems = null;
    }

    public EmiCustomCircleDyeingRecipe(ArrayList<Item> acceptedItems, ItemConvertible itemOutput, Identifier id) {
        super(List.of(EmiIngredient.of(acceptedItems.stream().map(EmiStack::of).toList()),
                EmiStack.of(UnidyeItems.CUSTOM_DYE)), EmiStack.of(itemOutput), id, false);
        this.itemOutput = itemOutput;
        this.itemTag = null;
        this.acceptedItems = acceptedItems;
    }

    @Override
    public SlotWidget getInputWidget(int slot, int x, int y) {
        if (slot == 4) {
            return new GeneratedSlotWidget(r -> EmiStack.of(UnidyeUtils.blendAndSetColor(new ItemStack(UnidyeItems.CUSTOM_DYE), getDyes(r), Lists.newArrayList())), unique, x, y);
        } else {
            if(itemTag != null) {
                return new SlotWidget(EmiIngredient.of(itemTag), x, y);
            } else if (acceptedItems != null) {
                return new SlotWidget(EmiIngredient.of(acceptedItems.stream().map(EmiStack::of).toList()), x, y);
            }
            Unidye.LOGGER.warn("Error loading EMI special recipe display for {} in ({}, {}) slot", this.id, x/18 + 1, y/18 + 1);
            return new SlotWidget(EmiStack.of(ItemStack.EMPTY), x, y);
        }
    }

    @Override
    public SlotWidget getOutputWidget(int x, int y) {
        return new GeneratedSlotWidget(r -> {
            ItemStack itemStack = UnidyeUtils.blendAndSetColor(new ItemStack(itemOutput), getDyes(r), Lists.newArrayList());
            itemStack.setCount(8);
            return EmiStack.of(itemStack);
        }, unique, x, y);
    }

    private List<DyeItem> getDyes(Random random) {
        List<DyeItem> dyes = Lists.newArrayList();
        int amount = 2 + random.nextInt(7);
        for (int i = 0; i < amount; i++) {
            dyes.add(DYES.get(random.nextInt(DYES.size())));
        }
        return dyes;
    }
}
