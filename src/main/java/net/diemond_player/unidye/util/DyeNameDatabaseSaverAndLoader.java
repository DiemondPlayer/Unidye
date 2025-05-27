package net.diemond_player.unidye.util;

import net.diemond_player.unidye.Unidye;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class DyeNameDatabaseSaverAndLoader extends PersistentState {

//    public HashMap<NbtComponent, String> database = new HashMap<>();
    public HashMap<Integer, String> database = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        nbt.putString("unidye.dye_name_database", database.toString());
        return nbt;
    }

    public static DyeNameDatabaseSaverAndLoader createFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        DyeNameDatabaseSaverAndLoader state = new DyeNameDatabaseSaverAndLoader();
        String string = tag.getString("unidye.dye_name_database");
        HashMap<Integer, String> database2 = new HashMap<>();
        string = string.substring(1, string.length()-1);
        String[] keyValuePairs = string.split(",");
        for(String pair : keyValuePairs) {
            String[] entry = pair.split("=");
            database2.put(Integer.valueOf(entry[0].trim()), entry[1].trim());
        }
        state.database = database2;
        return state;
    }

//    public static DyeNameDatabaseSaverAndLoader createFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
//        DyeNameDatabaseSaverAndLoader state = new DyeNameDatabaseSaverAndLoader();
//        String string = tag.getString("unidye.dye_name_database");
//        HashMap<NbtComponent, String> database2 = new HashMap<>();
//        string = string.substring(1, string.length()-1);
//        ArrayList<String> keyValuePairs = getStrings(string);
//        for(String pair : keyValuePairs) {
//            String[] entry = pair.split("=");
//            NbtCompound nbtCompound = new NbtCompound();
//            String s = entry[0].trim().substring(1, entry[0].length()-1);
//            String[] nbtString = s.split(",");
//            for (String pair2 : nbtString){
//                String[] stringAndIntRaw = pair2.split(":");
//                String[] stringAndInt = new String[2];
//                stringAndInt[0]=stringAndIntRaw[0]+":"+stringAndIntRaw[1];
//                stringAndInt[1]=stringAndIntRaw[2];
//                try {
//                    int i = Integer.parseInt(stringAndInt[1]);
//                    nbtCompound.putInt(stringAndInt[0].substring(1, stringAndInt[0].length()-1), i);
//                } catch (NumberFormatException ignored) {
//
//                }
//            }
//            database2.put(NbtComponent.of(nbtCompound), entry[1].trim());
//        }
//
//        state.database = database2;
//        return state;
//    }

//    private static @NotNull ArrayList<String> getStrings(String string) {
//        ArrayList<String> keyValuePairs = new ArrayList<>();
//        boolean recordParentheses = false;
//        boolean recordEqualCommas = false;
//        int index = 0;
//        StringBuilder stringBuilder = new StringBuilder();
//        for(Character c : string.toCharArray()){
//            index++;
//            if(c == '{') {
//                recordParentheses = true;
//            }
//            if(c == '}' && recordParentheses) {
//                recordParentheses = false;
//                stringBuilder.append(c);
//            }
//            if(c == '=') {
//                recordEqualCommas = true;
//            }
//            if((c == ',' && recordEqualCommas) || index==string.length()) {
//                recordEqualCommas = false;
//                recordParentheses = false;
//                if(index==string.length()) stringBuilder.append(c);
//                keyValuePairs.add(stringBuilder.toString());
//                stringBuilder = new StringBuilder();
//            }
//            if(recordParentheses || recordEqualCommas) stringBuilder.append(c);
//        }
//        return keyValuePairs;
//    }

    private static Type<DyeNameDatabaseSaverAndLoader> type = new Type<>(
            DyeNameDatabaseSaverAndLoader::new,
            DyeNameDatabaseSaverAndLoader::createFromNbt,
            null
    );


    public static DyeNameDatabaseSaverAndLoader getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
        DyeNameDatabaseSaverAndLoader state = persistentStateManager.getOrCreate(type, Unidye.MOD_ID);
        state.markDirty();
        return state;
    }
}
