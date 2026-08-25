package net.chaolux.delightlegacy.legacy.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class LegacyFoodItem extends ItemFood {
    private final LegacyFoodProperties foodProperties;
    public LegacyFoodItem(LegacyFoodProperties foodProperties) {
        super(foodProperties.getNutrition(),foodProperties.getSaturationModifier(),foodProperties.isMeat());
        this.foodProperties=foodProperties;
        if(foodProperties.isAlwaysEat()) {
            setAlwaysEdible();
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        return foodProperties.isFast() ? 16 : 32;
    }

    @Override
    protected void onFoodEaten(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        super.onFoodEaten(itemStack,world,entityPlayer);
        if(world.isRemote) return;
        for (LegacyFoodProperties.Effect effect : foodProperties.getEffectList()) {
            if(world.rand.nextFloat() > effect.getValue()) continue;
            PotionEffect potionEffect=effect.potionEffect();
            if(potionEffect != null) entityPlayer.addPotionEffect(potionEffect);
        }
    }
}
