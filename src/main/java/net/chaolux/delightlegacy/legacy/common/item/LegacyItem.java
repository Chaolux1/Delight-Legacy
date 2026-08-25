package net.chaolux.delightlegacy.legacy.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class LegacyItem extends Item {
    private final LegacyFoodProperties legacyFoodProperties;
    public LegacyItem() {
        this(new LegacyItemProperties());
    }

    public LegacyItem(LegacyItemProperties legacyItemProperties) {
        if(legacyItemProperties == null) legacyItemProperties=new LegacyItemProperties();
        this.legacyFoodProperties=legacyItemProperties.getFood();
        legacyItemProperties.apply(this);
    }

    public LegacyFoodProperties getLegacyFoodProperties() {
        return legacyFoodProperties;
    }

    public boolean isFood() {
        return legacyFoodProperties != null;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        if(legacyFoodProperties == null) return 0;
        return legacyFoodProperties.isFast() ? 16 : 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        return legacyFoodProperties != null ? EnumAction.eat : EnumAction.none;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        if(legacyFoodProperties == null) return itemStack;
        if(!entityPlayer.canEat(legacyFoodProperties.isAlwaysEat())) return itemStack;
        entityPlayer.setItemInUse(itemStack,getMaxItemUseDuration(itemStack));
        return itemStack;
    }

    @Override
    public ItemStack onEaten(ItemStack itemStack,World world,EntityPlayer entityPlayer) {
        if(legacyFoodProperties == null) return itemStack;
        if(!entityPlayer.capabilities.isCreativeMode) itemStack.stackSize--;
        entityPlayer.getFoodStats().addStats(legacyFoodProperties.getNutrition(),legacyFoodProperties.getSaturationModifier());
        world.playSoundAtEntity(entityPlayer,"random.burp",0.5f,world.rand.nextFloat() * 0.1f + 0.9f);
        if(!world.isRemote) {
            for (LegacyFoodProperties.Effect effect : legacyFoodProperties.getEffectList()) {
                if(world.rand.nextFloat() > effect.getValue()) continue;
                PotionEffect potionEffect=effect.potionEffect();
                if(potionEffect != null) entityPlayer.addPotionEffect(potionEffect);
            }
        }
        return itemStack;
    }
}
