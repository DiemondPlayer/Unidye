package net.diemond_player.unidye.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
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
import net.minecraft.text.Text;

public class UnidyeNameSuffixExclusionCommand {
    @SuppressWarnings("unused")
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("unidye")
                .then(CommandManager.literal("name")
                .then(CommandManager.literal("exclusion")
                .then(CommandManager.literal("suffix")
                .then(CommandManager.argument("suffix", StringArgumentType.greedyString())
                .executes(context -> run(context, StringArgumentType.getString(context, "suffix"))))))));
    }

    public static int run(CommandContext<ServerCommandSource> context, String suffix) {
        ServerPlayerEntity serverPlayerEntity = context.getSource().getPlayer();

        if (serverPlayerEntity != null) {
            ItemStack itemStack = serverPlayerEntity.getMainHandStack();
            if(itemStack.contains(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES)) {
                ItemNameAffixesComponent itemNameAffixesComponent = itemStack.get(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES);
                int color = itemNameAffixesComponent.sourceCustomDyeColor();
                itemStack.set(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, new ItemNameAffixesComponent(itemNameAffixesComponent.prefix(), Text.literal(suffix), color));

//            NbtCompound nbtCompound = itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
//            nbtCompound.remove("dye_shape");
//            NbtComponent nbtComponent = NbtComponent.of(nbtCompound);
                DyeDatabaseSaverAndLoader serverState = DyeDatabaseSaverAndLoader.getServerState(context.getSource().getWorld().getServer());
                if(serverState.database.containsKey(color)) {
                    DyeData dyeData = serverState.database.get(color);
                    dyeData.suffixExclusions.put(itemStack.getItem(), suffix);
                    serverState.database.replace(color, dyeData);
                    serverState.markDirty();
                }
            }
        }
        return 1;
    }
}
