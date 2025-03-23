package net.diemond_player.unidye.block.entity;

import net.diemond_player.unidye.block.custom.DyeableShulkerBoxBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.IntStream;

import static net.diemond_player.unidye.item.custom.CustomDyeItem.DEFAULT_WHITE_COLOR;

public class DyeableShulkerBoxBlockEntity extends LootableContainerBlockEntity
        implements SidedInventory, IDyeableBlockEntity {
    private int color = DEFAULT_WHITE_COLOR;
    public static final String ITEMS_KEY = "Items";
    private static final int[] AVAILABLE_SLOTS = IntStream.range(0, 27).toArray();
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
    private int viewerCount;
    private AnimationStage animationStage = AnimationStage.CLOSED;
    private float animationProgress;
    private float prevAnimationProgress;

    public DyeableShulkerBoxBlockEntity(BlockPos pos, BlockState state) {
        super(UnidyeBlockEntities.DYEABLE_SHULKER_BOX_BE, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, DyeableShulkerBoxBlockEntity blockEntity) {
        blockEntity.updateAnimation(world, pos, state);
    }

    private void updateAnimation(World world, BlockPos pos, BlockState state) {
        this.prevAnimationProgress = this.animationProgress;
        switch (this.animationStage) {
            case CLOSED: {
                this.animationProgress = 0.0f;
                break;
            }
            case OPENING: {
                this.animationProgress += 0.1f;
                if (this.animationProgress >= 1.0f) {
                    this.animationStage = DyeableShulkerBoxBlockEntity.AnimationStage.OPENED;
                    this.animationProgress = 1.0f;
                    DyeableShulkerBoxBlockEntity.updateNeighborStates(world, pos, state);
                }
                this.pushEntities(world, pos, state);
                break;
            }
            case CLOSING: {
                this.animationProgress -= 0.1f;
                if (!(this.animationProgress <= 0.0f)) break;
                this.animationStage = DyeableShulkerBoxBlockEntity.AnimationStage.CLOSED;
                this.animationProgress = 0.0f;
                DyeableShulkerBoxBlockEntity.updateNeighborStates(world, pos, state);
                break;
            }
            case OPENED: {
                this.animationProgress = 1.0f;
            }
        }
    }

    @Override
    public void markDirty() {
        if (this.world != null) {
            markDirty(this.world, this.pos, this.getCachedState());
        }
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public DyeableShulkerBoxBlockEntity.AnimationStage getAnimationStage() {
        return this.animationStage;
    }

    public Box getBoundingBox(BlockState state) {
        return ShulkerEntity.calculateBoundingBox(state.get(DyeableShulkerBoxBlock.FACING), 0.5f * this.getAnimationProgress(1.0f));
    }

    private void pushEntities(World world, BlockPos pos, BlockState state) {
        if (!(state.getBlock() instanceof DyeableShulkerBoxBlock)) {
            return;
        }
        Direction direction = state.get(DyeableShulkerBoxBlock.FACING);
        Box box = ShulkerEntity.calculateBoundingBox(direction, this.prevAnimationProgress, this.animationProgress).offset(pos);
        List<Entity> list = world.getOtherEntities(null, box);
        if (list.isEmpty()) {
            return;
        }
        for (int i = 0; i < list.size(); ++i) {
            Entity entity = list.get(i);
            if (entity.getPistonBehavior() == PistonBehavior.IGNORE) continue;
            entity.move(MovementType.SHULKER_BOX, new Vec3d((box.getXLength() + 0.01) * (double) direction.getOffsetX(), (box.getYLength() + 0.01) * (double) direction.getOffsetY(), (box.getZLength() + 0.01) * (double) direction.getOffsetZ()));
        }
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    @Override
    public boolean onSyncedBlockEvent(int type, int data) {
        if (type == 1) {
            this.viewerCount = data;
            if (data == 0) {
                this.animationStage = DyeableShulkerBoxBlockEntity.AnimationStage.CLOSING;
                DyeableShulkerBoxBlockEntity.updateNeighborStates(this.getWorld(), this.pos, this.getCachedState());
            }
            if (data == 1) {
                this.animationStage = DyeableShulkerBoxBlockEntity.AnimationStage.OPENING;
                DyeableShulkerBoxBlockEntity.updateNeighborStates(this.getWorld(), this.pos, this.getCachedState());
            }
            return true;
        }
        return super.onSyncedBlockEvent(type, data);
    }

    private static void updateNeighborStates(World world, BlockPos pos, BlockState state) {
        state.updateNeighbors(world, pos, Block.NOTIFY_ALL);
    }

    @Override
    public void onOpen(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            if (this.viewerCount < 0) {
                this.viewerCount = 0;
            }
            ++this.viewerCount;
            this.world.addSyncedBlockEvent(this.pos, this.getCachedState().getBlock(), 1, this.viewerCount);
            if (this.viewerCount == 1) {
                this.world.emitGameEvent((Entity) player, GameEvent.CONTAINER_OPEN, this.pos);
                this.world.playSound(null, this.pos, SoundEvents.BLOCK_SHULKER_BOX_OPEN, SoundCategory.BLOCKS, 0.5f, this.world.random.nextFloat() * 0.1f + 0.9f);
            }
        }
    }

    @Override
    public void onClose(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            --this.viewerCount;
            this.world.addSyncedBlockEvent(this.pos, this.getCachedState().getBlock(), 1, this.viewerCount);
            if (this.viewerCount <= 0) {
                this.world.emitGameEvent((Entity) player, GameEvent.CONTAINER_CLOSE, this.pos);
                this.world.playSound(null, this.pos, SoundEvents.BLOCK_SHULKER_BOX_CLOSE, SoundCategory.BLOCKS, 0.5f, this.world.random.nextFloat() * 0.1f + 0.9f);
            }
        }
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.shulkerBox");
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.readInventoryNbt(nbt);
        if (nbt.getInt("color") == 0) {
            color = DEFAULT_WHITE_COLOR;
        } else {
            color = nbt.getInt("color");
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (!this.serializeLootTable(nbt)) {
            Inventories.writeNbt(nbt, this.inventory, false);
        }
        if (color != DEFAULT_WHITE_COLOR) {
            nbt.putInt("color", color);
        }
    }

    public void readInventoryNbt(NbtCompound nbt) {
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.deserializeLootTable(nbt) && nbt.contains(ITEMS_KEY, NbtElement.LIST_TYPE)) {
            Inventories.readNbt(nbt, this.inventory);
        }
    }

    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
        return this.inventory;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.inventory = list;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return AVAILABLE_SLOTS;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return !(Block.getBlockFromItem(stack.getItem()) instanceof DyeableShulkerBoxBlock);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    public float getAnimationProgress(float delta) {
        return MathHelper.lerp(delta, this.prevAnimationProgress, this.animationProgress);
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new ShulkerBoxScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void setColor(int color) {
        this.color = color;
    }

    public boolean suffocates() {
        return this.animationStage == DyeableShulkerBoxBlockEntity.AnimationStage.CLOSED;
    }

    public enum AnimationStage {
        CLOSED,
        OPENING,
        OPENED,
        CLOSING;
    }
}
