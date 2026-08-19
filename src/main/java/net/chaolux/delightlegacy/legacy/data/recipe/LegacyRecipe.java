package net.chaolux.delightlegacy.legacy.data.recipe;

import net.minecraft.util.ResourceLocation;

public interface LegacyRecipe<C> {
    ResourceLocation getId();
    LegacyRecipeType<?> getType();
    LegacyRecipeSerializer<?> getSerializer();
    boolean matches(C input);
}
