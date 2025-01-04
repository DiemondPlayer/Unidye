package net.diemond_player.unidye.mixin;

import net.diemond_player.unidye.util.UnidyeAccessor;
import net.minecraft.block.entity.SignText;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AbstractSignEditScreen.class)
public abstract class AbstractSignEditScreenMixin {

    @Shadow
    private SignText text;

    @ModifyVariable(method = "renderSignText", at = @At(value = "STORE"), ordinal = 0)
    private int unidye$renderSignText(int value) {
        if (((UnidyeAccessor) this.text).unidye$getCustomColor() != 0xFFFFFF) {
            return ((UnidyeAccessor) this.text).unidye$getCustomColor();
        }
        return value;
    }
}
