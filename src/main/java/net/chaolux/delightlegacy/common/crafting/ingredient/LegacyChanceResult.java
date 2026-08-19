package net.chaolux.delightlegacy.common.crafting.ingredient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.chaolux.delightlegacy.legacy.data.recipe.LegacyRecipeJson;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class LegacyChanceResult {
    private final ItemStack itemStack;
    private final float chance;
    public LegacyChanceResult(ItemStack itemStack,float chance) {
        if(itemStack == null) throw new IllegalArgumentException("Result stack cannot be null");
        if(chance < 0.0f || chance > 1.0f) throw new IllegalArgumentException("Result chance must be between 0 and 1");
        this.itemStack=itemStack.copy();
        this.chance=chance;
    }

    public static LegacyChanceResult fromJson(JsonElement jsonElement) {
        if(jsonElement == null || !jsonElement.isJsonObject()) throw new JsonSyntaxException("Cutting result must be a json object");
        JsonObject jsonObject=jsonElement.getAsJsonObject();
        ItemStack stack= LegacyRecipeJson.getStack(jsonObject);
        float chance=LegacyRecipeJson.getFloat(jsonObject,"chance",1.0f);
        if(chance < 0.0f || chance > 1.0f) throw new JsonSyntaxException("Result chance must be between 0 and 1");
        return new LegacyChanceResult(stack,chance);
    }

    public ItemStack getItemStack() {
        return itemStack.copy();
    }

    public float getChance() {
        return chance;
    }

    public ItemStack roll(Random random) {
        return roll(random,0.0f);
    }

    public ItemStack roll(Random random,float fortuneBonus) {
        if(random == null) throw new IllegalArgumentException("Random cannot be null");
        float bonusChance=Math.max(0.0f,Math.min(1.0f,chance + fortuneBonus));
        int value=itemStack.stackSize;
        for (int index=0;index < itemStack.stackSize;index++) {
            if(random.nextFloat() > bonusChance) value--;
        }
        if(value <= 0) return null;
        ItemStack stack=itemStack.copy();
        stack.stackSize=value;
        return stack;
    }
}
