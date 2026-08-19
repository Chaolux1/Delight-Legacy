package net.chaolux.delightlegacy.legacy.data.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import net.chaolux.delightlegacy.DelightLegacy;
import net.chaolux.delightlegacy.legacy.assets.LegacyPackType;
import net.chaolux.delightlegacy.legacy.assets.LegacyResources;
import net.chaolux.delightlegacy.legacy.assets.ResourceLocations;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class LegacyRecipeManager {
    private static final String RECIPE="recipes";
    private static final String JSON=".json";
    private static final LegacyRecipeManager INSTANCE=new LegacyRecipeManager();
    private volatile Map<ResourceLocation,LegacyRecipe<?>> recipeMap= Collections.emptyMap();
    private volatile Map<LegacyRecipeType<?>,Map<ResourceLocation,LegacyRecipe<?>>> recipeTypeMap=Collections.emptyMap();
    private final List<IRecipe> iRecipeList=new ArrayList<IRecipe>();
    public static LegacyRecipeManager getInstance() {
        return INSTANCE;
    }

    public synchronized void reload() {
        removeCraftingRecipe();
        LinkedHashMap<ResourceLocation,LegacyRecipe<?>> loadRecipe=new LinkedHashMap<ResourceLocation,LegacyRecipe<?>>();
        LinkedHashMap<LegacyRecipeType<?>,LinkedHashMap<ResourceLocation,LegacyRecipe<?>>> loadType=new LinkedHashMap<LegacyRecipeType<?>,LinkedHashMap<ResourceLocation,LegacyRecipe<?>>>();
        List<IRecipe> iRecipes=new ArrayList<IRecipe>();
        int value=0;
        try {
            Set<ResourceLocation> set= LegacyResources.set(LegacyPackType.DATA,RECIPE,JSON);
            List<ResourceLocation> locations=new ArrayList<ResourceLocation>(set);
            Collections.sort(locations, new Comparator<ResourceLocation>() {
                @Override
                public int compare(ResourceLocation o1, ResourceLocation o2) {
                    return ResourceLocations.getString(o1).compareTo(ResourceLocations.getString(o2));
                }
            });
            for (ResourceLocation resourceLocation : locations) {
                try {
                    LegacyRecipe<?> legacyRecipe=load(resourceLocation,loadRecipe,loadType);
                    if(legacyRecipe instanceof IRecipe) iRecipes.add((IRecipe) legacyRecipe);
                } catch (Exception exception) {
                    value++;
                    DelightLegacy.LOGGER.error("Parsing error loading recipe resource {}: {}",ResourceLocations.getString(resourceLocation),exception.getMessage(),exception);
                }
            }
        } catch (IOException exception) {
            value++;
            DelightLegacy.LOGGER.error("Fail to enumerate recipe resources",exception);
        }
        recipeMap=Collections.unmodifiableMap(new LinkedHashMap<ResourceLocation,LegacyRecipe<?>>(loadRecipe));
        LinkedHashMap<LegacyRecipeType<?>,Map<ResourceLocation,LegacyRecipe<?>>> linkedHashMap=new LinkedHashMap<LegacyRecipeType<?>,Map<ResourceLocation,LegacyRecipe<?>>>();
        for (Map.Entry<LegacyRecipeType<?>,LinkedHashMap<ResourceLocation,LegacyRecipe<?>>> entry : loadType.entrySet()) {
            linkedHashMap.put(entry.getKey(),Collections.unmodifiableMap(new LinkedHashMap<ResourceLocation,LegacyRecipe<?>>(entry.getValue())));
        }
        recipeTypeMap=Collections.unmodifiableMap(linkedHashMap);
        setCraftingRecipe(iRecipes);
        DelightLegacy.LOGGER.info("Load {} legacy JSON recipes with {} error",recipeMap.size(),value);
    }

    public int getRecipe() {
        return recipeMap.size();
    }

    public Map<ResourceLocation,LegacyRecipe<?>> getRecipeMap() {
        return recipeMap;
    }

    public Optional<LegacyRecipe<?>> getRecipe(ResourceLocation resourceLocation) {
        return resourceLocation == null ? Optional.<LegacyRecipe<?>>empty() : Optional.ofNullable(recipeMap.get(resourceLocation));
    }

    public <T extends LegacyRecipe<?>> Optional<T> getRecipe(LegacyRecipeType<T> legacyRecipeType,ResourceLocation resourceLocation) {
        if(legacyRecipeType == null || resourceLocation == null) return Optional.empty();
        Map<ResourceLocation,LegacyRecipe<?>> legacyRecipeMap=recipeTypeMap.get(legacyRecipeType);
        if(legacyRecipeMap == null) return Optional.empty();
        LegacyRecipe<?> legacyRecipe=legacyRecipeMap.get(resourceLocation);
        return legacyRecipe == null ? Optional.<T>empty() : Optional.of(setRecipe(legacyRecipe));
    }

    public <T extends LegacyRecipe<?>> List<T> getList(LegacyRecipeType<T> legacyRecipeType) {
        if(legacyRecipeType == null) return Collections.emptyList();
        Map<ResourceLocation,LegacyRecipe<?>> legacyRecipeMap=recipeTypeMap.get(legacyRecipeType);
        if(legacyRecipeMap == null || legacyRecipeMap.isEmpty()) return Collections.emptyList();
        List<T> list=new ArrayList<T>(legacyRecipeMap.size());
        for (LegacyRecipe<?> legacyRecipe : legacyRecipeMap.values()) list.add(setRecipe(legacyRecipe));
        return Collections.unmodifiableList(list);
    }

    public <C,T extends LegacyRecipe<C>> Optional<T> getRecipeT(LegacyRecipeType<T> legacyRecipeType,C input) {
        for (T legacyRecipe : getList(legacyRecipeType)) {
            if(legacyRecipe.matches(input)) return Optional.of(legacyRecipe);
        }
        return Optional.empty();
    }

    private LegacyRecipe<?> load(ResourceLocation resourceLocation,Map<ResourceLocation,LegacyRecipe<?>> loadRecipe,Map<LegacyRecipeType<?>,LinkedHashMap<ResourceLocation,LegacyRecipe<?>>> loadType) throws IOException {
        ResourceLocation location=recipeResource(resourceLocation);
        JsonObject jsonObject;
        try(Reader reader=LegacyResources.reader(LegacyPackType.DATA,resourceLocation)) {
            JsonElement jsonElement=new JsonParser().parse(reader);
            if(jsonElement == null || !jsonElement.isJsonObject()) throw new JsonSyntaxException("Recipe path must be a json object");
            jsonObject=jsonElement.getAsJsonObject();
        }
        ResourceLocation serializer=LegacyRecipeJson.getLocation(jsonObject,"type");
        LegacyRecipeSerializer<?> legacyRecipeSerializer=LegacyRecipeRegistry.getSerializer(serializer);
        if(legacyRecipeSerializer == null) throw new JsonSyntaxException("Unknown recipe serializer '" + ResourceLocations.getString(serializer) + "'");
        LegacyRecipe<?> legacyRecipe=legacyRecipeSerializer.fromJson(location,jsonObject);
        if(legacyRecipe == null) throw new JsonSyntaxException("Recipe serializer return null");
        if(legacyRecipe.getType() == null) throw new JsonSyntaxException("Recipe return no recipe type");
        if(loadRecipe.containsKey(location)) throw new JsonSyntaxException("Duplicate recipe id '" + ResourceLocations.getString(location) + "'");
        loadRecipe.put(location,legacyRecipe);
        LinkedHashMap<ResourceLocation,LegacyRecipe<?>> linkedHashMap=loadType.get(legacyRecipe.getType());
        if(linkedHashMap == null) {
            linkedHashMap=new LinkedHashMap<ResourceLocation,LegacyRecipe<?>>();
            loadType.put(legacyRecipe.getType(),linkedHashMap);
        }
        linkedHashMap.put(location,legacyRecipe);
        return legacyRecipe;
    }

    private static ResourceLocation recipeResource(ResourceLocation resourceLocation) {
        String string=ResourceLocations.path(resourceLocation);
        String prefix=RECIPE + "/";
        if(!string.startsWith(prefix) || !string.endsWith(JSON)) throw new IllegalArgumentException("Not recipe resource: " + ResourceLocations.getString(resourceLocation));
        String path=string.substring(prefix.length(),string.length() - JSON.length());
        if(path.isEmpty()) throw new IllegalArgumentException("Recipe path cannot be empty");
        return ResourceLocations.locations(ResourceLocations.namespace(resourceLocation),path);
    }

    @SuppressWarnings("unchecked")
    private void removeCraftingRecipe() {
        if(iRecipeList.isEmpty()) return;
        List<IRecipe> recipeList= CraftingManager.getInstance().getRecipeList();
        recipeList.removeAll(iRecipeList);
        iRecipeList.clear();
    }

    @SuppressWarnings("unchecked")
    private void setCraftingRecipe(List<IRecipe> list) {
        if(list.isEmpty()) return;
        List<IRecipe> recipeList=CraftingManager.getInstance().getRecipeList();
        recipeList.addAll(list);
        iRecipeList.addAll(list);
    }

    @SuppressWarnings("unchecked")
    private static <T extends LegacyRecipe<?>> T setRecipe(LegacyRecipe<?> legacyRecipe) {
        return (T) legacyRecipe;
    }
}
