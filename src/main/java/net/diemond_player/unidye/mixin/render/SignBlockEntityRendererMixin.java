package net.diemond_player.unidye.mixin.render;

import com.llamalad7.mixinextras.sugar.Local;
import net.diemond_player.unidye.util.UnidyeAccessor;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(SignBlockEntityRenderer.class)
public abstract class SignBlockEntityRendererMixin {

    @ModifyArg(method = "renderText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/OrderedText;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)I"), index = 3)
    private int unidye$renderText1(int color, @Local(argsOnly = true) SignText signText) {
        UnidyeAccessor unidyeAccessor = (UnidyeAccessor) signText;
        if (unidyeAccessor.unidye$getCustomColor() != 0xFFFFFF) {
            int color2 = unidyeAccessor.unidye$getCustomColor();
            int j = (int) ((double) ColorHelper.Argb.getRed(color2) * 0.4);
            int k = (int) ((double) ColorHelper.Argb.getGreen(color2) * 0.4);
            int l = (int) ((double) ColorHelper.Argb.getBlue(color2) * 0.4);
            return ColorHelper.Argb.getArgb(0, j, k, l);
        }
        return color;
    }

    @ModifyArgs(method = "renderText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawWithOutline(Lnet/minecraft/text/OrderedText;FFIILorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private void unidye$renderText(Args args, @Local(argsOnly = true) SignText signText) {
        UnidyeAccessor unidyeAccessor = (UnidyeAccessor) signText;
        if (unidyeAccessor.unidye$getCustomColor() != 0xFFFFFF) {
            int color2 = unidyeAccessor.unidye$getCustomColor();
            int j = (int) ((double) ColorHelper.Argb.getRed(color2) * 0.4);
            int k = (int) ((double) ColorHelper.Argb.getGreen(color2) * 0.4);
            int l = (int) ((double) ColorHelper.Argb.getBlue(color2) * 0.4);
            args.set(3, color2);
            args.set(4, ColorHelper.Argb.getArgb(0, j, k, l));
        }
    }

    @ModifyArg(method = "render(Lnet/minecraft/block/entity/SignBlockEntity;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/block/BlockState;Lnet/minecraft/block/AbstractSignBlock;Lnet/minecraft/block/WoodType;Lnet/minecraft/client/model/Model;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/SignBlockEntityRenderer;renderText(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/SignText;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IIIZ)V", ordinal = 0), index = 1)
    private SignText unidye$render(SignText signText, @Local(argsOnly = true) SignBlockEntity signBlockEntity) {
        UnidyeAccessor unidyeAccessor = (UnidyeAccessor) signText;
        UnidyeAccessor unidyeAccessor2 = (UnidyeAccessor) signBlockEntity;
        unidyeAccessor.unidye$setCustomColor(unidyeAccessor2.unidye$getCustomColor());
        return signText;
    }

    @ModifyArg(method = "render(Lnet/minecraft/block/entity/SignBlockEntity;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/block/BlockState;Lnet/minecraft/block/AbstractSignBlock;Lnet/minecraft/block/WoodType;Lnet/minecraft/client/model/Model;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/SignBlockEntityRenderer;renderText(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/SignText;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IIIZ)V", ordinal = 1), index = 1)
    private SignText unidye$render1(SignText signText, @Local(argsOnly = true) SignBlockEntity signBlockEntity) {
        UnidyeAccessor unidyeAccessor = (UnidyeAccessor) signText;
        UnidyeAccessor unidyeAccessor2 = (UnidyeAccessor) signBlockEntity;
        unidyeAccessor.unidye$setCustomColor(unidyeAccessor2.unidye$getSecondaryCustomColor());
        return signText;
    }
}
