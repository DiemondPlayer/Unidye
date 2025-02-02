package net.diemond_player.unidye.block.entity;

import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.block.UnidyeBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class UnidyeBlockEntities {
    public static final BlockEntityType<DyeableBlockEntity> DYEABLE_BE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE, Identifier.of(Unidye.MOD_ID, "dyeable_block_entity"),
            BlockEntityType.Builder.create(DyeableBlockEntity::new,
                    UnidyeBlocks.CUSTOM_TERRACOTTA,
                    UnidyeBlocks.CUSTOM_CONCRETE,
                    UnidyeBlocks.CUSTOM_CONCRETE_POWDER,
                    UnidyeBlocks.CUSTOM_CARPET,
                    UnidyeBlocks.CUSTOM_CANDLE,
                    UnidyeBlocks.CUSTOM_CANDLE_CAKE
            ).build(null));

    public static final BlockEntityType<DyeableShulkerBoxBlockEntity> DYEABLE_SHULKER_BOX_BE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE, Identifier.of(Unidye.MOD_ID, "dyeable_shulker_box_block_entity"),
            BlockEntityType.Builder.create(DyeableShulkerBoxBlockEntity::new,
                    UnidyeBlocks.CUSTOM_SHULKER_BOX
            ).build(null));
    public static final BlockEntityType<DyeableBedBlockEntity> DYEABLE_BED_BE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE, Identifier.of(Unidye.MOD_ID, "dyeable_bed_block_entity"),
            BlockEntityType.Builder.create(DyeableBedBlockEntity::new,
                    UnidyeBlocks.CUSTOM_BED
            ).build(null));
    public static final BlockEntityType<DyeableBannerBlockEntity> DYEABLE_BANNER_BE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE, Identifier.of(Unidye.MOD_ID, "dyeable_banner_block_entity"),
            BlockEntityType.Builder.create(DyeableBannerBlockEntity::new,
                    UnidyeBlocks.CUSTOM_BANNER,
                    UnidyeBlocks.CUSTOM_WALL_BANNER
            ).build(null));
    public static final BlockEntityType<DyeableLeatheryBlockEntity> DYEABLE_LEATHERY_BE = Registry.register(
            Registries.BLOCK_ENTITY_TYPE, Identifier.of(Unidye.MOD_ID, "dyeable_leathery_block_entity"),
            BlockEntityType.Builder.create(DyeableLeatheryBlockEntity::new,
                    UnidyeBlocks.CUSTOM_WOOL,
                    UnidyeBlocks.CUSTOM_STAINED_GLASS,
                    UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE
            ).build(null));


    public static void registerBlockEntities() {
//        Unidye.LOGGER.info("Registering Block Entities for " + Unidye.MOD_ID);
    }
}
