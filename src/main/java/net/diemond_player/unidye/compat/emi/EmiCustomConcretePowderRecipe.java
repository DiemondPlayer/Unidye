package net.diemond_player.unidye.compat.emi;

import com.google.common.collect.Lists;
import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.GeneratedSlotWidget;
import dev.emi.emi.api.widget.SlotWidget;
import net.diemond_player.unidye.block.UnidyeBlocks;
import net.diemond_player.unidye.item.UnidyeItems;
import net.diemond_player.unidye.item.custom.CustomDyeItem;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class EmiCustomConcretePowderRecipe extends EmiPatternCraftingRecipe {
    private static final List<DyeItem> DYES = Stream.of(DyeColor.values()).map(DyeItem::byColor).filter(c -> !(c instanceof CustomDyeItem)).toList();
    public EmiCustomConcretePowderRecipe(Identifier id) {
        super(List.of(EmiStack.of(Items.SAND), EmiStack.of(Items.GRAVEL), EmiStack.of(UnidyeItems.CUSTOM_DYE)), EmiStack.of(UnidyeBlocks.CUSTOM_CONCRETE_POWDER), id);
    }

    @Override
    public SlotWidget getInputWidget(int slot, int x, int y) {
        if (slot == 0) {
            return new GeneratedSlotWidget(r -> {
                return EmiStack.of(UnidyeUtils.blendAndSetColor(new ItemStack(UnidyeItems.CUSTOM_DYE), getDyes(r), Lists.newArrayList()));
            }, unique, x, y);
        } else if (slot < 5) {
            return new SlotWidget(EmiStack.of(Items.SAND), x, y);
        }
        return new SlotWidget(EmiStack.of(Items.GRAVEL), x, y);
    }

    @Override
    public SlotWidget getOutputWidget(int x, int y) {
        return new GeneratedSlotWidget(r -> {
            ItemStack itemStack = UnidyeUtils.blendAndSetColor(new ItemStack(UnidyeBlocks.CUSTOM_CONCRETE_POWDER), getDyes(r), Lists.newArrayList());
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
