package net.diemond_player.unidye.registry;

import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.item.CustomDyeItem;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class UnidyeModelPredicates {
    public static void registerModModels() {
        ModelPredicateProviderRegistry.register(UnidyeItems.CUSTOM_DYE, Identifier.of(Unidye.MOD_ID, "dye_id"),
                (stack, world, entity, seed) -> CustomDyeItem.getDyeShapeAsFloat(stack));
    }
}
