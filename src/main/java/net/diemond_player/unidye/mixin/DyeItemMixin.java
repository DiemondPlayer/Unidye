package net.diemond_player.unidye.mixin;

import net.diemond_player.unidye.util.UnidyeAccessor;
import net.diemond_player.unidye.util.UnidyeColor;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DyeItem.class)
public abstract class DyeItemMixin extends Item {
    public DyeItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "useOnEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/SheepEntity;setColor(Lnet/minecraft/util/DyeColor;)V"))
    private void unidye$useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        UnidyeAccessor sheep = (UnidyeAccessor) entity;
        sheep.unidye$setCustomColor(0xFFFFFF);
        sheep.unidye$setSecondaryCustomColor(0xFFFFFF);
    }

    @Inject(method = "useOnEntity", at = @At("HEAD"), cancellable = true)
    private void unidye$useOnEntityCheck(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) throws NoSuchFieldException {
        SheepEntity sheepEntity;
        if (entity instanceof SheepEntity && (sheepEntity = (SheepEntity) entity).isAlive() && !sheepEntity.isSheared()) {
            sheepEntity.getWorld().playSoundFromEntity(user, sheepEntity, SoundEvents.ITEM_DYE_USE, SoundCategory.PLAYERS, 1.0f, 1.0f);
            if (!user.getWorld().isClient) {
                sheepEntity.setColor(((DyeItem) (Object) this).getColor());
                UnidyeAccessor sheep = (UnidyeAccessor) entity;
                sheep.unidye$setCustomColor(0xFFFFFF);
                sheep.unidye$setSecondaryCustomColor(0xFFFFFF);
                stack.decrement(1);
            }
            cir.setReturnValue(ActionResult.success(user.getWorld().isClient));
        }
    }

    @Inject(method = "useOnSign", at = @At("HEAD"), cancellable = true)
    private void unidye$useOnSign(World world, SignBlockEntity signBlockEntity, boolean front, PlayerEntity player, CallbackInfoReturnable<Boolean> cir) throws NoSuchFieldException {
        UnidyeAccessor unidyeAccessor = (UnidyeAccessor) signBlockEntity;
        if (front) {
            if (unidyeAccessor.unidye$getCustomColor() != 0xFFFFFF) {
                unidyeAccessor.unidye$setCustomColor(0xFFFFFF);
                if (signBlockEntity.changeText(text -> text.withColor(((DyeItem) (Object) this).getColor()), front)) {
                    unidyeAccessor.unidye$setCustomColor(0xFFFFFF);
                    signBlockEntity.markDirty();
                    world.updateListeners(signBlockEntity.getPos(), world.getBlockState(signBlockEntity.getPos()), world.getBlockState(signBlockEntity.getPos()), Block.NOTIFY_LISTENERS);
                    world.playSound(null, signBlockEntity.getPos(), SoundEvents.ITEM_DYE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    cir.setReturnValue(true);
                }
                signBlockEntity.markDirty();
                world.updateListeners(signBlockEntity.getPos(), world.getBlockState(signBlockEntity.getPos()), world.getBlockState(signBlockEntity.getPos()), Block.NOTIFY_LISTENERS);
                world.playSound(null, signBlockEntity.getPos(), SoundEvents.ITEM_DYE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                cir.setReturnValue(true);
            }
        } else {
            if (unidyeAccessor.unidye$getSecondaryCustomColor() != 0xFFFFFF) {
                unidyeAccessor.unidye$setSecondaryCustomColor(0xFFFFFF);
                if (signBlockEntity.changeText(text -> text.withColor(((DyeItem) (Object) this).getColor()), front)) {
                    unidyeAccessor.unidye$setSecondaryCustomColor(0xFFFFFF);
                    signBlockEntity.markDirty();
                    world.updateListeners(signBlockEntity.getPos(), world.getBlockState(signBlockEntity.getPos()), world.getBlockState(signBlockEntity.getPos()), Block.NOTIFY_LISTENERS);
                    world.playSound(null, signBlockEntity.getPos(), SoundEvents.ITEM_DYE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    cir.setReturnValue(true);
                }
                signBlockEntity.markDirty();
                world.updateListeners(signBlockEntity.getPos(), world.getBlockState(signBlockEntity.getPos()), world.getBlockState(signBlockEntity.getPos()), Block.NOTIFY_LISTENERS);
                world.playSound(null, signBlockEntity.getPos(), SoundEvents.ITEM_DYE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
                cir.setReturnValue(true);
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(UnidyeUtils.DYES.containsKey(stack.getItem())) {
            if (Screen.hasShiftDown()) {
                UnidyeColor unidyeColor = UnidyeUtils.DYES.get(stack.getItem());
                MutableText mutableText = Text.literal("■ ");
                tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(unidyeColor.woolColor)).append(Text.translatable("tooltip.unidye.wool_color").append(String.format("#%06X", (0xFFFFFF & unidyeColor.woolColor))).formatted(Formatting.GRAY)));
                mutableText = Text.literal("■ ");
                tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(unidyeColor.signColor)).append(Text.translatable("tooltip.unidye.sign_color").append(String.format("#%06X", (0xFFFFFF & unidyeColor.signColor))).formatted(Formatting.GRAY)));
                mutableText = Text.literal("■ ");
                tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(unidyeColor.glassColor)).append(Text.translatable("tooltip.unidye.glass_color").append(String.format("#%06X", (0xFFFFFF & unidyeColor.glassColor))).formatted(Formatting.GRAY)));
                mutableText = Text.literal("■ ");
                tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(unidyeColor.candleColor)).append(Text.translatable("tooltip.unidye.candle_color").append(String.format("#%06X", (0xFFFFFF & unidyeColor.candleColor))).formatted(Formatting.GRAY)));
                mutableText = Text.literal("■ ");
                tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(unidyeColor.fireworkColor)).append(Text.translatable("tooltip.unidye.firework_color").append(String.format("#%06X", (0xFFFFFF & unidyeColor.fireworkColor))).formatted(Formatting.GRAY)));
                mutableText = Text.literal("■ ");
                tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(unidyeColor.concreteColor)).append(Text.translatable("tooltip.unidye.concrete_color").append(String.format("#%06X", (0xFFFFFF & unidyeColor.concreteColor))).formatted(Formatting.GRAY)));
                mutableText = Text.literal("■ ");
                tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(unidyeColor.terracottaColor)).append(Text.translatable("tooltip.unidye.terracotta_color").append(String.format("#%06X", (0xFFFFFF & unidyeColor.terracottaColor))).formatted(Formatting.GRAY)));
                mutableText = Text.literal("■ ");
                tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(unidyeColor.shulkerBoxColor)).append(Text.translatable("tooltip.unidye.shulker_box_color").append(String.format("#%06X", (0xFFFFFF & unidyeColor.shulkerBoxColor))).formatted(Formatting.GRAY)));
                mutableText = Text.literal("■ ");
                tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(unidyeColor.leatherColor)).append(Text.translatable("tooltip.unidye.leather_color").append(String.format("#%06X", (0xFFFFFF & unidyeColor.leatherColor))).formatted(Formatting.GRAY)));
                mutableText = Text.literal("■ ");
                tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(unidyeColor.dyeColor)).append(Text.translatable("tooltip.unidye.dye_color").append(String.format("#%06X", (0xFFFFFF & unidyeColor.dyeColor))).formatted(Formatting.GRAY)));
            } else {
                tooltip.add(Text.translatable("tooltip.unidye.press_shift"));
            }
        }
    }
}
