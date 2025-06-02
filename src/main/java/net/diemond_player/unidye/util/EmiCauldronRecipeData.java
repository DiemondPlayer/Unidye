package net.diemond_player.unidye.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.tag.TagKey;

import java.util.ArrayList;

public class EmiCauldronRecipeData {
    public String name;
    public ArrayList<Item> acceptedItems;
    public TagKey<Item> tag;
    public ItemConvertible outputItem;

    public EmiCauldronRecipeData(String name, TagKey<Item> tag, ItemConvertible outputItem) {
        this.name = name;
        this.tag = tag;
        this.outputItem = outputItem;
    }

    public EmiCauldronRecipeData(String name, ArrayList<Item> acceptedItems, ItemConvertible outputItem) {
        this.name = name;
        this.acceptedItems = acceptedItems;
        this.outputItem = outputItem;
    }
}
