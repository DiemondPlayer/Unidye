package net.diemond_player.unidye.item;

import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.block.UnidyeBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class UnidyeItemGroups {
    public static final ItemGroup UNIDYE_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Unidye.MOD_ID, "unidye"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.unidye"))
                    .icon(() -> new ItemStack(UnidyeItems.CUSTOM_DYE)).entries((displayContext, entries) -> {

                        entries.add(UnidyeItems.CUSTOM_DYE);
                        entries.add(UnidyeBlocks.CUSTOM_WOOL);
                        entries.add(UnidyeBlocks.CUSTOM_CARPET);
                        entries.add(UnidyeBlocks.CUSTOM_TERRACOTTA);
                        entries.add(UnidyeBlocks.CUSTOM_CONCRETE);
                        entries.add(UnidyeBlocks.CUSTOM_CONCRETE_POWDER);
                        entries.add(UnidyeBlocks.CUSTOM_STAINED_GLASS);
                        entries.add(UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE);
                        entries.add(UnidyeBlocks.CUSTOM_SHULKER_BOX);
                        entries.add(UnidyeBlocks.CUSTOM_BED);
                        entries.add(UnidyeBlocks.CUSTOM_CANDLE);
                        entries.add(UnidyeItems.CUSTOM_BANNER);

                    }).build());

    public static void registerItemGroups() {
//        Unidye.LOGGER.info("Registering Item Groups for" + Unidye.MOD_ID);
    }
}
