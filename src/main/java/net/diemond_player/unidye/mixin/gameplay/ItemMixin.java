package net.diemond_player.unidye.mixin.gameplay;

import net.diemond_player.unidye.component.ItemNameAffixesComponent;
import net.diemond_player.unidye.item.CustomDyeItem;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.registry.UnidyeMaterialTypes;
import net.diemond_player.unidye.util.UnidyeMaterialType;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "appendTooltip", at = @At("HEAD"))
    public void unidye$appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type, CallbackInfo ci) {
        if (stack.getItem() instanceof DyeItem dyeItem && !(stack.getItem() instanceof CustomDyeItem)) {
            if (Screen.hasShiftDown()) {
                DyeColor dyeColor = dyeItem.getColor();
                ArrayList<UnidyeMaterialType> materialTypes = new ArrayList<>(UnidyeMaterialTypes.MATERIAL_TYPE.stream().toList());
                materialTypes.sort(Comparator.comparing(materialColor -> materialColor.getId().toString()));
                for(UnidyeMaterialType materialType : materialTypes){
                    Identifier id = materialType.getId();
                    if(materialType.materialColors.containsKey(dyeColor)) {
                        MutableText mutableText = Text.literal("■ ");
                        tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(materialType.getColor(dyeColor))).append(Text.translatable("tooltip." + id.getNamespace() + "." + id.getPath() + "_color").append(String.format("#%06X", (0xFFFFFF & materialType.getColor(dyeColor)))).formatted(Formatting.GRAY)));
                    }
                }
            } else {
                tooltip.add(Text.translatable("tooltip.unidye.press_shift"));
            }
        }
    }

    @Inject(method = "getName(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/text/Text;", at = @At(value = "HEAD"), cancellable = true)
    private void unidye$getName(ItemStack stack, CallbackInfoReturnable<Text> cir) {
        if(!(stack.getItem() instanceof ShieldItem)) return;
        if (!stack.contains(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES)) return;
        String translationKey = stack.getItem().getTranslationKey(stack);
        if(translationKey.equals("item.unidye.shield_custom_color")) {
            Text text = ItemNameAffixesComponent.getName(stack, translationKey);
            if(!Objects.equals(text, Text.empty())) cir.setReturnValue(text);
        }
    }
}
