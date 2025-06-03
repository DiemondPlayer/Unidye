package net.diemond_player.unidye.util;

import net.diemond_player.unidye.Unidye;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DyeDatabaseSaverAndLoader extends PersistentState {

//    public HashMap<NbtComponent, String> database = new HashMap<>();
    public HashMap<Integer, DyeData> database = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtCompound nbtCompound = new NbtCompound();
        int count = 0;
        for(Map.Entry<Integer, DyeData> entry : database.entrySet()){
            DyeData dyeData = entry.getValue();
            if(Objects.equals(dyeData.getPrefix(), "") && Objects.equals(dyeData.getSuffix(), "")) continue;
            NbtCompound entryNbt = new NbtCompound();
            entryNbt.putInt("color", entry.getKey());
            entryNbt.putString("prefix", dyeData.getPrefix());
            entryNbt.putString("suffix", dyeData.getSuffix());
            NbtCompound prefixExclusionsNbt = new NbtCompound();
            for(Map.Entry<Item, String> entry1 : dyeData.getPrefixExclusions().entrySet()){
                prefixExclusionsNbt.putString(entry1.getKey().toString(), entry1.getValue());
            }
            entryNbt.put("prefixExclusions", prefixExclusionsNbt);
            NbtCompound suffixExclusionsNbt = new NbtCompound();
            for(Map.Entry<Item, String> entry1 : dyeData.getSuffixExclusions().entrySet()){
                suffixExclusionsNbt.putString(entry1.getKey().toString(), entry1.getValue());
            }
            entryNbt.put("suffixExclusions", suffixExclusionsNbt);
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
            NbtCompound entryNbt = nbtCompound.getCompound(key);
            int color = entryNbt.getInt("color");
            String prefix = entryNbt.getString("prefix");
            String suffix = entryNbt.getString("suffix");
            HashMap<Item, String> prefixExclusions = new HashMap<>();
            NbtCompound prefixExclusionsNbt = entryNbt.getCompound("prefixExclusions");
            prefixExclusionsNbt.getKeys().forEach(key1 -> prefixExclusions.put(Registries.ITEM.get(Identifier.splitOn(key1, ':')), prefixExclusionsNbt.getString(key1)));
            HashMap<Item, String> suffixExclusions = new HashMap<>();
            NbtCompound suffixExclusionsNbt = entryNbt.getCompound("suffixExclusions");
            suffixExclusionsNbt.getKeys().forEach(key1 -> suffixExclusions.put(Registries.ITEM.get(Identifier.splitOn(key1, ':')), suffixExclusionsNbt.getString(key1)));
            DyeData dyeData = new DyeData(prefix, suffix, prefixExclusions, suffixExclusions);
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
