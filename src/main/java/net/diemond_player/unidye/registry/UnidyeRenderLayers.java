package net.diemond_player.unidye.registry;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;

import java.util.OptionalDouble;

import static net.minecraft.client.render.RenderPhase.*;

public class UnidyeRenderLayers {

//    public static final RenderPhase.Transparency TRANSPARENCY_CUSTOM = new RenderPhase.Transparency("unidye_transparency_custom", () -> {
//        RenderSystem.enableBlend();
//        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE);
//        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
//    }, () -> {
//        RenderSystem.disableBlend();
//        RenderSystem.defaultBlendFunc();
//    });
//
//    public static final RenderLayer.MultiPhase LINE_STRIP_NO_TRANSPARENCY = RenderLayer.of(
//            "unidye_line_strip_no_transparency",
//            VertexFormats.LINES,
//            VertexFormat.DrawMode.LINES,
//            1536,
//            RenderLayer.MultiPhaseParameters.builder()
//                    .program(LINES_PROGRAM)
//                    .lineWidth(new RenderPhase.LineWidth(OptionalDouble.empty()))
//                    .layering(VIEW_OFFSET_Z_LAYERING)
//                    .transparency(TRANSLUCENT_TRANSPARENCY)
//                    .target(ITEM_ENTITY_TARGET)
//                    .writeMaskState(ALL_MASK)
//                    .cull(DISABLE_CULLING)
//                    .build(false)
//    );

//    public static void registerRenderLayers() {
//    }
}
