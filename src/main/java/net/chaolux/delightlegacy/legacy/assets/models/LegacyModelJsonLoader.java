/*
Copyright (c) Forge Development LLC and contributors
SPDX-License-Identifier: LGPL-2.1-only
Modified by Chaolux, 2026
 */
package net.chaolux.delightlegacy.legacy.assets.models;

import com.google.gson.*;
import net.chaolux.delightlegacy.legacy.assets.LegacyPackType;
import net.chaolux.delightlegacy.legacy.assets.LegacyResources;
import net.chaolux.delightlegacy.legacy.assets.ResourceLocations;
import net.chaolux.delightlegacy.legacy.assets.ResourcePaths;
import net.chaolux.delightlegacy.legacy.assets.blockstates.LegacyBlockRenderer;
import net.chaolux.delightlegacy.legacy.assets.blockstates.LegacyBlockStateDefinition;
import net.chaolux.delightlegacy.legacy.assets.blockstates.LegacyBlockStateMapper;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class LegacyModelJsonLoader {
    public LegacyModelDefinition legacyModelDefinition(ResourceLocation resourceLocation) throws IOException {
        ResourceLocation location= ResourcePaths.model(resourceLocation);
        JsonObject jsonObject=read(location);
        return parseModel(resourceLocation,jsonObject);
    }

    public LegacyBlockStateDefinition legacyBlockStateDefinition(ResourceLocation resourceLocation) throws IOException {
        ResourceLocation location=ResourcePaths.blockState(resourceLocation);
        JsonObject jsonObject=read(location);
        return parseBlockState(jsonObject);
    }

    private JsonObject read(ResourceLocation resourceLocation) throws IOException {
        try(Reader reader= LegacyResources.reader(LegacyPackType.ASSETS,resourceLocation)) {
            JsonElement jsonElement=new JsonParser().parse(reader);
            if(jsonElement == null || !jsonElement.isJsonObject()) throw new JsonSyntaxException("Expect JSON in " + ResourceLocations.getString(resourceLocation));
            return jsonElement.getAsJsonObject();
        }
    }

    private LegacyModelDefinition parseModel(ResourceLocation resourceLocation,JsonObject jsonObject) {
        ResourceLocation parent=null;
        if(jsonObject.has("parent")) parent=ResourceLocations.parse(getString(jsonObject,"parent"));
        Boolean occlusion=jsonObject.has("ambientocclusion") ? getBoolean(jsonObject,"ambientocclusion") : null;
        String GUI=jsonObject.has("gui_light") ? getString(jsonObject,"gui_light") : null;
        String string=jsonObject.has("loader") ? getString(jsonObject,"loader") : null;
        Map<String,String> texture=new LinkedHashMap<String,String>();
        if(jsonObject.has("textures")) {
            JsonObject object=getObject(jsonObject,"textures");
            for (Map.Entry<String,JsonElement> entry : object.entrySet()) {
                if(!entry.getValue().isJsonPrimitive() || !entry.getValue().getAsJsonPrimitive().isString()) throw new JsonSyntaxException("Model texture '" + entry.getKey() + "'");
                texture.put(entry.getKey(),entry.getValue().getAsString());
            }
        }
        boolean define=jsonObject.has("elements");
        List<LegacyModelDefinition.Element> elementList=new ArrayList<LegacyModelDefinition.Element>();
        if(define) {
            JsonArray jsonArray=getArray(jsonObject,"elements");
            for (JsonElement jsonElement : jsonArray) {
                if(!jsonElement.isJsonObject()) throw new JsonSyntaxException("Model element must be a object");
                elementList.add(parseElement(jsonElement.getAsJsonObject()));
            }
        }
        Map<String,LegacyModelDefinition.Transform> stringTransformMap=new LinkedHashMap<String,LegacyModelDefinition.Transform>();
        if(jsonObject.has("display")) {
            JsonObject object=getObject(jsonObject,"display");
            for(Map.Entry<String,JsonElement> entry : object.entrySet()) {
                if(!entry.getValue().isJsonObject()) throw new JsonSyntaxException("Display transform '" + entry.getKey() + "' must be a object");
                stringTransformMap.put(entry.getKey(),parseTransform(entry.getValue().getAsJsonObject()));
            }
        }
        List<LegacyModelDefinition.Override> overrideList=new ArrayList<LegacyModelDefinition.Override>();
        if(jsonObject.has("overrides")) {
            JsonArray jsonArray=getArray(jsonObject,"overrides");
            for(JsonElement jsonElement : jsonArray) {
                if(!jsonElement.isJsonObject()) throw new JsonSyntaxException("Item model override must be a object");
                overrideList.add(parseOverride(jsonElement.getAsJsonObject()));
            }
        }
        return new LegacyModelDefinition(resourceLocation,parent,occlusion,GUI,texture,elementList,define,stringTransformMap,overrideList,false,false,string);
    }

    private LegacyModelDefinition.Element parseElement(JsonObject jsonObject) {
        float[] from=getVector(jsonObject,"from");
        float[] to=getVector(jsonObject,"to");
        for(int index=0;index < 3;index++) {
            if(from[index] < -16.0f || from[index] > 32.0f || to[index] < -16.0f || to[index] > 32.0f) throw new JsonSyntaxException("Model element must be between -16 and 32");
        }
        LegacyModelDefinition.Rotation rotation=null;
        if(jsonObject.has("rotation")) {
            JsonObject object=getObject(jsonObject,"rotation");
            float[] origin=getVector(object,"origin");
            LegacyModelDefinition.Axis axis;
            try {
                axis=LegacyModelDefinition.Axis.name(getString(object,"axis"));
            } catch (IllegalArgumentException exception) {
                throw new JsonSyntaxException("Invalid element rotate axis",exception);
            }
            float angle=getFloat(object,"angle");
            if(angle != 0.0f && angle != 22.5f && angle != -22.5f && angle != 45.0f && angle != -45.0f) throw new JsonSyntaxException("Element rotate angle must be 0,22.5,45");
            boolean scale=getBoolean(object,"rescale",false);
            rotation=new LegacyModelDefinition.Rotation(origin,axis,angle,scale);
        }
        boolean shade=getBoolean(jsonObject,"shade",true);
        JsonObject object=getObject(jsonObject,"faces");
        Map<LegacyDirection,LegacyModelDefinition.Face> faceMap=new EnumMap<LegacyDirection,LegacyModelDefinition.Face>(LegacyDirection.class);
        for(Map.Entry<String,JsonElement> entry : object.entrySet()) {
            LegacyDirection legacyDirection;
            try {
                legacyDirection=LegacyDirection.name(entry.getKey());
            } catch (IllegalArgumentException exception) {
                throw new JsonSyntaxException("Invalid direction '" + entry.getKey() + "'",exception);
            }
            if(!entry.getValue().isJsonObject()) throw new JsonSyntaxException("Model must be a object");
            faceMap.put(legacyDirection,parseFace(entry.getValue().getAsJsonObject()));
        }
        return new LegacyModelDefinition.Element(from,to,rotation,shade,faceMap);
    }

    private LegacyModelDefinition.Face parseFace(JsonObject jsonObject) {
        float[] uv=jsonObject.has("uv") ? getCurrentVector(jsonObject,"uv") : null;
        String string=getString(jsonObject,"texture");
        LegacyDirection legacyDirection=null;
        if(jsonObject.has("cullface")) {
            try {
                legacyDirection=LegacyDirection.name(getString(jsonObject,"cullface"));
            } catch (IllegalArgumentException exception) {
                throw new JsonSyntaxException("Invalid cullface",exception);
            }
        }
        int rotate=getInt(jsonObject,"rotation",0);
        if(rotate != 0 && rotate != 90 && rotate != 180 && rotate != 270) throw new JsonSyntaxException("Rotation must be 9,90,180,270");
        int tint=getInt(jsonObject,"tintindex",-1);
        return new LegacyModelDefinition.Face(uv,string,legacyDirection,rotate,tint);
    }

    private LegacyModelDefinition.Transform parseTransform(JsonObject jsonObject) {
        float[] rotate=jsonObject.has("rotation") ? getVector(jsonObject,"rotation") : new float[] {0.0f,0.0f,0.0f};
        float[] translation=jsonObject.has("translation") ? getVector(jsonObject,"translation") : new float[] {0.0f,0.0f,0.0f};
        float[] scale=jsonObject.has("scale") ? getVector(jsonObject,"scale") : new float[] {1.0f,1.0f,1.0f};
        return new LegacyModelDefinition.Transform(rotate,translation,scale);
    }

    private LegacyModelDefinition.Override parseOverride(JsonObject jsonObject) {
        JsonObject object=getObject(jsonObject,"predicate");
        Map<ResourceLocation,Float> resourceLocationFloatMap=new LinkedHashMap<ResourceLocation,Float>();
        for(Map.Entry<String,JsonElement> entry : object.entrySet()) {
            if(!entry.getValue().isJsonPrimitive() || !entry.getValue().getAsJsonPrimitive().isNumber()) throw new JsonSyntaxException("Item predicate value must be empty");
            resourceLocationFloatMap.put(ResourceLocations.parse(entry.getKey()),entry.getValue().getAsFloat());
        }
        ResourceLocation resourceLocation=ResourceLocations.parse(getString(jsonObject,"model"));
        return new LegacyModelDefinition.Override(resourceLocationFloatMap,resourceLocation);
    }

    private LegacyBlockStateDefinition parseBlockState(JsonObject jsonObject) {
        List<LegacyBlockStateDefinition.Variant> variantList=new ArrayList<LegacyBlockStateDefinition.Variant>();
        List<LegacyBlockStateDefinition.Part> partList=new ArrayList<LegacyBlockStateDefinition.Part>();
        if(jsonObject.has("variants")) {
            JsonObject object=getObject(jsonObject,"variants");
            for (Map.Entry<String,JsonElement> entry : object.entrySet()) {
                variantList.add(new LegacyBlockStateDefinition.Variant(parseVariantCondition(entry.getKey()),parseVariantModels(entry.getValue())));
            }
        }
        if(jsonObject.has("multipart")) {
            JsonArray jsonArray=getArray(jsonObject,"multipart");
            for (JsonElement jsonElement : jsonArray) {
                if(!jsonElement.isJsonObject()) throw new JsonSyntaxException("Multipart blockstate entry must be a object");
                JsonObject object=jsonElement.getAsJsonObject();
                LegacyBlockStateDefinition.Condition condition=object.has("when") ? parseWhen(object.get("when")) : LegacyBlockStateDefinition.Condition.TRUE;
                if(!object.has("apply")) throw new JsonSyntaxException("Multipart entry is missing apply");
                partList.add(new LegacyBlockStateDefinition.Part(condition,parseVariantModels(object.get("apply"))));
            }
        }
        if(variantList.isEmpty() && partList.isEmpty()) throw new JsonSyntaxException("Blockstate must contain variant");
        return new LegacyBlockStateDefinition(variantList,partList);
    }

    private LegacyBlockStateDefinition.Condition parseVariantCondition(String string) {
        if(string == null || string.trim().isEmpty()) return LegacyBlockStateDefinition.Condition.TRUE;
        List<LegacyBlockStateDefinition.Condition> conditionList=new ArrayList<LegacyBlockStateDefinition.Condition>();
        String[] strings=string.split(",");
        for(String variant : strings) {
            String[] variants=variant.split("=",2);
            if(variants.length != 2) throw new JsonSyntaxException("Invalid blockstate variant '" + variant + "'");
            Set<String> stringSet=new LinkedHashSet<String>();
            String[] stringMap=variants[1].trim().split("\\|");
            for(String value : stringMap) {
                stringSet.add(value.trim());
            }
            conditionList.add(new LegacyBlockStateDefinition.KeyValueCondition(variants[0].trim(),stringSet));
        }
        if(conditionList.size() == 1) return conditionList.get(0);
        return new LegacyBlockStateDefinition.AndCondition(conditionList);
    }

    private LegacyBlockStateDefinition.Condition parseWhen(JsonElement jsonElement) {
        if(jsonElement == null || jsonElement.isJsonNull()) return LegacyBlockStateDefinition.Condition.TRUE;
        if(!jsonElement.isJsonObject()) throw new JsonSyntaxException("Multipart when must be a object");
        JsonObject jsonObject=jsonElement.getAsJsonObject();
        List<LegacyBlockStateDefinition.Condition> conditionList=new ArrayList<LegacyBlockStateDefinition.Condition>();
        for (Map.Entry<String,JsonElement> entry : jsonObject.entrySet()) {
            String string=entry.getKey();
            if("OR".equals(string) || "AND".equals(string)) {
                if(!entry.getValue().isJsonArray()) throw new JsonSyntaxException(string + "condition must be a object");
                List<LegacyBlockStateDefinition.Condition> list=new ArrayList<LegacyBlockStateDefinition.Condition>();
                for (JsonElement element : entry.getValue().getAsJsonArray()) {
                    list.add(parseWhen(element));
                }
                conditionList.add("OR".equals(string) ? new LegacyBlockStateDefinition.OrCondition(list) : new LegacyBlockStateDefinition.AndCondition(list));
                continue;
            }
            if(!entry.getValue().isJsonPrimitive() || !entry.getValue().getAsJsonPrimitive().isString()) throw new JsonSyntaxException("Multipart condition must be a string");
            Set<String> stringSet=new LinkedHashSet<String>();
            String[] strings=entry.getValue().getAsString().split("\\|");
            for(String value : strings) {
                stringSet.add(value);
            }
            conditionList.add(new LegacyBlockStateDefinition.KeyValueCondition(string,stringSet));
        }
        if(conditionList.isEmpty()) return LegacyBlockStateDefinition.Condition.TRUE;
        if(conditionList.size() == 1) return conditionList.get(0);
        return new LegacyBlockStateDefinition.AndCondition(conditionList);
    }

    private List<LegacyBlockStateDefinition.Model> parseVariantModels(JsonElement jsonElement) {
        List<LegacyBlockStateDefinition.Model> modelList=new ArrayList<LegacyBlockStateDefinition.Model>();
        if(jsonElement.isJsonArray()) {
            for (JsonElement element : jsonElement.getAsJsonArray()) {
                if(!element.isJsonObject()) throw new JsonSyntaxException("Model must be a object");
                modelList.add(parseVariantModel(element.getAsJsonObject()));
            }
        } else if(jsonElement.isJsonObject()) {
            modelList.add(parseVariantModel(jsonElement.getAsJsonObject()));
        } else {
            throw new JsonSyntaxException("Model must be a object");
        }
        if(modelList.isEmpty()) throw new JsonSyntaxException("Model list cannot be empty");
        return modelList;
    }

    private LegacyBlockStateDefinition.Model parseVariantModel(JsonObject jsonObject) {
        ResourceLocation resourceLocation=ResourceLocations.parse(getString(jsonObject,"model"));
        int x=getInt(jsonObject,"x",0);
        int y=getInt(jsonObject,"y",0);
        if(x % 90 != 0 || y % 90 != 0) throw new JsonSyntaxException("Blockstate x and y rotate must be of 90");
        boolean lock=getBoolean(jsonObject,"uvlock",false);
        int weight=getInt(jsonObject,"weight",1);
        if(weight <= 0) throw new JsonSyntaxException("Model weight must be +");
        return new LegacyBlockStateDefinition.Model(resourceLocation,x,y,lock,weight);
    }

    private static JsonObject getObject(JsonObject jsonObject,String string) {
        JsonElement jsonElement=jsonObject.get(string);
        if(jsonElement == null || !jsonElement.isJsonObject()) throw new JsonSyntaxException("Expect '" + string + "'");
        return jsonElement.getAsJsonObject();
    }

    private static JsonArray getArray(JsonObject jsonObject,String string) {
        JsonElement jsonElement=jsonObject.get(string);
        if(jsonElement == null || !jsonElement.isJsonArray()) throw new JsonSyntaxException("Expect '" + string + "'");
        return jsonElement.getAsJsonArray();
    }

    private static String getString(JsonObject jsonObject,String string) {
        JsonElement jsonElement=jsonObject.get(string);
        if(jsonElement == null || !jsonElement.isJsonPrimitive() || !jsonElement.getAsJsonPrimitive().isString()) throw new JsonSyntaxException("Expect '" + string + "'");
        return jsonElement.getAsString();
    }

    private static boolean getBoolean(JsonObject jsonObject,String string) {
        JsonElement jsonElement=jsonObject.get(string);
        if(jsonElement == null || !jsonElement.isJsonPrimitive() || !jsonElement.getAsJsonPrimitive().isBoolean()) throw new JsonSyntaxException("Expect '" + string + "'");
        return jsonElement.getAsBoolean();
    }

    private static boolean getBoolean(JsonObject jsonObject,String string,boolean value) {
        return jsonObject.has(string) ? getBoolean(jsonObject,string) : value;
    }

    private static int getInt(JsonObject jsonObject,String string,int value) {
        if(!jsonObject.has(string)) return value;
        JsonElement jsonElement=jsonObject.get(string);
        if(!jsonElement.isJsonPrimitive() || !jsonElement.getAsJsonPrimitive().isNumber()) throw new JsonSyntaxException("Expect '" + string + "'");
        return jsonElement.getAsInt();
    }

    private static float getFloat(JsonObject jsonObject,String string) {
        JsonElement jsonElement=jsonObject.get(string);
        if(jsonElement == null || !jsonElement.isJsonPrimitive() || !jsonElement.getAsJsonPrimitive().isNumber()) throw new JsonSyntaxException("Expect '" + string + "'");
        return jsonElement.getAsFloat();
    }

    private static float[] getVector(JsonObject jsonObject,String string) {
        JsonArray jsonArray=getArray(jsonObject,string);
        if(jsonArray.size() != 3) throw new JsonSyntaxException("'" + string + "' must be contain 3 number");
        return new float[] {jsonArray.get(0).getAsFloat(),jsonArray.get(1).getAsFloat(),jsonArray.get(2).getAsFloat()};
    }

    private static float[] getCurrentVector(JsonObject jsonObject,String string) {
        JsonArray jsonArray=getArray(jsonObject,string);
        if(jsonArray.size() != 4) throw new JsonSyntaxException("'" + string + "' must be contain 4 number");
        return new float[] {jsonArray.get(0).getAsFloat(),jsonArray.get(1).getAsFloat(),jsonArray.get(2).getAsFloat(),jsonArray.get(3).getAsFloat()};
    }
}
