package net.diemond_player.unidye.entity;

import com.mojang.logging.LogUtils;
import net.diemond_player.unidye.block.UnidyeBlocks;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.GameRules;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.function.Predicate;

public class DyeableFallingBlockEntity extends FallingBlockEntity {

    private static final Logger LOGGER = LogUtils.getLogger();
    private BlockState block = UnidyeBlocks.CUSTOM_CONCRETE_POWDER.getDefaultState();
    public int timeFalling;
    public boolean dropItem = true;
    private boolean destroyedOnLanding = false;
    private boolean hurtEntities;
    private int fallHurtMax = 40;
    private float fallHurtAmount;
    @Nullable
    public NbtCompound blockEntityData;
    protected static final TrackedData<BlockPos> BLOCK_POS = DataTracker.registerData(DyeableFallingBlockEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    private static final TrackedData<Integer> CUSTOM_COLOR = DataTracker.registerData(DyeableFallingBlockEntity.class, TrackedDataHandlerRegistry.INTEGER);


    public DyeableFallingBlockEntity(EntityType<? extends FallingBlockEntity> entityType, World world) {
        super(entityType, world);
    }

    private DyeableFallingBlockEntity(World world, double x, double y, double z, BlockState block) {
        this(UnidyeEntities.DYEABLE_FALLING_BLOCK_ENTITY, world);
        this.block = block;
        this.intersectionChecked = true;
        this.setPosition(x, y, z);
        this.setVelocity(Vec3d.ZERO);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
        this.setFallingBlockPos(this.getBlockPos());
        this.setCustomColor(this.getCustomColor());
    }

    public static DyeableFallingBlockEntity spawnFromBlock(World world, BlockPos pos, BlockState state) {
        DyeableFallingBlockEntity fallingBlockEntity = new DyeableFallingBlockEntity(world, (double) pos.getX() + 0.5, pos.getY(), (double) pos.getZ() + 0.5, state.contains(Properties.WATERLOGGED) ? (BlockState) state.with(Properties.WATERLOGGED, false) : state);
        world.setBlockState(pos, state.getFluidState().getBlockState(), Block.NOTIFY_ALL);
        world.spawnEntity(fallingBlockEntity);
        return fallingBlockEntity;
    }

    public void setFallingBlockPos(BlockPos pos) {
        this.dataTracker.set(BLOCK_POS, pos);
    }

    public BlockPos getFallingBlockPos() {
        return this.dataTracker.get(BLOCK_POS);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(BLOCK_POS, BlockPos.ORIGIN);
        this.dataTracker.startTracking(CUSTOM_COLOR, 0xFFFFFF);
    }

    @Override
    public void tick() {
        if (this.block.isAir()) {
            this.discard();
        } else {
            Block block = this.block.getBlock();
            ++this.timeFalling;
            if (!this.hasNoGravity()) {
                this.setVelocity(this.getVelocity().add((double)0.0F, -0.04, (double)0.0F));
            }

            this.move(MovementType.SELF, this.getVelocity());
            if (!this.getWorld().isClient) {
                BlockPos blockPos = this.getBlockPos();
                boolean bl = this.block.getBlock() instanceof ConcretePowderBlock;
                boolean bl2 = bl && this.getWorld().getFluidState(blockPos).isIn(FluidTags.WATER);
                double d = this.getVelocity().lengthSquared();
                if (bl && d > (double)1.0F) {
                    BlockHitResult blockHitResult = this.getWorld().raycast(new RaycastContext(new Vec3d(this.prevX, this.prevY, this.prevZ), this.getPos(), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.SOURCE_ONLY, this));
                    if (blockHitResult.getType() != HitResult.Type.MISS && this.getWorld().getFluidState(blockHitResult.getBlockPos()).isIn(FluidTags.WATER)) {
                        blockPos = blockHitResult.getBlockPos();
                        bl2 = true;
                    }
                }

                if (!this.isOnGround() && !bl2) {
                    if (!this.getWorld().isClient && (this.timeFalling > 100 && (blockPos.getY() <= this.getWorld().getBottomY() || blockPos.getY() > this.getWorld().getTopY()) || this.timeFalling > 600)) {
                        if (this.dropItem && this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                            ItemStack itemStack = new ItemStack(UnidyeBlocks.CUSTOM_CONCRETE_POWDER);
                            UnidyeUtils.setColor(itemStack, getCustomColor());
                            this.dropStack(itemStack);
                        }

                        this.discard();
                    }
                } else {
                    BlockState blockState = this.getWorld().getBlockState(blockPos);
                    this.setVelocity(this.getVelocity().multiply(0.7, (double)-0.5F, 0.7));
                    if (!blockState.isOf(Blocks.MOVING_PISTON)) {
                        if (!this.destroyedOnLanding) {
                            boolean bl3 = blockState.canReplace(new AutomaticItemPlacementContext(this.getWorld(), blockPos, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
                            boolean bl4 = FallingBlock.canFallThrough(this.getWorld().getBlockState(blockPos.down())) && (!bl || !bl2);
                            boolean bl5 = this.block.canPlaceAt(this.getWorld(), blockPos) && !bl4;
                            if (bl3 && bl5) {
                                if (this.block.contains(Properties.WATERLOGGED) && this.getWorld().getFluidState(blockPos).getFluid() == Fluids.WATER) {
                                    this.block = (BlockState)this.block.with(Properties.WATERLOGGED, true);
                                }

                                if (this.getWorld().setBlockState(blockPos, this.block, 3)) {
                                    ((ServerWorld)this.getWorld()).getChunkManager().threadedAnvilChunkStorage.sendToOtherNearbyPlayers(this, new BlockUpdateS2CPacket(blockPos, this.getWorld().getBlockState(blockPos)));
                                    this.discard();
                                    if (block instanceof LandingBlock) {
                                        ((LandingBlock)block).onLanding(this.getWorld(), blockPos, this.block, blockState, this);
                                    }

                                    if (this.blockEntityData != null && this.block.hasBlockEntity()) {
                                        BlockEntity blockEntity = this.getWorld().getBlockEntity(blockPos);
                                        if (blockEntity != null) {
                                            NbtCompound nbtCompound = blockEntity.createNbt();

                                            for(String string : this.blockEntityData.getKeys()) {
                                                nbtCompound.put(string, this.blockEntityData.get(string).copy());
                                            }

                                            try {
                                                blockEntity.readNbt(nbtCompound);
                                            } catch (Exception exception) {
                                                LOGGER.error("Failed to load block entity from falling block", exception);
                                            }

                                            blockEntity.markDirty();
                                        }
                                    }
                                } else if (this.dropItem && this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                                    this.discard();
                                    this.onDestroyedOnLanding(block, blockPos);
                                    ItemStack itemStack = new ItemStack(UnidyeBlocks.CUSTOM_CONCRETE_POWDER);
                                    UnidyeUtils.setColor(itemStack, getCustomColor());
                                    this.dropStack(itemStack);
                                }
                            } else {
                                this.discard();
                                if (this.dropItem && this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                                    this.onDestroyedOnLanding(block, blockPos);
                                    ItemStack itemStack = new ItemStack(UnidyeBlocks.CUSTOM_CONCRETE_POWDER);
                                    UnidyeUtils.setColor(itemStack, getCustomColor());
                                    this.dropStack(itemStack);
                                }
                            }
                        } else {
                            this.discard();
                            this.onDestroyedOnLanding(block, blockPos);
                        }
                    }
                }
            }

            this.setVelocity(this.getVelocity().multiply(0.98));
        }
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        DamageSource damageSource2;
        if (!this.hurtEntities) {
            return false;
        }
        int i = MathHelper.ceil(fallDistance - 1.0f);
        if (i < 0) {
            return false;
        }
        Predicate<Entity> predicate = EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.and(EntityPredicates.VALID_LIVING_ENTITY);
        Block block = this.block.getBlock();
        if (block instanceof LandingBlock) {
            LandingBlock landingBlock = (LandingBlock) ((Object) block);
            damageSource2 = landingBlock.getDamageSource(this);
        } else {
            damageSource2 = this.getDamageSources().fallingBlock(this);
        }
        DamageSource damageSource22 = damageSource2;
        float f = Math.min(MathHelper.floor((float) i * this.fallHurtAmount), this.fallHurtMax);
        this.getWorld().getOtherEntities(this, this.getBoundingBox(), predicate).forEach(entity -> entity.damage(damageSource22, f));
        boolean bl = this.block.isIn(BlockTags.ANVIL);
        if (bl && f > 0.0f && this.random.nextFloat() < 0.05f + (float) i * 0.05f) {
            BlockState blockState = AnvilBlock.getLandingState(this.block);
            if (blockState == null) {
                this.destroyedOnLanding = true;
            } else {
                this.block = blockState;
            }
        }
        return false;
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.put("BlockState", NbtHelper.fromBlockState(this.block));
        nbt.putInt("Time", this.timeFalling);
        nbt.putBoolean("DropItem", this.dropItem);
        nbt.putBoolean("HurtEntities", this.hurtEntities);
        nbt.putFloat("FallHurtAmount", this.fallHurtAmount);
        nbt.putInt("FallHurtMax", this.fallHurtMax);
        if (this.blockEntityData != null) {
            nbt.put("TileEntityData", this.blockEntityData);
        }
        nbt.putBoolean("CancelDrop", this.destroyedOnLanding);
        nbt.putInt("unidye.custom_color", getCustomColor());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.block = NbtHelper.toBlockState(this.getWorld().createCommandRegistryWrapper(RegistryKeys.BLOCK), nbt.getCompound("BlockState"));
        this.timeFalling = nbt.getInt("Time");
        if (nbt.contains("HurtEntities", NbtElement.NUMBER_TYPE)) {
            this.hurtEntities = nbt.getBoolean("HurtEntities");
            this.fallHurtAmount = nbt.getFloat("FallHurtAmount");
            this.fallHurtMax = nbt.getInt("FallHurtMax");
        } else if (this.block.isIn(BlockTags.ANVIL)) {
            this.hurtEntities = true;
        }
        if (nbt.contains("DropItem", NbtElement.NUMBER_TYPE)) {
            this.dropItem = nbt.getBoolean("DropItem");
        }
        if (nbt.contains("TileEntityData", NbtElement.COMPOUND_TYPE)) {
            this.blockEntityData = nbt.getCompound("TileEntityData");
        }
        this.destroyedOnLanding = nbt.getBoolean("CancelDrop");
        if (this.block.isAir()) {
            this.block = Blocks.SAND.getDefaultState();
        }
        if (nbt.contains("unidye.custom_color")) {
            setCustomColor(nbt.getInt("unidye.custom_color"));
        }
    }

    public void setHurtEntities(float fallHurtAmount, int fallHurtMax) {
        this.hurtEntities = true;
        this.fallHurtAmount = fallHurtAmount;
        this.fallHurtMax = fallHurtMax;
    }

    public void setDestroyedOnLanding() {
        this.destroyedOnLanding = true;
    }

    public BlockState getBlockState() {
        return this.block;
    }

    @Override
    protected Text getDefaultName() {
        return Text.translatable("entity.minecraft.falling_block_type", this.block.getBlock().getName());
    }

    @Override
    public Packet<ClientPlayPacketListener> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this, Block.getRawIdFromState(this.getBlockState()));
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        this.block = Block.getStateFromRawId(packet.getEntityData());
        this.intersectionChecked = true;
        double d = packet.getX();
        double e = packet.getY();
        double f = packet.getZ();
        this.setPosition(d, e, f);
        this.setFallingBlockPos(this.getBlockPos());
        this.setCustomColor(this.getCustomColor());
    }

    @Override
    protected Box calculateBoundingBox() {
        return new Box(this.getPos().x - 0.5, this.getPos().y, this.getPos().z - 0.5, this.getPos().x + 0.5, this.getPos().y + 1.0, this.getPos().z + 0.5);
    }

    public int getCustomColor() {
        return this.getDataTracker().get(CUSTOM_COLOR);
    }

    public void setCustomColor(int color) {
        this.getDataTracker().set(CUSTOM_COLOR, color);
    }
}
