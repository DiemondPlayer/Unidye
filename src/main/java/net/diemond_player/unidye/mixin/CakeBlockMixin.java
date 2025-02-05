package net.diemond_player.unidye.mixin;

import net.diemond_player.unidye.block.UnidyeBlocks;
import net.diemond_player.unidye.block.custom.DyeableCandleBlock;
import net.diemond_player.unidye.block.entity.DyeableBlockEntity;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CakeBlock.class)
public abstract class CakeBlockMixin {
    @Inject(method = "onUseWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/entity/Entity;Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/BlockPos;)V"))
    public void unidye$onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ItemActionResult> cir) {
        ItemStack itemStack = player.getStackInHand(player.getActiveHand());
        if (itemStack.isOf(UnidyeBlocks.CUSTOM_CANDLE.asItem())) {
            ((DyeableCandleBlock) ((BlockItem) itemStack.getItem()).getBlock()).createBlockEntity(pos, CandleCakeBlock.getCandleCakeFromCandle((CandleBlock) Block.getBlockFromItem(itemStack.getItem())));
            if(world.getBlockEntity(pos) != null) {
                ((DyeableBlockEntity) world.getBlockEntity(pos)).color = UnidyeUtils.getColor(itemStack);
            }
        }
    }
}
