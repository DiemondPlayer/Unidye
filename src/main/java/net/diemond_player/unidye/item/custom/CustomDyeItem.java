package net.diemond_player.unidye.item.custom;

import net.diemond_player.unidye.util.UnidyeAccessor;
import net.diemond_player.unidye.util.UnidyeMaterialType;
import net.diemond_player.unidye.util.UnidyeMaterialTypes;
import net.minecraft.block.Block;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SignChangingItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class CustomDyeItem extends DyeItem implements SignChangingItem, DyeableItem {

    public static final String CLOSEST_VANILLA_DYE_ID_KEY = "closest_vanilla_dye_id";
    public static final int DEFAULT_WHITE_COLOR = 16777215;

    public CustomDyeItem(Settings settings) {
        super(DyeColor.WHITE, settings);
    }

    @Override
    public int getColor(ItemStack stack) {
        NbtCompound nbtCompound = stack.getSubNbt(DISPLAY_KEY);
        if (nbtCompound != null && nbtCompound.contains(COLOR_KEY, NbtElement.NUMBER_TYPE)) {
            return nbtCompound.getInt(COLOR_KEY);
        }
        return DEFAULT_WHITE_COLOR;
    }

    public static float getClosestVanillaDyeId(ItemStack stack) {
        NbtCompound nbtCompound = stack.getOrCreateNbt();
        float id;
        if (nbtCompound.contains(CLOSEST_VANILLA_DYE_ID_KEY, NbtElement.NUMBER_TYPE)) {
            id = nbtCompound.getInt(CLOSEST_VANILLA_DYE_ID_KEY);
        } else {
            return 0;
        }
        return id / 15;
    }

    public static Integer getMaterialColor(ItemStack stack, UnidyeMaterialType materialType) {
        NbtCompound nbtCompound = stack.getNbt();
        //This if statement converts old nbt keys to new ones so that world made before 2.0.0 don't get corrupted
        if (nbtCompound != null && nbtCompound.contains(materialType.getId().getPath(), NbtElement.NUMBER_TYPE)){
            int n = nbtCompound.getInt(materialType.getId().getPath());
            setMaterialColor(stack, n, materialType);
            stack.getNbt().remove(materialType.getId().getPath());
            return n;
        }
        if (nbtCompound != null && nbtCompound.contains(materialType.getId().toString(), NbtElement.NUMBER_TYPE)) {
            return nbtCompound.getInt(materialType.getId().toString());
        }
        return DEFAULT_WHITE_COLOR;
    }

    public static String getMaterialHexColor(ItemStack stack, UnidyeMaterialType materialType) {
        Integer color = getMaterialColor(stack, materialType);
        return String.format("#%06X", (0xFFFFFF & color));
    }

    public static void setMaterialColor(ItemStack itemStack, int n, UnidyeMaterialType materialType) {
        NbtCompound nbtCompound = itemStack.getOrCreateNbt();
        nbtCompound.putInt(materialType.getId().toString(), n);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack,world,tooltip,context);
        if (Screen.hasShiftDown()) {
            for(Map.Entry<Identifier, UnidyeMaterialType> entry : UnidyeMaterialTypes.MATERIAL_TYPES.entrySet()){
                Identifier id = entry.getKey();
                UnidyeMaterialType type = entry.getValue();
                if(type != UnidyeMaterialTypes.DYE) {
                    MutableText mutableText = Text.literal("■ ");
                    tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(getMaterialColor(stack, type))).append(Text.translatable("tooltip." + id.getNamespace() + "." + id.getPath() + "_color").append(getMaterialHexColor(stack, type)).formatted(Formatting.GRAY)));
                }
            }
        } else {
            tooltip.add(Text.translatable("tooltip.unidye.press_shift"));
        }
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        SheepEntity sheepEntity;
        if (entity instanceof SheepEntity
                && (sheepEntity = (SheepEntity) entity).isAlive()
                && !sheepEntity.isSheared()) {
            sheepEntity.getWorld().playSoundFromEntity(user, sheepEntity,
                    SoundEvents.ITEM_DYE_USE, SoundCategory.PLAYERS, 1.0f, 1.0f);
            if (!user.getWorld().isClient) {
                UnidyeAccessor sheep = (UnidyeAccessor) sheepEntity;
                sheep.unidye$setCustomColor(getMaterialColor(stack, UnidyeMaterialTypes.LEATHER));
                sheep.unidye$setSecondaryCustomColor(getMaterialColor(stack, UnidyeMaterialTypes.WOOL));
                stack.decrement(1);
            }
            return ActionResult.success(user.getWorld().isClient);
        }
        return ActionResult.PASS;
    }

    @Override
    public boolean useOnSign(World world, SignBlockEntity signBlockEntity, boolean front, PlayerEntity player) {
        UnidyeAccessor unidyeAccessor = (UnidyeAccessor) signBlockEntity;
        if (unidyeAccessor.unidye$getCustomColor() != getMaterialColor(player.getStackInHand(player.getActiveHand()), UnidyeMaterialTypes.SIGN)) {
            world.playSound(null, signBlockEntity.getPos(), SoundEvents.ITEM_DYE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
            if (front) {
                unidyeAccessor.unidye$setCustomColor(getMaterialColor(player.getStackInHand(player.getActiveHand()), UnidyeMaterialTypes.SIGN));
            } else {
                unidyeAccessor.unidye$setSecondaryCustomColor(getMaterialColor(player.getStackInHand(player.getActiveHand()), UnidyeMaterialTypes.SIGN));
            }
            signBlockEntity.markDirty();
            world.updateListeners(signBlockEntity.getPos(), world.getBlockState(signBlockEntity.getPos()), world.getBlockState(signBlockEntity.getPos()), Block.NOTIFY_LISTENERS);
            return true;
        }
        return false;
    }
}