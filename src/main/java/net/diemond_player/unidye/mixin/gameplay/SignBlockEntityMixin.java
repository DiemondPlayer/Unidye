package net.diemond_player.unidye.mixin.gameplay;

import net.diemond_player.unidye.util.UnidyeAccessor;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SignBlockEntity.class)
public abstract class SignBlockEntityMixin implements UnidyeAccessor {
    @Unique
    private int customColorFront = 0xFFFFFF;
    @Unique
    private int customColorBack = 0xFFFFFF;

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void unidye$writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo ci) {
        nbt.putInt("unidye.custom_color_front", customColorFront);
        nbt.putInt("unidye.custom_color_back", customColorBack);
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void unidye$readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo ci) {
        if (nbt.contains("unidye.custom_color_front")) {
            this.customColorFront = nbt.getInt("unidye.custom_color_front");
        }
        if (nbt.contains("unidye.custom_color_back")) {
            this.customColorBack = nbt.getInt("unidye.custom_color_back");
        }
    }

    @Override
    public int unidye$getCustomColor() {
        return customColorFront;
    }

    @Override
    public void unidye$setCustomColor(int customColorFront) {
        this.customColorFront = customColorFront;
    }

    @Override
    public int unidye$getSecondaryCustomColor() {
        return customColorBack;
    }

    @Override
    public void unidye$setSecondaryCustomColor(int customColorBack) {
        this.customColorBack = customColorBack;
    }
}
