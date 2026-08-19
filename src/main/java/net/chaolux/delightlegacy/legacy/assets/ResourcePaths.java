package net.chaolux.delightlegacy.legacy.assets;

import net.minecraft.util.ResourceLocation;

public class ResourcePaths {
    private static final String JSON=".json";
    private static final String PNG=".png";
    public static ResourceLocation blockState(ResourceLocation resourceLocations) {
        return setMap(resourceLocations,"blockstates/",JSON);
    }

    public static ResourceLocation model(ResourceLocation resourceLocations) {
        return setMap(resourceLocations,"models/",JSON);
    }

    public static ResourceLocation blockModel(ResourceLocation resourceLocations) {
        return model(ResourceLocations.prefixPath(resourceLocations,"block/"));
    }

    public static ResourceLocation itemModel(ResourceLocation resourceLocations) {
        return model(ResourceLocations.prefixPath(resourceLocations,"item/"));
    }

    public static ResourceLocation texture(ResourceLocation resourceLocation) {
        return setMap(resourceLocation,"textures/",PNG);
    }

    public static ResourceLocation blockTexture(ResourceLocation resourceLocation) {
        return texture(ResourceLocations.prefixPath(resourceLocation,"block/"));
    }

    public static ResourceLocation itemTexture(ResourceLocation resourceLocation) {
        return texture(ResourceLocations.prefixPath(resourceLocation,"item/"));
    }

    public static ResourceLocation recipe(ResourceLocation resourceLocations) {
        return setMap(resourceLocations,"recipes/",JSON);
    }

    public static ResourceLocation advancement(ResourceLocation resourceLocation) {
        return setMap(resourceLocation,"advancements/",JSON);
    }

    public static ResourceLocation lootTable(ResourceLocation resourceLocation) {
        return setMap(resourceLocation,"loot_tables/",JSON);
    }

    public static String path(LegacyPackType legacyPackType,ResourceLocation resourceLocation) {
        if(legacyPackType == null) throw new IllegalArgumentException("Legacy pack type cannot be null");
        if(resourceLocation == null) throw new IllegalArgumentException("Resource location cannot be null");
        String string=ResourceLocations.path(resourceLocation);
        isValidSafePath(string);
        return legacyPackType.getString() + "/" + ResourceLocations.namespace(resourceLocation) + "/" + string;
    }

    private static ResourceLocation setMap(ResourceLocation resourceLocation,String string,String map) {
        if(resourceLocation == null) throw new IllegalArgumentException("Resource id cannot be null");
        String path=ResourceLocations.path(resourceLocation);
        String mapPath=string + path;
        if(!mapPath.endsWith(map)) mapPath += map;
        return ResourceLocations.locations(ResourceLocations.namespace(resourceLocation),mapPath);
    }

    private static void isValidSafePath(String string) {
        if(string.startsWith("/") || string.endsWith("/") || string.contains("../") || string.contains("/..") || string.equals("..")) throw new IllegalArgumentException("Unsafe path resource path: " + string);
    }
}
