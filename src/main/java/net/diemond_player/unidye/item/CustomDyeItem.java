package net.diemond_player.unidye.item;

import net.diemond_player.unidye.component.ItemNameAffixesComponent;
import net.diemond_player.unidye.component.MaterialColorsComponent;
import net.diemond_player.unidye.registry.UnidyeMaterialTypes;
import net.diemond_player.unidye.util.UnidyeAccessor;
import net.diemond_player.unidye.util.UnidyeMaterialType;
import net.minecraft.block.Block;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SignChangingItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

import static net.diemond_player.unidye.component.MaterialColorsComponent.*;

public class CustomDyeItem extends DyeItem implements SignChangingItem{
    //no longer used but used to convert to a new key
    public static final String CLOSEST_VANILLA_DYE_ID_KEY = "closest_vanilla_dye_id";
    //this is where the dye stores its shape now, and it's not an int, it's a string
    public static final String DYE_SHAPE = "dye_shape";

    public CustomDyeItem(Settings settings) {
        super(DyeColor.WHITE, settings);
    }

    public static String getDyeShape(ItemStack stack) {
        NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(new NbtCompound()));
        String name = "white";
        //This if statement converts old nbt keys to new ones so that world made before 2.0.0 don't get corrupted
        if (nbtComponent != null && nbtComponent.contains(CLOSEST_VANILLA_DYE_ID_KEY)) {
            NbtCompound nbtCompound = nbtComponent.copyNbt();
            name = DyeColor.byId(nbtCompound.getInt(CLOSEST_VANILLA_DYE_ID_KEY)).getName();
            nbtCompound.remove(CLOSEST_VANILLA_DYE_ID_KEY);
            nbtCompound.putString(DYE_SHAPE, name);
        } else if (nbtComponent != null && nbtComponent.contains(DYE_SHAPE)) {
            NbtCompound nbtCompound = nbtComponent.copyNbt();
            name = nbtCompound.getString(DYE_SHAPE);
        }
        return name;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack,context,tooltip,type);
        MaterialColorsComponent.appendTooltip(stack, tooltip::add);
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
                sheep.unidye$setCustomDyeItemStack(stack.copyWithCount(1));
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

//    @Override
//    public void onCraft(ItemStack itemStack, World world) {
//        ItemNameAffixesComponent.updatePrefix(itemStack, world);
//        super.onCraft(itemStack, world);
//    }
//
//    @Override
//    public void onCraftByPlayer(ItemStack itemStack, World world, PlayerEntity player) {
//        ItemNameAffixesComponent.updatePrefix(itemStack, world);
//        super.onCraftByPlayer(itemStack, world, player);
//    }

    @Override
    public DyeColor getColor() {
        //try {
//            Unidye.LOGGER.warn("{} returns DyeColor.WHITE as a requirement rather than an actual color, calling the {} method is not recommended.", this.getClass().getName(), this.getClass().getMethod("getColor").getName());
//       } catch (NoSuchMethodException ignored) {
//     }
        return super.getColor();
    }

    @Override
    public Text getName(ItemStack stack) {
        Text text = ItemNameAffixesComponent.getName(stack, this.getTranslationKey());
        return !Objects.equals(text, Text.empty()) ? text : super.getName(stack);
    }
}