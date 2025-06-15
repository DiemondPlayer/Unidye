package net.diemond_player.unidye.mixin.compat.amendments;

import net.diemond_player.unidye.item.CustomDyeItem;
import net.mehvahdjukaar.amendments.events.behaviors.CauldronDyeWater;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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