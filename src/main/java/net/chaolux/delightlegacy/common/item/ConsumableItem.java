package net.chaolux.delightlegacy.common.item;

import net.chaolux.delightlegacy.legacy.common.item.LegacyFoodProperties;
import net.chaolux.delightlegacy.legacy.common.item.LegacyItemProperties;
import net.chaolux.delightlegacy.legacy.registry.LegacyItemRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ConsumableItem extends Item {
    private final LegacyFoodProperties legacyFoodProperties;
    private final boolean hasFoodEffectTooltip;
    private final boolean hasCustomTooltip;
    public ConsumableItem(LegacyItemProperties legacyItemProperties) {
        this(legacyItemProperties,true,false);
    }

    public ConsumableItem(LegacyItemProperties legacyItemProperties,boolean hasFoodEffectTooltip) {
        this(legacyItemProperties,hasFoodEffectTooltip,false);
    }

    public ConsumableItem(LegacyItemProperties legacyItemProperties,boolean hasFoodEffectTooltip,boolean hasCustomTooltip) {
        if(legacyItemProperties == null) legacyItemProperties=new LegacyItemProperties();
        this.legacyFoodProperties=legacyItemProperties.getFood();
        this.hasFoodEffectTooltip=hasFoodEffectTooltip;
        this.hasCustomTooltip=hasCustomTooltip;
        legacyItemProperties.apply(this);
    }

    public LegacyFoodProperties getLegacyFoodProperties() {
        return legacyFoodProperties;
    }

    public boolean isHasFoodEffectTooltip() {
        return hasFoodEffectTooltip;
    }

    public boolean isHasCustomTooltip() {
        return hasCustomTooltip;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        return legacyFoodProperties != null && legacyFoodProperties.isFast() ? 16 : 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        return EnumAction.eat;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        if(legacyFoodProperties != null && !entityPlayer.canEat(legacyFoodProperties.isAlwaysEat())) return itemStack;
        entityPlayer.setItemInUse(itemStack,getMaxItemUseDuration(itemStack));
        return itemStack;
    }

    @Override
    public ItemStack onEaten(ItemStack itemStack,World world,EntityPlayer entityPlayer) {
        if(!world.isRemote) {
            if(legacyFoodProperties != null) {
                entityPlayer.getFoodStats().addStats(legacyFoodProperties.getNutrition(),legacyFoodProperties.getSaturationModifier());
                for(LegacyFoodProperties.Effect effect : legacyFoodProperties.getEffectList()) {
                    if(world.rand.nextFloat() <= effect.getValue()) {
                        PotionEffect potionEffect=effect.potionEffect();
                        if(potionEffect != null) entityPlayer.addPotionEffect(potionEffect);
                    }
                }
            }
            affectConsumer(itemStack,world,entityPlayer);
        }
        Item item=hasContainerItem() ? getContainerItem() : null;
        if(!entityPlayer.capabilities.isCreativeMode) itemStack.stackSize--;
        if(itemStack.stackSize <= 0) return item == null ? itemStack : new ItemStack(item);
        if(!world.isRemote && !entityPlayer.capabilities.isCreativeMode && item != null) {
            ItemStack stack=new ItemStack(item);
            if(!entityPlayer.inventory.addItemStackToInventory(stack)) entityPlayer.dropPlayerItemWithRandomChoice(stack,false);
        }
        return itemStack;
    }

    public void affectConsumer(ItemStack itemStack, World world, EntityLivingBase entityLivingBase) {

    }
}
