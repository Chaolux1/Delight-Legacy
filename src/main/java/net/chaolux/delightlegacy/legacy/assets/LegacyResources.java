package net.chaolux.delightlegacy.legacy.assets;

import net.minecraft.util.ResourceLocation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class LegacyResources {
    private static volatile ResourceProvider assets;
    private static volatile ResourceProvider data;
    public static synchronized void initialize(Class<?> classPath) {
        if(classPath == null) throw new IllegalArgumentException("Class path cannot be null");
        ClassPathResourceProvider classPathResourceProvider=new ClassPathResourceProvider(classPath);
        if(assets == null) assets =classPathResourceProvider;
        if(data == null) data =classPathResourceProvider;
    }

    public static boolean isInitialize() {
        return assets != null && data != null;
    }

    public static ResourceProvider getProvider(LegacyPackType legacyPackType) {
        getLocation(legacyPackType);
        ResourceProvider resourceProvider=legacyPackType == LegacyPackType.ASSETS ? assets : data;
        if(resourceProvider == null) throw new IllegalStateException("Legacy Resource has not been initialize yet");
        return resourceProvider;
    }

    public static synchronized void setProvider(LegacyPackType legacyPackType,ResourceProvider resourceProvider) {
        getLocation(legacyPackType);
        if(resourceProvider == null) throw new IllegalArgumentException("Resource provider cannot be null");
        if(legacyPackType == LegacyPackType.ASSETS) {
            assets=resourceProvider;
        } else {
            data=resourceProvider;
        }
    }

    public static boolean is(LegacyPackType legacyPackType, ResourceLocation resourceLocation) {
        return getProvider(legacyPackType).is(legacyPackType,resourceLocation);
    }

    public static InputStream inputStream(LegacyPackType legacyPackType,ResourceLocation resourceLocation) throws IOException {
        return getProvider(legacyPackType).inputStream(legacyPackType,resourceLocation);
    }

    public static InputStream inputAssets(ResourceLocation resourceLocation) throws IOException {
        return inputStream(LegacyPackType.ASSETS,resourceLocation);
    }

    public static InputStream inputData(ResourceLocation resourceLocation) throws IOException {
        return inputStream(LegacyPackType.DATA,resourceLocation);
    }

    public static Set<ResourceLocation> set(LegacyPackType legacyPackType,String string,String suffix) throws IOException {
        return getProvider(legacyPackType).set(legacyPackType,string,suffix);
    }

    public static Set<ResourceLocation> setAssets(String string,String suffix) throws IOException {
        return set(LegacyPackType.ASSETS,string,suffix);
    }

    public static Set<ResourceLocation> setData(String string,String suffix) throws IOException {
        return set(LegacyPackType.DATA,string,suffix);
    }

    public static Reader reader(LegacyPackType legacyPackType,ResourceLocation resourceLocation) throws IOException {
        return new BufferedReader(new InputStreamReader(inputStream(legacyPackType,resourceLocation), StandardCharsets.UTF_8));
    }

    public static String read(LegacyPackType legacyPackType,ResourceLocation resourceLocation) throws IOException {
        StringBuilder stringBuilder=new StringBuilder();
        try(Reader reader = reader(legacyPackType,resourceLocation)) {
            char[] chars=new char[4096];
            int read;
            while ((read=reader.read(chars)) != -1) {
                stringBuilder.append(chars,0,read);
            }
        }
        return stringBuilder.toString();
    }

    private static void getLocation(LegacyPackType legacyPackType) {
        if(legacyPackType == null) throw new IllegalArgumentException("Pack type cannot be null");
    }
}
