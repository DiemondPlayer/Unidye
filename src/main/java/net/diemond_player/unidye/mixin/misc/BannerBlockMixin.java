package net.diemond_player.unidye.mixin.misc;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.diemond_player.unidye.registry.UnidyeBlocks;
import net.minecraft.block.BannerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BannerBlock.class)
public abstract class BannerBlockMixin {
    @ModifyReturnValue(method = "getForColor", at = @At("RETURN"))
    private static Block unidye$getForColor(Block original) {
        return original == UnidyeBlocks.CUSTOM_BANNER ? Blocks.WHITE_BANNER : original;
    }
}
