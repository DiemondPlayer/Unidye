package net.diemond_player.unidye.block;

// Mostly taken with permission from Hecco's Bountiful Fares mod back when Unidye was still indev for 1.20.1
// Source: https://github.com/Heccology/Bountiful-Fares/blob/1.20.1/src/main/java/net/hecco/bountifulfares/block/interfaces/DyeableCeramicBlockInterface.java

import net.diemond_player.unidye.block.entity.DyeableBlockEntity;
import net.diemond_player.unidye.block.entity.IDyeableBlockEntity;
import net.diemond_player.unidye.component.ItemNameAffixesComponent;
import net.diemond_player.unidye.component.MaterialColorsComponent;
import net.diemond_player.unidye.component.RecipeStacksComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import static net.diemond_player.unidye.component.MaterialColorsComponent.DEFAULT_WHITE_COLOR;


public interface IDyeableBlock extends BlockEntityProvider {

    @Override
    default BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DyeableBlockEntity(pos, state);
    }

    default ItemStack pickBlock(BlockView world, BlockPos pos, ItemStack stack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        int color = DEFAULT_WHITE_COLOR;
        ItemNameAffixesComponent itemNameAffixesComponent = null;
        RecipeStacksComponent recipeStacksComponent = null;
        ProfileComponent profileComponent = null;
        MaterialColorsComponent materialColorsComponent = null;
        if (blockEntity instanceof IDyeableBlockEntity iDyeableBlockEntity) {
            color = iDyeableBlockEntity.getColor();
            itemNameAffixesComponent = iDyeableBlockEntity.getItemNameAffixes();
            recipeStacksComponent = iDyeableBlockEntity.getRecipeStacks();
            profileComponent = iDyeableBlockEntity.getProfile();
            materialColorsComponent = iDyeableBlockEntity.getMaterialColors();
        }
        stack.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(color, true));
        if(itemNameAffixesComponent != null && !itemNameAffixesComponent.equals(ItemNameAffixesComponent.DEFAULT)) stack.set(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, itemNameAffixesComponent);
        if(recipeStacksComponent != null && !recipeStacksComponent.equals(RecipeStacksComponent.DEFAULT)) stack.set(UnidyeDataComponentTypes.RECIPE_STACKS, recipeStacksComponent);
        if(profileComponent != null) stack.set(DataComponentTypes.PROFILE, profileComponent);
        if(materialColorsComponent != null && !materialColorsComponent.equals(MaterialColorsComponent.DEFAULT)) stack.set(UnidyeDataComponentTypes.MATERIAL_COLORS, materialColorsComponent);
        return stack;
    }
}
