package net.chaolux.delightlegacy.legacy.assets.lang;

import net.chaolux.delightlegacy.DelightLegacy;

public class LegacyLanguageMapper {
    public static String namespace(String string) {
        if(string == null) return null;
        String[] strings=string.split("\\.",-1);
        StringBuilder stringBuilder=new StringBuilder(string.length() + 8);
        for (int index=0;index < strings.length;index++) {
            if(index > 0) stringBuilder.append('.');
            String part=strings[index];
            if(DelightLegacy.MODERN_MOD_ID.equals(part)) {
                stringBuilder.append(DelightLegacy.MOD_ID);
            } else {
                stringBuilder.append(part);
            }
        }
        return stringBuilder.toString();
    }

    public static String legacy(String string) {
        if(string == null) return null;
        String[] strings=string.split("\\.",-1);
        if(strings.length == 3 && "item".equals(strings[0])) return string.endsWith(".name") ? string : string + ".name";
        if(strings.length == 3 && "block".equals(strings[0])) return "tile." + strings[1] + "." + strings[2] + ".name";
        if(strings.length == 3 && "entity".equals(strings[0])) return string.endsWith(".name") ? string : string + ".name";
        return string;
    }

    public static String legacyLocations(String string) {
        if(string == null || string.isEmpty()) return string;
        String[] strings=string.split("_",-1);
        if(strings.length != 2) return string;
        if(strings[0].isEmpty() || strings[1].isEmpty()) return string;
        return strings[0].toLowerCase() + "_" + strings[1].toUpperCase();
    }
}
