package net.diemond_player.unidye.compat.emi;

import com.google.common.collect.Lists;
import dev.emi.emi.EmiPort;
import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.GeneratedSlotWidget;
import dev.emi.emi.api.widget.SlotWidget;
import net.diemond_player.unidye.block.UnidyeBlocks;
import net.diemond_player.unidye.block.entity.UnidyeBlockEntities;
import net.diemond_player.unidye.item.UnidyeItems;
import net.diemond_player.unidye.item.custom.CustomDyeItem;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class EmiCustomShieldDecorationRecipe extends EmiPatternCraftingRecipe {

    private static final List<DyeItem> DYES = Stream.of(DyeColor.values()).map(DyeItem::byColor).filter(c -> !(c instanceof CustomDyeItem)).toList();
    private EmiStack banner;

    public EmiCustomShieldDecorationRecipe(Identifier id) {
        super(List.of(
                EmiStack.of(UnidyeItems.CUSTOM_BANNER),
                EmiStack.of(Items.SHIELD)), EmiStack.of(Items.SHIELD), id);
    }

    @Override
    public SlotWidget getInputWidget(int slot, int x, int y) {
        if (slot == 0) {
            return new SlotWidget(EmiStack.of(Items.SHIELD), x, y);
        } else if (slot == 1) {
            return new GeneratedSlotWidget(r -> {
                banner = getPattern(r);
                return banner;
            }, unique, x, y);
        }
        return new SlotWidget(EmiStack.EMPTY, x, y);
    }

    @Override
    public SlotWidget getOutputWidget(int x, int y) {
        return new GeneratedSlotWidget(r -> getShield(r), unique, x, y);
    }

    public EmiStack getPattern(Random random) {
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
        return EmiStack.of(stack);
    }

    public EmiStack getShield(Random random) {
        ItemStack stack = banner.getItemStack();
        ItemStack stack2 = new ItemStack(Items.SHIELD);
        NbtCompound nbtCompound = BlockItem.getBlockEntityNbt(stack);
        NbtCompound nbtCompound2 = nbtCompound == null ? new NbtCompound() : nbtCompound.copy();
        nbtCompound2.putInt("Base", UnidyeUtils.getColor(stack));
        nbtCompound2.putBoolean("CustomColored", true);
        BlockItem.setBlockEntityNbt(stack2, UnidyeBlockEntities.DYEABLE_BANNER_BE, nbtCompound2);
        return EmiStack.of(stack2);
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
