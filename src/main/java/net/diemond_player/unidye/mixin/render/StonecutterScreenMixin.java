package net.diemond_player.unidye.mixin.render;

import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.StonecutterScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.StonecutterScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(StonecutterScreen.class)
public abstract class StonecutterScreenMixin extends HandledScreen<StonecutterScreenHandler> {

    public StonecutterScreenMixin(StonecutterScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @ModifyArg(method = "renderRecipeIcons", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawItem(Lnet/minecraft/item/ItemStack;II)V"))
    public ItemStack unidye$renderRecipeIcons(ItemStack itemStack) {
        if(Unidye.STONECUTTER_PRESERVE_COLOR.contains(itemStack.getItem())){
            ItemStack stack = this.handler.getSlot(0).getStack();
            if(itemStack.contains(DataComponentTypes.DYED_COLOR)) itemStack.set(DataComponentTypes.DYED_COLOR, stack.get(DataComponentTypes.DYED_COLOR));
            if(itemStack.contains(UnidyeDataComponentTypes.MATERIAL_COLORS)) itemStack.set(UnidyeDataComponentTypes.MATERIAL_COLORS, stack.get(UnidyeDataComponentTypes.MATERIAL_COLORS));
            if(itemStack.contains(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES)) itemStack.set(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, stack.get(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES));
            if(itemStack.contains(DataComponentTypes.PROFILE)) itemStack.set(DataComponentTypes.PROFILE, stack.get(DataComponentTypes.PROFILE));
        }
        return itemStack;
    }
}