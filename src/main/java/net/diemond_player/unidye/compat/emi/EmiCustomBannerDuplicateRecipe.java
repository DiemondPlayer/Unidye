package net.diemond_player.unidye.compat.emi;

import com.google.common.collect.Lists;
import dev.emi.emi.EmiPort;
import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.GeneratedSlotWidget;
import dev.emi.emi.api.widget.SlotWidget;
import net.diemond_player.unidye.registry.UnidyeBlocks;
import net.diemond_player.unidye.registry.UnidyeBlockEntities;
import net.diemond_player.unidye.registry.UnidyeItems;
import net.diemond_player.unidye.item.CustomDyeItem;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class EmiCustomBannerDuplicateRecipe extends EmiPatternCraftingRecipe {

    private static final List<DyeItem> DYES = Stream.of(DyeColor.values()).map(DyeItem::byColor).filter(c -> !(c instanceof CustomDyeItem)).toList();

    public EmiCustomBannerDuplicateRecipe(Identifier id) {
        super(List.of(
                        EmiStack.of(UnidyeItems.CUSTOM_BANNER),
                        EmiStack.of(UnidyeItems.CUSTOM_BANNER).setRemainder(EmiStack.of(UnidyeItems.CUSTOM_BANNER))),
                EmiStack.of(UnidyeItems.CUSTOM_BANNER), id);
    }

    @Override
    public SlotWidget getInputWidget(int slot, int x, int y) {
        if (slot == 0) {
            return new GeneratedSlotWidget(r -> {
                ItemStack itemStack = UnidyeUtils.blendAndSetColor(new ItemStack(UnidyeBlocks.CUSTOM_BANNER), getDyes(r), Lists.newArrayList());
                return EmiStack.of(itemStack);
            }, unique, x, y);
        } else if (slot == 1) {
            return new GeneratedSlotWidget(r -> getPattern(r, true), unique, x, y);
        }
        return new SlotWidget(EmiStack.EMPTY, x, y);
    }

    @Override
    public SlotWidget getOutputWidget(int x, int y) {
        return new GeneratedSlotWidget(r -> getPattern(r, false) , unique, x, y);
    }

    public EmiStack getPattern(Random random, boolean reminder) {
        ItemStack stack = UnidyeUtils.blendAndSetColor(new ItemStack(UnidyeBlocks.CUSTOM_BANNER), getDyes(random), Lists.newArrayList());
        int patterns = 1 + Math.max(random.nextInt(5), random.nextInt(3));
        BannerPattern.Patterns pattern = new BannerPattern.Patterns();
        for (int i = 0; i < patterns; i++) {
            pattern = EmiPort.addRandomBanner(pattern, random);
        }

        NbtCompound tag = new NbtCompound();
        tag.put("Patterns", pattern.toNbt());

        BlockItem.setBlockEntityNbt(stack, UnidyeBlockEntities.DYEABLE_BANNER_BE, tag);
        //stack.setNbt(tag);
        EmiStack emiStack = EmiStack.of(stack);
        if (reminder) {
            emiStack.setRemainder(EmiStack.of(stack));
        }
        return emiStack;
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
