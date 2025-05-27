package net.diemond_player.unidye.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.diemond_player.unidye.component.ItemNamePrefixComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.registry.UnidyeItems;
import net.diemond_player.unidye.util.DyeNameDatabaseSaverAndLoader;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class UnidyeNameCommand {
    @SuppressWarnings("unused")
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("unidye")
                .then(CommandManager.literal("name")
                .then(CommandManager.argument("name", StringArgumentType.greedyString())
                .executes(context -> run(context, StringArgumentType.getString(context, "name"))))));
    }

    public static int run(CommandContext<ServerCommandSource> context, String name) {
        ServerPlayerEntity serverPlayerEntity = context.getSource().getPlayer();

        if (serverPlayerEntity != null) {
            ItemStack itemStack = serverPlayerEntity.getMainHandStack();
            if(itemStack.contains(UnidyeDataComponentTypes.ITEM_NAME_PREFIX)) {
                ItemNamePrefixComponent itemNamePrefixComponent = itemStack.get(UnidyeDataComponentTypes.ITEM_NAME_PREFIX);
                int color = itemNamePrefixComponent.sourceCustomDyeColor();
                itemStack.set(UnidyeDataComponentTypes.ITEM_NAME_PREFIX, new ItemNamePrefixComponent(Text.literal(name), color));

//            NbtCompound nbtCompound = itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
//            nbtCompound.remove("dye_shape");
//            NbtComponent nbtComponent = NbtComponent.of(nbtCompound);
                DyeNameDatabaseSaverAndLoader serverState = DyeNameDatabaseSaverAndLoader.getServerState(context.getSource().getWorld().getServer());
                if (!serverState.database.containsKey(color)) {
                    serverState.database.put(color, name);
                } else {
                    serverState.database.replace(color, name);
                }
                serverState.markDirty();
            }
        }
        return 1;
    }
}
