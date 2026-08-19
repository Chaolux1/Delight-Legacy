package net.chaolux.delightlegacy.legacy.data.recipe;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;

public interface LegacyRecipeSerializer<T extends LegacyRecipe<?>> {
    T fromJson(ResourceLocation resourceLocation, JsonObject jsonObject);
}
