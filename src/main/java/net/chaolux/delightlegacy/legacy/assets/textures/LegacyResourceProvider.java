package net.chaolux.delightlegacy.legacy.assets.textures;

import net.chaolux.delightlegacy.legacy.assets.LegacyPackType;
import net.chaolux.delightlegacy.legacy.assets.ResourceLocations;
import net.chaolux.delightlegacy.legacy.assets.ResourceProvider;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class LegacyResourceProvider implements ResourceProvider {
    private final IResourceManager iResourceManager;
    private final ResourceProvider resourceProvider;
    public LegacyResourceProvider(IResourceManager iResourceManager,ResourceProvider resourceProvider) {
        if(iResourceManager == null) throw new IllegalArgumentException("Resource manager cannot be null");
        if(resourceProvider == null) throw new IllegalArgumentException("Resource provider cannot be null");
        this.iResourceManager=iResourceManager;
        this.resourceProvider=resourceProvider;
    }

    @Override
    public boolean is(LegacyPackType legacyPackType, ResourceLocation resourceLocation) {
        if(legacyPackType != LegacyPackType.ASSETS) return resourceProvider.is(legacyPackType,resourceLocation);
        try {
            iResourceManager.getResource(resourceLocation);
            return true;
        } catch (IOException exception) {
            return false;
        }
    }

    @Override
    public InputStream inputStream(LegacyPackType legacyPackType,ResourceLocation resourceLocation) throws IOException {
        if(legacyPackType != LegacyPackType.ASSETS) return resourceProvider.inputStream(legacyPackType,resourceLocation);
        return iResourceManager.getResource(resourceLocation).getInputStream();
    }

    @Override
    public Set<ResourceLocation> set(LegacyPackType legacyPackType,String string,String suffix) throws IOException {
        return resourceProvider.set(legacyPackType,string,suffix);
    }
}
