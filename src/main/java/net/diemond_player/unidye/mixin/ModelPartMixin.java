package net.diemond_player.unidye.mixin;

import net.diemond_player.unidye.util.UnidyeAccessor;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Map;

@Mixin(ModelPart.class)
public abstract class ModelPartMixin implements UnidyeAccessor {

    @Mutable
    @Final
    @Shadow
    private final List<ModelPart.Cuboid> cuboids;


    @Mutable
    @Final
    @Shadow
    private final Map<String, ModelPart> children;

    protected ModelPartMixin(List<ModelPart.Cuboid> cuboids, Map<String, ModelPart> children) {
        this.cuboids = cuboids;
        this.children = children;
    }

    @Shadow
    private void renderCuboids(MatrixStack.Entry entry, VertexConsumer vertexConsumer, int light, int overlay, int color) {

    }

    @Override
    public void unidye$renderCustomBed(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        if (!((ModelPart) (Object) this).visible) {
            return;
        }
        if (this.cuboids.isEmpty() && this.children.isEmpty()) {
            return;
        }
        matrices.push();
        ((ModelPart) (Object) this).rotate(matrices);
        if (!((ModelPart) (Object) this).hidden) {
            this.renderCuboids(matrices.peek(), vertices, light, overlay, color);
        }
        this.children.get("left_leg").render(matrices, vertices, light, overlay);
        this.children.get("right_leg").render(matrices, vertices, light, overlay);
        this.children.get("main").render(matrices, vertices, light, overlay, color);
        if (this.children.containsKey("main1")) {
            this.children.get("main1").render(matrices, vertices, light, overlay);
        }
        if (this.children.containsKey("main2")) {
            this.children.get("main2").render(matrices, vertices, light, overlay);
        }
        matrices.pop();
    }
}
