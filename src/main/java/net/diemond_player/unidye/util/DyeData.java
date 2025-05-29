package net.diemond_player.unidye.util;

import net.minecraft.item.Item;

import java.util.HashMap;

public class DyeData {
    public String prefix;
    public String suffix;

    public HashMap<Item, String> prefixExclusions = new HashMap<>();

    public DyeData(HashMap<Item, String> prefixExclusions, String prefix) {
        this.prefixExclusions = prefixExclusions;
        this.prefix = prefix;
    }

    public DyeData(String affix, boolean isSuffix) {
        if(isSuffix){
            this.suffix = affix;
        }else {
            this.prefix = affix;
        }
    }

    public DyeData(String prefix) {
        this(prefix, false);
    }

    public DyeData() {
    }

    public DyeData(String prefix, String suffix) {
        this.suffix = suffix;
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix == null ? "" : prefix;
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


    public String getSuffix() {
        return suffix == null ? "" : suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public String toString() {
        return "DyeData{" +
                "prefix='" + prefix + '\'' +
                ", suffix='" + suffix + '\'' +
                ", prefixExclusions=" + prefixExclusions +
                '}';
    }
}
