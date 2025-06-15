package net.diemond_player.unidye.util;

import net.minecraft.component.type.ProfileComponent;
import net.minecraft.item.Item;

import java.util.HashMap;

public class DyeData {
    public ProfileComponent profileComponent;
    public String prefix;
    public String suffix;

    public HashMap<Item, String> prefixExclusions = new HashMap<>();
    public HashMap<Item, String> suffixExclusions = new HashMap<>();

    public HashMap<Item, String> getSuffixExclusions() {
        return suffixExclusions;
    }

    public void setSuffixExclusions(HashMap<Item, String> suffixExclusions) {
        this.suffixExclusions = suffixExclusions;
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

    public DyeData(String prefix, String suffix, HashMap<Item, String> prefixExclusions, HashMap<Item, String> suffixExclusions) {
        this.suffix = suffix;
        this.prefix = prefix;
        this.suffixExclusions = suffixExclusions;
        this.prefixExclusions = prefixExclusions;
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

    public ProfileComponent getProfileComponent() {
        return profileComponent;
    }

    public void setProfileComponent(ProfileComponent profileComponent) {
        this.profileComponent = profileComponent;
    }

    @Override
    public String toString() {
        return "DyeData{" +
                "profileComponent=" + profileComponent +
                ", prefix='" + prefix + '\'' +
                ", suffix='" + suffix + '\'' +
                ", prefixExclusions=" + prefixExclusions +
                ", suffixExclusions=" + suffixExclusions +
                '}';
    }
}
