package net.diemond_player.unidye.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.diemond_player.unidye.util.UnidyeAccessor;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.client.render.entity.feature.WolfCollarFeatureRenderer;
import net.minecraft.entity.passive.WolfEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(WolfCollarFeatureRenderer.class)
public abstract class WolfCollarFeatureRendererMixin {

    @ModifyArgs(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/WolfEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/WolfEntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"))
    private void unidye$render(Args args, @Local(argsOnly = true) WolfEntity wolfEntity) {
        UnidyeAccessor wolf = (UnidyeAccessor) wolfEntity;
        int customColor = wolf.unidye$getCustomColor();
        if (customColor != 0xFFFFFF) {
            float[] fs = UnidyeUtils.getColorArray(customColor);
            args.set(4, fs[0]);
            args.set(5, fs[1]);
            args.set(6, fs[2]);
        }
    }
}