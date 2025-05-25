package net.diemond_player.unidye.mixin.misc;

import net.diemond_player.unidye.util.UnidyeAccessor;
import net.minecraft.block.entity.SignText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(SignText.class)
public abstract class SignTextMixin implements UnidyeAccessor {
    @Unique
    private int customColor = 0xFFFFFF;

    @Override
    public int unidye$getCustomColor() {
        return customColor;
    }

    @Override
    public void unidye$setCustomColor(int customColor) {
        this.customColor = customColor;
    }
}
