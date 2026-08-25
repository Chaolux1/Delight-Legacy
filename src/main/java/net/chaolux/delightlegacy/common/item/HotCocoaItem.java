package net.chaolux.delightlegacy.common.item;

import net.chaolux.delightlegacy.legacy.common.item.LegacyItemProperties;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HotCocoaItem extends DrinkableItem {
    public HotCocoaItem(LegacyItemProperties legacyItemProperties) {
        super(legacyItemProperties,false,true);
    }

    @Override
    public void affectConsumer(ItemStack itemStack, World world, EntityLivingBase entityLivingBase) {
        Collection collection=entityLivingBase.getActivePotionEffects();
        List<PotionEffect> potionEffectList=new ArrayList<PotionEffect>();
        ItemStack stack=new ItemStack(Items.milk_bucket);
        for(Object object : collection) {
            PotionEffect potionEffect=(PotionEffect) object;
            Potion potion=potionEffect.getPotionID() >= 0 && potionEffect.getPotionID() < Potion.potionTypes.length ? Potion.potionTypes[potionEffect.getPotionID()] : null;
            if(potion != null && potion.isBadEffect() && potionEffect.isCurativeItem(stack)) potionEffectList.add(potionEffect);
        }
        if(!potionEffectList.isEmpty()) {
            PotionEffect potionEffect=potionEffectList.get(world.rand.nextInt(potionEffectList.size()));
            entityLivingBase.removePotionEffect(potionEffect.getPotionID());
        }
    }
}
