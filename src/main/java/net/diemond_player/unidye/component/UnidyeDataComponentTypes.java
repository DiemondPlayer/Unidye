package net.diemond_player.unidye.component;

import net.diemond_player.unidye.Unidye;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class UnidyeDataComponentTypes {

    public static final ComponentType<CustomBannerPatternsComponent> CUSTOM_BANNER_PATTERNS = register(
            "custom_banner_patterns", builder -> builder.codec(CustomBannerPatternsComponent.CODEC).packetCodec(CustomBannerPatternsComponent.PACKET_CODEC).cache()
    );

    private static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(Unidye.MOD_ID, name),
                builderOperator.apply(ComponentType.builder()).build());
    }

    public static void registerModDataComponentTypes(){
//        Unidye.LOGGER.info("Registering Unidye Data Component Types");
    }
}
