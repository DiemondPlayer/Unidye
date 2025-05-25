package net.diemond_player.unidye.mixin.misc;

import net.diemond_player.unidye.block.entity.DyeableBannerBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.map.MapBannerMarker;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(MapBannerMarker.class)
public abstract class MapBannerMarkerMixin {
    @Inject(method = "fromWorldBlock", at = @At(value = "HEAD"), cancellable = true)
    private static void unidye$fromWorldBlock(BlockView blockView, BlockPos blockPos, CallbackInfoReturnable<MapBannerMarker> cir) {
        BlockEntity blockEntity = blockView.getBlockEntity(blockPos);
        if (blockEntity instanceof DyeableBannerBlockEntity bannerBlockEntity) {
            Text text = bannerBlockEntity.hasCustomName() ? bannerBlockEntity.getCustomName() : null;
            MapBannerMarker mapBannerMarker = new MapBannerMarker(blockPos, DyeColor.WHITE, Optional.ofNullable(text));
            cir.setReturnValue(mapBannerMarker);
        }
    }
}
