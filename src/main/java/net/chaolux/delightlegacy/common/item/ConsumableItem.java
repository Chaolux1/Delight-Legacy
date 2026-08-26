package net.chaolux.delightlegacy.common.item;

import net.chaolux.delightlegacy.DelightLegacy;
import net.chaolux.delightlegacy.common.utility.TextUtils;
import net.chaolux.delightlegacy.legacy.common.item.LegacyFoodProperties;
import net.chaolux.delightlegacy.legacy.common.item.LegacyItem;
import net.chaolux.delightlegacy.legacy.common.item.LegacyItemProperties;
import net.chaolux.delightlegacy.legacy.registry.LegacyItemRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import java.util.List;

public class ConsumableItem extends LegacyItem {
    private final boolean hasFoodEffectTooltip;
    private final boolean hasCustomTooltip;
    public ConsumableItem(LegacyItemProperties legacyItemProperties) {
        this(legacyItemProperties,true,false);
    }

    public ConsumableItem(LegacyItemProperties legacyItemProperties,boolean hasFoodEffectTooltip) {
        this(legacyItemProperties,hasFoodEffectTooltip,false);
    }

    public ConsumableItem(LegacyItemProperties legacyItemProperties,boolean hasFoodEffectTooltip,boolean hasCustomTooltip) {
        super(legacyItemProperties);
        this.hasFoodEffectTooltip=hasFoodEffectTooltip;
        this.hasCustomTooltip=hasCustomTooltip;
    }

    public boolean hasFoodEffectTooltip() {
        return hasFoodEffectTooltip;
    }

    public boolean hasCustomTooltip() {
        return hasCustomTooltip;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        return EnumAction.eat;
    }

    @Override
    public ItemStack onEaten(ItemStack itemStack,World world,EntityPlayer entityPlayer) {
        if(!world.isRemote) affectConsumer(itemStack,world,entityPlayer);
        Item item=hasContainerItem() ? getContainerItem() : null;
        if(isFood()) {
            itemStack=super.onEaten(itemStack,world,entityPlayer);
        } else if (!entityPlayer.capabilities.isCreativeMode) {
            itemStack.stackSize--;
        }
        if(entityPlayer.capabilities.isCreativeMode || item == null) return itemStack;
        if(itemStack.stackSize <= 0) return new ItemStack(item);
        if(!world.isRemote) {
            ItemStack containerStack=new ItemStack(item);
            if(!entityPlayer.inventory.addItemStackToInventory(containerStack)) entityPlayer.dropPlayerItemWithRandomChoice(containerStack,false);
        }
        return itemStack;
    }

    public void affectConsumer(ItemStack itemStack, World world, EntityLivingBase entityLivingBase) {

    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list,boolean isAdvanced) {
        if(hasCustomTooltip) list.add(TextUtils.tooltip(getTooltip(itemStack)));
        if(hasFoodEffectTooltip) TextUtils.addFoodEffectTooltip(itemStack,list,1.0f);
    }

    private String getTooltip(ItemStack itemStack) {
        String string=getUnlocalizedName(itemStack);
        String prefix="item." + DelightLegacy.MOD_ID + ".";
        if(string.startsWith(prefix)) return string.substring(prefix.length());
        if(string.startsWith("item.")) return string.substring("item.".length());
        return string;
    }
}
