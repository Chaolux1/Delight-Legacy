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
import net.chaolux.delightlegacy.legacy.assets.ResourceLocations;
import net.chaolux.delightlegacy.registry.ModRecipeSerializers;
import net.chaolux.delightlegacy.registry.ModRecipeTypes;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShapelessRecipe implements LegacyCraftingRecipe {
    public static final int MAX=9;
    private final ResourceLocation id;
    private final String group;
    private final List<LegacyIngredient> legacyIngredientList;
    private final ItemStack result;
    public ShapelessRecipe(ResourceLocation id,String group,List<LegacyIngredient> legacyIngredientList,ItemStack result) {
        if(id == null) throw new IllegalArgumentException("Recipe id cannot be null");
        if(legacyIngredientList == null || legacyIngredientList.isEmpty()) throw new IllegalArgumentException("Shapeless crafting recipe with one ingredient");
        if(legacyIngredientList.size() > MAX) throw new IllegalArgumentException("Shapeless crafting recipe at most " + MAX + " ingredients");
        if(result == null) throw new IllegalArgumentException("Crafting result cannot be null");
        this.id=id;
        this.group=group == null ? "" : group;
        this.legacyIngredientList= Collections.unmodifiableList(new ArrayList<LegacyIngredient>(legacyIngredientList));
        this.result=result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public LegacyRecipeType<?> getType() {
        return ModRecipeTypes.CRAFTING;
    }

    @Override
    public LegacyRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.SHAPELESS_CRAFTING;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting) {
        if(inventoryCrafting == null) return false;
        List<ItemStack> list=new ArrayList<ItemStack>();
        for(int index=0;index < inventoryCrafting.getSizeInventory();index++) {
            ItemStack itemStack=inventoryCrafting.getStackInSlot(index);
            if(itemStack != null) list.add(itemStack);
        }
        if(list.size() != legacyIngredientList.size()) return false;
        boolean[] is=new boolean[legacyIngredientList.size()];
        return match(list,0,is);
    }

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        return matches(inventoryCrafting);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        return result.copy();
    }

    @Override
    public int getRecipeSize() {
        return legacyIngredientList.size();
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result.copy();
    }

    public List<LegacyIngredient> getLegacyIngredientList() {
        return legacyIngredientList;
    }

    private boolean match(List<ItemStack> itemStackList,int value,boolean[] is) {
        if(value >= itemStackList.size()) return true;
        ItemStack itemStack=itemStackList.get(value);
        for(int index=0;index < legacyIngredientList.size();index++) {
            if(is[index]) continue;
            LegacyIngredient legacyIngredient=legacyIngredientList.get(index);
            if(!legacyIngredient.is(itemStack)) continue;
            is[index]=true;
            if(match(itemStackList,value + 1,is)) return true;
            is[index]=false;
        }
        return false;
    }

    public static final class Serializer implements LegacyRecipeSerializer<ShapelessRecipe> {
        @Override
        public ShapelessRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            String string=LegacyRecipeJson.getString(jsonObject,"group","");
            JsonArray jsonArray=LegacyRecipeJson.getArray(jsonObject,"ingredients");
            if(jsonArray.size() == 0) throw new JsonSyntaxException("Shapeless crafting recipe with one ingredient");
            if(jsonArray.size() > MAX) throw new JsonSyntaxException("Shapeless crafting recipe at most " + MAX + " ingredients");
            List<LegacyIngredient> list=new ArrayList<LegacyIngredient>(jsonArray.size());
            for (JsonElement jsonElement : jsonArray) {
                LegacyIngredient legacyIngredient=LegacyIngredient.fromJson(jsonElement);
                if(legacyIngredient.isEmpty()) throw new JsonSyntaxException("Shapeless crafting ingredient cannot be empty");
                list.add(legacyIngredient);
            }
            ItemStack itemStack=LegacyRecipeJson.getStack(LegacyRecipeJson.getObject(jsonObject,"result"));
            return new ShapelessRecipe(resourceLocation,string,list,itemStack);
        }
    }
}
