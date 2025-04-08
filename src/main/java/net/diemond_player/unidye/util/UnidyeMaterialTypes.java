package net.diemond_player.unidye.util;

import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.block.UnidyeBlocks;
import net.diemond_player.unidye.item.UnidyeItems;
import net.minecraft.item.Item;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class UnidyeMaterialTypes {
    // A class to store all the registered Material Types added by base Unidye
    public static final HashMap<Identifier, UnidyeMaterialType> MATERIAL_TYPES = new HashMap<>();
    public static Map<Item, UnidyeMaterialType> ITEM_TO_MATERIAL_TYPE = new HashMap<>();


    public static final UnidyeMaterialType DYE = registerMaterialType("dye", new HashMap<>(){{
        put(DyeColor.WHITE, 0xF5F4FF);
        put(DyeColor.LIGHT_GRAY, 0xD4D2DB);
        put(DyeColor.GRAY, 0x898989);
        put(DyeColor.BLACK, 0x565656);
        put(DyeColor.BROWN, 0x773100);
        put(DyeColor.RED, 0xFF0000);
        put(DyeColor.ORANGE, 0xEA8800);
        put(DyeColor.YELLOW, 0xF7DA00);
        put(DyeColor.LIME, 0x73D800);
        put(DyeColor.GREEN, 0x6FBA00);
        put(DyeColor.CYAN, 0x1183AF);
        put(DyeColor.LIGHT_BLUE, 0x3287FF);
        put(DyeColor.BLUE, 0x0055FF);
        put(DyeColor.PURPLE, 0x851CCC);
        put(DyeColor.MAGENTA, 0xCC28C3);
        put(DyeColor.PINK, 0xEA4DA1);
    }});

    public static final UnidyeMaterialType WOOL = registerMaterialType("wool", new HashMap<>(){{
        put(DyeColor.WHITE, 0xe9ecec);
        put(DyeColor.LIGHT_GRAY, 0x8e8e86);
        put(DyeColor.GRAY, 0x3e4447);
        put(DyeColor.BLACK, 0x252529);
        put(DyeColor.BROWN, 0x7A4E2F);
        put(DyeColor.RED, 0xa02722);
        put(DyeColor.ORANGE, 0xf07613);
        put(DyeColor.YELLOW, 0xf8c527);
        put(DyeColor.LIME, 0x70b919);
        put(DyeColor.GREEN, 0x546d1b);
        put(DyeColor.CYAN, 0x17949B);
        put(DyeColor.LIGHT_BLUE, 0x3aafd9);
        put(DyeColor.BLUE, 0x3D41AF);
        put(DyeColor.PURPLE, 0x792aac);
        put(DyeColor.MAGENTA, 0xbd44b3);
        put(DyeColor.PINK, 0xF38AAA);
    }});

    public static final UnidyeMaterialType TERRACOTTA = registerMaterialType("terracotta", new HashMap<>(){{
        put(DyeColor.WHITE, 0xFFDAC6);
        put(DyeColor.LIGHT_GRAY, 0xAF8B7E);
        put(DyeColor.GRAY, 0x49352D);
        put(DyeColor.BLACK, 0x301E16);
        put(DyeColor.BROWN, 0x664430);
        put(DyeColor.RED, 0xB24C3C);
        put(DyeColor.ORANGE, 0xC66831);
        put(DyeColor.YELLOW, 0xE5A330);
        put(DyeColor.LIME, 0x839144);
        put(DyeColor.GREEN, 0x626B38);
        put(DyeColor.CYAN, 0x707272);
        put(DyeColor.LIGHT_BLUE, 0x918EAF);
        put(DyeColor.BLUE, 0x614F75);
        put(DyeColor.PURPLE, 0x96586D);
        put(DyeColor.MAGENTA, 0xC4758F);
        put(DyeColor.PINK, 0xC66161);
    }});

    public static final UnidyeMaterialType CONCRETE = registerMaterialType("concrete", new HashMap<>(){{
        put(DyeColor.WHITE, 0xcfd5d6);
        put(DyeColor.LIGHT_GRAY, 0x7d7d73);
        put(DyeColor.GRAY, 0x36393d);
        put(DyeColor.BLACK, 0x080a0f);
        put(DyeColor.BROWN, 0x603b1f);
        put(DyeColor.RED, 0x8e2020);
        put(DyeColor.ORANGE, 0xe06100);
        put(DyeColor.YELLOW, 0xf0af15);
        put(DyeColor.LIME, 0x5ea818);
        put(DyeColor.GREEN, 0x495b24);
        put(DyeColor.CYAN, 0x157788);
        put(DyeColor.LIGHT_BLUE, 0x2389c6);
        put(DyeColor.BLUE, 0x2c2e8f);
        put(DyeColor.PURPLE, 0x641f9c);
        put(DyeColor.MAGENTA, 0xa9309f);
        put(DyeColor.PINK, 0xd5658e);
    }});

    public static final UnidyeMaterialType GLASS = registerMaterialType("glass", new HashMap<>(){{
        put(DyeColor.WHITE, 0xffffff);
        put(DyeColor.LIGHT_GRAY, 0x999999);
        put(DyeColor.GRAY, 0x4b4b4b);
        put(DyeColor.BLACK, 0x191919);
        put(DyeColor.BROWN, 0x664b32);
        put(DyeColor.RED, 0x993232);
        put(DyeColor.ORANGE, 0xd77f32);
        put(DyeColor.YELLOW, 0xe5e532);
        put(DyeColor.LIME, 0x7fcc19);
        put(DyeColor.GREEN, 0x667f32);
        put(DyeColor.CYAN, 0x4b7f99);
        put(DyeColor.LIGHT_BLUE, 0x6699d7);
        put(DyeColor.BLUE, 0x324bb2);
        put(DyeColor.PURPLE, 0x7f3fb2);
        put(DyeColor.MAGENTA, 0xb24bd7);
        put(DyeColor.PINK, 0xf27fa4);
    }});

    public static final UnidyeMaterialType SHULKER_BOX = registerMaterialType("shulker_box", new HashMap<>(){{
        put(DyeColor.WHITE, 0xEAEDED);
        put(DyeColor.LIGHT_GRAY, 0x8C8C83);
        put(DyeColor.GRAY, 0x3F4447);
        put(DyeColor.BLACK, 0x1F1F23);
        put(DyeColor.BROWN, 0x754829);
        put(DyeColor.RED, 0x9B2523);
        put(DyeColor.ORANGE, 0xF77111);
        put(DyeColor.YELLOW, 0xFFC826);
        put(DyeColor.LIME, 0x72BC18);
        put(DyeColor.GREEN, 0x556D1D);
        put(DyeColor.CYAN, 0x178791);
        put(DyeColor.LIGHT_BLUE, 0x3BB8DD);
        put(DyeColor.BLUE, 0x33369B);
        put(DyeColor.PURPLE, 0x7226A8);
        put(DyeColor.MAGENTA, 0xBA3FAF);
        put(DyeColor.PINK, 0xF48BAB);
    }});

    public static final UnidyeMaterialType CANDLE = registerMaterialType("candle", new HashMap<>(){{
        put(DyeColor.WHITE, 0xFFFFFF);
        put(DyeColor.LIGHT_GRAY, 0xA3A09A);
        put(DyeColor.GRAY, 0x6A757B);
        put(DyeColor.BLACK, 0x31304C);
        put(DyeColor.BROWN, 0x8F5B35);
        put(DyeColor.RED, 0xBF3B33);
        put(DyeColor.ORANGE, 0xFF9929);
        put(DyeColor.YELLOW, 0xFFDA39);
        put(DyeColor.LIME, 0x8BCE29);
        put(DyeColor.GREEN, 0x658718);
        put(DyeColor.CYAN, 0x16ABAA);
        put(DyeColor.LIGHT_BLUE, 0x31AEDE);
        put(DyeColor.BLUE, 0x406FC2);
        put(DyeColor.PURPLE, 0x8B30BD);
        put(DyeColor.MAGENTA, 0xC544B4);
        put(DyeColor.PINK, 0xF699B4);
    }});

    public static final UnidyeMaterialType LEATHER = registerMaterialType("leather", new HashMap<>(){{
        for(DyeColor dyeColor : DyeColor.values()){
            put(dyeColor, UnidyeUtils.getColorByColorArray(dyeColor.getColorComponents()));
        }
    }});

    public static final UnidyeMaterialType SIGN = registerMaterialType("sign", new HashMap<>(){{
        for(DyeColor dyeColor : DyeColor.values()){
            put(dyeColor, dyeColor.getSignColor());
        }
    }});

    public static final UnidyeMaterialType FIREWORK = registerMaterialType("firework", new HashMap<>(){{
        for(DyeColor dyeColor : DyeColor.values()){
            put(dyeColor, dyeColor.getFireworkColor());
        }
    }});


    private static UnidyeMaterialType registerMaterialType(String name, HashMap<DyeColor, Integer> colors) {
        return registerMaterialType(Unidye.MOD_ID, name, colors);
    }

    public static UnidyeMaterialType registerMaterialType(String modId, String name, HashMap<DyeColor, Integer> colors) {
        Identifier id = new Identifier(modId, name);
        UnidyeMaterialType type = new UnidyeMaterialType(colors, id);
        if(!MATERIAL_TYPES.containsKey(id)) {
            MATERIAL_TYPES.put(id, type);
        }else{
            Unidye.LOGGER.warn("Failed registering a Unidye material type: this material id already exists! Caused by {} while trying to add {} to {}", modId, name, id);
        }
        return type;
    }

    public static void addItemToMaterialType(Item item, UnidyeMaterialType type){
        if(!ITEM_TO_MATERIAL_TYPE.containsKey(item)){
            ITEM_TO_MATERIAL_TYPE.put(item, type);
        }else{
            Unidye.LOGGER.warn("Failed adding an item to {} Unidye material type: {} already has a material type!", type.getId().toString(), item);
        }
    }

    public static UnidyeMaterialType getMaterialType(Item dyeableItem) {
        return UnidyeMaterialTypes.ITEM_TO_MATERIAL_TYPE.getOrDefault(dyeableItem, UnidyeMaterialTypes.LEATHER);
    }

    public static void registerMaterialTypes(){

    }

//    public static final UnidyeMaterialType PATTERN = registerMaterialType("pattern", new HashMap<>(){{
//        put(DyeColor.WHITE, );
//        put(DyeColor.LIGHT_GRAY, );
//        put(DyeColor.GRAY, );
//        put(DyeColor.BLACK, );
//        put(DyeColor.BROWN, );
//        put(DyeColor.RED, );
//        put(DyeColor.ORANGE, );
//        put(DyeColor.YELLOW, );
//        put(DyeColor.LIME, );
//        put(DyeColor.GREEN, );
//        put(DyeColor.CYAN, );
//        put(DyeColor.LIGHT_BLUE, );
//        put(DyeColor.BLUE, );
//        put(DyeColor.PURPLE, );
//        put(DyeColor.MAGENTA, );
//        put(DyeColor.PINK, );
//    }});
}
