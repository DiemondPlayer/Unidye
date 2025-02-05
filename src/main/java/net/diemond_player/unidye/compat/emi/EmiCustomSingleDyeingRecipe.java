package net.diemond_player.unidye.compat.emi;

import com.google.common.collect.Lists;
import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.GeneratedSlotWidget;
import dev.emi.emi.api.widget.SlotWidget;
import net.diemond_player.unidye.item.custom.CustomDyeItem;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EmiCustomSingleDyeingRecipe extends EmiPatternCraftingRecipe {
    private static final List<DyeItem> DYES = Stream.of(DyeColor.values()).map(DyeItem::byColor).filter(c -> !(c instanceof CustomDyeItem)).toList();
    private final Item item_output;
    private final TagKey<Item> tag;

    public EmiCustomSingleDyeingRecipe(TagKey<Item> tag, Item item, Identifier id) {
        super(List.of(
                EmiIngredient.of(DYES.stream().map(i -> (EmiIngredient) EmiStack.of(i)).collect(Collectors.toList())),
                EmiIngredient.of(tag)), EmiStack.of(item), id);
        this.tag = tag;
        this.item_output = item;
    }

    @Override
    public SlotWidget getInputWidget(int slot, int x, int y) {
        if (slot == 0) {
            return new SlotWidget(EmiIngredient.of(tag), x, y);
        } else {
            final int s = slot - 1;
            return new GeneratedSlotWidget(r -> {
                List<DyeItem> dyes = getDyes(r);
                if (s < dyes.size()) {
                    return EmiStack.of(dyes.get(s));
                }
                return EmiStack.EMPTY;
            }, unique, x, y);
        }
    }

    @Override
    public SlotWidget getOutputWidget(int x, int y) {
        return new GeneratedSlotWidget(r -> EmiStack.of(UnidyeUtils.blendAndSetColor(new ItemStack(item_output), getDyes(r), Lists.newArrayList())), unique, x, y);
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
