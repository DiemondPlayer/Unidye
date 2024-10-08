package net.diemond_player.unidye.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.diemond_player.unidye.block.entity.DyeableBannerBlockEntity;
import net.diemond_player.unidye.util.IEntityAccessor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.MapRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.map.MapBannerMarker;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.BlockView;
import org.joml.Matrix4f;
import org.lwjgl.opengl.INTELMapTexture;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Objects;

@Mixin(targets = "net.minecraft.client.render.MapRenderer$MapTexture")
public abstract class MapRendererMixin{
//    @Shadow
//    private MapState state;
//    @Mutable
//    @Final
//    @Shadow
//    private final NativeImageBackedTexture texture;
//    @Mutable
//    @Final
//    @Shadow
//    private final RenderLayer renderLayer;
//    @Shadow
//    private boolean needsUpdate = true;
//
//    protected MapRendererMixin(NativeImageBackedTexture texture, RenderLayer renderLayer) {
//        this.texture = texture;
//        this.renderLayer = renderLayer;
//    }
//
//    @Shadow
//    private void updateTexture() {
//
//    }
//
//    @Unique
//    void draw(MatrixStack matrices, VertexConsumerProvider vertexConsumers, boolean hidePlayerIcons, int light) {
//        if (this.needsUpdate) {
//            this.updateTexture();
//            this.needsUpdate = false;
//        }
//        boolean i = false;
//        boolean j = false;
//        float f = 0.0f;
//        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
//        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(this.renderLayer);
//        vertexConsumer.vertex(matrix4f, 0.0f, 128.0f, -0.01f).color(255, 255, 255, 255).texture(0.0f, 1.0f).light(light).next();
//        vertexConsumer.vertex(matrix4f, 128.0f, 128.0f, -0.01f).color(255, 255, 255, 255).texture(1.0f, 1.0f).light(light).next();
//        vertexConsumer.vertex(matrix4f, 128.0f, 0.0f, -0.01f).color(255, 255, 255, 255).texture(1.0f, 0.0f).light(light).next();
//        vertexConsumer.vertex(matrix4f, 0.0f, 0.0f, -0.01f).color(255, 255, 255, 255).texture(0.0f, 0.0f).light(light).next();
//        int k = 0;
//        for (MapIcon mapIcon : this.state.getIcons()) {
//            if (hidePlayerIcons && !mapIcon.isAlwaysRendered()) continue;
//            matrices.push();
//            matrices.translate(0.0f + (float)mapIcon.getX() / 2.0f + 64.0f, 0.0f + (float)mapIcon.getZ() / 2.0f + 64.0f, -0.02f);
//            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)(mapIcon.getRotation() * 360) / 16.0f));
//            matrices.scale(4.0f, 4.0f, 3.0f);
//            matrices.translate(-0.125f, 0.125f, 0.0f);
//            byte b = mapIcon.getTypeId();
//            float g = (float)(b % 16 + 0) / 16.0f;
//            float h = (float)(b / 16 + 0) / 16.0f;
//            float l = (float)(b % 16 + 1) / 16.0f;
//            float m = (float)(b / 16 + 1) / 16.0f;
//            Matrix4f matrix4f2 = matrices.peek().getPositionMatrix();
//            float n = -0.001f;
//            VertexConsumer vertexConsumer2 = vertexConsumers.getBuffer(RenderLayer.getText(new Identifier("textures/map/map_icons.png")));
//            vertexConsumer2.vertex(matrix4f2, -1.0f, 1.0f, (float)k * -0.001f).color(255, 0, 0, 255).texture(g, h).light(light).next();
//            vertexConsumer2.vertex(matrix4f2, 1.0f, 1.0f, (float)k * -0.001f).color(255, 0, 0, 255).texture(l, h).light(light).next();
//            vertexConsumer2.vertex(matrix4f2, 1.0f, -1.0f, (float)k * -0.001f).color(255, 0, 0, 255).texture(l, m).light(light).next();
//            vertexConsumer2.vertex(matrix4f2, -1.0f, -1.0f, (float)k * -0.001f).color(255, 0, 0, 255).texture(g, m).light(light).next();
//            matrices.pop();
//            if (mapIcon.getText() != null) {
//                TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
//                Text text = mapIcon.getText();
//                float o = textRenderer.getWidth(text);
//                float f2 = 25.0f / o;
//                Objects.requireNonNull(textRenderer);
//                float p = MathHelper.clamp(f2, 0.0f, 6.0f / 9.0f);
//                matrices.push();
//                matrices.translate(0.0f + (float)mapIcon.getX() / 2.0f + 64.0f - o * p / 2.0f, 0.0f + (float)mapIcon.getZ() / 2.0f + 64.0f + 4.0f, -0.025f);
//                matrices.scale(p, p, 1.0f);
//                matrices.translate(0.0f, 0.0f, -0.1f);
//                textRenderer.draw(text, 0.0f, 0.0f, -1, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, Integer.MIN_VALUE, light);
//                matrices.pop();
//            }
//            ++k;
//        }
//    }
}
