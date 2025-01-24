package net.diemond_player.unidye.entity;

import net.diemond_player.unidye.Unidye;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class UnidyeEntities {

    public static final EntityType<DyeableFallingBlockEntity> DYEABLE_FALLING_BLOCK_ENTITY = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(Unidye.MOD_ID, "dyeable_falling_block"),
            EntityType.Builder.create(DyeableFallingBlockEntity::new, SpawnGroup.MISC)
                    .build());

    public static void registerModEntities() {
        Unidye.LOGGER.info("Registering Mod Entities for " + Unidye.MOD_ID);
    }
}