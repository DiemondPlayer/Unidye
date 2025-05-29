package net.diemond_player.unidye.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.diemond_player.unidye.component.ItemNameAffixesComponent;
import net.diemond_player.unidye.component.RecipeStacksComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.registry.UnidyeItemGroups;
import net.diemond_player.unidye.registry.UnidyeItems;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.List;

public class UnidyeColorizeCommand {
    @SuppressWarnings("unused")
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("unidye")
                .then(CommandManager.literal("colorize").requires(source -> source.hasPermissionLevel(4)).executes(UnidyeColorizeCommand::run)));
    }

    public static int run(CommandContext<ServerCommandSource> context) {
        for (ItemConvertible item : UnidyeItemGroups.UNIDYE_ITEM_GROUP_ITEMS) {
            if (!(item == UnidyeItems.CUSTOM_DYE) && item.asItem().getDefaultStack().isIn(ItemTags.DYEABLE)) {

                ServerPlayerEntity serverPlayerEntity = context.getSource().getPlayer();

                if (serverPlayerEntity != null) {
                    ItemStack itemStack = UnidyeUtils.blendAndSetColor(new ItemStack(item), Lists.newArrayList(), List.of(serverPlayerEntity.getMainHandStack()));
                    ItemNameAffixesComponent.updateAffixes(itemStack, serverPlayerEntity.getWorld());
                    itemStack.set(UnidyeDataComponentTypes.RECIPE_STACKS, RecipeStacksComponent.fromItemStacks(List.of(new ItemStack(item), serverPlayerEntity.getMainHandStack()), 1, true));
                    boolean bl = serverPlayerEntity.getInventory().insertStack(itemStack);
                    if (bl && itemStack.isEmpty()) {
                        itemStack.setCount(1);
                        ItemEntity itemEntity = serverPlayerEntity.dropItem(itemStack, false);
                        if (itemEntity != null) {
                            itemEntity.setDespawnImmediately();
                        }

                        serverPlayerEntity.getWorld()
                                .playSound(
                                        null,
                                        serverPlayerEntity.getX(),
                                        serverPlayerEntity.getY(),
                                        serverPlayerEntity.getZ(),
                                        SoundEvents.ENTITY_ITEM_PICKUP,
                                        SoundCategory.PLAYERS,
                                        0.2F,
                                        ((serverPlayerEntity.getRandom().nextFloat() - serverPlayerEntity.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F
                                );
                        serverPlayerEntity.currentScreenHandler.sendContentUpdates();
                    } else {
                        ItemEntity itemEntity = serverPlayerEntity.dropItem(itemStack, false);
                        if (itemEntity != null) {
                            itemEntity.resetPickupDelay();
                            itemEntity.setOwner(serverPlayerEntity.getUuid());
                        }
                    }
                }
            }
        }
        return 1;
    }
}
