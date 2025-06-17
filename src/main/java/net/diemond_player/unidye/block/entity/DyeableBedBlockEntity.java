package net.diemond_player.unidye.block.entity;

import net.diemond_player.unidye.component.ItemNameAffixesComponent;
import net.diemond_player.unidye.component.MaterialColorsComponent;
import net.diemond_player.unidye.component.RecipeStacksComponent;
import net.diemond_player.unidye.registry.UnidyeBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import static net.diemond_player.unidye.component.MaterialColorsComponent.DEFAULT_WHITE_COLOR;

public class DyeableBedBlockEntity extends BlockEntity implements IDyeableBlockEntity {
    private int color = DEFAULT_WHITE_COLOR;
    private ItemNameAffixesComponent itemNameAffixesComponent = ItemNameAffixesComponent.DEFAULT;
    private RecipeStacksComponent recipeStacksComponent = RecipeStacksComponent.DEFAULT;
    private ProfileComponent profileComponent = null;
    private MaterialColorsComponent materialColorsComponent = MaterialColorsComponent.DEFAULT;

    public DyeableBedBlockEntity(BlockPos pos, BlockState state) {
        super(UnidyeBlockEntities.DYEABLE_BED_BE, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        writeCommonNbt(nbt, registryLookup);
        super.writeNbt(nbt, registryLookup);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        readCommonNbt(nbt, registryLookup);
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
    public MaterialColorsComponent getMaterialColors() {
        return this.materialColorsComponent;
    }

    @Override
    public void setMaterialColors(MaterialColorsComponent materialColorsComponent) {
        this.materialColorsComponent = materialColorsComponent;
        this.markDirty();
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        readCommonComponents(components);
    }

    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        addCommonComponents(componentMapBuilder);
    }
}