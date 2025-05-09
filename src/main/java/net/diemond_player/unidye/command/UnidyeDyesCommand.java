package net.diemond_player.unidye.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.diemond_player.unidye.item.CustomDyeItem;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class UnidyeDyesCommand {

    @SuppressWarnings("unused")
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("unidye")
                .then(CommandManager.literal("dyes").requires(source -> source.hasPermissionLevel(4)).executes(UnidyeDyesCommand::run)));
    }

    public static int run(CommandContext<ServerCommandSource> context) {
        for (Item item : Registries.ITEM.stream().filter(i -> i instanceof DyeItem && !(i instanceof CustomDyeItem)).map(i -> (DyeItem) i).toList()) {
            ItemStack itemStack = new ItemStack(item, 64);
            ServerPlayerEntity serverPlayerEntity = context.getSource().getPlayer();

            if(serverPlayerEntity != null) {
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
