package net.diemond_player.unidye.block;

import net.diemond_player.unidye.block.entity.IDyeableBlockEntity;
import net.diemond_player.unidye.component.ItemNameAffixesComponent;
import net.diemond_player.unidye.component.MaterialColorsComponent;
import net.diemond_player.unidye.component.RecipeStacksComponent;
import net.diemond_player.unidye.registry.UnidyeBlocks;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CandleCakeBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;

import static net.minecraft.block.CakeBlock.BITES;

public class DyeableCandleCakeBlock extends CandleCakeBlock implements IDyeableBlock {
    public DyeableCandleCakeBlock(Block candle, Settings settings) {
        super(candle, settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        ActionResult actionResult = tryEat(world, pos, Blocks.CAKE.getDefaultState(), player);
        if (actionResult.isAccepted()) {
            if (blockEntity instanceof IDyeableBlockEntity dyeableBlockEntity) {
                ItemNameAffixesComponent itemNameAffixesComponent = dyeableBlockEntity.getItemNameAffixes();
                RecipeStacksComponent recipeStacksComponent = dyeableBlockEntity.getRecipeStacks();
                ProfileComponent profileComponent = dyeableBlockEntity.getProfile();
                MaterialColorsComponent materialColorsComponent = dyeableBlockEntity.getMaterialColors();
                ItemStack itemStack1 = new ItemStack(UnidyeBlocks.CUSTOM_CANDLE);
                UnidyeUtils.setColor(itemStack1, dyeableBlockEntity.getColor());
                if(recipeStacksComponent != null && !recipeStacksComponent.equals(RecipeStacksComponent.DEFAULT)) itemStack1.set(UnidyeDataComponentTypes.RECIPE_STACKS, dyeableBlockEntity.getRecipeStacks());
                if(itemNameAffixesComponent != null && !itemNameAffixesComponent.equals(ItemNameAffixesComponent.DEFAULT)) itemStack1.set(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, dyeableBlockEntity.getItemNameAffixes());
                if(profileComponent != null) itemStack1.set(DataComponentTypes.PROFILE, dyeableBlockEntity.getProfile());
                if(materialColorsComponent != null && !materialColorsComponent.equals(MaterialColorsComponent.DEFAULT)) itemStack1.set(UnidyeDataComponentTypes.MATERIAL_COLORS, dyeableBlockEntity.getMaterialColors());
                dropStack(world, pos, itemStack1);
            }
        }

        return actionResult;
    }

    public static ActionResult tryEat(WorldAccess world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!player.canConsume(false)) {
            return ActionResult.PASS;
        } else {
            player.incrementStat(Stats.EAT_CAKE_SLICE);
            player.getHungerManager().add(2, 0.1F);
            int i = state.get(BITES);
            world.emitGameEvent(player, GameEvent.EAT, pos);
            if (i < 6) {
                world.setBlockState(pos, state.with(BITES, i + 1), 3);
            } else {
                world.removeBlock(pos, false);
                world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, pos);
            }
            return ActionResult.SUCCESS;
        }
    }
}
