package net.diemond_player.unidye.entity;

import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.block.DyeableConcretePowderBlock;
import net.diemond_player.unidye.block.entity.DyeableBlockEntity;
import net.diemond_player.unidye.component.ItemNameAffixesComponent;
import net.diemond_player.unidye.component.RecipeStacksComponent;
import net.diemond_player.unidye.mixin.util.FallingBlockEntityAccessor;
import net.diemond_player.unidye.payload.SetColorAndRerenderBlockPayload;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.registry.UnidyeEntities;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import static net.diemond_player.unidye.item.CustomDyeItem.DEFAULT_WHITE_COLOR;

public class DyeableFallingBlockEntity extends FallingBlockEntity {

    protected static final TrackedData<Integer> CUSTOM_COLOR = DataTracker.registerData(DyeableFallingBlockEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public DyeableFallingBlockEntity(EntityType<? extends FallingBlockEntity> entityType, World world) {
        super(entityType, world);
    }

    private DyeableFallingBlockEntity(World world, double x, double y, double z, BlockState block) {
        this(UnidyeEntities.DYEABLE_FALLING_BLOCK_ENTITY, world);
        ((FallingBlockEntityAccessor)this).setBlock(block);
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
        DyeableFallingBlockEntity fallingBlockEntity = new DyeableFallingBlockEntity(
                world,
                (double)pos.getX() + 0.5,
                pos.getY(),
                (double)pos.getZ() + 0.5,
                state.contains(Properties.WATERLOGGED) ? state.with(Properties.WATERLOGGED, Boolean.FALSE) : state
        );
        world.setBlockState(pos, state.getFluidState().getBlockState(), Block.NOTIFY_ALL);
        world.spawnEntity(fallingBlockEntity);
        return fallingBlockEntity;
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(CUSTOM_COLOR, 0xFFFFFF);
    }

    @Override
    public void tick() {
        if (((FallingBlockEntityAccessor)this).getBlock().isAir()) {
            this.discard();
        } else {
            Block block = ((FallingBlockEntityAccessor)this).getBlock().getBlock();
            this.timeFalling++;
            this.applyGravity();
            this.move(MovementType.SELF, this.getVelocity());
            this.tickPortalTeleportation();
            if (!this.getWorld().isClient && (this.isAlive() || this.shouldDupe)) {
                BlockPos blockPos = this.getBlockPos();
                boolean bl = ((FallingBlockEntityAccessor)this).getBlock().getBlock() instanceof DyeableConcretePowderBlock;
                boolean bl2 = bl && this.getWorld().getFluidState(blockPos).isIn(FluidTags.WATER);
                double d = this.getVelocity().lengthSquared();
                if (bl && d > 1.0) {
                    BlockHitResult blockHitResult = this.getWorld()
                            .raycast(new RaycastContext(new Vec3d(this.prevX, this.prevY, this.prevZ), this.getPos(), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.SOURCE_ONLY, this));
                    if (blockHitResult.getType() != HitResult.Type.MISS && this.getWorld().getFluidState(blockHitResult.getBlockPos()).isIn(FluidTags.WATER)) {
                        blockPos = blockHitResult.getBlockPos();
                        bl2 = true;
                    }
                }

                if (this.isOnGround() || bl2) {
                    BlockState blockState = this.getWorld().getBlockState(blockPos);
                    this.setVelocity(this.getVelocity().multiply(0.7, -0.5, 0.7));
                    if (!blockState.isOf(Blocks.MOVING_PISTON)) {
                        if (!((FallingBlockEntityAccessor)this).getDestroyedOnLanding()) {
                            boolean bl3 = blockState.canReplace(new AutomaticItemPlacementContext(this.getWorld(), blockPos, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
                            boolean bl4 = FallingBlock.canFallThrough(this.getWorld().getBlockState(blockPos.down())) && (!bl || !bl2);
                            boolean bl5 = ((FallingBlockEntityAccessor)this).getBlock().canPlaceAt(this.getWorld(), blockPos) && !bl4;
                            if (bl3 && bl5) {
                                if (((FallingBlockEntityAccessor)this).getBlock().contains(Properties.WATERLOGGED) && this.getWorld().getFluidState(blockPos).getFluid() == Fluids.WATER) {
                                    ((FallingBlockEntityAccessor)this).setBlock(((FallingBlockEntityAccessor)this).getBlock().with(Properties.WATERLOGGED, Boolean.TRUE));
                                }

                                if (this.getWorld().setBlockState(blockPos, ((FallingBlockEntityAccessor)this).getBlock(), Block.NOTIFY_ALL)) {
//                                    BlockEntity fallenBlockEntity = this.getWorld().getBlockEntity(blockPos);
//                                    if (fallenBlockEntity instanceof DyeableBlockEntity dyeableBlockEntity) {
//                                        dyeableBlockEntity.setColor(this.getCustomColor());
//                                    }
//                                    ((ServerWorld)this.getWorld())
//                                            .getChunkManager()
//                                            .chunkLoadingManager
//                                            .sendToOtherNearbyPlayers(this, new BlockUpdateS2CPacket(blockPos, this.getWorld().getBlockState(blockPos)));
//                                    for (ServerPlayerEntity player : PlayerLookup.tracking((ServerWorld) this.getWorld(), blockPos)) {
//                                        ServerPlayNetworking.send(player, new SetColorAndRerenderBlockPayload(blockPos, this.getCustomColor()));
//                                    }
                                    this.discard();
                                    if (block instanceof LandingBlock) {
                                        ((LandingBlock)block).onLanding(this.getWorld(), blockPos, ((FallingBlockEntityAccessor)this).getBlock(), blockState, this);
                                    }

                                    if (this.blockEntityData != null && ((FallingBlockEntityAccessor)this).getBlock().hasBlockEntity()) {
                                        BlockEntity blockEntity = this.getWorld().getBlockEntity(blockPos);
                                        if (blockEntity != null) {
                                            NbtCompound nbtCompound = blockEntity.createNbt(this.getWorld().getRegistryManager());

                                            for (String string : this.blockEntityData.getKeys()) {
                                                nbtCompound.put(string, this.blockEntityData.get(string).copy());
                                            }

                                            try {
                                                blockEntity.read(nbtCompound, this.getWorld().getRegistryManager());
                                            } catch (Exception var15) {
                                                Unidye.LOGGER.error("Failed to load block entity from falling block", var15);
                                            }

                                            blockEntity.markDirty();
                                        }
                                    }
                                } else if (this.dropItem && this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                                    ItemStack itemStack = getDropItemstack();
                                    this.discard();
                                    this.onDestroyedOnLanding(block, blockPos);
                                    this.dropStack(itemStack);
                                }
                            } else {
                                ItemStack itemStack = getDropItemstack();
                                this.discard();
                                if (this.dropItem && this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                                    this.onDestroyedOnLanding(block, blockPos);
                                    this.dropStack(itemStack);
                                }
                            }
                        } else {
                            this.discard();
                            this.onDestroyedOnLanding(block, blockPos);
                        }
                    }
                } else if (!this.getWorld().isClient
                        && (this.timeFalling > 100 && (blockPos.getY() <= this.getWorld().getBottomY() || blockPos.getY() > this.getWorld().getTopY()) || this.timeFalling > 600)) {
                    if (this.dropItem && this.getWorld().getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                        this.dropStack(getDropItemstack());
                    }

                    this.discard();
                }
            }

            this.setVelocity(this.getVelocity().multiply(0.98));
        }
    }

    private ItemStack getDropItemstack(){
        ItemStack itemStack = new ItemStack(((FallingBlockEntityAccessor)this).getBlock().getBlock(), 1);
        NbtCompound nbt = this.blockEntityData != null ? this.blockEntityData : new NbtCompound();
        if (nbt.getInt("color") != DEFAULT_WHITE_COLOR) UnidyeUtils.setColor(itemStack, nbt.getInt("color"));
        if (nbt.contains("itemNameAffixes")) {
            ItemNameAffixesComponent.CODEC
                    .parse(this.getWorld().getRegistryManager().getOps(NbtOps.INSTANCE), nbt.get("itemNameAffixes"))
                    .resultOrPartial(itemNameAffixes -> Unidye.LOGGER.error("Failed to parse item name affixes: '{}'", itemNameAffixes))
                    .ifPresent(itemNameAffixesComponent -> itemStack.set(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES,itemNameAffixesComponent));
        }
        if (nbt.contains("recipeStacks")) {
            RecipeStacksComponent.CODEC
                    .parse(this.getWorld().getRegistryManager().getOps(NbtOps.INSTANCE), nbt.get("recipeStacks"))
                    .resultOrPartial(recipeStacks -> Unidye.LOGGER.error("Failed to parse recipe stacks: '{}'", recipeStacks))
                    .ifPresent(recipeStacksComponent -> itemStack.set(UnidyeDataComponentTypes.RECIPE_STACKS, recipeStacksComponent));
        }
        if (nbt.contains("profile")) {
            ProfileComponent.CODEC
                    .parse(this.getWorld().getRegistryManager().getOps(NbtOps.INSTANCE), nbt.get("profile"))
                    .resultOrPartial(profile -> Unidye.LOGGER.error("Failed to parse profile: '{}'", profile))
                    .ifPresent(profileComponent -> itemStack.set(DataComponentTypes.PROFILE, profileComponent));
        }
        return itemStack;
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("unidye.custom_color", getCustomColor());
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("unidye.custom_color")) {
            setCustomColor(nbt.getInt("unidye.custom_color"));
        }
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        this.setCustomColor(this.getCustomColor());
    }

    public int getCustomColor() {
        return this.getDataTracker().get(CUSTOM_COLOR);
    }

    public void setCustomColor(int color) {
        this.getDataTracker().set(CUSTOM_COLOR, color);
    }
}