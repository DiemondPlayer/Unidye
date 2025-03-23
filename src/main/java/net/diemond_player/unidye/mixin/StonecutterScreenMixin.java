package net.diemond_player.unidye.mixin;

import net.diemond_player.unidye.Unidye;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.StonecutterScreen;
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
            itemStack.setNbt(this.handler.getSlot(0).getStack().getOrCreateNbt());
        }
        return itemStack;
    }
}