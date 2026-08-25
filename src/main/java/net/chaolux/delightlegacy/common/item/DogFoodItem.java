package net.chaolux.delightlegacy.common.item;

import net.chaolux.delightlegacy.legacy.common.item.LegacyItemProperties;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class DogFoodItem extends ConsumableItem {
    public static final List<PotionEffect> EFFECTS;
    public DogFoodItem(LegacyItemProperties legacyItemProperties) {
        super(legacyItemProperties);
    }

    static {
        EFFECTS=Arrays.asList(new PotionEffect(Potion.moveSpeed.id, 6000, 0), new PotionEffect(Potion.damageBoost.id, 6000, 0), new PotionEffect(Potion.resistance.id, 6000, 0));
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer entityPlayer, EntityLivingBase entityLivingBase) {
        return feed(itemStack,entityPlayer,entityLivingBase);
    }

    public boolean feed(ItemStack itemStack,EntityPlayer entityPlayer,EntityLivingBase entityLivingBase) {
        if(!(entityLivingBase instanceof EntityWolf)) return false;
        EntityWolf entityWolf=(EntityWolf) entityLivingBase;
        if(!entityWolf.isEntityAlive() || !entityWolf.isTamed()) return false;
        World world=entityWolf.worldObj;
        if(!world.isRemote) {
            entityWolf.setHealth(entityWolf.getMaxHealth());
            for (PotionEffect potionEffect : EFFECTS) {
                entityWolf.addPotionEffect(new PotionEffect(potionEffect));
            }
            world.playSoundAtEntity(entityWolf,"random.eat",0.8f,0.8f);
            for (int index=0;index < 5;index++) {
                world.spawnParticle("heart",entityWolf.posX + (world.rand.nextDouble() - 0.5) * entityWolf.width,entityWolf.posY + 0.5 + world.rand.nextDouble() * entityWolf.height,entityWolf.posZ + (world.rand.nextDouble() - 0.5) * entityWolf.width,0.0,0.05,0.0);
            }
            if(!entityPlayer.capabilities.isCreativeMode) itemStack.stackSize--;
        }
        return true;
    }
}
