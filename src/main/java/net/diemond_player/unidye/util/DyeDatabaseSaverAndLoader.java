package net.diemond_player.unidye.util;

import net.diemond_player.unidye.Unidye;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class DyeDatabaseSaverAndLoader extends PersistentState {

//    public HashMap<NbtComponent, String> database = new HashMap<>();
    public HashMap<Integer, DyeData> database = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtCompound nbtCompound = new NbtCompound();
        int count = 0;
        for(Map.Entry<Integer, DyeData> entry : database.entrySet()){
            NbtCompound entryNbt = new NbtCompound();
            DyeData dyeData = entry.getValue();
            entryNbt.putInt("color", entry.getKey());
            entryNbt.putString("prefix", dyeData.getPrefix());
            entryNbt.putString("suffix", dyeData.getSuffix());
            //add exclusions
            nbtCompound.put(String.valueOf(count), entryNbt);
            count++;
        }
        nbt.put("unidye.dye_name_database", nbtCompound);
        return nbt;
    }

    public static DyeDatabaseSaverAndLoader createFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        DyeDatabaseSaverAndLoader state = new DyeDatabaseSaverAndLoader();
        NbtCompound nbtCompound = tag.getCompound("unidye.dye_name_database");
        nbtCompound.getKeys().forEach(key ->{
            int color = nbtCompound.getCompound(key).getInt("color");
            String prefix = nbtCompound.getCompound(key).getString("prefix");
            String suffix = nbtCompound.getCompound(key).getString("suffix");
            DyeData dyeData = new DyeData(prefix, suffix);
            //add exclusions
            state.database.put(color, dyeData);
        });
        return state;
    }

//    public static DyeData getDyeData(ServerWorld serverWorld, int customDyeColor) {
//        DyeDatabaseSaverAndLoader serverState = getServerState(serverWorld.getServer());
//
//        DyeData dyeData = serverState.database.computeIfAbsent(customDyeColor, color -> new DyeData());
//
//        return dyeData;
//    }

//    public static DyeDatabaseSaverAndLoader createFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
//        DyeDatabaseSaverAndLoader state = new DyeDatabaseSaverAndLoader();
//        String string = tag.getString("unidye.dye_name_database");
//        HashMap<Integer, String> database2 = new HashMap<>();
//        string = string.substring(1, string.length()-1);
//        String[] keyValuePairs = string.split(",");
//        for(String pair : keyValuePairs) {
//            String[] entry = pair.split("=");
//            database2.put(Integer.valueOf(entry[0].trim()), entry[1].trim());
//        }
//        state.database = database2;
//        return state;
//    }

//    public static DyeDatabaseSaverAndLoader createFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
//        DyeDatabaseSaverAndLoader state = new DyeDatabaseSaverAndLoader();
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

    private static Type<DyeDatabaseSaverAndLoader> type = new Type<>(
            DyeDatabaseSaverAndLoader::new,
            DyeDatabaseSaverAndLoader::createFromNbt,
            null
    );


    public static DyeDatabaseSaverAndLoader getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();
        DyeDatabaseSaverAndLoader state = persistentStateManager.getOrCreate(type, Unidye.MOD_ID);
        state.markDirty();
        return state;
    }
}
