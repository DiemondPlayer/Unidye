package net.diemond_player.unidye.util;

import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class UnidyeMaterialType {

    private final Identifier id;

    public final Map<DyeColor, UnidyeMaterialColor> materialColors;

    public UnidyeMaterialType(Map<DyeColor, Integer> materialColors, Identifier id) {
        Map<DyeColor, UnidyeMaterialColor> newMap = new HashMap<>();
        materialColors.entrySet().forEach((i -> newMap.put(i.getKey(), new UnidyeMaterialColor(i.getValue(), this, i.getKey()))));
        this.materialColors = newMap;
        this.id = id;
    }

    public Identifier getId() {
        return id;
    }

    public void addColor(DyeColor dyeColor, int color){
        materialColors.put(dyeColor, new UnidyeMaterialColor(color, this, dyeColor));
    }

    public int getColor(DyeColor dyeColor){
        UnidyeMaterialColor materialColor = materialColors.getOrDefault(dyeColor, UnidyeMaterialTypes.LEATHER.materialColors.get(dyeColor));
        return materialColor.getColor();
    }
}
