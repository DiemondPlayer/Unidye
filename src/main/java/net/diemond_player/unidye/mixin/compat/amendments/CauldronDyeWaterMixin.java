package net.diemond_player.unidye.mixin.compat.amendments;

import net.diemond_player.unidye.item.CustomDyeItem;
import net.mehvahdjukaar.amendments.common.block.DyeCauldronBlock;
import net.mehvahdjukaar.amendments.common.item.DyeBottleItem;
import net.mehvahdjukaar.amendments.common.tile.LiquidCauldronBlockTile;
import net.mehvahdjukaar.amendments.events.behaviors.CauldronDyeWater;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CauldronDyeWater.class)
public abstract class CauldronDyeWaterMixin {
    @Inject(method = "tryPerformingAction", at = @At("HEAD"), cancellable = true)
    public void unidye$tryPerformingAction(BlockState state, BlockPos pos, World level, PlayerEntity player, Hand hand, ItemStack stack, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (player.getStackInHand(hand).getItem() instanceof CustomDyeItem) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}