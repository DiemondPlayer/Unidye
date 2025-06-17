package net.diemond_player.unidye.item;

import net.diemond_player.unidye.block.entity.IDyeableBlockEntity;
import net.diemond_player.unidye.component.ItemNameAffixesComponent;
import net.diemond_player.unidye.component.MaterialColorsComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.registry.UnidyeMaterialTypes;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import java.util.List;
import java.util.Objects;

import static net.diemond_player.unidye.component.MaterialColorsComponent.DEFAULT_WHITE_COLOR;

public class DyeableBlockItem extends BlockItem {
    public DyeableBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public ActionResult place(ItemPlacementContext context) {
        ItemStack itemStack = context.getStack().copy();
        ActionResult result = super.place(context);
        BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
        if(result.isAccepted() && blockEntity instanceof IDyeableBlockEntity iDyeableBlockEntity) {
            iDyeableBlockEntity.setColor(UnidyeUtils.getColor(itemStack));
            if(itemStack.contains(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES)) iDyeableBlockEntity.setItemNameAffixes(itemStack.get(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES));
            if(itemStack.contains(UnidyeDataComponentTypes.RECIPE_STACKS)) iDyeableBlockEntity.setRecipeStacks(itemStack.get(UnidyeDataComponentTypes.RECIPE_STACKS));
            if(itemStack.contains(UnidyeDataComponentTypes.MATERIAL_COLORS)) {
                iDyeableBlockEntity.setMaterialColors(itemStack.get(UnidyeDataComponentTypes.MATERIAL_COLORS));
            }else{
                int leatherColor = getLeatherColor(itemStack);
                if(leatherColor != DEFAULT_WHITE_COLOR) {
                    iDyeableBlockEntity.setMaterialColors(new MaterialColorsComponent(List.of(new MaterialColorsComponent.MaterialColor(UnidyeMaterialTypes.LEATHER.getId(), leatherColor))));
                }
            }
            if(itemStack.get(DataComponentTypes.PROFILE) != null) iDyeableBlockEntity.setProfile(itemStack.get(DataComponentTypes.PROFILE));
        }
        return result;
    }

    @Override
    public Text getName(ItemStack stack) {
        Text text = ItemNameAffixesComponent.getName(stack, this.getTranslationKey());
        return !Objects.equals(text, Text.empty()) ? text : super.getName(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack,context,tooltip,type);
        MaterialColorsComponent.appendTooltip(stack, tooltip::add);
    }

    //used for conversion
    public static int getLeatherColor(ItemStack stack) {
        NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(new NbtCompound()));
        if (nbtComponent.contains(UnidyeMaterialTypes.LEATHER.getId().toString())) {
            return nbtComponent.copyNbt().getInt(UnidyeMaterialTypes.LEATHER.getId().toString());
        }
        return DEFAULT_WHITE_COLOR;
    }
}
