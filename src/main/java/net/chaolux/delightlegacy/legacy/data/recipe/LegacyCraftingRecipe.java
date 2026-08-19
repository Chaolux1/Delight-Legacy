package net.chaolux.delightlegacy.legacy.data.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.IRecipe;

public interface LegacyCraftingRecipe extends LegacyRecipe<InventoryCrafting>, IRecipe {
    String getGroup();
}
