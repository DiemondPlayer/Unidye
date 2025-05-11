package net.diemond_player.unidye.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.diemond_player.unidye.Unidye;
import net.diemond_player.unidye.item.CustomDyeItem;
import net.diemond_player.unidye.registry.UnidyeItems;
import net.diemond_player.unidye.util.DyeNameDatabaseSaverAndLoader;
import net.diemond_player.unidye.util.UnidyeUtils;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.List;

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
            if(itemStack.isOf(UnidyeItems.CUSTOM_DYE)) {
                itemStack.set(DataComponentTypes.ITEM_NAME, Text.literal(name));
            }
            NbtCompound nbtCompound = itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
            nbtCompound.remove("dye_shape");
            NbtComponent nbtComponent = NbtComponent.of(nbtCompound);
            DyeNameDatabaseSaverAndLoader serverState = DyeNameDatabaseSaverAndLoader.getServerState(context.getSource().getWorld().getServer());
            if (!serverState.database.containsKey(nbtComponent)) {
                serverState.database.put(nbtComponent, name);
            }else{
                serverState.database.replace(nbtComponent, name);
            }
            serverState.markDirty();
        }
        return 1;
    }
}
