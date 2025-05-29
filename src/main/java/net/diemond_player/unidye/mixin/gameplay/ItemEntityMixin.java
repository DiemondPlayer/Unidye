package net.diemond_player.unidye.mixin.gameplay;

import com.llamalad7.mixinextras.sugar.Local;
import net.diemond_player.unidye.component.CustomBannerPatternsComponent;
import net.diemond_player.unidye.component.ItemNameAffixesComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
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
        if (itemStack.contains(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES)){
            ItemNameAffixesComponent.updateAffixes(itemStack, world);
        }
        if (itemStack.contains(UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS)){
            CustomBannerPatternsComponent.Builder builder = new CustomBannerPatternsComponent.Builder();
            for(CustomBannerPatternsComponent.Layer layer : itemStack.get(UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS).layers()){
                ItemNameAffixesComponent itemNameAffixesComponent = layer.itemNameAffixesComponent().updateAffixes(world);
                builder.add(layer.pattern(), layer.color(), itemNameAffixesComponent);
            }
            itemStack.set(UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS, builder.build());
        }
        return itemStack;
    }
}
