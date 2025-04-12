package net.diemond_player.unidye.mixin;

import net.diemond_player.unidye.registry.UnidyeBlocks;
import net.diemond_player.unidye.item.DyeableLeatheryBlockItem;
import net.diemond_player.unidye.util.UnidyeAccessor;
import net.diemond_player.unidye.util.UnidyeMaterialTypes;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin implements UnidyeAccessor {
    @Unique
    private static final TrackedData<Integer> CUSTOM_COLOR = DataTracker.registerData(SheepEntity.class, TrackedDataHandlerRegistry.INTEGER);
    @Unique
    private static final TrackedData<Integer> SECONDARY_CUSTOM_COLOR = DataTracker.registerData(SheepEntity.class, TrackedDataHandlerRegistry.INTEGER);

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("unidye.custom_color", unidye$getCustomColor());
        nbt.putInt("unidye.secondary_custom_color", unidye$getSecondaryCustomColor());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("unidye.custom_color")) {
            unidye$setCustomColor(nbt.getInt("unidye.custom_color"));
        }
        if (nbt.contains("unidye.secondary_custom_color")) {
            unidye$setSecondaryCustomColor(nbt.getInt("unidye.secondary_custom_color"));
        }
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    private void initDataTracker(CallbackInfo ci) {
        ((SheepEntity) (Object) this).getDataTracker().startTracking(CUSTOM_COLOR, 0xFFFFFF);
        ((SheepEntity) (Object) this).getDataTracker().startTracking(SECONDARY_CUSTOM_COLOR, 0xFFFFFF);
    }

    @Inject(method = "getLootTableId", at = @At("HEAD"), cancellable = true)
    private void getLootTableId(CallbackInfoReturnable<Identifier> cir) {
        UnidyeAccessor sheep = (UnidyeAccessor) ((SheepEntity) (Object) this);
        if (sheep.unidye$getCustomColor() != 0xFFFFFF) {
            cir.setReturnValue(((SheepEntity) (Object) this).getType().getLootTableId());
        }
    }

    @Inject(method = "createChild(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/PassiveEntity;)Lnet/minecraft/entity/passive/PassiveEntity;", at = @At("HEAD"), cancellable = true)
    private void createChild(ServerWorld world, PassiveEntity entity, CallbackInfoReturnable<PassiveEntity> cir) {
        SheepEntity sheepEntity = (SheepEntity) EntityType.SHEEP.create(world);
        if (sheepEntity != null) {
            UnidyeAccessor firstSheep = (UnidyeAccessor) ((SheepEntity) (Object) this);
            UnidyeAccessor secondSheep = (UnidyeAccessor) entity;
            UnidyeAccessor sheep = (UnidyeAccessor) sheepEntity;
            if (firstSheep.unidye$getCustomColor() != 0xFFFFFF && secondSheep.unidye$getCustomColor() != 0xFFFFFF) {
                int customColor1 = firstSheep.unidye$getCustomColor();
                int red1 = (customColor1 >> 16 & 0xFF);
                int green1 = (customColor1 >> 8 & 0xFF);
                int blue1 = (customColor1 & 0xFF);
                int customColor2 = secondSheep.unidye$getCustomColor();
                int red2 = (customColor2 >> 16 & 0xFF);
                int green2 = (customColor2 >> 8 & 0xFF);
                int blue2 = (customColor2 & 0xFF);
                int customColor3;
                int red3 = (int) Math.sqrt((double) (red1 * red1 + red2 * red2) / 2);
                int green3 = (int) Math.sqrt((double) (green1 * green1 + green2 * green2) / 2);
                int blue3 = (int) Math.sqrt((double) (blue1 * blue1 + blue2 * blue2) / 2);
                customColor3 = red3;
                customColor3 = (customColor3 << 8) + green3;
                customColor3 = (customColor3 << 8) + blue3;
                sheep.unidye$setCustomColor(customColor3);
                int customColor11 = firstSheep.unidye$getSecondaryCustomColor();
                int red11 = (customColor11 >> 16 & 0xFF);
                int green11 = (customColor11 >> 8 & 0xFF);
                int blue11 = (customColor11 & 0xFF);
                int customColor22 = secondSheep.unidye$getSecondaryCustomColor();
                int red22 = (customColor22 >> 16 & 0xFF);
                int green22 = (customColor22 >> 8 & 0xFF);
                int blue22 = (customColor22 & 0xFF);
                int customColor33;
                int red33 = (int) Math.sqrt((double) (red11 * red11 + red22 * red22) / 2);
                int green33 = (int) Math.sqrt((double) (green11 * green11 + green22 * green22) / 2);
                int blue33 = (int) Math.sqrt((double) (blue11 * blue11 + blue22 * blue22) / 2);
                customColor33 = red33;
                customColor33 = (customColor33 << 8) + green33;
                customColor33 = (customColor33 << 8) + blue33;
                sheep.unidye$setSecondaryCustomColor(customColor33);
                cir.setReturnValue(sheepEntity);
            } else if (firstSheep.unidye$getCustomColor() != 0xFFFFFF && secondSheep.unidye$getCustomColor() == 0xFFFFFF) {
                int customColor1 = firstSheep.unidye$getCustomColor();
                int red1 = (customColor1 >> 16 & 0xFF);
                int green1 = (customColor1 >> 8 & 0xFF);
                int blue1 = (customColor1 & 0xFF);
                SheepEntity second = (SheepEntity) entity;
                float[] customColor2 = UnidyeUtils.getColorArray(UnidyeMaterialTypes.LEATHER.getColor(second.getColor()));
                customColor2[0] = customColor2[0] * 255.0f;
                customColor2[1] = customColor2[1] * 255.0f;
                customColor2[2] = customColor2[2] * 255.0f;
                int customColor3;
                int red3 = (int) Math.sqrt((double) (red1 * red1 + customColor2[0] * customColor2[0]) / 2);
                int green3 = (int) Math.sqrt((double) (green1 * green1 + customColor2[1] * customColor2[1]) / 2);
                int blue3 = (int) Math.sqrt((double) (blue1 * blue1 + customColor2[2] * customColor2[2]) / 2);
                customColor3 = red3;
                customColor3 = (customColor3 << 8) + green3;
                customColor3 = (customColor3 << 8) + blue3;
                sheep.unidye$setCustomColor(customColor3);
                int customColor11 = firstSheep.unidye$getSecondaryCustomColor();
                int red11 = (customColor11 >> 16 & 0xFF);
                int green11 = (customColor11 >> 8 & 0xFF);
                int blue11 = (customColor11 & 0xFF);
                float[] customColor22 = UnidyeUtils.getColorArray(UnidyeMaterialTypes.WOOL.getColor(second.getColor()));
                customColor22[0] = customColor22[0] * 255.0f;
                customColor22[1] = customColor22[1] * 255.0f;
                customColor22[2] = customColor22[2] * 255.0f;
                int customColor33;
                int red33 = (int) Math.sqrt((double) (red11 * red11 + customColor22[0] * customColor22[0]) / 2);
                int green33 = (int) Math.sqrt((double) (green11 * green11 + customColor22[1] * customColor22[1]) / 2);
                int blue33 = (int) Math.sqrt((double) (blue11 * blue11 + customColor22[2] * customColor22[2]) / 2);
                customColor33 = red33;
                customColor33 = (customColor33 << 8) + green33;
                customColor33 = (customColor33 << 8) + blue33;
                sheep.unidye$setSecondaryCustomColor(customColor33);
                cir.setReturnValue(sheepEntity);
            } else if (secondSheep.unidye$getCustomColor() != 0xFFFFFF && firstSheep.unidye$getCustomColor() == 0xFFFFFF) {
                int customColor1 = secondSheep.unidye$getCustomColor();
                int red1 = (customColor1 >> 16 & 0xFF);
                int green1 = (customColor1 >> 8 & 0xFF);
                int blue1 = (customColor1 & 0xFF);
                SheepEntity first = (SheepEntity) ((SheepEntity) (Object) this);
                float[] customColor2 = UnidyeUtils.getColorArray(UnidyeMaterialTypes.LEATHER.getColor(first.getColor()));
                customColor2[0] = customColor2[0] * 255.0f;
                customColor2[1] = customColor2[1] * 255.0f;
                customColor2[2] = customColor2[2] * 255.0f;
                int customColor3;
                int red3 = (int) Math.sqrt((double) (red1 * red1 + customColor2[0] * customColor2[0]) / 2);
                int green3 = (int) Math.sqrt((double) (green1 * green1 + customColor2[1] * customColor2[1]) / 2);
                int blue3 = (int) Math.sqrt((double) (blue1 * blue1 + customColor2[2] * customColor2[2]) / 2);
                customColor3 = red3;
                customColor3 = (customColor3 << 8) + green3;
                customColor3 = (customColor3 << 8) + blue3;
                sheep.unidye$setCustomColor(customColor3);
                int customColor11 = secondSheep.unidye$getSecondaryCustomColor();
                int red11 = (customColor11 >> 16 & 0xFF);
                int green11 = (customColor11 >> 8 & 0xFF);
                int blue11 = (customColor11 & 0xFF);
                float[] customColor22 = UnidyeUtils.getColorArray(UnidyeMaterialTypes.WOOL.getColor(first.getColor()));
                customColor22[0] = customColor22[0] * 255.0f;
                customColor22[1] = customColor22[1] * 255.0f;
                customColor22[2] = customColor22[2] * 255.0f;
                int customColor33;
                int red33 = (int) Math.sqrt((double) (red11 * red11 + customColor22[0] * customColor22[0]) / 2);
                int green33 = (int) Math.sqrt((double) (green11 * green11 + customColor22[1] * customColor22[1]) / 2);
                int blue33 = (int) Math.sqrt((double) (blue11 * blue11 + customColor22[2] * customColor22[2]) / 2);
                customColor33 = red33;
                customColor33 = (customColor33 << 8) + green33;
                customColor33 = (customColor33 << 8) + blue33;
                sheep.unidye$setSecondaryCustomColor(customColor33);
                cir.setReturnValue(sheepEntity);
            }
        }
    }

    @Inject(method = "sheared", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/SheepEntity;setSheared(Z)V", shift = At.Shift.AFTER), cancellable = true)
    private void sheared(SoundCategory shearedSoundCategory, CallbackInfo ci) {
        UnidyeAccessor sheep = (UnidyeAccessor) ((SheepEntity) (Object) this);
        if (sheep.unidye$getCustomColor() != 0xFFFFFF) {
            int i = 1 + ((SheepEntity) (Object) this).getRandom().nextInt(3);
            for (int j = 0; j < i; ++j) {
                DyeableItem item = (DyeableItem) UnidyeBlocks.CUSTOM_WOOL.asItem();
                ItemStack itemStack = UnidyeBlocks.CUSTOM_WOOL.asItem().getDefaultStack();
                item.setColor(itemStack, sheep.unidye$getSecondaryCustomColor());
                DyeableLeatheryBlockItem.setLeatherColor(itemStack, sheep.unidye$getCustomColor());
                ItemEntity itemEntity = ((SheepEntity) (Object) this).dropStack(itemStack, 1);
                if (itemEntity != null) {
                    itemEntity.setVelocity(itemEntity.getVelocity().add((double) ((((SheepEntity) (Object) this).getRandom().nextFloat() - ((SheepEntity) (Object) this).getRandom().nextFloat()) * 0.1F), (double) (((SheepEntity) (Object) this).getRandom().nextFloat() * 0.05F), (double) ((((SheepEntity) (Object) this).getRandom().nextFloat() - ((SheepEntity) (Object) this).getRandom().nextFloat()) * 0.1F)));
                }
            }
            ci.cancel();
        }
    }

    @Override
    public int unidye$getCustomColor() {
        return ((SheepEntity) (Object) this).getDataTracker().get(CUSTOM_COLOR);
    }

    @Override
    public void unidye$setCustomColor(int color) {
        ((SheepEntity) (Object) this).getDataTracker().set(CUSTOM_COLOR, color);
    }

    @Override
    public int unidye$getSecondaryCustomColor() {
        return ((SheepEntity) (Object) this).getDataTracker().get(SECONDARY_CUSTOM_COLOR);
    }

    @Override
    public void unidye$setSecondaryCustomColor(int color) {
        ((SheepEntity) (Object) this).getDataTracker().set(SECONDARY_CUSTOM_COLOR, color);
    }
}