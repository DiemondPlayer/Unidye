package net.diemond_player.unidye.mixin.gameplay;

import com.google.common.collect.Lists;
import net.diemond_player.unidye.component.RecipeStacksComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.registry.UnidyeItems;
import net.diemond_player.unidye.util.UnidyeAccessor;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin extends AnimalEntity implements UnidyeAccessor {
    @Shadow public abstract boolean isSheared();

    @Unique
    private static final TrackedData<ItemStack> CUSTOM_DYE_ITEMSTACK = DataTracker.registerData(SheepEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    protected SheepEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            UnidyeAccessor sheep = (UnidyeAccessor) this;
            if (!sheep.unidye$getCustomDyeItemStack().isEmpty() && !this.isSheared()) {
                this.drop(serverWorld, damageSource);
                ItemStack itemStack = unidye$getCustomWoolItemStack();
                ((SheepEntity) (Object) this).dropStack(itemStack.copyWithCount(1));
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void unidye$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        if(!unidye$getCustomDyeItemStack().isEmpty()) nbt.put("unidye.custom_dye_stack", ((SheepEntity) (Object) this).getDataTracker().get(CUSTOM_DYE_ITEMSTACK).encode(this.getRegistryManager()));
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void unidye$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        ItemStack itemStack;
        if (nbt.contains("unidye.custom_dye_stack", NbtElement.COMPOUND_TYPE)) {
            NbtCompound nbtCompound = nbt.getCompound("Item");
            itemStack = (ItemStack)ItemStack.fromNbt(this.getRegistryManager(), nbtCompound).orElse(ItemStack.EMPTY);
        } else {
            itemStack = ItemStack.EMPTY;
        }
        this.unidye$setCustomDyeItemStack(itemStack);
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    private void unidye$initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(CUSTOM_DYE_ITEMSTACK, ItemStack.EMPTY);
    }

    @Inject(method = "getLootTableId", at = @At("HEAD"), cancellable = true)
    private void unidye$getLootTableId(CallbackInfoReturnable<RegistryKey<LootTable>> cir) {
        UnidyeAccessor sheep = (UnidyeAccessor) ((SheepEntity) (Object) this);
        if (!sheep.unidye$getCustomDyeItemStack().isEmpty()) {
            cir.setReturnValue(((SheepEntity) (Object) this).getType().getLootTableId());
        }
    }

    @Inject(method = "createChild(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/PassiveEntity;)Lnet/minecraft/entity/passive/PassiveEntity;", at = @At("HEAD"), cancellable = true)
    private void unidye$createChild(ServerWorld world, PassiveEntity entity, CallbackInfoReturnable<PassiveEntity> cir) {
        SheepEntity sheepEntity = (SheepEntity) EntityType.SHEEP.create(world);
        if (sheepEntity != null) {
            ItemStack itemStack = unidye$getChildCustomDyeStack(this, (SheepEntity) entity);
            if(!itemStack.isEmpty()) {
                ((UnidyeAccessor)sheepEntity).unidye$setCustomDyeItemStack(itemStack);
                cir.setReturnValue(sheepEntity);
            }
        }
    }

    @Unique
    private ItemStack unidye$getChildCustomDyeStack(AnimalEntity firstParent, AnimalEntity secondParent){
        UnidyeAccessor firstSheep = (UnidyeAccessor) ((SheepEntity) (Object) firstParent);
        UnidyeAccessor secondSheep = (UnidyeAccessor) secondParent;
        if(firstSheep.unidye$getCustomDyeItemStack().isEmpty() && secondSheep.unidye$getCustomDyeItemStack().isEmpty()) return ItemStack.EMPTY;
        List<DyeItem> dyeItems = Lists.newArrayList();
        List<ItemStack> customDyeItems = Lists.newArrayList();
        if(firstSheep.unidye$getCustomDyeItemStack().isEmpty()){
            dyeItems.add(DyeItem.byColor(((SheepEntity)firstParent).getColor()));
        }else{
            customDyeItems.add(firstSheep.unidye$getCustomDyeItemStack());
        }
        if(secondSheep.unidye$getCustomDyeItemStack().isEmpty()){
            dyeItems.add(DyeItem.byColor(((SheepEntity)secondParent).getColor()));
        }else{
            customDyeItems.add(secondSheep.unidye$getCustomDyeItemStack());
        }
        ItemStack itemStack = UnidyeUtils.blendAndSetColor(new ItemStack(UnidyeItems.CUSTOM_DYE), dyeItems, customDyeItems);
        List<ItemStack> itemStacks = customDyeItems;
        itemStacks.addAll(dyeItems.stream().map(ItemStack::new).toList());
        itemStack.set(UnidyeDataComponentTypes.RECIPE_STACKS, RecipeStacksComponent.fromItemStacks(itemStacks, itemStacks.size(), true).optimizeRecipeStacks());
        return itemStack;
    }

    @Inject(method = "sheared", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/SheepEntity;setSheared(Z)V", shift = At.Shift.AFTER), cancellable = true)
    private void unidye$sheared(SoundCategory shearedSoundCategory, CallbackInfo ci) {
        UnidyeAccessor sheep = (UnidyeAccessor) ((SheepEntity) (Object) this);
        if (!sheep.unidye$getCustomDyeItemStack().isEmpty()) {
            int i = 1 + ((SheepEntity) (Object) this).getRandom().nextInt(3);
            for (int j = 0; j < i; ++j) {
                ItemStack itemStack = unidye$getCustomWoolItemStack();
                ItemEntity itemEntity = ((SheepEntity) (Object) this).dropStack(itemStack, 1);
                if (itemEntity != null) {
                    itemEntity.setVelocity(itemEntity.getVelocity().add((double) ((((SheepEntity) (Object) this).getRandom().nextFloat() - ((SheepEntity) (Object) this).getRandom().nextFloat()) * 0.1F), (double) (((SheepEntity) (Object) this).getRandom().nextFloat() * 0.05F), (double) ((((SheepEntity) (Object) this).getRandom().nextFloat() - ((SheepEntity) (Object) this).getRandom().nextFloat()) * 0.1F)));
                }
            }
            ci.cancel();
        }
    }

    @Unique
    private ItemStack unidye$getCustomWoolItemStack() {
        CraftingRecipeInput craftingRecipeInput = CraftingRecipeInput.create(3, 3, List.of(
                new ItemStack(Blocks.WHITE_WOOL), new ItemStack(Blocks.WHITE_WOOL), new ItemStack(Blocks.WHITE_WOOL),
                new ItemStack(Blocks.WHITE_WOOL), this.unidye$getCustomDyeItemStack(), new ItemStack(Blocks.WHITE_WOOL),
                new ItemStack(Blocks.WHITE_WOOL), new ItemStack(Blocks.WHITE_WOOL), new ItemStack(Blocks.WHITE_WOOL)));
        ItemStack itemStack = this.getWorld()
                .getRecipeManager()
                .getFirstMatch(RecipeType.CRAFTING, craftingRecipeInput, this.getWorld())
                .map(recipe -> ((CraftingRecipe)recipe.value()).craft(craftingRecipeInput, this.getWorld().getRegistryManager()))
                .orElse(ItemStack.EMPTY);
        return itemStack.isEmpty() ? itemStack : itemStack.copyWithCount(1);
    }

    @Override
    public ItemStack unidye$getCustomDyeItemStack() {
        return ((SheepEntity) (Object) this).getDataTracker().get(CUSTOM_DYE_ITEMSTACK);
    }

    @Override
    public void unidye$setCustomDyeItemStack(ItemStack itemStack) {
        ((SheepEntity) (Object) this).getDataTracker().set(CUSTOM_DYE_ITEMSTACK, itemStack);
    }
}