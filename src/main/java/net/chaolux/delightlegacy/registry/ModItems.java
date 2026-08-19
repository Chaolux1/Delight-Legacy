package net.chaolux.delightlegacy.registry;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class ModItems {
    public static Item TEST;

    public static void register() {
        TEST=new Item().setUnlocalizedName("delightlegacy.test").setTextureName("minecraft:wheat").setCreativeTab(ModCreativeTabs.DELIGHT_LEGACY);
        GameRegistry.registerItem(TEST,"test");
    }
}
