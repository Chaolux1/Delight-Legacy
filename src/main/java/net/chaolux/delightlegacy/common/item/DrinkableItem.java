package net.chaolux.delightlegacy.common.item;

import net.chaolux.delightlegacy.legacy.common.item.LegacyItemProperties;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;

public class DrinkableItem extends ConsumableItem {
    public DrinkableItem(LegacyItemProperties legacyItemProperties) {
        super(legacyItemProperties);
    }

    public DrinkableItem(LegacyItemProperties legacyItemProperties,boolean hasFoodEffectTooltip) {
        super(legacyItemProperties,hasFoodEffectTooltip);
    }

    public DrinkableItem(LegacyItemProperties legacyItemProperties,boolean hasFoodEffectTooltip, boolean hasCustomTooltip) {
        super(legacyItemProperties,hasFoodEffectTooltip,hasCustomTooltip);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        return 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        return EnumAction.drink;
    }
}
