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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ShapedRecipe implements LegacyCraftingRecipe {
    private final int width;
    private final int height;
    private final ResourceLocation id;
    private final String group;
    private final LegacyIngredient[] legacyIngredients;
    private final ItemStack result;
    public ShapedRecipe(ResourceLocation id,String group,int width,int height,LegacyIngredient[] legacyIngredients,ItemStack result) {
        if(id == null) throw new IllegalArgumentException("Recipe id cannot be null");
        if(width <= 0 || width > 3 || height <= 0 || height > 3) throw new IllegalArgumentException("Shaped crafting recipe must in 3x3");
        if(legacyIngredients == null || legacyIngredients.length != width * height) throw new IllegalArgumentException("Shaped ingredient array is invalid size");
        if(result == null) throw new IllegalArgumentException("Crafting result cannot be null");
        this.id=id;
        this.group=group == null ? "" : group;
        this.width=width;
        this.height=height;
        this.legacyIngredients=new LegacyIngredient[legacyIngredients.length];
        System.arraycopy(legacyIngredients,0,this.legacyIngredients,0,legacyIngredients.length);
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
        return ModRecipeSerializers.SHAPED_CRAFTING;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting) {
        if(inventoryCrafting == null) return false;
        int inventoryWidth=getInventoryWidth(inventoryCrafting);
        int inventoryHeight=getInventoryHeight(inventoryCrafting);
        if(width > inventoryWidth || height > inventoryHeight) return false;
        for (int indexX=0;indexX <= inventoryWidth - width;indexX++) {
            for (int indexY=0;indexY <= inventoryHeight - height;indexY++) {
                if(offset(inventoryCrafting,indexX,indexY,false)) return true;
                if(offset(inventoryCrafting,indexX,indexY,true)) return true;
            }
        }
        return false;
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
        return width * height;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return result.copy();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public LegacyIngredient[] getLegacyIngredients() {
        LegacyIngredient[] ingredients=new LegacyIngredient[legacyIngredients.length];
        System.arraycopy(legacyIngredients,0,ingredients,0,legacyIngredients.length);
        return ingredients;
    }

    private boolean offset(InventoryCrafting inventoryCrafting,int offsetX,int offsetY,boolean mirror) {
        int inventoryWidth=getInventoryWidth(inventoryCrafting);
        int inventoryHeight=getInventoryHeight(inventoryCrafting);
        for(int indexY=0;indexY < inventoryHeight;indexY++) {
            for (int indexX=0;indexX < inventoryWidth;indexX++) {
                int recipeX=indexX - offsetX;
                int recipeY=indexY - offsetY;
                LegacyIngredient legacyIngredient=LegacyIngredient.EMPTY;
                if(recipeX >= 0 && recipeX < width && recipeY >= 0 && recipeY < height) {
                    int x=mirror ? width - recipeX - 1 : recipeX;
                    legacyIngredient=legacyIngredients[x + recipeY * width];
                }
                ItemStack itemStack=inventoryCrafting.getStackInRowAndColumn(indexX,indexY);
                if(legacyIngredient == null || legacyIngredient.isEmpty()) {
                    if(itemStack != null) return false;
                } else if (!legacyIngredient.is(itemStack)) return false;
            }
        }
        return true;
    }

    private static int getInventoryWidth(InventoryCrafting inventoryCrafting) {
        int size=inventoryCrafting.getSizeInventory();
        if(size == 4) return 2;
        if(size == 9) return 3;
        int width=(int) Math.sqrt(size);
        if(width * width != size) throw new IllegalArgumentException("Crafting recipe size: " + size);
        return width;
    }

    private static int getInventoryHeight(InventoryCrafting inventoryCrafting) {
        int width=getInventoryWidth(inventoryCrafting);
        return inventoryCrafting.getSizeInventory() / width;
    }

    public static final class Serializer implements LegacyRecipeSerializer<ShapedRecipe> {
        @Override
        public ShapedRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            String string=LegacyRecipeJson.getString(jsonObject,"group","");
            JsonObject object=LegacyRecipeJson.getObject(jsonObject,"key");
            Map<Character,LegacyIngredient> map=parse(object);
            JsonArray jsonArray=LegacyRecipeJson.getArray(jsonObject,"pattern");
            String[] pattern=parsePattern(jsonArray);
            String[] shrinkPattern=shrink(pattern);
            int height=shrinkPattern.length;
            int width=shrinkPattern[0].length();
            if(width > 3 || height > 3) throw new JsonSyntaxException("Shaped crafting recipe must in 3x3");
            Set<Character> set=new HashSet<Character>();
            LegacyIngredient[] ingredients=new LegacyIngredient[width * height];
            for (int indexY=0;indexY < height;indexY++) {
                String row=shrinkPattern[indexY];
                for (int indexX=0;indexX < width;indexX++) {
                    char chars=row.charAt(indexX);
                    if (chars == ' ') {
                        ingredients[indexX +indexY * width]=LegacyIngredient.EMPTY;
                        continue;
                    }
                    LegacyIngredient legacyIngredient=map.get(chars);
                    if(legacyIngredient == null) throw new JsonSyntaxException("Pattern chars '" + chars + "'");
                    ingredients[indexX + indexY * width]=legacyIngredient;
                    set.add(chars);
                }
            }
            for (Character character : map.keySet()) {
                if(!set.contains(character)) throw new JsonSyntaxException("Key defines character '" + character + "'");
            }
            ItemStack result=LegacyRecipeJson.getStack(LegacyRecipeJson.getObject(jsonObject,"result"));
            return new ShapedRecipe(resourceLocation,string,width,height,ingredients,result);
        }
        private static Map<Character,LegacyIngredient> parse(JsonObject jsonObject) {
            Map<Character,LegacyIngredient> map=new HashMap<Character,LegacyIngredient>();
            for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String string=entry.getKey();
                if(string.length() != 1) throw new JsonSyntaxException("Invalid key entry '" + string + "' crafting chars must be one character");
                char chars=string.charAt(0);
                if(chars == ' ') throw new JsonSyntaxException("Space is reserve for empty crafting slots and cannot be empty");
                LegacyIngredient legacyIngredient=LegacyIngredient.fromJson(entry.getValue());
                if(legacyIngredient.isEmpty()) throw new JsonSyntaxException("Ingredient for chars '" + chars + "' cannot be empty");
                map.put(chars,legacyIngredient);
            }
            return map;
        }
        private static String[] parsePattern(JsonArray jsonArray) {
            if(jsonArray.size() == 0) throw new JsonSyntaxException("Shaped crafting pattern cannot be empty");
            if(jsonArray.size() > 3) throw new JsonSyntaxException("Shaped crafting pattern cannot have more than 3");
            String[] strings=new String[jsonArray.size()];
            int width=-1;
            for (int index=0;index < jsonArray.size();index++) {
                JsonElement jsonElement=jsonArray.get(index);
                if(!jsonElement.isJsonPrimitive() || !jsonElement.getAsJsonPrimitive().isString()) throw new JsonSyntaxException("Each shaped crafting pattern must be string");
                String string=jsonElement.getAsString();
                if(width == -1) {
                    width=string.length();
                } else if(string.length() != width) throw new JsonSyntaxException("All shaped crafting pattern must have a same width");
                if(string.length() > 3) throw new JsonSyntaxException("Shaped crafting pattern cannot have more than 3 columns");
                strings[index]=string;
            }
            if(width <= 0) throw new JsonSyntaxException("Shaped crafting pattern row cannot be empty");
            return strings;
        }
        private static String[] shrink(String[] strings) {
            int firstRow=0;
            int secondRow=strings.length - 1;
            while (firstRow <= secondRow && isPattern(strings[firstRow])) {
                firstRow++;
            }
            while (secondRow >= firstRow && isPattern(strings[secondRow])) {
                secondRow--;
            }
            if(firstRow > secondRow) throw new JsonSyntaxException("Shaped crafting pattern cannot contain only spaces");
            int firstColumn=Integer.MAX_VALUE;
            int secondColumn= -1;
            for(int index=firstRow;index <= secondRow;index++) {
                String string=strings[index];
                int rowFirst=firstNoSpace(string);
                int rowSecond=secondNoSpace(string);
                if(rowFirst >= 0) {
                    firstColumn=Math.min(firstColumn,rowFirst);
                    secondColumn=Math.max(secondColumn,rowSecond);
                }
            }
            if(secondColumn < firstColumn) throw new JsonSyntaxException("Shaped crafting pattern cannot contain only spaces");
            String[] result=new String[secondRow - firstRow + 1];
            for(int index=0;index < result.length;index++) {
                result[index]=strings[firstRow + index].substring(firstColumn,secondColumn + 1);
            }
            return result;
        }
        private static boolean isPattern(String string) {
            return firstNoSpace(string) < 0;
        }
        private static int firstNoSpace(String string) {
            for(int index=0;index < string.length();index++) {
                if(string.charAt(index) != ' ') return index;
            }
            return -1;
        }
        private static int secondNoSpace(String string) {
            for(int index=string.length() - 1; index >= 0;index--) {
                if(string.charAt(index) != ' ') return index;
            }
            return -1;
        }
    }


}
