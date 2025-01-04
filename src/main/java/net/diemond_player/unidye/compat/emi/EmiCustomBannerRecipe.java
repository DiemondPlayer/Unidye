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

public class EmiCustomBannerRecipe extends EmiPatternCraftingRecipe {
    private static final List<DyeItem> DYES = Stream.of(DyeColor.values()).map(DyeItem::byColor).filter(c -> !(c instanceof CustomDyeItem)).toList();

    public EmiCustomBannerRecipe(Identifier id) {
        super(List.of(EmiStack.of(UnidyeBlocks.CUSTOM_WOOL), EmiStack.of(Items.STICK)), EmiStack.of(UnidyeItems.CUSTOM_BANNER), id, false);
    }

    @Override
    public SlotWidget getInputWidget(int slot, int x, int y) {
        if (slot < 6) {
            return new GeneratedSlotWidget(r -> {
                return EmiStack.of(UnidyeUtils.blendAndSetColor(new ItemStack(UnidyeBlocks.CUSTOM_WOOL), getDyes(r), Lists.newArrayList()));
            }, unique, x, y);
        } else if (slot == 7) {
            return new SlotWidget(EmiStack.of(Items.STICK), x, y);
        }
        return new GeneratedSlotWidget(r -> {
            return EmiStack.EMPTY;
        }, unique, x, y);
    }

    @Override
    public SlotWidget getOutputWidget(int x, int y) {
        return new GeneratedSlotWidget(r -> {
            ItemStack itemStack = UnidyeUtils.blendAndSetColor(new ItemStack(UnidyeBlocks.CUSTOM_BANNER), getDyes(r), Lists.newArrayList());
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
