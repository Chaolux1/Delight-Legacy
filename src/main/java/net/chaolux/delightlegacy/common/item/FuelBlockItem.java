package net.chaolux.delightlegacy.common.item;

import net.chaolux.delightlegacy.legacy.common.item.LegacyItemProperties;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class FuelBlockItem extends ItemBlock {
    private final int burnTime;
    public FuelBlockItem(Block block) {
        this(block,new LegacyItemProperties(),100);
    }

    public FuelBlockItem(Block block,LegacyItemProperties legacyItemProperties) {
        this(block,legacyItemProperties,100);
    }

    public FuelBlockItem(Block block,LegacyItemProperties legacyItemProperties,int burnTime) {
        super(block);
        this.burnTime=burnTime;
        if(legacyItemProperties != null) legacyItemProperties.apply(this);
    }

    public int getBurnTime() {
        return burnTime;
    }
}
