package net.chaolux.delightlegacy.registry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class ModCreativeTabs {
    public static final CreativeTabs DELIGHT_LEGACY;

    static {
        DELIGHT_LEGACY=new CreativeTabs("delightlegacy") {
            @Override
            public Item getTabIconItem() {
                return Items.wheat;
            }
        };
    }
}
