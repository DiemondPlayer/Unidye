package net.diemond_player.unidye.util;

import net.minecraft.item.Item;

import java.util.HashMap;

public class DyeData {
    public String prefix;

    public HashMap<Item, String> prefixExclusions = new HashMap<>();

    public DyeData(HashMap<Item, String> prefixExclusions, String prefix) {
        this.prefixExclusions = prefixExclusions;
        this.prefix = prefix;
    }

    public DyeData(String prefix) {
        this.prefix = prefix;
    }

    public DyeData() {
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public HashMap<Item, String> getPrefixExclusions() {
        return prefixExclusions;
    }

    public void setPrefixExclusions(HashMap<Item, String> prefixExclusions) {
        this.prefixExclusions = prefixExclusions;
    }
}
