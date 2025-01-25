package net.diemond_player.unidye.mixin;

import net.diemond_player.unidye.block.entity.UnidyeBlockEntities;
import net.diemond_player.unidye.component.CustomBannerPatternsComponent;
import net.diemond_player.unidye.component.UnidyeDataComponentTypes;
import net.diemond_player.unidye.item.UnidyeItems;
import net.diemond_player.unidye.item.custom.CustomDyeItem;
import net.diemond_player.unidye.item.custom.DyeableBannerItem;
import net.diemond_player.unidye.util.UnidyeColor;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.component.type.DyedColorComponent;
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
import net.minecraft.util.DyeColor;
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
                itemStack3 = itemStack.copyWithCount(1);
                if (itemStack2.getItem() instanceof CustomDyeItem) {
                    itemStack3.apply(
                            UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS,
                            CustomBannerPatternsComponent.DEFAULT,
                            component -> new CustomBannerPatternsComponent.Builder().addAll(component).add(pattern, CustomDyeItem.getMaterialColor(itemStack2, "leather")).build()
                    );
                } else {
                    DyeColor dyeColor = ((DyeItem) itemStack2.getItem()).getColor();
                    itemStack3.apply(
                            UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS,
                            CustomBannerPatternsComponent.DEFAULT,
                            component -> new CustomBannerPatternsComponent.Builder().addAll(component).add(pattern, UnidyeColor.byId(dyeColor.getId()).leatherColor).build()
                    );
                }
            }

            if (!ItemStack.areEqual(itemStack3, this.outputSlot.getStack())) {
                this.outputSlot.setStackNoCallbacks(itemStack3);
            }
            ci.cancel();
        } else if (itemStack2.getItem() instanceof CustomDyeItem) {
            ItemStack itemStack3 = new ItemStack(UnidyeItems.CUSTOM_BANNER, 1);
            itemStack3.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(UnidyeColor.byId(itemStack.get(DataComponentTypes.BASE_COLOR).getId()).leatherColor, true));
            for(BannerPatternsComponent.Layer layer : itemStack.get(DataComponentTypes.BANNER_PATTERNS).layers()){
                itemStack3.apply(
                        UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS,
                        CustomBannerPatternsComponent.DEFAULT,
                        component -> new CustomBannerPatternsComponent.Builder().addAll(component).add(layer.pattern(), UnidyeColor.byId(layer.color().getId()).leatherColor).build()
                );
            }
            if (!itemStack.isEmpty() && !itemStack2.isEmpty()) {
                itemStack3.apply(
                        UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS,
                        CustomBannerPatternsComponent.DEFAULT,
                        component -> new CustomBannerPatternsComponent.Builder().addAll(component).add(pattern, CustomDyeItem.getMaterialColor(itemStack, "leather")).build()
                );
            }

            if (!ItemStack.areEqual(itemStack3, this.outputSlot.getStack())) {
                this.outputSlot.setStackNoCallbacks(itemStack3);
            }
            ci.cancel();
        }
    }
}
