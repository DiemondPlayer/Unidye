package net.diemond_player.unidye.mixin;

import net.diemond_player.unidye.registry.UnidyeBlockEntities;
import net.diemond_player.unidye.registry.UnidyeItems;
import net.diemond_player.unidye.item.CustomDyeItem;
import net.diemond_player.unidye.item.DyeableBannerItem;
import net.diemond_player.unidye.util.UnidyeMaterialTypes;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoomScreenHandler.class)
public abstract class LoomScreenHandlerMixin extends ScreenHandler {
    protected LoomScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, Inventory input) {
        super(type, syncId);
        this.input = input;
    }

    @Mutable
    @Final
    @Shadow
    Slot dyeSlot;

    @Mutable
    @Final
    @Shadow
    Slot bannerSlot;


    @Final
    @Shadow
    private Slot outputSlot;

    @Mutable
    @Final
    @Shadow
    private final Inventory input;


    @Inject(method = "updateOutputSlot", at = @At(value = "HEAD"), cancellable = true)
    private void unidye$updateOutputSlot(RegistryEntry<BannerPattern> pattern, CallbackInfo ci) {
        ItemStack itemStack = this.bannerSlot.getStack();
        ItemStack itemStack2 = this.dyeSlot.getStack();
        if (itemStack.isOf(UnidyeItems.CUSTOM_BANNER)) {
            ItemStack itemStack3 = ItemStack.EMPTY;
            if (!itemStack.isEmpty() && !itemStack2.isEmpty()) {
                NbtList nbtList;
                itemStack3 = itemStack.copyWithCount(1);
                NbtCompound nbtCompound = BlockItem.getBlockEntityNbt(itemStack3);
                if (nbtCompound != null && nbtCompound.contains("Patterns", NbtElement.LIST_TYPE)) {
                    nbtList = nbtCompound.getList("Patterns", NbtElement.COMPOUND_TYPE);
                } else {
                    nbtList = new NbtList();
                    if (nbtCompound == null) {
                        nbtCompound = new NbtCompound();
                    }
                    nbtCompound.put("Patterns", nbtList);
                }
                NbtCompound nbtCompound2 = new NbtCompound();
                nbtCompound2.putString("Pattern", pattern.value().getId());
                if (itemStack2.getItem() instanceof CustomDyeItem) {
                    nbtCompound2.putInt("Color", CustomDyeItem.getMaterialColor(itemStack2, UnidyeMaterialTypes.LEATHER));
                } else {
                    nbtCompound2.putInt("Color", ((DyeItem) itemStack2.getItem()).getColor().getId());
                }
                nbtList.add(nbtCompound2);
                BlockItem.setBlockEntityNbt(itemStack3, UnidyeBlockEntities.DYEABLE_BANNER_BE, nbtCompound);
            }
            if (!ItemStack.areEqual(itemStack3, this.outputSlot.getStack())) {
                this.outputSlot.setStackNoCallbacks(itemStack3);
            }
            ci.cancel();
        } else if (itemStack2.getItem() instanceof CustomDyeItem) {
            ItemStack itemStack3 = new ItemStack(UnidyeItems.CUSTOM_BANNER, 1);
            if (!itemStack.isEmpty() && !itemStack2.isEmpty()) {
                NbtList nbtList;
                ItemStack itemStack1 = itemStack.copy();
                NbtCompound nbtCompound = BlockItem.getBlockEntityNbt(itemStack1);
                float[] a = ((BannerItem) itemStack1.getItem()).getColor().getColorComponents();
                int n = (int) (a[0] * 255);
                n = (n << 8) + (int) (a[1] * 255);
                n = (n << 8) + (int) (a[2] * 255);
                ((DyeableBannerItem) itemStack3.getItem()).setColor(itemStack3, n);
                if (nbtCompound != null && nbtCompound.contains("Patterns", NbtElement.LIST_TYPE)) {
                    nbtList = nbtCompound.getList("Patterns", NbtElement.COMPOUND_TYPE);
                } else {
                    nbtList = new NbtList();
                    if (nbtCompound == null) {
                        nbtCompound = new NbtCompound();
                    }
                    nbtCompound.put("Patterns", nbtList);
                }
                NbtCompound nbtCompound2 = new NbtCompound();
                nbtCompound2.putString("Pattern", pattern.value().getId());
                nbtCompound2.putInt("Color", CustomDyeItem.getMaterialColor(itemStack2, UnidyeMaterialTypes.LEATHER));
                nbtList.add(nbtCompound2);
                BlockItem.setBlockEntityNbt(itemStack3, UnidyeBlockEntities.DYEABLE_BANNER_BE, nbtCompound);
            }
            if (!ItemStack.areEqual(itemStack3, this.outputSlot.getStack())) {
                this.outputSlot.setStackNoCallbacks(itemStack3);
            }
            ci.cancel();
        }
    }
}
