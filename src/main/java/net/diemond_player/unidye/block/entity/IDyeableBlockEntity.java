package net.diemond_player.unidye.block.entity;

import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.component.ItemNameAffixesComponent;
import net.diemond_player.unidye.component.RecipeStacksComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static net.diemond_player.unidye.component.MaterialColorsComponent.DEFAULT_WHITE_COLOR;

public interface IDyeableBlockEntity {
    int getColor();

    void setColor(int color);

    ItemNameAffixesComponent getItemNameAffixes();

    void setItemNameAffixes(ItemNameAffixesComponent itemNameAffixesComponent);

    RecipeStacksComponent getRecipeStacks();

    void setRecipeStacks(RecipeStacksComponent recipeStacksComponent);

    ProfileComponent getProfile();

    void setProfile(ProfileComponent profileComponent);

    static int getColor(BlockView world, BlockPos pos) {
        if (world == null) {
            return DEFAULT_WHITE_COLOR;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof IDyeableBlockEntity dyeableBlockEntity) {
            return dyeableBlockEntity.getColor();
        } else {
            return DEFAULT_WHITE_COLOR;
        }
    }

    default void writeCommonNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (getColor() != DEFAULT_WHITE_COLOR) {
            nbt.putInt("color", getColor());
        }
        if (getItemNameAffixes() != null && !getItemNameAffixes().equals(ItemNameAffixesComponent.DEFAULT)) {
            nbt.put("itemNameAffixes", ItemNameAffixesComponent.CODEC.encodeStart(registryLookup.getOps(NbtOps.INSTANCE), getItemNameAffixes()).getOrThrow());
        }
        if (getRecipeStacks() != null && !getRecipeStacks().equals(RecipeStacksComponent.DEFAULT)) {
            nbt.put("recipeStacks", RecipeStacksComponent.CODEC.encodeStart(registryLookup.getOps(NbtOps.INSTANCE), getRecipeStacks()).getOrThrow());
        }
        if (getProfile() != null) {
            nbt.put("profile", ProfileComponent.CODEC.encodeStart(registryLookup.getOps(NbtOps.INSTANCE), getProfile()).getOrThrow());
        }
    }

    default void readCommonNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (nbt.getInt("color") == 0) {
            setColor(DEFAULT_WHITE_COLOR);
        } else {
            setColor(nbt.getInt("color"));
        }
        if (nbt.contains("itemNameAffixes")) {
            ItemNameAffixesComponent.CODEC
                    .parse(registryLookup.getOps(NbtOps.INSTANCE), nbt.get("itemNameAffixes"))
                    .resultOrPartial(itemNameAffixes -> Unidye.LOGGER.error("Failed to parse item name affixes: '{}'", itemNameAffixes))
                    .ifPresent(this::setItemNameAffixes);
        }
        if (nbt.contains("recipeStacks")) {
            RecipeStacksComponent.CODEC
                    .parse(registryLookup.getOps(NbtOps.INSTANCE), nbt.get("recipeStacks"))
                    .resultOrPartial(recipeStacks -> Unidye.LOGGER.error("Failed to parse recipe stacks: '{}'", recipeStacks))
                    .ifPresent(this::setRecipeStacks);
        }
        if (nbt.contains("profile")) {
            ProfileComponent.CODEC
                    .parse(registryLookup.getOps(NbtOps.INSTANCE), nbt.get("profile"))
                    .resultOrPartial(profile -> Unidye.LOGGER.error("Failed to parse profile: '{}'", profile))
                    .ifPresent(this::setProfile);
        }
    }

    default void readCommonComponents(BlockEntity.ComponentsAccess components) {
        setColor(components.getOrDefault(DataComponentTypes.DYED_COLOR, new DyedColorComponent(DyedColorComponent.DEFAULT_COLOR, true)).rgb());
        setRecipeStacks(components.getOrDefault(UnidyeDataComponentTypes.RECIPE_STACKS, RecipeStacksComponent.DEFAULT));
        setItemNameAffixes(components.getOrDefault(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, ItemNameAffixesComponent.DEFAULT));
        setProfile(components.getOrDefault(DataComponentTypes.PROFILE, null));
    }

    default void addCommonComponents(ComponentMap.Builder componentMapBuilder) {
        componentMapBuilder.add(DataComponentTypes.DYED_COLOR, new DyedColorComponent(getColor(), true));
        componentMapBuilder.add(UnidyeDataComponentTypes.RECIPE_STACKS, getRecipeStacks());
        componentMapBuilder.add(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, getItemNameAffixes());
        componentMapBuilder.add(DataComponentTypes.PROFILE, getProfile());
    }
}
