package net.diemond_player.unidye.util;

import net.minecraft.util.DyeColor;

public class UnidyeMaterialColor {
    private int color;
    private UnidyeMaterialType materialType;
    private DyeColor dyeColor;

    public UnidyeMaterialColor(int color, UnidyeMaterialType materialType, DyeColor dyeColor) {
        this.color = color;
        this.materialType = materialType;
        this.dyeColor = dyeColor;
    }

    public int getColor() {
        return color;
    }

    public float[] getColorArray() {
        int j = (color & 0xFF0000) >> 16;
        int k = (color & 0xFF00) >> 8;
        int l = (color & 0xFF);
        return new float[]{(float) j / 255.0f, (float) k / 255.0f, (float) l / 255.0f};
    }

    public String getColorHex() {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    public UnidyeMaterialType getMaterialType() {
        return materialType;
    }

    public DyeColor getDyeColor() {
        return dyeColor;
    }
}
