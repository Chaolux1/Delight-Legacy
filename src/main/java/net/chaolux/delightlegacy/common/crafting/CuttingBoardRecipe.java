package net.chaolux.delightlegacy.common.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.chaolux.delightlegacy.common.crafting.ingredient.LegacyChanceResult;
import net.chaolux.delightlegacy.legacy.data.recipe.*;
import net.chaolux.delightlegacy.registry.ModRecipeSerializers;
import net.chaolux.delightlegacy.registry.ModRecipeTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CuttingBoardRecipe implements LegacyRecipe<CuttingBoardRecipeWrapper> {
    public static final int MAX_RESULTS=4;
    private final ResourceLocation id;
    private final String group;
    private final LegacyIngredient input;
    private final LegacyIngredient tool;
    private final List<LegacyChanceResult> results;
    private final String soundEvent;
    public CuttingBoardRecipe(ResourceLocation id, String group, LegacyIngredient input,LegacyIngredient tool,List<LegacyChanceResult> results,String soundEvent) {
        if(id == null) throw new IllegalArgumentException("Recipe id cannot be null");
        if(input == null || input.isEmpty()) throw new IllegalArgumentException("Cutting input cannot be empty");
        if(tool == null || tool.isEmpty()) throw new IllegalArgumentException("Cutting tool cannot be empty");
        if(results == null || results.isEmpty()) throw new IllegalArgumentException("Cutting results cannot be empty");
        if(results.size() > MAX_RESULTS) throw new IllegalArgumentException("Cutting recipe at most " + MAX_RESULTS + " result");
        this.id=id;
        this.group=group == null ? "" : group;
        this.input=input;
        this.tool=tool;
        this.results= Collections.unmodifiableList(new ArrayList<LegacyChanceResult>(results));
        this.soundEvent=soundEvent == null ? "" : soundEvent;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public LegacyRecipeType<?> getType() {
        return ModRecipeTypes.CUTTING;
    }

    @Override
    public LegacyRecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CUTTING;
    }

    @Override
    public boolean matches(CuttingBoardRecipeWrapper cuttingBoardRecipeWrapper) {
        return cuttingBoardRecipeWrapper != null && matchesInput(cuttingBoardRecipeWrapper.getInput()) && matchesTool(cuttingBoardRecipeWrapper.getTool());
    }

    public boolean matchesInput(ItemStack itemStack) {
        return input.is(itemStack);
    }

    public boolean matchesTool(ItemStack itemStack) {
        return tool.is(itemStack);
    }

    public String getGroup() {
        return group;
    }

    public LegacyIngredient getInput() {
        return input;
    }

    public LegacyIngredient getTool() {
        return tool;
    }

    public List<LegacyChanceResult> getRollableResults() {
        return results;
    }

    public List<ItemStack> getResults() {
        List<ItemStack> itemStackList=new ArrayList<ItemStack>(results.size());
        for (LegacyChanceResult legacyChanceResult : results) {
            itemStackList.add(legacyChanceResult.getItemStack());
        }
        return Collections.unmodifiableList(itemStackList);
    }

    public String getSoundEventID() {
        return soundEvent;
    }

    public List<ItemStack> rollResult(Random random) {
        return rollResult(random,0.0f);
    }

    public List<ItemStack> rollResult(Random random,float chance) {
        List<ItemStack> itemStackList=new ArrayList<ItemStack>();
        for (LegacyChanceResult legacyChanceResult : results) {
            ItemStack itemStack=legacyChanceResult.roll(random,chance);
            if(itemStack != null && itemStack.stackSize > 0) itemStackList.add(itemStack);
        }
        return itemStackList;
    }

    public static final class Serializer implements LegacyRecipeSerializer<CuttingBoardRecipe> {
        @Override
        public CuttingBoardRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            String string= LegacyRecipeJson.getString(jsonObject,"group","");
            JsonArray jsonArray=LegacyRecipeJson.getArray(jsonObject,"ingredients");
            if(jsonArray.size() == 0) throw new JsonSyntaxException("No ingredients for cutting recipe");
            if(jsonArray.size() > 1) throw new JsonSyntaxException("Too many ingredients for cutting recipe! Please define only one ingredient");
            LegacyIngredient legacyIngredient=LegacyIngredient.fromJson(jsonArray.get(0));
            if(legacyIngredient.isEmpty()) throw new JsonSyntaxException("Cutting ingredient cannot be empty");
            JsonElement jsonElement=LegacyRecipeJson.getJson(jsonObject,"tool");
            LegacyIngredient ingredient=LegacyIngredient.fromJson(jsonElement);
            if(ingredient.isEmpty()) throw new JsonSyntaxException("Cutting tool cannot be empty");
            JsonArray array=LegacyRecipeJson.getArray(jsonObject,"result");
            if(array.size() == 0) throw new JsonSyntaxException("No result for cutting board recipe");
            if(array.size() > MAX_RESULTS) throw new JsonSyntaxException("Too many results for cutting recipe! The maximum quantity of unique results is 4");
            List<LegacyChanceResult> list=new ArrayList<LegacyChanceResult>(array.size());
            for (JsonElement element : array) {
                list.add(LegacyChanceResult.fromJson(element));
            }
            String soundEvent=LegacyRecipeJson.getString(jsonObject,"sound","");
            return new CuttingBoardRecipe(resourceLocation,string,legacyIngredient,ingredient,list,soundEvent);
        }
    }
}
