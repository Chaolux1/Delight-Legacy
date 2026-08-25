package net.chaolux.delightlegacy.common.item;

import net.chaolux.delightlegacy.legacy.common.item.LegacyItemProperties;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MelonJuiceItem extends DrinkableItem {
    public MelonJuiceItem(LegacyItemProperties legacyItemProperties) {
        super(legacyItemProperties,false,true);
    }

    @Override
    public void affectConsumer(ItemStack itemStack, World world, EntityLivingBase entityLivingBase) {
        entityLivingBase.heal(2.0f);
    }
}
