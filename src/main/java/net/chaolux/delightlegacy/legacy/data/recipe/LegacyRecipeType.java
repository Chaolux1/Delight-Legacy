/*
Copyright (c) Forge Development LLC and contributors
SPDX-License-Identifier: LGPL-2.1-only
Modified by Chaolux, 2026
 */
package net.chaolux.delightlegacy.legacy.data.recipe;

import net.chaolux.delightlegacy.legacy.assets.ResourceLocations;
import net.minecraft.util.ResourceLocation;

public class LegacyRecipeType<T extends LegacyRecipe<?>> {
    private final ResourceLocation resourceLocation;
    LegacyRecipeType(ResourceLocation resourceLocation) {
        if(resourceLocation == null) throw new IllegalArgumentException("Recipe type id cannot be null");
        this.resourceLocation=resourceLocation;
    }

    public ResourceLocation getId() {
        return resourceLocation;
    }

    @Override
    public String toString() {
        return ResourceLocations.getString(resourceLocation);
    }
}
