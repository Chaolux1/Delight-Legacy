package net.chaolux.delightlegacy.legacy.assets.lang;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.chaolux.delightlegacy.DelightLegacy;
import net.chaolux.delightlegacy.legacy.assets.LegacyPackType;
import net.chaolux.delightlegacy.legacy.assets.LegacyResources;
import net.chaolux.delightlegacy.legacy.assets.ResourceLocations;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringTranslate;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LegacyLanguageLoader {
    private static final String LANG="lang";
    private static final String JSON=".json";
    private static final String LANG_SUFFIX=".lang";
    public static synchronized void reload() {
        if (!LegacyResources.isInitialize())
            throw new IllegalStateException("Legacy resource have not been initialize");
        LinkedHashMap<String, HashMap<String, String>> linkedHashMap = new LinkedHashMap<String, HashMap<String, String>>();
        int json = loadJson(linkedHashMap);
        int legacy = loadLegacy(linkedHashMap);
        for (Map.Entry<String, HashMap<String, String>> entry : linkedHashMap.entrySet()) {
            LanguageRegistry.instance().injectLanguage(entry.getKey(), entry.getValue());
        }
        DelightLegacy.LOGGER.info("Load modern language: {} JSON, {} legacy, {} locate", json, legacy, linkedHashMap.size());
    }

    private static int loadJson(Map<String,HashMap<String,String>> mapMap) {
        int load=0;
        try {
            Set<ResourceLocation> resourceLocationsSet=LegacyResources.setAssets(LANG,JSON);
            for(ResourceLocation resourceLocation : resourceLocationsSet) {
                if(!isNamespace(resourceLocation)) continue;
                String string=locate(resourceLocation,JSON,true);
                if(string == null) continue;
                try(Reader reader=LegacyResources.reader(LegacyPackType.ASSETS,resourceLocation)) {
                    JsonElement jsonElement=new JsonParser().parse(reader);
                    if(jsonElement == null || !jsonElement.isJsonObject()) {
                        DelightLegacy.LOGGER.warn("Language JSON {} is not object",ResourceLocations.getString(resourceLocation));
                        continue;
                    }
                    HashMap<String,String> stringStringHashMap=getLanguage(mapMap,string);
                    JsonObject jsonObject=jsonElement.getAsJsonObject();
                    for(Map.Entry<String,JsonElement> entry : jsonObject.entrySet()) {
                        JsonElement element=entry.getValue();
                        if(element == null || !element.isJsonPrimitive()) {
                            DelightLegacy.LOGGER.warn("No string language value {} in {}",entry.getKey(),ResourceLocations.getString(resourceLocation));
                            continue;
                        }
                        String value=element.getAsString();
                        addModernAtlas(stringStringHashMap,entry.getKey(),value);
                        load++;
                    }
                } catch (Exception exception) {
                    DelightLegacy.LOGGER.error("Cannot load modern language " + ResourceLocations.getString(resourceLocation),exception);
                }
            }
        } catch (Exception exception) {
            DelightLegacy.LOGGER.error("Cannot scan modern language JSON",exception);
        }
        return load;
    }

    private static int loadLegacy(Map<String,HashMap<String,String>> mapMap) {
        int load=0;
        try {
            Set<ResourceLocation> resourceLocationSet=LegacyResources.setAssets(LANG,LANG_SUFFIX);
            for(ResourceLocation resourceLocation : resourceLocationSet) {
                if(!isNamespace(resourceLocation)) continue;
                String string=locate(resourceLocation,LANG_SUFFIX,false);
                if(string == null) continue;
                try(InputStream inputStream=LegacyResources.inputStream(LegacyPackType.ASSETS,resourceLocation)) {
                    HashMap<String,String> stringStringHashMap= StringTranslate.parseLangFile(inputStream);
                    HashMap<String,String> stringHashMap=getLanguage(mapMap,string);
                    for (Map.Entry<String,String> entry : stringStringHashMap.entrySet()) {
                        addLegacyAtlas(stringHashMap,entry.getKey(),entry.getValue());
                        load++;
                    }
                } catch (Exception exception) {
                    DelightLegacy.LOGGER.error("Cannot load legacy language " + ResourceLocations.getString(resourceLocation),exception);
                }
            }
        } catch (IOException exception) {
            DelightLegacy.LOGGER.error("Cannot scan legacy .lang",exception);
        }
        return load;
    }

    private static void addModernAtlas(HashMap<String,String> stringStringHashMap,String string,String value) {
        if(string == null || string.isEmpty()) return;
        String mapper=LegacyLanguageMapper.namespace(string);
        String legacy=LegacyLanguageMapper.legacy(string);
        String mapperLegacy=LegacyLanguageMapper.legacy(mapper);
        stringStringHashMap.put(string,value);
        stringStringHashMap.put(mapper,value);
        stringStringHashMap.put(legacy,value);
        stringStringHashMap.put(mapperLegacy,value);
    }

    private static void addLegacyAtlas(HashMap<String,String> stringStringHashMap,String string,String value) {
        if(string == null || string.isEmpty()) return;
        String mapper=LegacyLanguageMapper.namespace(string);
        stringStringHashMap.put(string,value);
        stringStringHashMap.put(mapper,value);
    }

    private static HashMap<String,String> getLanguage(Map<String,HashMap<String,String>> mapMap,String string) {
        HashMap<String,String> stringStringHashMap=mapMap.get(string);
        if(stringStringHashMap == null) {
            stringStringHashMap=new HashMap<String,String>();
            mapMap.put(string,stringStringHashMap);
        }
        return stringStringHashMap;
    }

    private static boolean isNamespace(ResourceLocation resourceLocation) {
        String string=ResourceLocations.namespace(resourceLocation);
        return DelightLegacy.MOD_ID.equals(string) || DelightLegacy.MODERN_MOD_ID.equals(string);
    }

    private static String locate(ResourceLocation resourceLocation,String string,boolean modern) {
        String path=ResourceLocations.path(resourceLocation);
        if(path == null || !path.startsWith(LANG + "/") || !path.endsWith(string)) return null;
        String suffix=path.substring(path.lastIndexOf('/') + 1,path.length() - string.length());
        if(suffix.isEmpty()) return null;
        return modern ? LegacyLanguageMapper.legacyLocations(suffix) : suffix;
    }

}
