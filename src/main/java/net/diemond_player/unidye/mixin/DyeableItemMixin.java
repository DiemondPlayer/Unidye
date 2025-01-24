package net.diemond_player.unidye.mixin;

import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.item.DyeItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.ArrayList;
import java.util.List;

@Mixin(DyeableItem.class)
public interface DyeableItemMixin {
    /**
     * @author Diemond_Player
     * @reason redirects to Unidye algorithm
     */
    //TODO change this
    @Overwrite
    static ItemStack blendAndSetColor(ItemStack stack, List<DyeItem> colors) {
        return UnidyeUtils.blendAndSetColor(stack, colors, new ArrayList<>());
    }
}
