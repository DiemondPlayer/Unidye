package net.diemond_player.unidye.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.diemond_player.unidye.component.ItemNameAffixesComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.util.DyeDatabaseSaverAndLoader;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;

public class UnidyeNameRemoveDatabaseCommand {
    @SuppressWarnings("unused")
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("unidye")
                .then(CommandManager.literal("name")
                .then(CommandManager.literal("remove")
                .then(CommandManager.literal("database")
                .executes(UnidyeNameRemoveDatabaseCommand::run)))));
    }

    public static int run(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity serverPlayerEntity = context.getSource().getPlayer();

        if (serverPlayerEntity != null) {
            ItemStack itemStack = serverPlayerEntity.getMainHandStack();
            if(itemStack.contains(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES)) {
                ItemNameAffixesComponent itemNameAffixesComponent = itemStack.get(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES);
                int color = itemNameAffixesComponent.sourceCustomDyeColor();
                itemStack.set(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, itemNameAffixesComponent.noAffixes());
            }
//            NbtCompound nbtCompound = itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
//            nbtCompound.remove("dye_shape");
//            NbtComponent nbtComponent = NbtComponent.of(nbtCompound);
            DyeDatabaseSaverAndLoader serverState = DyeDatabaseSaverAndLoader.getServerState(context.getSource().getWorld().getServer());
            serverState.database = new HashMap<>();
            serverState.markDirty();
        }
        return 1;
    }
}
