package net.diemond_player.unidye.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;

public class UnidyeRandomCommand {
    @SuppressWarnings("unused")
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("unidye")
                .then(CommandManager.literal("random")
                .requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.argument("item", ItemStackArgumentType.itemStack(commandRegistryAccess))
                .then(CommandManager.argument("min_amount", IntegerArgumentType.integer(1))
                .then(CommandManager.argument("max_amount", IntegerArgumentType.integer(1))
                .then(CommandManager.argument("amount_of_entries", IntegerArgumentType.integer(1))
                .executes(context -> run(
                        context,
                        ItemStackArgumentType.getItemStackArgument(context, "item"),
                        IntegerArgumentType.getInteger(context, "min_amount"),
                        IntegerArgumentType.getInteger(context, "max_amount"),
                        IntegerArgumentType.getInteger(context, "amount_of_entries")))))))));
    }

    public static int run(CommandContext<ServerCommandSource> context, ItemStackArgument item, int min_amount, int max_amount, int amount_of_entries) {
        if(!(item.getItem() instanceof DyeableItem)){
            return 0;
        }
        for (int i = 0; i<amount_of_entries; i++) {
            ItemStack itemStack = new ItemStack(item.getItem());
            ServerPlayerEntity serverPlayerEntity = context.getSource().getPlayer();
            List<DyeItem> dyeItems = Lists.newArrayList();
            Random random = new Random();
            if(max_amount != min_amount) {
                for (int j = 0; j < random.nextInt(min_amount, max_amount); j++) {
                    dyeItems.add((DyeItem) UnidyeUtils.DYES.keySet().stream().toList().get(random.nextInt(0, UnidyeUtils.DYES.size()-1)));
                }
            }else{
                for (int j = 0; j < min_amount; j++) {
                    dyeItems.add((DyeItem) UnidyeUtils.DYES.keySet().stream().toList().get(random.nextInt(0, UnidyeUtils.DYES.size() - 1)));
                }
            }
            itemStack = UnidyeUtils.blendAndSetColor(itemStack, dyeItems, Lists.newArrayList());

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
