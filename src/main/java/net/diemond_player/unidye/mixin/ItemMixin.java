package net.diemond_player.unidye.mixin;

import net.diemond_player.unidye.item.CustomDyeItem;
import net.diemond_player.unidye.util.UnidyeMaterialType;
import net.diemond_player.unidye.util.UnidyeMaterialTypes;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "appendTooltip", at = @At("HEAD"))
    public void unidye$appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
        if (stack.getItem() instanceof DyeItem dyeItem && !(stack.getItem() instanceof CustomDyeItem)) {
            if (Screen.hasShiftDown()) {
                DyeColor dyeColor = dyeItem.getColor();
                for(Map.Entry<Identifier, UnidyeMaterialType> entry : UnidyeMaterialTypes.MATERIAL_TYPES.entrySet()){
                    Identifier id = entry.getKey();
                    UnidyeMaterialType type = entry.getValue();
                    if(type.materialColors.containsKey(dyeColor)) {
                        MutableText mutableText = Text.literal("■ ");
                        tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(type.getColor(dyeColor))).append(Text.translatable("tooltip." + id.getNamespace() + "." + id.getPath() + "_color").append(String.format("#%06X", (0xFFFFFF & type.getColor(dyeColor)))).formatted(Formatting.GRAY)));
                    }
                }
            } else {
                tooltip.add(Text.translatable("tooltip.unidye.press_shift"));
            }
        }
    }
}
