/*
Copyright (c) Forge Development LLC and contributors
SPDX-License-Identifier: LGPL-2.1-only
Modified by Chaolux, 2026
 */
package net.chaolux.delightlegacy.legacy.data.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import cpw.mods.fml.common.registry.GameRegistry;
import net.chaolux.delightlegacy.legacy.assets.ResourceLocations;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class LegacyRecipeJson {
    public static JsonElement getJson(JsonObject jsonObject,String string) {
        if(jsonObject == null) throw new JsonSyntaxException("Expected json object");
        if(!jsonObject.has(string)) throw new JsonSyntaxException("Missing require property '" + string + "'");
        return jsonObject.get(string);
    }

    public static JsonObject getObject(JsonObject jsonObject,String string) {
        JsonElement jsonElement=getJson(jsonObject, string);
        if(!jsonElement.isJsonObject()) throw new JsonSyntaxException("Expected '" + string + "'to be a object");
        return jsonElement.getAsJsonObject();
    }

    public static JsonArray getArray(JsonObject jsonObject,String string) {
        JsonElement jsonElement=getJson(jsonObject,string);
        if(!jsonElement.isJsonArray()) throw new JsonSyntaxException("Expected '" + string + "'to be an array");
        return jsonElement.getAsJsonArray();
    }

    public static String getString(JsonObject jsonObject,String string) {
        JsonElement jsonElement=getJson(jsonObject,string);
        if(!jsonElement.isJsonPrimitive() || !jsonElement.getAsJsonPrimitive().isString()) throw new JsonSyntaxException("Expected '" + string + "' to be a string");
        return jsonElement.getAsString();
    }

    public static String getString(JsonObject jsonObject,String string,String object) {
        return jsonObject == null || !jsonObject.has(string) ? object : getString(jsonObject,string);
    }

    public static int getInt(JsonObject jsonObject,String string,int value) {
        if(jsonObject == null || !jsonObject.has(string)) return value;
        JsonElement jsonElement=jsonObject.get(string);
        if(!jsonElement.isJsonPrimitive() || !jsonElement.getAsJsonPrimitive().isNumber()) throw new JsonSyntaxException("Expected '" + string +"' to be a number");
        return jsonElement.getAsInt();
    }

    public static float getFloat(JsonObject jsonObject,String string,float value) {
        if(jsonObject == null || !jsonObject.has(string)) return value;
        JsonElement jsonElement=jsonObject.get(string);
        if(!jsonElement.isJsonPrimitive() || !jsonElement.getAsJsonPrimitive().isNumber()) throw new JsonSyntaxException("Expected '" + string + "' to be a number");
        return jsonElement.getAsFloat();
    }

    public static ResourceLocation getLocation(JsonObject jsonObject,String string) {
        String object=getString(jsonObject,string);
        try {
            return ResourceLocations.parse(object);
        } catch (IllegalArgumentException exception) {
            throw new JsonSyntaxException("Invalid resource location in '" + string + "': " + object);
        }
    }

    public static ItemStack getStack(JsonObject jsonObject) {
        ResourceLocation resourceLocation=getLocation(jsonObject,"item");
        Item item= GameRegistry.findItem(ResourceLocations.namespace(resourceLocation),ResourceLocations.path(resourceLocation));
        if(item == null) throw new JsonSyntaxException("Unknown item '" + ResourceLocations.getString(resourceLocation) + "'");
        int count=getInt(jsonObject,"count",1);
        int data=getInt(jsonObject,"data",0);
        if(count <= 0) throw new JsonSyntaxException("Item count must be greater than zero");
        if(data < 0) throw new JsonSyntaxException("Item data value cannot be negative");
        return new ItemStack(item,count,data);
    }
}
