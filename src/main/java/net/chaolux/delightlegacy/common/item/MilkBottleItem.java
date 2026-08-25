package net.chaolux.delightlegacy.common.item;

import net.chaolux.delightlegacy.legacy.common.item.LegacyItemProperties;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MilkBottleItem extends DrinkableItem {
    public MilkBottleItem(LegacyItemProperties legacyItemProperties) {
        super(legacyItemProperties,false,true);
    }

    @Override
    public void affectConsumer(ItemStack itemStack, World world, EntityLivingBase entityLivingBase) {
        Collection collection=entityLivingBase.getActivePotionEffects();
        List<PotionEffect>  potionEffectList=new ArrayList<PotionEffect>();
        ItemStack stack=new ItemStack(Items.milk_bucket);
        for(Object object : collection) {
            PotionEffect potionEffect=(PotionEffect) object;
            if(potionEffect.isCurativeItem(stack)) potionEffectList.add(potionEffect);
        }
        if(!potionEffectList.isEmpty()) {
            PotionEffect potionEffect=potionEffectList.get(world.rand.nextInt(potionEffectList.size()));
            entityLivingBase.removePotionEffect(potionEffect.getPotionID());
        }
    }
}
