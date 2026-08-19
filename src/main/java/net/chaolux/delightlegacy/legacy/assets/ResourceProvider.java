package net.chaolux.delightlegacy.legacy.assets;

import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public interface ResourceProvider {
    boolean is(LegacyPackType legacyPackType, ResourceLocation resourceLocation);
    InputStream inputStream(LegacyPackType legacyPackType,ResourceLocation resourceLocation) throws IOException;
    Set<ResourceLocation> set(LegacyPackType legacyPackType,String string,String suffix) throws IOException;
}
