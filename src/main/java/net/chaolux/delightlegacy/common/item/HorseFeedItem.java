package net.chaolux.delightlegacy.common.item;

import net.chaolux.delightlegacy.legacy.common.item.LegacyItemProperties;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

public class HorseFeedItem extends Item {
    public static final List<PotionEffect> EFFECTS;
    public HorseFeedItem(LegacyItemProperties legacyItemProperties) {
        if(legacyItemProperties != null) legacyItemProperties.apply(this);
    }

    static {
        EFFECTS= Arrays.asList(new PotionEffect(Potion.moveSpeed.id,6000,1),new PotionEffect(Potion.jump.id,6000,0));
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer entityPlayer, EntityLivingBase entityLivingBase) {
        return feed(itemStack,entityPlayer,entityLivingBase);
    }

    public boolean feed(ItemStack itemStack,EntityPlayer entityPlayer,EntityLivingBase entityLivingBase) {
        if(!(entityLivingBase instanceof EntityHorse)) return false;
        EntityHorse entityHorse=(EntityHorse) entityLivingBase;
        if(!entityHorse.isEntityAlive() || !entityHorse.isTame()) return false;
        World world=entityHorse.worldObj;
        if(!world.isRemote) {
            entityHorse.setHealth(entityHorse.getMaxHealth());
            for (PotionEffect potionEffect : EFFECTS) {
                entityHorse.addPotionEffect(new PotionEffect(potionEffect));
            }
            world.playSoundAtEntity(entityHorse,"mob.horse.eat",0.8f,0.8f);
            for(int index=0;index < 5;index++) {
                world.spawnParticle("heart",entityHorse.posX + (world.rand.nextDouble() - 0.5) * entityHorse.width,entityHorse.posY + 0.5 + world.rand.nextDouble() * entityHorse.height,entityHorse.posZ + (world.rand.nextDouble() - 0.5) * entityHorse.width,0.0,0.05,0.0);
            }
            if(!entityPlayer.capabilities.isCreativeMode) itemStack.stackSize--;
        }
        return true;
    }
}
