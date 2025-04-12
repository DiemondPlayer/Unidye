package net.diemond_player.unidye.mixin;

import net.diemond_player.unidye.item.CustomDyeItem;
import net.diemond_player.unidye.util.UnidyeAccessor;
import net.diemond_player.unidye.registry.UnidyeMaterialTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WolfEntity.class)
public abstract class WolfEntityMixin implements UnidyeAccessor {
    @Unique
    private static final TrackedData<Integer> CUSTOM_COLOR = DataTracker.registerData(WolfEntity.class, TrackedDataHandlerRegistry.INTEGER);

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void unidye$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("unidye.custom_color", unidye$getCustomColor());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void unidye$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("unidye.custom_color")) {
            unidye$setCustomColor(nbt.getInt("unidye.custom_color"));
        }
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    private void unidye$initDataTracker(CallbackInfo ci) {
        ((WolfEntity) (Object) this).getDataTracker().startTracking(CUSTOM_COLOR, 0xFFFFFF);
    }


    @Inject(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/WolfEntity;setCollarColor(Lnet/minecraft/util/DyeColor;)V"))
    private void unidye$interactMobReset(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        UnidyeAccessor wolf = (UnidyeAccessor) ((WolfEntity) (Object) this);
        wolf.unidye$setCustomColor(0xFFFFFF);
    }

    @Inject(method = "interactMob", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/DyeItem;getColor()Lnet/minecraft/util/DyeColor;", shift = At.Shift.AFTER), cancellable = true)
    private void unidye$interactMobCheck(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();
        UnidyeAccessor wolf = (UnidyeAccessor) ((WolfEntity) (Object) this);
        DyeColor dyeColor = ((DyeItem) item).getColor();
        if (wolf.unidye$getCustomColor() != 0xFFFFFF) {
            ((WolfEntity) (Object) this).setCollarColor(dyeColor);
            wolf.unidye$setCustomColor(0xFFFFFF);
            if (player.getAbilities().creativeMode) cir.setReturnValue(ActionResult.SUCCESS);
            itemStack.decrement(1);
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void unidye$interactMobSet(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();
        UnidyeAccessor wolf = (UnidyeAccessor) ((WolfEntity) (Object) this);
        if (!(((WolfEntity) (Object) this).getWorld().isClient)) {
            if (((WolfEntity) (Object) this).isTamed()) {
                if (item instanceof CustomDyeItem && ((WolfEntity) (Object) this).isOwner(player)) {
                    wolf.unidye$setCustomColor(CustomDyeItem.getMaterialColor(itemStack, UnidyeMaterialTypes.LEATHER));
                    if (!player.getAbilities().creativeMode) {
                        itemStack.decrement(1);
                    }
                    cir.setReturnValue(ActionResult.SUCCESS);
                }
            }
        }
    }


    @Override
    public int unidye$getCustomColor() {
        return ((WolfEntity) (Object) this).getDataTracker().get(CUSTOM_COLOR);
    }

    @Override
    public void unidye$setCustomColor(int color) {
        ((WolfEntity) (Object) this).getDataTracker().set(CUSTOM_COLOR, color);
    }
}