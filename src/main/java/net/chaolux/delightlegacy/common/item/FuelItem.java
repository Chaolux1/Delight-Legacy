package net.chaolux.delightlegacy.common.item;

import net.chaolux.delightlegacy.legacy.common.item.LegacyItemProperties;
import net.minecraft.item.Item;

public class FuelItem extends Item {
    private final int burnTime;
    public FuelItem(LegacyItemProperties legacyItemProperties) {
        this(legacyItemProperties,100);
    }

    public FuelItem(LegacyItemProperties legacyItemProperties,int burnTime) {
        this.burnTime=burnTime;
        if(legacyItemProperties != null) legacyItemProperties.apply(this);
    }

    public int getBurnTime() {
        return burnTime;
    }
}
