package net.diemond_player.unidye.block.entity;

import com.mojang.logging.LogUtils;
import net.diemond_player.unidye.component.CustomBannerPatternsComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.registry.UnidyeBlockEntities;
import net.diemond_player.unidye.registry.UnidyeBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import static net.diemond_player.unidye.item.CustomDyeItem.DEFAULT_WHITE_COLOR;

public class DyeableBannerBlockEntity extends BlockEntity implements Nameable, IDyeableBlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();
    @Nullable
    private Text customName;
    private CustomBannerPatternsComponent patterns = CustomBannerPatternsComponent.DEFAULT;
    public int color = DEFAULT_WHITE_COLOR;

    public DyeableBannerBlockEntity(BlockPos pos, BlockState state) {
        super(UnidyeBlockEntities.DYEABLE_BANNER_BE, pos, state);
    }

    @Override
    public void markDirty() {
        if (this.world != null) {
            markDirty(this.world, this.pos, this.getCachedState());
        }
    }

    public static int getColor(BlockView world, BlockPos pos) {
        if (world == null) {
            return DEFAULT_WHITE_COLOR;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof DyeableBannerBlockEntity dyeableBlockEntity) {
            return dyeableBlockEntity.color;
        } else {
            return DEFAULT_WHITE_COLOR;
        }
    }

    public void readFrom(ItemStack stack) {
        this.readComponents(stack);
    }

    @Override
    public Text getName() {
        if (this.customName != null) {
            return this.customName;
        }
        return Text.translatable("block.unidye.custom_banner");
    }

    @Override
    @Nullable
    public Text getCustomName() {
        return this.customName;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        if (!this.patterns.equals(CustomBannerPatternsComponent.DEFAULT)) {
            nbt.put("patterns", CustomBannerPatternsComponent.CODEC.encodeStart(registryLookup.getOps(NbtOps.INSTANCE), this.patterns).getOrThrow());
        }
        if (this.customName != null) {
            nbt.putString("CustomName", Text.Serialization.toJsonString(this.customName, registryLookup));
        }
        if (color != DEFAULT_WHITE_COLOR) {
            nbt.putInt("color", color);
        }
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if (nbt.contains("CustomName", NbtElement.STRING_TYPE)) {
            this.customName = Text.Serialization.fromJson(nbt.getString("CustomName"), registryLookup);
        }

        if (nbt.contains("patterns")) {
            CustomBannerPatternsComponent.CODEC
                    .parse(registryLookup.getOps(NbtOps.INSTANCE), nbt.get("patterns"))
                    .resultOrPartial(patterns -> LOGGER.error("Failed to parse banner patterns: '{}'", patterns))
                    .ifPresent(patterns -> this.patterns = patterns);
        }
        if (nbt.getInt("color") == 0) {
            color = DEFAULT_WHITE_COLOR;
        } else {
            color = nbt.getInt("color");
        }
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return this.createNbt(registryLookup);
    }

    public CustomBannerPatternsComponent getPatterns() {
        return this.patterns;
    }

    public ItemStack getPickStack() {
        ItemStack itemStack = new ItemStack(UnidyeBlocks.CUSTOM_BANNER);
        itemStack.applyComponentsFrom(this.createComponentMap());
        DyeableBannerBlockEntity blockEntity = UnidyeBlockEntities.DYEABLE_BANNER_BE.get(world, pos);
        int color = DEFAULT_WHITE_COLOR;
        if (blockEntity != null) {
            color = blockEntity.color;
        }
        itemStack.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(color, true));
        return itemStack;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
        this.markDirty();
    }

    @Override
    protected void readComponents(BlockEntity.ComponentsAccess components) {
        super.readComponents(components);
        this.patterns = components.getOrDefault(UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS, CustomBannerPatternsComponent.DEFAULT);
        this.customName = components.get(DataComponentTypes.CUSTOM_NAME);
    }

    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        componentMapBuilder.add(UnidyeDataComponentTypes.CUSTOM_BANNER_PATTERNS, this.patterns);
        componentMapBuilder.add(DataComponentTypes.CUSTOM_NAME, this.customName);
    }
}