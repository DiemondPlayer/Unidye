package net.diemond_player.unidye.recipe;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.diemond_player.unidye.item.CustomDyeItem;
import net.diemond_player.unidye.registry.UnidyeMaterialTypes;
import net.diemond_player.unidye.registry.UnidyeSpecialRecipes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Util;
import net.minecraft.world.World;

import java.util.Map;

import static net.diemond_player.unidye.component.MaterialColorsComponent.getMaterialColor;

public class CustomFireworkStarRecipe extends SpecialCraftingRecipe {
    public CustomFireworkStarRecipe(CraftingRecipeCategory category) {
        super(category);
    }

    private static final Ingredient TYPE_MODIFIER = Ingredient.ofItems(
            Items.FIRE_CHARGE,
            Items.FEATHER,
            Items.GOLD_NUGGET,
            Items.SKELETON_SKULL,
            Items.WITHER_SKELETON_SKULL,
            Items.CREEPER_HEAD,
            Items.PLAYER_HEAD,
            Items.DRAGON_HEAD,
            Items.ZOMBIE_HEAD,
            Items.PIGLIN_HEAD
    );
    private static final Ingredient TRAIL_MODIFIER = Ingredient.ofItems(Items.DIAMOND);
    private static final Ingredient FLICKER_MODIFIER = Ingredient.ofItems(Items.GLOWSTONE_DUST);
    private static final Map<Item, FireworkExplosionComponent.Type> TYPE_MODIFIER_MAP = Util.make(
            Maps.newHashMap(), typeModifiers -> {
                typeModifiers.put(Items.FIRE_CHARGE, FireworkExplosionComponent.Type.LARGE_BALL);
                typeModifiers.put(Items.FEATHER, FireworkExplosionComponent.Type.BURST);
                typeModifiers.put(Items.GOLD_NUGGET, FireworkExplosionComponent.Type.STAR);
                typeModifiers.put(Items.SKELETON_SKULL, FireworkExplosionComponent.Type.CREEPER);
                typeModifiers.put(Items.WITHER_SKELETON_SKULL, FireworkExplosionComponent.Type.CREEPER);
                typeModifiers.put(Items.CREEPER_HEAD, FireworkExplosionComponent.Type.CREEPER);
                typeModifiers.put(Items.PLAYER_HEAD, FireworkExplosionComponent.Type.CREEPER);
                typeModifiers.put(Items.DRAGON_HEAD, FireworkExplosionComponent.Type.CREEPER);
                typeModifiers.put(Items.ZOMBIE_HEAD, FireworkExplosionComponent.Type.CREEPER);
                typeModifiers.put(Items.PIGLIN_HEAD, FireworkExplosionComponent.Type.CREEPER);
            }
    );
    private static final Ingredient GUNPOWDER = Ingredient.ofItems(Items.GUNPOWDER);

    @Override
    public boolean matches(CraftingRecipeInput recipeInputInventory, World world) {
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        boolean bl5 = false;
        boolean bl6 = false;

        for (int i = 0; i < recipeInputInventory.getSize(); i++) {
            ItemStack itemStack = recipeInputInventory.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                if (TYPE_MODIFIER.test(itemStack)) {
                    if (bl3) {
                        return false;
                    }

                    bl3 = true;
                } else if (FLICKER_MODIFIER.test(itemStack)) {
                    if (bl5) {
                        return false;
                    }

                    bl5 = true;
                } else if (TRAIL_MODIFIER.test(itemStack)) {
                    if (bl4) {
                        return false;
                    }

                    bl4 = true;
                } else if (GUNPOWDER.test(itemStack)) {
                    if (bl) {
                        return false;
                    }

                    bl = true;
                } else {
                    if (!(itemStack.getItem() instanceof DyeItem)) {
                        return false;
                    }
                    if (itemStack.getItem() instanceof CustomDyeItem){
                        bl6 = true;
                    }

                    bl2 = true;
                }
            }
        }

        return bl && bl2 && bl6;
    }

    @Override
    public ItemStack craft(CraftingRecipeInput recipeInputInventory, RegistryWrapper.WrapperLookup lookup) {
        FireworkExplosionComponent.Type type = FireworkExplosionComponent.Type.SMALL_BALL;
        boolean bl = false;
        boolean bl2 = false;
        IntList intList = new IntArrayList();

        for (int i = 0; i < recipeInputInventory.getSize(); i++) {
            ItemStack itemStack = recipeInputInventory.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                if (TYPE_MODIFIER.test(itemStack)) {
                    type = TYPE_MODIFIER_MAP.get(itemStack.getItem());
                } else if (FLICKER_MODIFIER.test(itemStack)) {
                    bl = true;
                } else if (TRAIL_MODIFIER.test(itemStack)) {
                    bl2 = true;
                } else if (itemStack.getItem() instanceof DyeItem) {
                    if (itemStack.getItem() instanceof CustomDyeItem){
                        intList.add(((Integer) getMaterialColor(itemStack, UnidyeMaterialTypes.FIREWORK)).intValue());
                    } else {
                        intList.add(((DyeItem) itemStack.getItem()).getColor().getFireworkColor());
                    }
                }
            }
        }

        ItemStack itemStack2 = new ItemStack(Items.FIREWORK_STAR);
        itemStack2.set(DataComponentTypes.FIREWORK_EXPLOSION, new FireworkExplosionComponent(type, intList, IntList.of(), bl2, bl));
        return itemStack2;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return UnidyeSpecialRecipes.CUSTOM_FIREWORK_STAR;
    }
}
