package net.diemond_player.unidye.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.diemond_player.unidye.component.ItemNameAffixesComponent;
import net.diemond_player.unidye.registry.UnidyeDataComponentTypes;
import net.diemond_player.unidye.util.DyeData;
import net.diemond_player.unidye.util.DyeDatabaseSaverAndLoader;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;

public class UnidyeNameRemoveCommand {
    @SuppressWarnings("unused")
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("unidye")
                .then(CommandManager.literal("name")
                .then(CommandManager.literal("remove")
                .then(CommandManager.literal("prefix")
                .executes(context -> run(context, true, false, false))))));
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("unidye")
                .then(CommandManager.literal("name")
                .then(CommandManager.literal("remove")
                .then(CommandManager.literal("suffix")
                .executes(context -> run(context, false, true, false))))));
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("unidye")
                .then(CommandManager.literal("name")
                .then(CommandManager.literal("remove")
                .executes(context -> run(context, true, true, false)))));
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("unidye")
                .then(CommandManager.literal("name")
                .then(CommandManager.literal("remove")
                .then(CommandManager.literal("exclusion")
                .then(CommandManager.literal("prefix")
                .executes(context -> run(context, true, false, true)))))));
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("unidye")
                .then(CommandManager.literal("name")
                .then(CommandManager.literal("remove")
                .then(CommandManager.literal("exclusion")
                .then(CommandManager.literal("suffix")
                .executes(context -> run(context, false, true, true)))))));
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("unidye")
                .then(CommandManager.literal("name")
                .then(CommandManager.literal("remove")
                .then(CommandManager.literal("exclusion")
                .executes(context -> run(context, true, true, true))))));
    }

    public static int run(CommandContext<ServerCommandSource> context, boolean removePrefix, boolean removeSuffix, boolean isExclusion) {
        ServerPlayerEntity serverPlayerEntity = context.getSource().getPlayer();

        if (serverPlayerEntity != null) {
            ItemStack itemStack = serverPlayerEntity.getMainHandStack();
            Item item = itemStack.getItem();
            if (itemStack.contains(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES)) {
                DyeDatabaseSaverAndLoader serverState = DyeDatabaseSaverAndLoader.getServerState(context.getSource().getWorld().getServer());
                ItemNameAffixesComponent itemNameAffixesComponent = itemStack.get(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES);
                int color = itemNameAffixesComponent.sourceCustomDyeColor();
                if (serverState.database.containsKey(color)) {
                    DyeData dyeData = serverState.database.get(color);
                    if (removePrefix && removeSuffix) {
                        if(!isExclusion) {
                            itemStack.set(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, itemNameAffixesComponent.noAffixes());
                            serverState.database.remove(color);
                        }else{
                            dyeData.prefixExclusions.remove(item);
                            dyeData.suffixExclusions.remove(item);
                            ItemNameAffixesComponent.updateAffixes(itemStack, context.getSource().getWorld());
                        }
                    } else if (removePrefix) {
                        if(!isExclusion) {
                            itemStack.set(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, itemNameAffixesComponent.noPrefix());
                            dyeData.setPrefix("");
                            dyeData.setPrefixExclusions(new HashMap<>());
                            serverState.database.replace(color, dyeData);
                        }else{
                            dyeData.prefixExclusions.remove(item);
                            ItemNameAffixesComponent.updateAffixes(itemStack, context.getSource().getWorld());
                        }
                    } else if (removeSuffix) {
                        if(!isExclusion) {
                            itemStack.set(UnidyeDataComponentTypes.ITEM_NAME_AFFIXES, itemNameAffixesComponent.noSuffix());
                            dyeData.setSuffix("");
                            dyeData.setSuffixExclusions(new HashMap<>());
                            serverState.database.replace(color, dyeData);
                        }else{
                            dyeData.suffixExclusions.remove(item);
                            ItemNameAffixesComponent.updateAffixes(itemStack, context.getSource().getWorld());
                        }
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
