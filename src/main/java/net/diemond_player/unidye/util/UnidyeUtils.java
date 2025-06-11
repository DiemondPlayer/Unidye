package net.diemond_player.unidye.util;

import net.diemond_player.unidye.component.ItemNameAffixesComponent;
import net.diemond_player.unidye.item.CustomDyeItem;
import net.diemond_player.unidye.item.DyeableLeatheryBlockItem;
import net.diemond_player.unidye.registry.UnidyeBlocks;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.registry.UnidyeMaterialTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static net.diemond_player.unidye.item.CustomDyeItem.*;
import static net.diemond_player.unidye.registry.UnidyeMaterialTypes.getMaterialType;

public class UnidyeUtils {

    public static final HashMap<DyeColor, String> DYE_COLOR_TO_NAME = new HashMap<>() {{
        put(DyeColor.WHITE, "white");
        put(DyeColor.LIGHT_GRAY, "light_gray");
        put(DyeColor.GRAY, "gray");
        put(DyeColor.BLACK, "black");
        put(DyeColor.BROWN, "brown");
        put(DyeColor.RED, "red");
        put(DyeColor.ORANGE, "orange");
        put(DyeColor.YELLOW, "yellow");
        put(DyeColor.LIME, "lime");
        put(DyeColor.GREEN, "green");
        put(DyeColor.CYAN, "cyan");
        put(DyeColor.LIGHT_BLUE, "light_blue");
        put(DyeColor.BLUE, "blue");
        put(DyeColor.PURPLE, "purple");
        put(DyeColor.MAGENTA, "magenta");
        put(DyeColor.PINK, "pink");
    }};

//    public static Map<Item, UnidyeColor> DYES = new HashMap<>() {{
//        put(Items.WHITE_DYE, UnidyeColor.WHITE);
//        put(Items.LIGHT_GRAY_DYE, UnidyeColor.LIGHT_GRAY);
//        put(Items.GRAY_DYE, UnidyeColor.GRAY);
//        put(Items.BLACK_DYE, UnidyeColor.BLACK);
//        put(Items.BROWN_DYE, UnidyeColor.BROWN);
//        put(Items.RED_DYE, UnidyeColor.RED);
//        put(Items.ORANGE_DYE, UnidyeColor.ORANGE);
//        put(Items.YELLOW_DYE, UnidyeColor.YELLOW);
//        put(Items.LIME_DYE, UnidyeColor.LIME);
//        put(Items.GREEN_DYE, UnidyeColor.GREEN);
//        put(Items.CYAN_DYE, UnidyeColor.CYAN);
//        put(Items.LIGHT_BLUE_DYE, UnidyeColor.LIGHT_BLUE);
//        put(Items.BLUE_DYE, UnidyeColor.BLUE);
//        put(Items.PURPLE_DYE, UnidyeColor.PURPLE);
//        put(Items.MAGENTA_DYE, UnidyeColor.MAGENTA);
//        put(Items.PINK_DYE, UnidyeColor.PINK);
////        put(ElsDyeModItems.MINT_DYE, UnidyeColor.ELL_MINT);
//    }};
//    public static Map<Item, String> MATERIAL_TYPES = new HashMap<>() {{
//        put(UnidyeBlocks.CUSTOM_WOOL.asItem(), "wool");
//        put(UnidyeBlocks.CUSTOM_CARPET.asItem(), "wool");
//        put(UnidyeBlocks.CUSTOM_TERRACOTTA.asItem(), "terracotta");
//        put(UnidyeBlocks.CUSTOM_CONCRETE.asItem(), "concrete");
//        put(UnidyeBlocks.CUSTOM_CONCRETE_POWDER.asItem(), "concrete");
//        put(UnidyeBlocks.CUSTOM_STAINED_GLASS.asItem(), "glass");
//        put(UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE.asItem(), "glass");
//        put(UnidyeItems.CUSTOM_DYE, "dye");
//        put(UnidyeBlocks.CUSTOM_SHULKER_BOX.asItem(), "shulker_box");
//        put(UnidyeBlocks.CUSTOM_CANDLE.asItem(), "candle");
//    }};

    public static void setColor(ItemStack stack, int color) {
        stack.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(color, true));
    }

    public static int getColor(ItemStack stack) {
        if(stack.get(DataComponentTypes.DYED_COLOR) != null) {
            return stack.get(DataComponentTypes.DYED_COLOR).rgb();
        }
        return DEFAULT_WHITE_COLOR;
    }

    public static boolean hasColor(ItemStack stack) {
        if(stack.get(DataComponentTypes.DYED_COLOR) != null) {
            return stack.get(DataComponentTypes.DYED_COLOR).rgb() != DEFAULT_WHITE_COLOR;
        } else {
            return false;
        }
    }

    public static void updateSourceCustomDyeColor(ItemStack stack, List<DyeItem> colors,
                                                       List<ItemStack> customColors) {
        int n;
        int[] is = new int[3];
        int j = 0;

        if (stack.contains(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES)) {
            if (stack.get(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES).sourceCustomDyeColor() != 0xFFFFFF) {
                int k = stack.get(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES).sourceCustomDyeColor();
                float f = (float) (k >> 16 & 0xFF);
                float g = (float) (k >> 8 & 0xFF);
                float h = (float) (k & 0xFF);
                is[0] = is[0] + (int) (f * f);
                is[1] = is[1] + (int) (g * g);
                is[2] = is[2] + (int) (h * h);
                ++j;
            }
        }
        for (DyeItem dyeItem : colors) {
            float[] fs = getColorArray(UnidyeMaterialTypes.DYE, dyeItem);
            int l = (int) (fs[0] * 255.0f * fs[0] * 255.0f);
            int m = (int) (fs[1] * 255.0f * fs[1] * 255.0f);
            n = (int) (fs[2] * 255.0f * fs[2] * 255.0f);
            is[0] = is[0] + l;
            is[1] = is[1] + m;
            is[2] = is[2] + n;
            ++j;
        }
        for (ItemStack customDye : customColors) {
            float[] fs = getCustomColorArray(UnidyeMaterialTypes.DYE, customDye);
            int l = (int) (fs[0] * 255.0f * fs[0] * 255.0f);
            int m = (int) (fs[1] * 255.0f * fs[1] * 255.0f);
            n = (int) (fs[2] * 255.0f * fs[2] * 255.0f);
            is[0] = is[0] + l;
            is[1] = is[1] + m;
            is[2] = is[2] + n;
            ++j;
        }
        int k = (int) Math.sqrt((double) is[0] / j);
        int o = (int) Math.sqrt((double) is[1] / j);
        int p = (int) Math.sqrt((double) is[2] / j);
        n = ColorHelper.Argb.getArgb(0, k, o, p);
        stack.set(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, new ItemNameAffixesComponent(Text.empty(), Text.empty(), n));
    }

    public static ItemStack blendAndSetColor(ItemStack stack, List<DyeItem> colors, List<ItemStack> customColors) {
        ItemStack itemStack = ItemStack.EMPTY;
        int n;
        int[] is = new int[3];
        int j = 0;
        Item item = stack.getItem();
        if (stack.isIn(ItemTags.DYEABLE)) {
            itemStack = stack.copyWithCount(1);
            if (itemStack.getItem() instanceof CustomDyeItem) {
                blendAndSetCustomDyeColor(itemStack, colors, customColors);
            }
            if (itemStack.isOf(UnidyeBlocks.CUSTOM_WOOL.asItem()) || itemStack.isOf(UnidyeBlocks.CUSTOM_STAINED_GLASS.asItem()) || itemStack.isOf(UnidyeBlocks.CUSTOM_STAINED_GLASS_PANE.asItem())) {
                blendAndSetLeatherColor(itemStack, colors, customColors);
            }
            updateSourceCustomDyeColor(itemStack, colors, customColors);
            if (UnidyeUtils.hasColor(stack)) {
                int k = UnidyeUtils.getColor(itemStack);
                float f = (float) (k >> 16 & 0xFF) / 255.0f;
                float g = (float) (k >> 8 & 0xFF) / 255.0f;
                float h = (float) (k & 0xFF) / 255.0f;
                is[0] = is[0] + (int) (f * 255.0f * f * 255.0f);
                is[1] = is[1] + (int) (g * 255.0f * g * 255.0f);
                is[2] = is[2] + (int) (h * 255.0f * h * 255.0f);
                ++j;
            }
            for (DyeItem dyeItem : colors) {
                float[] fs = getColorArray(getMaterialType(item), dyeItem);
                int l = (int) (fs[0] * 255.0f * fs[0] * 255.0f);
                int m = (int) (fs[1] * 255.0f * fs[1] * 255.0f);
                n = (int) (fs[2] * 255.0f * fs[2] * 255.0f);
                is[0] = is[0] + l;
                is[1] = is[1] + m;
                is[2] = is[2] + n;
                ++j;
            }
            for (ItemStack customDyeItem : customColors) {
                float[] fs = getCustomColorArray(getMaterialType(item), customDyeItem);
                int l = (int) (fs[0] * 255.0f * fs[0] * 255.0f);
                int m = (int) (fs[1] * 255.0f * fs[1] * 255.0f);
                n = (int) (fs[2] * 255.0f * fs[2] * 255.0f);
                is[0] = is[0] + l;
                is[1] = is[1] + m;
                is[2] = is[2] + n;
                ++j;
            }
        }
        if (item == null) {
            return ItemStack.EMPTY;
        }
        int k = (int) Math.sqrt((double) is[0] / j);
        int o = (int) Math.sqrt((double) is[1] / j);
        int p = (int) Math.sqrt((double) is[2] / j);
        n = ColorHelper.Argb.getArgb(0, k, o, p);
        UnidyeUtils.setColor(itemStack, n);
        if (stack.getItem() instanceof CustomDyeItem) {
            defineClosestVanillaDye(itemStack);
        }
        return itemStack;
    }


    public static void blendAndSetLeatherColor(ItemStack stack, List<DyeItem> colors, List<ItemStack> customColors) {
        int n;
        int[] is = new int[3];
        int j = 0;
        if (UnidyeUtils.hasColor(stack)) {
            int k = DyeableLeatheryBlockItem.getLeatherColor(stack);
            float f = (float) (k >> 16 & 0xFF);
            float g = (float) (k >> 8 & 0xFF);
            float h = (float) (k & 0xFF);
            is[0] = is[0] + (int) (f * f);
            is[1] = is[1] + (int) (g * g);
            is[2] = is[2] + (int) (h * h);
            ++j;
        }
        for (DyeItem dyeItem : colors) {
            float[] fs = getColorArray(UnidyeMaterialTypes.LEATHER, dyeItem);
            int l = (int) (fs[0] * 255.0f * fs[0] * 255.0f);
            int m = (int) (fs[1] * 255.0f * fs[1] * 255.0f);
            n = (int) (fs[2] * 255.0f * fs[2] * 255.0f);
            is[0] = is[0] + l;
            is[1] = is[1] + m;
            is[2] = is[2] + n;
            ++j;
        }
        for (ItemStack customDye : customColors) {
            float[] fs = getCustomColorArray(UnidyeMaterialTypes.LEATHER, customDye);
            int l = (int) (fs[0] * 255.0f * fs[0] * 255.0f);
            int m = (int) (fs[1] * 255.0f * fs[1] * 255.0f);
            n = (int) (fs[2] * 255.0f * fs[2] * 255.0f);
            is[0] = is[0] + l;
            is[1] = is[1] + m;
            is[2] = is[2] + n;
            ++j;
        }
        int k = (int) Math.sqrt((double) is[0] / j);
        int o = (int) Math.sqrt((double) is[1] / j);
        int p = (int) Math.sqrt((double) is[2] / j);
        n = ColorHelper.Argb.getArgb(0, k, o, p);
        DyeableLeatheryBlockItem.setLeatherColor(stack, n);
    }

    public static void blendAndSetCustomDyeColor(ItemStack stack, List<DyeItem> colors, List<ItemStack> customColors) {
        for(UnidyeMaterialType type : UnidyeMaterialTypes.MATERIAL_TYPE.stream().toList()){
            if(type != UnidyeMaterialTypes.DYE) {
                blendAndSetMaterialColor(stack, colors, customColors, type);
            }
        }
    }

    public static void defineClosestVanillaDye(ItemStack itemStack) {
        float[] customColorArray = getCustomColorArray(UnidyeMaterialTypes.DYE, itemStack);
        double distance;
        double minDistance = -1;
        String name = "white";
        for (DyeItem dyeItem : Registries.ITEM.stream().filter(i -> i instanceof DyeItem && !(i instanceof CustomDyeItem)).map(i -> (DyeItem) i).toList()) {
            float[] colorArray = UnidyeUtils.getColorArray(UnidyeMaterialTypes.DYE.getColor(dyeItem.getColor()));
            distance = Math.pow(customColorArray[0] - colorArray[0], 2)
                    + Math.pow(customColorArray[1] - colorArray[1], 2) + Math.pow(customColorArray[2] - colorArray[2], 2);
            if (distance < minDistance || minDistance == -1) {
                minDistance = distance;
                name = DYE_COLOR_TO_NAME.getOrDefault(dyeItem.getColor(), "white");
            }
        }
        NbtComponent nbtComponent = itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(new NbtCompound()));
        NbtCompound nbtCompound = nbtComponent.copyNbt();
        nbtCompound.putString(DYE_SHAPE, name);
        itemStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbtCompound));
    }


    public static void blendAndSetMaterialColor(ItemStack stack, List<DyeItem> colors,
                                         List<ItemStack> customColors, UnidyeMaterialType materialType) {
        int n;
        int[] is = new int[3];
        int j = 0;
        if (UnidyeUtils.hasColor(stack)) {
            int k = getMaterialColor(stack, materialType);
            float f = (float) (k >> 16 & 0xFF);
            float g = (float) (k >> 8 & 0xFF);
            float h = (float) (k & 0xFF);
            is[0] = is[0] + (int) (f * f);
            is[1] = is[1] + (int) (g * g);
            is[2] = is[2] + (int) (h * h);
            ++j;
        }
        for (DyeItem dyeItem : colors) {
            float[] fs = getColorArray(materialType, dyeItem);
            int l = (int) (fs[0] * 255.0f * fs[0] * 255.0f);
            int m = (int) (fs[1] * 255.0f * fs[1] * 255.0f);
            n = (int) (fs[2] * 255.0f * fs[2] * 255.0f);
            is[0] = is[0] + l;
            is[1] = is[1] + m;
            is[2] = is[2] + n;
            ++j;
        }
        for (ItemStack customDye : customColors) {
            float[] fs = getCustomColorArray(materialType, customDye);
            int l = (int) (fs[0] * 255.0f * fs[0] * 255.0f);
            int m = (int) (fs[1] * 255.0f * fs[1] * 255.0f);
            n = (int) (fs[2] * 255.0f * fs[2] * 255.0f);
            is[0] = is[0] + l;
            is[1] = is[1] + m;
            is[2] = is[2] + n;
            ++j;
        }
        int k = (int) Math.sqrt((double) is[0] / j);
        int o = (int) Math.sqrt((double) is[1] / j);
        int p = (int) Math.sqrt((double) is[2] / j);
        n = ColorHelper.Argb.getArgb(0, k, o, p);
        setMaterialColor(stack, n, materialType);
    }

    public static float[] getColorArray(UnidyeMaterialType type, DyeItem dyeItem) {
        DyeColor dyeColor = dyeItem.getColor();
        if(type.materialColors.containsKey(dyeColor)){
            return getColorArray(type.getColor(dyeColor));
        }else{
            return getColorArray(UnidyeMaterialTypes.LEATHER.getColor(dyeColor));
        }
    }

    public static float[] getCustomColorArray(UnidyeMaterialType materialType, ItemStack itemStack) {
        int color;
        if(materialType == UnidyeMaterialTypes.DYE){
            color = getColor(itemStack);
        }else {
            color = getMaterialColor(itemStack, materialType);
        }
        int j = (color & 0xFF0000) >> 16;
        int k = (color & 0xFF00) >> 8;
        int l = (color & 0xFF);
        return new float[]{(float) j / 255.0f, (float) k / 255.0f, (float) l / 255.0f};
    }


    public static float[] getColorArray(int n) {
        int j = (n & 0xFF0000) >> 16;
        int k = (n & 0xFF00) >> 8;
        int l = (n & 0xFF);
        return new float[]{(float) j / 255.0f, (float) k / 255.0f, (float) l / 255.0f};
    }

    public static int getColorByColorArray(float[] fs){
        int n = (int) (fs[0]*255.0f);
        n = (n << 8) + (int) (fs[1]*255.0f);
        n = (n << 8) + (int) (fs[2]*255.0f);
        return n;
    }

    public static Optional<DyeColor> findDyeColorByLeatherColor(int leatherColor){
        return Arrays.stream(DyeColor.values()).filter(dyeColor ->
                ColorHelper.Argb.withAlpha(0, dyeColor.getEntityColor()) == leatherColor).findFirst();
    }
}
