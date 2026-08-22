/*
Copyright (c) Forge Development LLC and contributors
SPDX-License-Identifier: LGPL-2.1-only
Modified by Chaolux, 2026
 */
package net.chaolux.delightlegacy.legacy.assets.textures;

import net.chaolux.delightlegacy.legacy.assets.LegacyPackType;
import net.chaolux.delightlegacy.legacy.assets.LegacyResources;
import net.chaolux.delightlegacy.legacy.assets.ResourceLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LegacyTextureAtlas {
    private final Map<ResourceLocation, TextureAtlasSprite> spriteMap=new LinkedHashMap<ResourceLocation,TextureAtlasSprite>();
    public void build(TextureMap textureMap, Set<ResourceLocation> resourceLocationSet) {
        spriteMap.clear();
        for(ResourceLocation resourceLocation : resourceLocationSet) {
            ResourceLocation location=find(resourceLocation);
            if(location == null) continue;
            String string="delightlegacy:modern/" + ResourceLocations.namespace(resourceLocation) + "/" + ResourceLocations.path(resourceLocation);
            LegacyTextureAtlasSprite legacyTextureAtlasSprite=new LegacyTextureAtlasSprite(string,location);
            if(!textureMap.setTextureEntry(string,legacyTextureAtlasSprite)){
                TextureAtlasSprite textureAtlasSprite=textureMap.getTextureExtry(string);
                if(textureAtlasSprite != null) {
                    spriteMap.put(resourceLocation, textureAtlasSprite);
                    continue;
                }
            }
            spriteMap.put(resourceLocation,legacyTextureAtlasSprite);
        }
    }

    public IIcon getIcon(ResourceLocation resourceLocation) {
        TextureAtlasSprite textureAtlasSprite=spriteMap.get(resourceLocation);
        if(textureAtlasSprite != null) return textureAtlasSprite;
        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("missingno");
    }

    private ResourceLocation find(ResourceLocation resourceLocation) {
        String namespace=ResourceLocations.namespace(resourceLocation);
        String string=ResourceLocations.path(resourceLocation);
        ResourceLocation location=ResourceLocations.locations(namespace,"textures/" + string + ".png");
        if(LegacyResources.is(LegacyPackType.ASSETS,location)) return location;
        if(string.startsWith("block/")) {
            ResourceLocation legacy=ResourceLocations.locations(namespace,"textures/blocks/" + string.substring("block/".length()) + ".png");
            if(LegacyResources.is(LegacyPackType.ASSETS,legacy)) return legacy;
        }
        if(string.startsWith("item/")) {
            ResourceLocation legacy=ResourceLocations.locations(namespace,"textures/items/" + string.substring("item/".length()) + ".png");
            if(LegacyResources.is(LegacyPackType.ASSETS,legacy)) return legacy;
        }
        return null;
    }
}
