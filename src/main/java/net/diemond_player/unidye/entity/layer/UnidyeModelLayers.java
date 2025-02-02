package net.diemond_player.unidye.entity.layer;

import net.diemond_player.unidye.Unidye;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class UnidyeModelLayers {
    public static final EntityModelLayer CUSTOM_SHULKER =
            new EntityModelLayer(Identifier.of(Unidye.MOD_ID, "shulker_custom"), "main");
    public static final EntityModelLayer CUSTOM_BED_FOOT =
            new EntityModelLayer(Identifier.of(Unidye.MOD_ID, "custom_bed_foot"), "main");
    public static final EntityModelLayer CUSTOM_BED_HEAD =
            new EntityModelLayer(Identifier.of(Unidye.MOD_ID, "custom_bed_head"), "main");
    public static final EntityModelLayer CUSTOM_BANNER =
            new EntityModelLayer(Identifier.of(Unidye.MOD_ID, "custom_banner"), "main");
}