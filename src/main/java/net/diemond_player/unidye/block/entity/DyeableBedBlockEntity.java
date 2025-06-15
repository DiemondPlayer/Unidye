package net.diemond_player.unidye.block.entity;

import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.component.ItemNameAffixesComponent;
import net.diemond_player.unidye.component.RecipeStacksComponent;
import net.diemond_player.unidye.registry.UnidyeBlockEntities;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import static net.diemond_player.unidye.component.MaterialColorsComponent.DEFAULT_WHITE_COLOR;

public class DyeableBedBlockEntity extends BlockEntity implements IDyeableBlockEntity {
    private int color = DEFAULT_WHITE_COLOR;
    private ItemNameAffixesComponent itemNameAffixesComponent = ItemNameAffixesComponent.DEFAULT;
    private RecipeStacksComponent recipeStacksComponent = RecipeStacksComponent.DEFAULT;
    private ProfileComponent profileComponent = null;

    public DyeableBedBlockEntity(BlockPos pos, BlockState state) {
        super(UnidyeBlockEntities.DYEABLE_BED_BE, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (color != DEFAULT_WHITE_COLOR) {
            nbt.putInt("color", color);
        }
        if (itemNameAffixesComponent != null && !itemNameAffixesComponent.equals(ItemNameAffixesComponent.DEFAULT)) {
            nbt.put("itemNameAffixes", ItemNameAffixesComponent.CODEC.encodeStart(registryLookup.getOps(NbtOps.INSTANCE), this.itemNameAffixesComponent).getOrThrow());
        }
        if (recipeStacksComponent != null && !recipeStacksComponent.equals(RecipeStacksComponent.DEFAULT)) {
            nbt.put("recipeStacks", RecipeStacksComponent.CODEC.encodeStart(registryLookup.getOps(NbtOps.INSTANCE), this.recipeStacksComponent).getOrThrow());
        }
        if (profileComponent != null) {
            nbt.put("profile", ProfileComponent.CODEC.encodeStart(registryLookup.getOps(NbtOps.INSTANCE), this.profileComponent).getOrThrow());
        }
        super.writeNbt(nbt, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if (nbt.getInt("color") == 0) {
            color = DEFAULT_WHITE_COLOR;
        } else {
            color = nbt.getInt("color");
        }
        if (nbt.contains("itemNameAffixes")) {
            ItemNameAffixesComponent.CODEC
                    .parse(registryLookup.getOps(NbtOps.INSTANCE), nbt.get("itemNameAffixes"))
                    .resultOrPartial(itemNameAffixes -> Unidye.LOGGER.error("Failed to parse item name affixes: '{}'", itemNameAffixes))
                    .ifPresent(itemNameAffixesComponent -> this.itemNameAffixesComponent = itemNameAffixesComponent);
        }
        if (nbt.contains("recipeStacks")) {
            RecipeStacksComponent.CODEC
                    .parse(registryLookup.getOps(NbtOps.INSTANCE), nbt.get("recipeStacks"))
                    .resultOrPartial(recipeStacks -> Unidye.LOGGER.error("Failed to parse recipe stacks: '{}'", recipeStacks))
                    .ifPresent(recipeStacksComponent -> this.recipeStacksComponent = recipeStacksComponent);
        }
        if (nbt.contains("profile")) {
            ProfileComponent.CODEC
                    .parse(registryLookup.getOps(NbtOps.INSTANCE), nbt.get("profile"))
                    .resultOrPartial(profile -> Unidye.LOGGER.error("Failed to parse profile: '{}'", profile))
                    .ifPresent(profileComponent -> this.profileComponent = profileComponent);
        }
    }

    @Override
    public void markDirty() {
        if (this.world != null) {
            markDirty(this.world, this.pos, this.getCachedState());
        }
    }


    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return this.createNbt(registryLookup);
    }

    public static int getColor(BlockView world, BlockPos pos) {
        if (world == null) {
            return DEFAULT_WHITE_COLOR;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof DyeableBedBlockEntity dyeableBlockEntity) {
            return dyeableBlockEntity.color;
        } else {
            return DEFAULT_WHITE_COLOR;
        }
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
    public ItemNameAffixesComponent getItemNameAffixes() {
        return this.itemNameAffixesComponent;
    }

    @Override
    public void setItemNameAffixes(ItemNameAffixesComponent itemNameAffixesComponent) {
        this.itemNameAffixesComponent = itemNameAffixesComponent;
        this.markDirty();
    }

    @Override
    public RecipeStacksComponent getRecipeStacks() {
        return this.recipeStacksComponent;
    }

    @Override
    public void setRecipeStacks(RecipeStacksComponent recipeStacksComponent) {
        this.recipeStacksComponent = recipeStacksComponent;
        this.markDirty();
    }

    @Override
    public ProfileComponent getProfile() {
        return this.profileComponent;
    }

    @Override
    public void setProfile(ProfileComponent profileComponent) {
        this.profileComponent = profileComponent;
        this.markDirty();
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        this.color = components.getOrDefault(DataComponentTypes.DYED_COLOR, new DyedColorComponent(DyedColorComponent.DEFAULT_COLOR, true)).rgb();
        this.recipeStacksComponent = components.getOrDefault(UnidyeDataComponentTypes.RECIPE_STACKS, RecipeStacksComponent.DEFAULT);
        this.itemNameAffixesComponent = components.getOrDefault(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, ItemNameAffixesComponent.DEFAULT);
        this.profileComponent = components.getOrDefault(DataComponentTypes.PROFILE, null);
    }

    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        componentMapBuilder.add(DataComponentTypes.DYED_COLOR, new DyedColorComponent(color, true));
        componentMapBuilder.add(UnidyeDataComponentTypes.RECIPE_STACKS, this.recipeStacksComponent);
        componentMapBuilder.add(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, this.itemNameAffixesComponent);
        componentMapBuilder.add(DataComponentTypes.PROFILE, this.profileComponent);
    }
}