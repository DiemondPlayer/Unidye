package net.diemond_player.unidye.util;

import net.diemond_player.unidye.component.ItemNameAffixesComponent;
import net.diemond_player.unidye.component.MaterialColorsComponent;
import net.diemond_player.unidye.item.CustomDyeItem;

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
import net.minecraft.util.math.ColorHelper;

import java.util.*;

import static net.diemond_player.unidye.component.MaterialColorsComponent.*;
import static net.diemond_player.unidye.item.CustomDyeItem.DYE_SHAPE;

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
//        put(ElsDyeModItems.MINT_DYE, UnidyeColor.ELL_MINT);
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
        if(stack.get(DataComponentTypes.DYED_COLOR) == null) return false;
        return stack.get(DataComponentTypes.DYED_COLOR).rgb() != DEFAULT_WHITE_COLOR;
    }

    public static void updateSourceCustomDyeColor(ItemStack stack, List<DyeItem> colors,
                                                       List<ItemStack> customColors) {
        ArrayList<Integer> colorsToMix = new ArrayList<>(colors.stream().map(dyeItem -> UnidyeMaterialTypes.DYE.getColor(dyeItem.getColor())).toList());
        colorsToMix.addAll(customColors.stream().map(itemStack -> getMaterialColor(itemStack, UnidyeMaterialTypes.DYE)).toList());
        if (stack.contains(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES)) {
            if (stack.get(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES).sourceCustomDyeColor() != 0xFFFFFF) {
                colorsToMix.add(stack.get(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES).sourceCustomDyeColor());
            }
        }
        int mixedColor = blendColors(colorsToMix);
        stack.set(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, new ItemNameAffixesComponent(Text.empty(), Text.empty(), mixedColor));
    }

    public static ItemStack blendAndSetColor(ItemStack stack, List<DyeItem> colors, List<ItemStack> customColors) {
        ItemStack itemStack = ItemStack.EMPTY;
        Item item = stack.getItem();
        if (stack.isIn(ItemTags.DYEABLE)) {
            itemStack = stack.copyWithCount(1);
            updateSourceCustomDyeColor(itemStack, colors, customColors);
            if (item instanceof CustomDyeItem) {
                for(UnidyeMaterialType type : UnidyeMaterialTypes.MATERIAL_TYPE.stream().toList()){
                    blendAndSetMaterialColor(itemStack, colors, customColors, type);
                }
                defineClosestVanillaDye(itemStack);
            } else {
                for (UnidyeMaterialType type : UnidyeMaterialTypes.getAdditionalMaterialTypes(item)) {
                    blendAndSetMaterialColor(itemStack, colors, customColors, type);
                }
                blendAndSetMaterialColor(itemStack, colors, customColors, UnidyeMaterialTypes.getMaterialType(item));
            }
        }
        return itemStack;
    }

    //only used for custom dye items!
    public static void defineClosestVanillaDye(ItemStack itemStack) {
        int[] customColorArray = getColorIntArray(getColor(itemStack));
        double distance;
        double minDistance = -1;
        String name = "white";
        for (DyeItem dyeItem : Registries.ITEM.stream().filter(i -> i instanceof DyeItem && !(i instanceof CustomDyeItem)).map(i -> (DyeItem) i).toList()) {
            int[] colorArray = UnidyeUtils.getColorIntArray(UnidyeMaterialTypes.DYE.getColor(dyeItem.getColor()));
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
        boolean isBaseMaterialColor = UnidyeMaterialTypes.getMaterialType(stack.getItem()) == materialType;
        ArrayList<Integer> colorsToMix = new ArrayList<>(colors.stream().map(dyeItem -> materialType.getColor(dyeItem.getColor())).toList());
        colorsToMix.addAll(customColors.stream().map(itemStack -> getMaterialColor(itemStack, materialType)).toList());
        if ((isBaseMaterialColor && UnidyeUtils.hasColor(stack)) || (!isBaseMaterialColor && containsMaterialColor(stack, materialType)))
            colorsToMix.add(getMaterialColor(stack, materialType));
        int mixedColor = blendColors(colorsToMix);
        if(isBaseMaterialColor){
            UnidyeUtils.setColor(stack, mixedColor);
        }else{
            setMaterialColor(stack, mixedColor, materialType);
        }
    }

    private static int blendColors(ArrayList<Integer> colorsToMix) {
        int[] colorChannels = new int[3];
        for(Integer color : colorsToMix){
            int[] is = getColorIntArray(color);
            int red = is[0] * is[0];
            int green = is[1] * is[1];
            int blue = is[2] * is[2];
            colorChannels[0] = colorChannels[0] + red;
            colorChannels[1] = colorChannels[1] + green;
            colorChannels[2] = colorChannels[2] + blue;
        }
        int size = colorsToMix.size();
        int mixedRed = (int) Math.sqrt((double) colorChannels[0] / size);
        int mixedGreen = (int) Math.sqrt((double) colorChannels[1] / size);
        int mixedBlue = (int) Math.sqrt((double) colorChannels[2] / size);
        return ColorHelper.Argb.getArgb(0, mixedRed, mixedGreen, mixedBlue);
    }

    public static float[] getColorFloatArray(int n) {
        int[] is = getColorIntArray(n);
        return new float[]{(float) is[0] / 255.0f, (float) is[1] / 255.0f, (float) is[2] / 255.0f};
    }

    public static int[] getColorIntArray(int n) {
        int j = (n & 0xFF0000) >> 16;
        int k = (n & 0xFF00) >> 8;
        int l = (n & 0xFF);
        return new int[]{j, k, l};
    }

    public static int getColorByColorArray(float[] fs){
        return getColorByColorArray(new int[]{(int) (fs[0]*255.0f), (int) (fs[1]*255.0f), (int) (fs[2]*255.0f)});
    }

    public static int getColorByColorArray(int[] is){
        int n = is[0];
        n = (n << 8) + is[1];
        n = (n << 8) + is[2];
        return n;
    }

    public static Optional<DyeColor> findDyeColorByLeatherColor(int leatherColor){
        return Arrays.stream(DyeColor.values()).filter(dyeColor ->
                ColorHelper.Argb.withAlpha(0, dyeColor.getEntityColor()) == leatherColor).findFirst();
    }
}
