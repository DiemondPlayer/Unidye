package net.diemond_player.unidye.mixin.gameplay;

import com.llamalad7.mixinextras.sugar.Local;
import net.diemond_player.unidye.component.ItemNamePrefixComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.registry.UnidyeItems;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @ModifyVariable(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;DDD)V", at = @At("HEAD"),
            ordinal = 0, argsOnly = true)
    private static ItemStack unidye$init(ItemStack itemStack, @Local(argsOnly = true) World world) {
        if (itemStack.isOf(UnidyeItems.CUSTOM_DYE)) {
            ItemNamePrefixComponent.updateCustomDyePrefix(itemStack, world);
        } else if (itemStack.contains(UnidyeDataComponentTypes.ITEM_NAME_PREFIX)){
            ItemNamePrefixComponent.updatePrefix(itemStack, world);
        }
        return itemStack;
    }
}
