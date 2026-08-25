package net.chaolux.delightlegacy.common.item;

import net.chaolux.delightlegacy.legacy.common.item.LegacyItemProperties;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PopsicleItem extends ConsumableItem {
    public PopsicleItem(LegacyItemProperties legacyItemProperties) {
        super(legacyItemProperties);
    }

    @Override
    public void affectConsumer(ItemStack itemStack, World world, EntityLivingBase entityLivingBase) {
        entityLivingBase.extinguish();
    }
}
