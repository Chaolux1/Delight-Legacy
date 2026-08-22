package net.chaolux.delightlegacy.legacy.assets.models;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.chaolux.delightlegacy.legacy.assets.blockstates.LegacyBlockRenderer;
import net.chaolux.delightlegacy.legacy.assets.blockstates.LegacyBlockStateMapper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LegacyModelRegistry {
    private static final Map<Item, ResourceLocation> ITEM_MODELS=new IdentityHashMap<Item,ResourceLocation>();
    private static final Map<Block,BlockRegistration> BLOCK_MODELS=new IdentityHashMap<Block,BlockRegistration>();
    private static final Map<Item,Map<ResourceLocation,LegacyItemValue>> ITEM_VALUE=new IdentityHashMap<Item,Map<ResourceLocation,LegacyItemValue>>();
    private static boolean registered;
    public static void register() {
        if(registered) return;
        int value= RenderingRegistry.getNextAvailableRenderId();
        LegacyModelRenderID.setModelRenderID(value);
        RenderingRegistry.registerBlockHandler(new LegacyBlockRenderer(value));
        registered=true;
    }

    public static void registerItem(Item item,ResourceLocation resourceLocation) {
        if(item == null || resourceLocation == null) throw new IllegalArgumentException("Item model cannot be null");
        ITEM_MODELS.put(item,resourceLocation);
        MinecraftForgeClient.registerItemRenderer(item,new LegacyItemRenderer(item));
    }

    public static void registerBlock(Block block, ResourceLocation resourceLocation, LegacyBlockStateMapper legacyBlockStateMapper) {
        if(block == null || resourceLocation == null || legacyBlockStateMapper == null) throw new IllegalArgumentException("Block state mapper cannot be null");
        BLOCK_MODELS.put(block,new BlockRegistration(resourceLocation,legacyBlockStateMapper));
    }

    public static void registerItemValue(Item item,ResourceLocation resourceLocation,LegacyItemValue legacyItemValue) {
        if(item == null || resourceLocation == null || legacyItemValue == null) throw new IllegalArgumentException("Item value cannot be null");
        Map<ResourceLocation,LegacyItemValue> map=ITEM_VALUE.get(item);
        if(map == null) {
            map = new LinkedHashMap<ResourceLocation, LegacyItemValue>();
            ITEM_VALUE.put(item, map);
        }
        map.put(resourceLocation,legacyItemValue);
    }

    public static ResourceLocation getItemModel(Item item) {
        return ITEM_MODELS.get(item);
    }

    public static BlockRegistration getBlock(Block block) {
        return BLOCK_MODELS.get(block);
    }

    public static Map<Item,ResourceLocation> getItems() {
        return Collections.unmodifiableMap(ITEM_MODELS);
    }

    public static Map<Block,BlockRegistration> getBlocks() {
        return Collections.unmodifiableMap(BLOCK_MODELS);
    }

    public static float getItemValue(ItemStack itemStack,ResourceLocation resourceLocation) {
        if(itemStack == null) return 0.0f;
        Map<ResourceLocation,LegacyItemValue> map=ITEM_VALUE.get(itemStack.getItem());
        if(map == null) return 0.0f;
        LegacyItemValue legacyItemValue=map.get(resourceLocation);
        return legacyItemValue == null ? 0.0f : legacyItemValue.getValue(itemStack);
    }

    public static final class BlockRegistration {
        private final ResourceLocation resourceLocation;
        private final LegacyBlockStateMapper legacyBlockStateMapper;
        private BlockRegistration(ResourceLocation resourceLocation,LegacyBlockStateMapper legacyBlockStateMapper) {
            this.resourceLocation=resourceLocation;
            this.legacyBlockStateMapper=legacyBlockStateMapper;
        }

        public ResourceLocation getBlock() {
            return resourceLocation;
        }

        public LegacyBlockStateMapper getMapper() {
            return legacyBlockStateMapper;
        }
    }
}
