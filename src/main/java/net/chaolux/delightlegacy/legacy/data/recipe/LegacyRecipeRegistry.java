/*
Copyright (c) Forge Development LLC and contributors
SPDX-License-Identifier: LGPL-2.1-only
Modified by Chaolux, 2026
 */
package net.chaolux.delightlegacy.legacy.data.recipe;

import net.chaolux.delightlegacy.legacy.assets.ResourceLocations;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class LegacyRecipeRegistry {
    private static final Map<ResourceLocation,LegacyRecipeType<?>> TYPES=new LinkedHashMap<ResourceLocation,LegacyRecipeType<?>>();
    private static final Map<ResourceLocation,LegacyRecipeSerializer<?>> SERIALIZERS=new LinkedHashMap<ResourceLocation,LegacyRecipeSerializer<?>>();
    public static synchronized <T extends LegacyRecipe<?>> LegacyRecipeType<T> registerType(ResourceLocation resourceLocation) {
        getRecipe(resourceLocation);
        if(TYPES.containsKey(resourceLocation)) throw new IllegalStateException("Duplicate recipe type: " + ResourceLocations.getString(resourceLocation));
        LegacyRecipeType<T> legacyRecipeType=new LegacyRecipeType<T>(resourceLocation);
        TYPES.put(resourceLocation,legacyRecipeType);
        return legacyRecipeType;
    }

    public static synchronized <T extends LegacyRecipe<?>> LegacyRecipeSerializer<T> registerSerializer(ResourceLocation resourceLocation,LegacyRecipeSerializer<T> legacyRecipeSerializer) {
        getRecipe(resourceLocation);
        if(legacyRecipeSerializer == null) throw new IllegalArgumentException("Recipe serializer cannot be null");
        if(SERIALIZERS.containsKey(resourceLocation)) throw new IllegalStateException("Duplicate recipe serializer: " + ResourceLocations.getString(resourceLocation));
        SERIALIZERS.put(resourceLocation,legacyRecipeSerializer);
        return legacyRecipeSerializer;
    }

    public static LegacyRecipeType<?> getType(ResourceLocation resourceLocation) {
        return TYPES.get(resourceLocation);
    }

    public static LegacyRecipeSerializer<?> getSerializer(ResourceLocation resourceLocation) {
        return SERIALIZERS.get(resourceLocation);
    }

    public static boolean isType(ResourceLocation resourceLocation) {
        return TYPES.containsKey(resourceLocation);
    }

    public static boolean isSerializer(ResourceLocation resourceLocation) {
        return SERIALIZERS.containsKey(resourceLocation);
    }

    public static Map<ResourceLocation,LegacyRecipeType<?>> getTypes() {
        return Collections.unmodifiableMap(TYPES);
    }

    public static Map<ResourceLocation,LegacyRecipeSerializer<?>> getSerializers() {
        return Collections.unmodifiableMap(SERIALIZERS);
    }

    private static void getRecipe(ResourceLocation resourceLocation) {
        if(resourceLocation == null) throw new IllegalArgumentException("Registry id cannot be null");
    }
}
