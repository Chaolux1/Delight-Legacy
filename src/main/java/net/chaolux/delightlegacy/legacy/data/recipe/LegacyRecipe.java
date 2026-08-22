/*
Copyright (c) Forge Development LLC and contributors
SPDX-License-Identifier: LGPL-2.1-only
Modified by Chaolux, 2026
 */
package net.chaolux.delightlegacy.legacy.data.recipe;

import net.minecraft.util.ResourceLocation;

public interface LegacyRecipe<C> {
    ResourceLocation getId();
    LegacyRecipeType<?> getType();
    LegacyRecipeSerializer<?> getSerializer();
    boolean matches(C input);
}
