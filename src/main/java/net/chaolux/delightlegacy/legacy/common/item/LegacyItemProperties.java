/*
Copyright (c) Forge Development LLC and contributors
SPDX-License-Identifier: LGPL-2.1-only
Modified by Chaolux, 2026
 */
package net.chaolux.delightlegacy.legacy.common.item;

import net.minecraft.item.Item;

public class LegacyItemProperties {
    private int stackSize=64;
    private Item craftRemainder;
    private LegacyFoodProperties legacyFoodProperties;
    public LegacyItemProperties stacksTo(int stackSize) {
        if(stackSize < 1 || stackSize > 64)  throw new IllegalArgumentException("Stack size must be between 1 and 64");
        this.stackSize=stackSize;
        return this;
    }

    public LegacyItemProperties craftRemainder(Item item) {
        this.craftRemainder=item;
        return this;
    }

    public LegacyItemProperties food(LegacyFoodProperties legacyFoodProperties) {
        this.legacyFoodProperties=legacyFoodProperties;
        return this;
    }

    public int getStackSize() {
        return stackSize;
    }

    public Item getCraftRemainder() {
        return craftRemainder;
    }

    public LegacyFoodProperties getFood() {
        return legacyFoodProperties;
    }

    public void apply(Item item) {
        item.setMaxStackSize(stackSize);
        if(craftRemainder != null) item.setContainerItem(craftRemainder);
    }
}
