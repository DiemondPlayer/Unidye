package net.diemond_player.unidye.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.diemond_player.unidye.item.UnidyeItemGroups;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @ModifyArg(method = "renderBakedItemQuads", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumer;quad(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/model/BakedQuad;FFFFII)V"), index = 5)
    private float renderBakedItemQuads(float alpha, @Local(argsOnly = true) ItemStack stack) {
        if(UnidyeItemGroups.UNIDYE_GROUP.contains(stack.getItem().getDefaultStack())) {
            return 1.0f;
        }
        return alpha;
    }
}