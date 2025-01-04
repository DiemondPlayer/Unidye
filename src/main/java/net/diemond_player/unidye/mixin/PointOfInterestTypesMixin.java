package net.diemond_player.unidye.mixin;

import net.diemond_player.unidye.block.UnidyeBlocks;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.BedPart;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.Optional;

@Mixin(PointOfInterestTypes.class)
public abstract class PointOfInterestTypesMixin {
    @Inject(method = "getTypeForState", at = @At(value = "RETURN"), cancellable = true)
    private static void unidye$checkForCustomBedStates(BlockState state, CallbackInfoReturnable<Optional<RegistryEntry<PointOfInterestType>>> cir) {
        Block[] blocks = new Block[1];
        blocks[0] = UnidyeBlocks.CUSTOM_BED;
        if (state.getBlock() instanceof BedBlock && Arrays.stream(blocks).anyMatch(x -> x.equals(state.getBlock())) && state.get(BedBlock.PART) == BedPart.HEAD) {
            cir.setReturnValue(
                    Optional.ofNullable(Registries.POINT_OF_INTEREST_TYPE.getEntry(PointOfInterestTypes.HOME).orElse(null))
            );
        }
    }
}
