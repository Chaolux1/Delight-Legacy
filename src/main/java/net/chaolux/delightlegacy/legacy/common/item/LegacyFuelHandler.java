package net.chaolux.delightlegacy.legacy.common.item;

import cpw.mods.fml.common.IFuelHandler;
import net.chaolux.delightlegacy.common.item.FuelBlockItem;
import net.chaolux.delightlegacy.common.item.FuelItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class LegacyFuelHandler implements IFuelHandler {
    @Override
    public int getBurnTime(ItemStack itemStack) {
        if(itemStack == null) return 0;
        Item item=itemStack.getItem();
        if(item instanceof FuelItem) return ((FuelItem) item).getBurnTime();
        if(item instanceof FuelBlockItem) return ((FuelBlockItem) item).getBurnTime();
        return 0;
    }
}
