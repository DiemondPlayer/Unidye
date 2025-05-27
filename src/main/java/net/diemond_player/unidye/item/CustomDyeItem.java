package net.diemond_player.unidye.item;

import net.diemond_player.unidye.component.ItemNamePrefixComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomDyeItem extends DyeItem implements SignChangingItem{

    public static final HashMap<String, Float> DYE_NAME_TO_FLOAT = new HashMap<>() {{
        put("white", 0.0000f);
        put("orange", 0.0001f);
        put("magenta", 0.0002f);
        put("light_blue", 0.0003f);
        put("yellow", 0.0004f);
        put("lime", 0.0005f);
        put("pink", 0.0006f);
        put("gray", 0.0007f);
        put("light_gray", 0.0008f);
        put("cyan", 0.0009f);
        put("purple", 0.0010f);
        put("blue", 0.0011f);
        put("brown", 0.0012f);
        put("green", 0.0013f);
        put("red", 0.0014f);
        put("black", 0.0015f);
    }};

    //no longer used but used to convert to a new key
    public static final String CLOSEST_VANILLA_DYE_ID_KEY = "closest_vanilla_dye_id";
    //this is where the dye stores its shape now, and it's not an int, it's a string
    public static final String DYE_SHAPE = "dye_shape";
    public static final int DEFAULT_WHITE_COLOR = 16777215;

    public CustomDyeItem(Settings settings) {
        super(DyeColor.WHITE, settings);
    }

    public static float getDyeShapeAsFloat(ItemStack stack) {
        NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(new NbtCompound()));
        String name = "white";
        //This if statement converts old nbt keys to new ones so that world made before 2.0.0 don't get corrupted
        if (nbtComponent != null && nbtComponent.contains(CLOSEST_VANILLA_DYE_ID_KEY)){
            NbtCompound nbtCompound = nbtComponent.copyNbt();
            name = DyeColor.byId(nbtCompound.getInt(CLOSEST_VANILLA_DYE_ID_KEY)).getName();
            nbtCompound.remove(CLOSEST_VANILLA_DYE_ID_KEY);
            nbtCompound.putString(DYE_SHAPE, name);
        } else if (nbtComponent != null && nbtComponent.contains(DYE_SHAPE)) {
            NbtCompound nbtCompound = nbtComponent.copyNbt();
            name = nbtCompound.getString(DYE_SHAPE);
        }
        return DYE_NAME_TO_FLOAT.getOrDefault(name, 0f);
    }

    public static Integer getMaterialColor(ItemStack stack, UnidyeMaterialType materialType) {
        NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(new NbtCompound()));
        //This if statement converts old nbt keys to new ones so that world made before 2.0.0 don't get corrupted
        if (nbtComponent != null && nbtComponent.contains(materialType.getId().getPath())){
            NbtCompound nbtCompound = nbtComponent.copyNbt();
            int n = nbtCompound.getInt(materialType.getId().getPath());
            nbtCompound.remove(materialType.getId().getPath());
            stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbtCompound));
            setMaterialColor(stack, n, materialType);
            return n;
        }
        if (nbtComponent != null && nbtComponent.contains(materialType.getId().toString())) {
            return nbtComponent.copyNbt().getInt(materialType.getId().toString());
        }
        return DEFAULT_WHITE_COLOR;
    }

    public static String getMaterialHexColor(ItemStack stack, UnidyeMaterialType materialType) {
        Integer color = getMaterialColor(stack, materialType);
        return String.format("#%06X", (0xFFFFFF & color));
    }

    public static void setMaterialColor(ItemStack itemStack, int n, UnidyeMaterialType materialType) {
        NbtComponent nbtComponent = itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(new NbtCompound()));
        NbtCompound nbtCompound = nbtComponent.copyNbt();
        nbtCompound.putInt(materialType.getId().toString(), n);
        itemStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbtCompound));
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack,context,tooltip,type);
        if (Screen.hasShiftDown()) {
            for(Map.Entry<Identifier, UnidyeMaterialType> entry : UnidyeMaterialTypes.MATERIAL_TYPES.entrySet()){
                Identifier id = entry.getKey();
                UnidyeMaterialType materialType = entry.getValue();
                if(materialType != UnidyeMaterialTypes.DYE) {
                    MutableText mutableText = Text.literal("■ ");
                    tooltip.add(mutableText.setStyle(mutableText.getStyle().withColor(getMaterialColor(stack, materialType))).append(Text.translatable("tooltip." + id.getNamespace() + "." + id.getPath() + "_color").append(getMaterialHexColor(stack, materialType)).formatted(Formatting.GRAY)));
                }
            }
        } else {
            tooltip.add(Text.translatable("tooltip.unidye.press_shift"));
        }
        tooltip.add(stack.getOrDefault(UnidyeDataComponentTypes.ITEM_NAME_PREFIX, ItemNamePrefixComponent.DEFAULT).toText());
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

    @Override
    public void onCraft(ItemStack itemStack, World world) {
        ItemNamePrefixComponent.updateCustomDyePrefix(itemStack, world);
        super.onCraft(itemStack, world);
    }

    @Override
    public void onCraftByPlayer(ItemStack itemStack, World world, PlayerEntity player) {
        ItemNamePrefixComponent.updateCustomDyePrefix(itemStack, world);
        super.onCraftByPlayer(itemStack, world, player);
    }

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
        Text text = ItemNamePrefixComponent.getName(stack, this.getTranslationKey());
        return text != null ? text : super.getName(stack);
    }
}