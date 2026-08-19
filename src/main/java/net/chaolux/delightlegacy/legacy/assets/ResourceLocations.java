package net.chaolux.delightlegacy.legacy.assets;

import net.chaolux.delightlegacy.DelightLegacy;
import net.minecraft.util.ResourceLocation;

public class ResourceLocations {
    public static final String NAMESPACE_MINECRAFT="minecraft";
    public static final char NAMESPACE_SEPARATOR=':';
    public static ResourceLocation resourceLocations(String string) {
        return locations(DelightLegacy.MOD_ID,string);
    }

    public static ResourceLocation locations(String string) {
        return locations(NAMESPACE_MINECRAFT,string);
    }

    public static ResourceLocation locations(String namespace,String string) {
        getNamespace(namespace);
        getPath(string);
        return new ResourceLocation(namespace,string);
    }

    public static ResourceLocation parse(String string) {
        return parse(string,NAMESPACE_MINECRAFT);
    }

    public static ResourceLocation parse(String string,String namespaceMinecraft) {
        if(string == null) throw new IllegalArgumentException("Resource location cannot be null");
        getNamespace(namespaceMinecraft);
        String namespace=namespaceMinecraft;
        String path=string;
        int separator=string.indexOf(NAMESPACE_SEPARATOR);
        if(separator >= 0) {
            namespace=string.substring(0,separator);
            path=string.substring(separator + 1);
        }
        return locations(namespace,path);
    }

    public static ResourceLocation tryParse(String string) {
        return tryParse(string,NAMESPACE_MINECRAFT);
    }

    public static ResourceLocation tryParse(String string,String namespaceMinecraft) {
        try {
            return parse(string,namespaceMinecraft);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    public static ResourceLocation withPath(ResourceLocation resourceLocations,String string) {
        getLocation(resourceLocations);
        return locations(namespace(resourceLocations),string);
    }

    public static ResourceLocation prefixPath(ResourceLocation resourceLocations,String string) {
        getLocation(resourceLocations);
        if(string == null) throw new IllegalArgumentException("Path prefix cannot be null");
        return withPath(resourceLocations,string + path(resourceLocations));
    }

    public static ResourceLocation suffixPath(ResourceLocation resourceLocations,String string) {
        getLocation(resourceLocations);
        if(string == null) throw new IllegalArgumentException("Path suffix cannot be null");
        return withPath(resourceLocations,path(resourceLocations) + string);
    }

    public static String namespace(ResourceLocation resourceLocations) {
        getLocation(resourceLocations);
        return resourceLocations.getResourceDomain();
    }

    public static String path(ResourceLocation resourceLocations) {
        getLocation(resourceLocations);
        return resourceLocations.getResourcePath();
    }

    public static String getString(ResourceLocation resourceLocations) {
        return namespace(resourceLocations) + NAMESPACE_SEPARATOR + path(resourceLocations);
    }

    public static boolean isValid(String string) {
        return tryParse(string) != null;
    }

    public static boolean isValid(String namespace,String string) {
        return isValidNamespace(namespace) && isValidPath(string);
    }

    public static boolean isValidNamespace(String string) {
        if(string == null || string.isEmpty()) return false;
        for (int index=0;index < string.length();index++) {
            if(!isValidNamespaceSeparator(string.charAt(index))) return false;
        }
        return true;
    }

    public static boolean isValidPath(String string) {
        if(string == null || string.isEmpty()) return false;
        for (int index=0;index < string.length();index++) {
            if(!isValidPathSeparator(string.charAt(index))) return false;
        }
        return true;
    }

    private static boolean isValidNamespaceSeparator(char separator) {
        return separator >= 'a' && separator <= 'z' || separator >= '0' && separator <= '9' || separator == '_' || separator == '-' || separator == '.';
    }

    private static boolean isValidPathSeparator(char separator) {
        return isValidNamespaceSeparator(separator) || separator == '/';
    }

    private static void getNamespace(String string) {
        if(!isValidNamespace(string)) throw new IllegalArgumentException("Invalid resource namespace '" + string + "'.");
    }

    private static void getPath(String string) {
        if(!isValidPath(string)) throw new IllegalArgumentException("Invalid resource path '" + string + "'.");
    }

    private static void getLocation(ResourceLocation resourceLocations) {
        if(resourceLocations == null) throw new IllegalArgumentException("Resource location cannot be null");
    }
}
