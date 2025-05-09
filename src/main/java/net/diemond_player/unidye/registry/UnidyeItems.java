package net.diemond_player.unidye.registry;

import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.item.CustomDyeItem;
import net.diemond_player.unidye.item.DyeableBannerItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class UnidyeItems {

    public static final Item CUSTOM_DYE = registerItem("custom_dye",
            new CustomDyeItem(new Item.Settings()));

    public static final Item CUSTOM_BANNER = registerItem("custom_banner",
            new DyeableBannerItem(UnidyeBlocks.CUSTOM_BANNER, UnidyeBlocks.CUSTOM_WALL_BANNER, new Item.Settings().maxCount(16)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(Unidye.MOD_ID, name), item);
    }

    public static void registerModItems() {
        //Unidye.LOGGER.info("Registering Mod Items for" + Unidye.MOD_ID);
    }

}
