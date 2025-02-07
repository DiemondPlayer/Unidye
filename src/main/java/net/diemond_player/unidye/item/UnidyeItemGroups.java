package net.diemond_player.unidye.item;

import com.google.common.collect.Lists;
import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.block.UnidyeBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class UnidyeItemGroups {

    public static final ArrayList<ItemConvertible> UNIDYE_ITEM_GROUP_ITEMS =
            Lists.newArrayList(UnidyeItems.CUSTOM_DYE,
                    UnidyeBlocks.CUSTOM_WOOL,
                    UnidyeBlocks.CUSTOM_CARPET,
                    UnidyeBlocks.CUSTOM_TERRACOTTA,
                    UnidyeBlocks.CUSTOM_CONCRETE,
                    UnidyeBlocks.CUSTOM_CONCRETE_POWDER,
                    UnidyeBlocks.CUSTOM_STAINED_GLASS,
                    UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE,
                    UnidyeBlocks.CUSTOM_SHULKER_BOX,
                    UnidyeBlocks.CUSTOM_BED,
                    UnidyeBlocks.CUSTOM_CANDLE,
                    UnidyeItems.CUSTOM_BANNER);

    public static final ItemGroup UNIDYE_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(Unidye.MOD_ID, "unidye"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.unidye"))
                    .icon(() -> new ItemStack(UnidyeItems.CUSTOM_DYE)).entries((displayContext, entries) -> {
                        UNIDYE_ITEM_GROUP_ITEMS.forEach(entries::add);
                    }).build());

    public static void registerItemGroups() {
        //Unidye.LOGGER.info("Registering Item Groups for" + Unidye.MOD_ID);
    }
}
