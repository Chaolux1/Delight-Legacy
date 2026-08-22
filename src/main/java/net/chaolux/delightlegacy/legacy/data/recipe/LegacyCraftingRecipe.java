/*
Copyright (c) Forge Development LLC and contributors
SPDX-License-Identifier: LGPL-2.1-only
Modified by Chaolux, 2026
 */
package net.chaolux.delightlegacy.legacy.data.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.IRecipe;

public interface LegacyCraftingRecipe extends LegacyRecipe<InventoryCrafting>, IRecipe {
    String getGroup();
}
