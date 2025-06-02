package net.diemond_player.unidye.registry;

import net.diemond_player.unidye.Unidye;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class UnidyeTags {

    public static final TagKey<Item> C_CONCRETES = createConventionalItemTag("concretes");

    private static TagKey<Item> createItemTag(String name){
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(Unidye.MOD_ID, name));
    }

    private static TagKey<Item> createConventionalItemTag(String name){
        return TagKey.of(RegistryKeys.ITEM, Identifier.of("c", name));
    }
}
