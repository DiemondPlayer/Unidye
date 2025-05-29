package net.diemond_player.unidye.mixin.misc;

import net.diemond_player.unidye.component.CustomBannerPatternsComponent;
import net.diemond_player.unidye.component.ItemNameAffixesComponent;
import net.diemond_player.unidye.item.CustomDyeItem;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.registry.UnidyeItems;
import net.diemond_player.unidye.registry.UnidyeMaterialTypes;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.LoomScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.ColorHelper;
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

    @Shadow
    final Property selectedPattern = Property.create();

    @Inject(method = "updateOutputSlot", at = @At(value = "HEAD"), cancellable = true)
    private void unidye$updateOutputSlot(RegistryEntry<BannerPattern> pattern, CallbackInfo ci) {
        ItemStack itemStack = this.bannerSlot.getStack();
        ItemStack itemStack2 = this.dyeSlot.getStack();
        if (itemStack.isOf(UnidyeItems.CUSTOM_BANNER)) {
            ItemStack itemStack3 = ItemStack.EMPTY;
            if (!itemStack.isEmpty() && !itemStack2.isEmpty()) {
                itemStack3 = itemStack.copyWithCount(1);
                if (itemStack2.getItem() instanceof CustomDyeItem) {
                    itemStack3.apply(
                            UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS,
                            CustomBannerPatternsComponent.DEFAULT,
                            component -> new CustomBannerPatternsComponent.Builder().addAll(component).add(pattern, CustomDyeItem.getMaterialColor(itemStack2, UnidyeMaterialTypes.LEATHER), itemStack2.getOrDefault(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, ItemNameAffixesComponent.DEFAULT)).build()
                    );
                } else {
                    DyeColor dyeColor = ((DyeItem) itemStack2.getItem()).getColor();
                    itemStack3.apply(
                            UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS,
                            CustomBannerPatternsComponent.DEFAULT,
                            component -> new CustomBannerPatternsComponent.Builder().addAll(component).add(pattern, ColorHelper.Argb.withAlpha(0, dyeColor.getEntityColor())).build()
                    );
                }
            }
            if (!ItemStack.areEqual(itemStack3, this.outputSlot.getStack())) {
                this.outputSlot.setStackNoCallbacks(itemStack3);
            }
            ci.cancel();
        } else if (itemStack2.getItem() instanceof CustomDyeItem) {
            ItemStack itemStack3 = new ItemStack(UnidyeItems.CUSTOM_BANNER, 1);
            itemStack3.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(ColorHelper.Argb.withAlpha(0, ((BannerItem) itemStack.getItem()).getColor().getEntityColor()), false));
            itemStack3.set(DataComponentTypes.ITEM_NAME, itemStack.getName());
            for (BannerPatternsComponent.Layer layer : itemStack.get(DataComponentTypes.BANNER_PATTERNS).layers()) {
                itemStack3.apply(
                        UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS,
                        CustomBannerPatternsComponent.DEFAULT,
                        component -> new CustomBannerPatternsComponent.Builder().addAll(component).add(layer.pattern(), ColorHelper.Argb.withAlpha(0, ((BannerItem) itemStack.getItem()).getColor().getEntityColor())).build()
                );
            }
            if (!itemStack.isEmpty() && !itemStack2.isEmpty()) {
                itemStack3.apply(
                        UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS,
                        CustomBannerPatternsComponent.DEFAULT,
                        component -> new CustomBannerPatternsComponent.Builder().addAll(component).add(pattern, CustomDyeItem.getMaterialColor(itemStack2, UnidyeMaterialTypes.LEATHER), itemStack2.getOrDefault(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, ItemNameAffixesComponent.DEFAULT)).build()
                );
            }

            if (!ItemStack.areEqual(itemStack3, this.outputSlot.getStack())) {
                this.outputSlot.setStackNoCallbacks(itemStack3);
            }
            ci.cancel();
        }
    }

    @Inject(method = "onContentChanged", at = @At(value = "HEAD"), cancellable = true)
    private void unidye$onContentChanged(Inventory inventory, CallbackInfo ci) {
        CustomBannerPatternsComponent bannerPatternsComponent = this.bannerSlot.getStack().getOrDefault(UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS, CustomBannerPatternsComponent.DEFAULT);
        if (bannerPatternsComponent.layers().size() >= 6) {
            this.selectedPattern.set(-1);
            this.outputSlot.setStackNoCallbacks(ItemStack.EMPTY);
            this.sendContentUpdates();
            ci.cancel();
        }
    }
}