package net.diemond_player.unidye.item.custom;

import net.diemond_player.unidye.util.UnidyeAccessor;
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
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomDyeItem extends DyeItem implements SignChangingItem, DyeableItem {

    public static final String CLOSEST_VANILLA_DYE_ID_KEY = "closest_vanilla_dye_id";
    public static final int DEFAULT_COLOR = 16777215;

    public CustomDyeItem(Settings settings) {
        super(DyeColor.WHITE, settings);
    }

    @Override
    public int getColor(ItemStack stack) {
        NbtCompound nbtCompound = stack.getSubNbt(DISPLAY_KEY);
        if (nbtCompound != null && nbtCompound.contains(COLOR_KEY, NbtElement.NUMBER_TYPE)) {
            return nbtCompound.getInt(COLOR_KEY);
        }
        return DEFAULT_COLOR;
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

    public static Integer getMaterialColor(ItemStack stack, String materialType) {
        NbtCompound nbtCompound = stack.getNbt();
        if (nbtCompound != null && nbtCompound.contains(materialType, NbtElement.NUMBER_TYPE)) {
            return nbtCompound.getInt(materialType);
        }
        return DEFAULT_COLOR;
    }

    public static String getMaterialHexColor(ItemStack stack, String materialType) {
        Integer color = getMaterialColor(stack, materialType);
        return String.format("#%06X", (0xFFFFFF & color));
    }

    public static void setMaterialColor(ItemStack itemStack, int n, String materialType) {
        NbtCompound nbtCompound = itemStack.getOrCreateNbt();
        nbtCompound.putInt(materialType, n);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            MutableText mutableText = Text.literal("■ ");
            tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(getMaterialColor(stack, "wool"))).append(Text.translatable("tooltip.unidye.wool_color").append(getMaterialHexColor(stack, "wool")).formatted(Formatting.GRAY)));
            mutableText = Text.literal("■ ");
            tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(getMaterialColor(stack, "sign"))).append(Text.translatable("tooltip.unidye.sign_color").append(getMaterialHexColor(stack, "sign")).formatted(Formatting.GRAY)));
            mutableText = Text.literal("■ ");
            tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(getMaterialColor(stack, "glass"))).append(Text.translatable("tooltip.unidye.glass_color").append(getMaterialHexColor(stack, "glass")).formatted(Formatting.GRAY)));
            mutableText = Text.literal("■ ");
            tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(getMaterialColor(stack, "candle"))).append(Text.translatable("tooltip.unidye.candle_color").append(getMaterialHexColor(stack, "candle")).formatted(Formatting.GRAY)));
            mutableText = Text.literal("■ ");
            tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(getMaterialColor(stack, "firework"))).append(Text.translatable("tooltip.unidye.firework_color").append(getMaterialHexColor(stack, "firework")).formatted(Formatting.GRAY)));
            mutableText = Text.literal("■ ");
            tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(getMaterialColor(stack, "concrete"))).append(Text.translatable("tooltip.unidye.concrete_color").append(getMaterialHexColor(stack, "concrete")).formatted(Formatting.GRAY)));
            mutableText = Text.literal("■ ");
            tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(getMaterialColor(stack, "terracotta"))).append(Text.translatable("tooltip.unidye.terracotta_color").append(getMaterialHexColor(stack, "terracotta")).formatted(Formatting.GRAY)));
            mutableText = Text.literal("■ ");
            tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(getMaterialColor(stack, "shulker_box"))).append(Text.translatable("tooltip.unidye.shulker_box_color").append(getMaterialHexColor(stack, "shulker_box")).formatted(Formatting.GRAY)));
            mutableText = Text.literal("■ ");
            tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(getMaterialColor(stack, "leather"))).append(Text.translatable("tooltip.unidye.leather_color").append(getMaterialHexColor(stack, "leather")).formatted(Formatting.GRAY)));
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
                sheep.unidye$setCustomColor(getMaterialColor(stack, "leather"));
                sheep.unidye$setSecondaryCustomColor(getMaterialColor(stack, "wool"));
                stack.decrement(1);
            }
            return ActionResult.success(user.getWorld().isClient);
        }
        return ActionResult.PASS;
    }

    @Override
    public boolean useOnSign(World world, SignBlockEntity signBlockEntity, boolean front, PlayerEntity player) {
        UnidyeAccessor unidyeAccessor = (UnidyeAccessor) signBlockEntity;
        if (unidyeAccessor.unidye$getCustomColor() != getMaterialColor(player.getStackInHand(player.getActiveHand()), "sign")) {
            world.playSound(null, signBlockEntity.getPos(), SoundEvents.ITEM_DYE_USE, SoundCategory.BLOCKS, 1.0f, 1.0f);
            if (front) {
                unidyeAccessor.unidye$setCustomColor(getMaterialColor(player.getStackInHand(player.getActiveHand()), "sign"));
            } else {
                unidyeAccessor.unidye$setSecondaryCustomColor(getMaterialColor(player.getStackInHand(player.getActiveHand()), "sign"));
            }
            signBlockEntity.markDirty();
            world.updateListeners(signBlockEntity.getPos(), world.getBlockState(signBlockEntity.getPos()), world.getBlockState(signBlockEntity.getPos()), Block.NOTIFY_LISTENERS);
            return true;
        }
        return false;
    }
}