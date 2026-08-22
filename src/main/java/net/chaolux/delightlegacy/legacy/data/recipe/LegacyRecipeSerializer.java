/*
Copyright (c) Forge Development LLC and contributors
SPDX-License-Identifier: LGPL-2.1-only
Modified by Chaolux, 2026
 */
package net.chaolux.delightlegacy.legacy.data.recipe;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

public interface LegacyRecipeSerializer<T extends LegacyRecipe<?>> {
    T fromJson(ResourceLocation resourceLocation, JsonObject jsonObject);
}
