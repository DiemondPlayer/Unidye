package net.diemond_player.unidye.block;

import net.diemond_player.unidye.block.entity.DyeableLeatheryBlockEntity;
import net.diemond_player.unidye.block.entity.IDyeableBlockEntity;
import net.diemond_player.unidye.component.ItemNameAffixesComponent;
import net.diemond_player.unidye.component.RecipeStacksComponent;
import net.diemond_player.unidye.registry.UnidyeBlockEntities;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.registry.UnidyeMaterialTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

import static net.diemond_player.unidye.item.CustomDyeItem.DEFAULT_WHITE_COLOR;


public class DyeableWoolBlock extends DyeableBlock{
    public DyeableWoolBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        if (IDyeableBlockEntity.getColor(world, pos) != DEFAULT_WHITE_COLOR) {
            return pickBlock(world, pos, new ItemStack(this));
        } else {
            return new ItemStack(this);
        }
    }

    @Override
    public ItemStack pickBlock(BlockView world, BlockPos pos, ItemStack stack) {
        DyeableLeatheryBlockEntity blockEntity = UnidyeBlockEntities.DYEABLE_LEATHERY_BE.get(world, pos);
        int color = DEFAULT_WHITE_COLOR;
        int bannerColor = DEFAULT_WHITE_COLOR;
        ItemNameAffixesComponent itemNameAffixesComponent = null;
        RecipeStacksComponent recipeStacksComponent = null;
        ProfileComponent profileComponent = null;
        if (blockEntity != null) {
            color = blockEntity.getColor();
            bannerColor = blockEntity.leatherColor;
            itemNameAffixesComponent = blockEntity.getItemNameAffixes();
            recipeStacksComponent = blockEntity.getRecipeStacks();
            profileComponent = blockEntity.getProfile();
        }
        stack.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(color, true));
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putInt(UnidyeMaterialTypes.LEATHER.getId().toString(), bannerColor);
        stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbtCompound));
        if(itemNameAffixesComponent != null) stack.set(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, itemNameAffixesComponent);
        if(recipeStacksComponent != null) stack.set(UnidyeDataComponentTypes.RECIPE_STACKS, recipeStacksComponent);
        if(profileComponent != null) stack.set(DataComponentTypes.PROFILE, profileComponent);
        return stack;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DyeableLeatheryBlockEntity(pos, state);
    }
}
