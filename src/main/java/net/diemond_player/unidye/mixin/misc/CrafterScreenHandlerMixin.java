package net.diemond_player.unidye.mixin.misc;

import com.llamalad7.mixinextras.sugar.Local;
import net.diemond_player.unidye.component.ItemNameAffixesComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.CrafterScreenHandler;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CrafterScreenHandler.class)
public abstract class CrafterScreenHandlerMixin {
    @ModifyArg(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/CraftingResultInventory;setStack(ILnet/minecraft/item/ItemStack;)V"), index = 1)
    private ItemStack unidye$updateResult(ItemStack itemStack, @Local World world) {
        if (itemStack.contains(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES)){
            ItemNameAffixesComponent.updateAffixes(itemStack, world);
        }
        return itemStack;
    }
}
