package net.diemond_player.unidye.mixin;

import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.registry.UnidyeItems;
import net.diemond_player.unidye.util.DyeNameDatabaseSaverAndLoader;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingScreenHandler.class)
public abstract class CraftingScreenHandlerMixin {

    @Inject(method = "updateResult", at = @At("TAIL"))
    private static void unidye$updateResult(ScreenHandler handler, World world, PlayerEntity player, RecipeInputInventory craftingInventory, CraftingResultInventory resultInventory, @Nullable RecipeEntry<CraftingRecipe> recipe, CallbackInfo ci) {
        ItemStack itemStack = resultInventory.getStack(0);
        if(itemStack.isOf(UnidyeItems.CUSTOM_DYE)){
            if(!world.isClient){
                NbtCompound nbtCompound = itemStack.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
                nbtCompound.remove("dye_shape");
                NbtComponent nbtComponent = NbtComponent.of(nbtCompound);
                DyeNameDatabaseSaverAndLoader serverState = DyeNameDatabaseSaverAndLoader.getServerState(((ServerWorld)world).getServer());
                Unidye.LOGGER.info(serverState.database.toString());
                if (serverState.database.containsKey(nbtComponent)) {
                    itemStack.set(DataComponentTypes.ITEM_NAME, Text.literal(serverState.database.get(nbtComponent)));
                }
            }
        }
    }
}
