package net.diemond_player.unidye.util;

import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.item.UnidyeItems;
import net.diemond_player.unidye.item.custom.CustomDyeItem;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class UnidyeModelPredicateProvider {
    public static void registerModModels() {
        ModelPredicateProviderRegistry.register(UnidyeItems.CUSTOM_DYE, Identifier.of(Unidye.MOD_ID, "dye_id"),
                (stack, world, entity, seed) -> CustomDyeItem.getClosestVanillaDyeId(stack));
    }
}
