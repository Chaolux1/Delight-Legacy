package net.chaolux.delightlegacy.legacy.common.item;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.chaolux.delightlegacy.common.item.DogFoodItem;
import net.chaolux.delightlegacy.common.item.HorseFeedItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class LegacyAnimalFeedHandler {
    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent entityInteractEvent) {
        EntityPlayer entityPlayer=entityInteractEvent.entityPlayer;
        Entity entity=entityInteractEvent.target;
        ItemStack itemStack=entityPlayer.getCurrentEquippedItem();
        if(itemStack == null || !(entity instanceof EntityLivingBase)) return;
        Item item=itemStack.getItem();
        boolean handle=false;
        if(item instanceof DogFoodItem) {
            handle=((DogFoodItem) item).feed(itemStack,entityPlayer,(EntityLivingBase) entity);
        } else if(item instanceof HorseFeedItem) {
            handle=((HorseFeedItem) item).feed(itemStack,entityPlayer,(EntityLivingBase) entity);
        }
        if(handle) entityInteractEvent.setCanceled(true);
    }
}
