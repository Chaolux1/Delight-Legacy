package net.chaolux.delightlegacy.legacy.assets.textures;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class LegacyTextureAtlasSprite extends TextureAtlasSprite {
    private final ResourceLocation resourceLocation;
    public LegacyTextureAtlasSprite(String string, ResourceLocation resourceLocation) {
        super(string);
        this.resourceLocation=resourceLocation;
    }

    @Override
    public boolean hasCustomLoader(IResourceManager iResourceManager,ResourceLocation resourceLocation) {
        return true;
    }

    @Override
    public boolean load(IResourceManager iResourceManager,ResourceLocation resourceLocation) {
        InputStream inputStream=null;
        try {
            IResource iResource=iResourceManager.getResource(this.resourceLocation);
            inputStream=iResource.getInputStream();
            BufferedImage bufferedImage= ImageIO.read(inputStream);
            if(bufferedImage == null) throw new IOException("Can not decode texture " + this.resourceLocation);
            IMetadataSection iMetadataSection=iResource.hasMetadata() ? iResource.getMetadata("animation") : null;
            AnimationMetadataSection animationMetadataSection=iMetadataSection instanceof AnimationMetadataSection ? (AnimationMetadataSection) iMetadataSection : null;
            int value= Minecraft.getMinecraft().gameSettings.mipmapLevels;
            BufferedImage[] bufferedImages=new BufferedImage[value + 1];
            bufferedImages[0]=bufferedImage;
            loadSprite(bufferedImages,animationMetadataSection,false);
            return false;
        } catch (IOException exception) {
            throw new RuntimeException("Can not load model texture " + this.resourceLocation,exception);
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException exception) {

                }
            }
        }
    }
}
