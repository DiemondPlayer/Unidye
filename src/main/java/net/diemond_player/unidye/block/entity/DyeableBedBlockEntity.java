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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import static net.diemond_player.unidye.item.CustomDyeItem.DEFAULT_WHITE_COLOR;

public class DyeableBedBlockEntity extends BlockEntity implements IDyeableBlockEntity {
    private int color = DEFAULT_WHITE_COLOR;
    private ItemNameAffixesComponent itemNameAffixesComponent = ItemNameAffixesComponent.DEFAULT;
    private RecipeStacksComponent recipeStacksComponent = RecipeStacksComponent.DEFAULT;

    public DyeableBedBlockEntity(BlockPos pos, BlockState state) {
        super(UnidyeBlockEntities.DYEABLE_BED_BE, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        if (color != DEFAULT_WHITE_COLOR) {
            nbt.putInt("color", color);
        }
        if (!itemNameAffixesComponent.equals(ItemNameAffixesComponent.DEFAULT)) {
            nbt.put("itemNameAffixes", ItemNameAffixesComponent.CODEC.encodeStart(registryLookup.getOps(NbtOps.INSTANCE), this.itemNameAffixesComponent).getOrThrow());
        }
        if (!recipeStacksComponent.equals(RecipeStacksComponent.DEFAULT)) {
            nbt.put("recipeStacks", RecipeStacksComponent.CODEC.encodeStart(registryLookup.getOps(NbtOps.INSTANCE), this.recipeStacksComponent).getOrThrow());
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
    }

    @Override
    protected void readComponents(ComponentsAccess components) {
        super.readComponents(components);
        this.color = components.getOrDefault(DataComponentTypes.DYED_COLOR, new DyedColorComponent(DyedColorComponent.DEFAULT_COLOR, true)).rgb();
        this.recipeStacksComponent = components.getOrDefault(UnidyeDataComponentTypes.RECIPE_STACKS, RecipeStacksComponent.DEFAULT);
        this.itemNameAffixesComponent = components.getOrDefault(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, ItemNameAffixesComponent.DEFAULT);
    }

    @Override
    protected void addComponents(ComponentMap.Builder componentMapBuilder) {
        super.addComponents(componentMapBuilder);
        componentMapBuilder.add(DataComponentTypes.DYED_COLOR, new DyedColorComponent(color, true));
        componentMapBuilder.add(UnidyeDataComponentTypes.RECIPE_STACKS, this.recipeStacksComponent);
        componentMapBuilder.add(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, this.itemNameAffixesComponent);
    }
}