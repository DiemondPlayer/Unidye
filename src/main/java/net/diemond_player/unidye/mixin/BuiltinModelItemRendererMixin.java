package net.diemond_player.unidye.mixin;

import net.diemond_player.unidye.block.DyeableBannerBlock;
import net.diemond_player.unidye.block.DyeableBedBlock;
import net.diemond_player.unidye.block.DyeableShulkerBoxBlock;
import net.diemond_player.unidye.block.DyeableWallBannerBlock;
import net.diemond_player.unidye.block.entity.DyeableBannerBlockEntity;
import net.diemond_player.unidye.block.entity.DyeableBedBlockEntity;
import net.diemond_player.unidye.block.entity.DyeableShulkerBoxBlockEntity;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.entity.client.renderer.DyeableBannerBlockEntityRenderer;
import net.diemond_player.unidye.registry.UnidyeBlocks;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.Block;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltinModelItemRenderer.class)
public abstract class BuiltinModelItemRendererMixin {
    @Mutable
    @Final
    @Shadow
    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    @Shadow
    private ShieldEntityModel modelShield;

    protected BuiltinModelItemRendererMixin(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
        this.blockEntityRenderDispatcher = blockEntityRenderDispatcher;
    }

    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    private void unidye$render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        Item item = stack.getItem();
        if (item == Items.SHIELD) {
            if(stack.contains(UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS)
                    && stack.contains(DataComponentTypes.DYED_COLOR)) {
                matrices.push();
                matrices.scale(1.0F, -1.0F, -1.0F);
                SpriteIdentifier spriteIdentifier = ModelLoader.SHIELD_BASE;
                VertexConsumer vertexConsumer = spriteIdentifier.getSprite()
                        .getTextureSpecificVertexConsumer(
                                ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, this.modelShield.getLayer(spriteIdentifier.getAtlasId()), true, stack.hasGlint())
                        );
                this.modelShield.getHandle().render(matrices, vertexConsumer, light, overlay);
                DyeableBannerBlockEntityRenderer.renderCanvas(
                        matrices,
                        vertexConsumers,
                        light,
                        overlay,
                        this.modelShield.getPlate(),
                        ModelLoader.SHIELD_BASE,
                        false,
                        UnidyeUtils.getColor(stack),
                        stack.get(UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS),
                        stack.hasGlint()
                );
                matrices.pop();
                ci.cancel();
            }
        }
    }
}
