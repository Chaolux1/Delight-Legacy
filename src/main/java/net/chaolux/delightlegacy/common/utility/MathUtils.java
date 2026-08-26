package net.chaolux.delightlegacy.common.utility;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

import java.util.Random;

public class MathUtils {
    public static final Random RAND=new Random();
    public static int calcRedstoneFromInventory(IInventory inventory) {
        return Container.calcRedstoneFromInventory(inventory);
    }
}
