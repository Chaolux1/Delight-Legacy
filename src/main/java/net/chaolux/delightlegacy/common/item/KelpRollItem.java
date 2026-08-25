package net.chaolux.delightlegacy.common.item;

import net.chaolux.delightlegacy.legacy.common.item.LegacyItemProperties;
import net.minecraft.item.ItemStack;

public class KelpRollItem extends ConsumableItem {
    public KelpRollItem(LegacyItemProperties legacyItemProperties) {
        super(legacyItemProperties);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        return 64;
    }
}
