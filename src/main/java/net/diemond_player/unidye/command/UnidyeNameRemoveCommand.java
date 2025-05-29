package net.diemond_player.unidye.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.diemond_player.unidye.component.ItemNameAffixesComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.util.DyeData;
import net.diemond_player.unidye.util.DyeDatabaseSaverAndLoader;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class UnidyeNameRemoveCommand {
    @SuppressWarnings("unused")
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("unidye")
                .then(CommandManager.literal("name")
                .then(CommandManager.literal("remove")
                .then(CommandManager.literal("prefix")
                .executes(context -> run(context, true, false))))));
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("unidye")
                .then(CommandManager.literal("name")
                .then(CommandManager.literal("remove")
                .then(CommandManager.literal("suffix")
                .executes(context -> run(context, false, true))))));
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("unidye")
                .then(CommandManager.literal("name")
                .then(CommandManager.literal("remove")
                .executes(context -> run(context, true, true)))));
    }

    public static int run(CommandContext<ServerCommandSource> context, boolean removePrefix, boolean removeSuffix) {
        ServerPlayerEntity serverPlayerEntity = context.getSource().getPlayer();

        if (serverPlayerEntity != null) {
            ItemStack itemStack = serverPlayerEntity.getMainHandStack();
            if (itemStack.contains(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES)) {
                DyeDatabaseSaverAndLoader serverState = DyeDatabaseSaverAndLoader.getServerState(context.getSource().getWorld().getServer());
                ItemNameAffixesComponent itemNameAffixesComponent = itemStack.get(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES);
                int color = itemNameAffixesComponent.sourceCustomDyeColor();
                if (serverState.database.containsKey(color)) {
                    DyeData dyeData = serverState.database.get(color);
                    if (removePrefix && removeSuffix) {
                        itemStack.set(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, ItemNameAffixesComponent.noAffixes(color));
                        serverState.database.remove(color);
                    } else if (removePrefix) {
                        itemStack.set(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, ItemNameAffixesComponent.noPrefix(itemNameAffixesComponent.suffix(), color));
                        dyeData.setPrefix("");
                        serverState.database.replace(color, dyeData);
                    } else if (removeSuffix) {
                        itemStack.set(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, ItemNameAffixesComponent.noSuffix(itemNameAffixesComponent.prefix(), color));
                        dyeData.setSuffix("");
                        serverState.database.replace(color, dyeData);
                    }
                    serverState.markDirty();
                }
//            NbtCompound nbtCompound = itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
//            nbtCompound.remove("dye_shape");
//            NbtComponent nbtComponent = NbtComponent.of(nbtCompound);
            }
        }
        return 1;
    }
}
