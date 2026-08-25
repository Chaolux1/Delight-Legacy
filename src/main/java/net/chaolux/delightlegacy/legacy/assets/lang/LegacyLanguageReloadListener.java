package net.chaolux.delightlegacy.legacy.assets.lang;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public class LegacyLanguageReloadListener implements IResourceManagerReloadListener {
    @Override
    public void onResourceManagerReload(IResourceManager iResourceManager) {
        LegacyLanguageLoader.reload();
        Minecraft minecraft=Minecraft.getMinecraft();
        if(minecraft.getLanguageManager() != null) minecraft.getLanguageManager().onResourceManagerReload(iResourceManager);
    }
}
