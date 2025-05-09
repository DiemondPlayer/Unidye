package net.diemond_player.unidye.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.diemond_player.unidye.item.CustomDyeItem;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.List;

public class UnidyeVanillifyCommand {
    @SuppressWarnings("unused")
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("unidye")
                .then(CommandManager.literal("vanillify")
                .requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.argument("item", ItemStackArgumentType.itemStack(commandRegistryAccess))
                .executes(context -> run(
                        context,
                        ItemStackArgumentType.getItemStackArgument(context, "item"))))));
    }

    public static int run(CommandContext<ServerCommandSource> context, ItemStackArgument item) {
        if(!(item.getItem() instanceof DyeableItem)){
            return 0;
        }
        List<DyeItem> allRegisteredDyeItems = Registries.ITEM.stream().filter(item2 -> item2 instanceof DyeItem && !(item2 instanceof CustomDyeItem)).map(item2 -> (DyeItem) item2).toList();
        for (DyeItem dyeItem : allRegisteredDyeItems) {
            ServerPlayerEntity serverPlayerEntity = context.getSource().getPlayer();

            if (serverPlayerEntity != null) {
                ItemStack itemStack = UnidyeUtils.blendAndSetColor(new ItemStack(item.getItem()), List.of(dyeItem), Lists.newArrayList());
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
        return 1;
    }
}
