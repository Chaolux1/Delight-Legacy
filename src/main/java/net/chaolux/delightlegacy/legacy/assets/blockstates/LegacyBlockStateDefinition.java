/*
Copyright (c) Forge Development LLC and contributors
SPDX-License-Identifier: LGPL-2.1-only
Modified by Chaolux, 2026
 */
package net.chaolux.delightlegacy.legacy.assets.blockstates;

import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.concurrent.locks.Condition;

public class LegacyBlockStateDefinition {
    private final List<Variant> variantList;
    private final List<Part> partList;
    public LegacyBlockStateDefinition(List<Variant> variantList,List<Part> partList) {
        this.variantList= Collections.unmodifiableList(new ArrayList<Variant>(variantList));
        this.partList=Collections.unmodifiableList(new ArrayList<Part>(partList));
    }

    public List<Model> modelList(Map<String,String> stringMap,long seed) {
        List<Model> models=new ArrayList<Model>();
        Random random=new Random(seed);
        Variant variant=null;
        for(Variant currentVariant : variantList) {
            if(!currentVariant.condition.matches(stringMap)) continue;
            if(variant == null || currentVariant.condition.specificity() > variant.condition.specificity()) variant=currentVariant;
        }
        if(variant != null) {
            Model model=chose(variant.modelList,random);
            if(model != null) models.add(model);
        }
        for(Part part : partList) {
            if(!part.condition.matches(stringMap)) continue;
            Model model=chose(part.modelList,random);
            if(model != null) models.add(model);
        }
        return models;
    }

    public Set<ResourceLocation> resourceLocationSet() {
        Set<ResourceLocation> resourceLocation=new LinkedHashSet<ResourceLocation>();
        for (Variant variant : variantList) {
            for (Model model : variant.modelList) {
                resourceLocation.add(model.resourceLocation);
            }
        }
        for (Part part : partList) {
            for(Model model : part.modelList) {
                resourceLocation.add(model.resourceLocation);
            }
        }
        return resourceLocation;
    }

    private static Model chose(List<Model> modelList,Random random) {
        if(modelList == null || modelList.isEmpty()) return null;
        int index=0;
        for(Model model : modelList) {
            index += Math.max(1,model.index);
        }
        int value=random.nextInt(index);
        for (Model model : modelList) {
            value -= Math.max(1,model.index);
            if(value < 0) return model;
        }
        return modelList.get(modelList.size() - 1);
    }

    public static final class Variant {
        private final Condition condition;
        private final List<Model> modelList;
        public Variant(Condition condition,List<Model> modelList) {
            this.condition=condition;
            this.modelList=Collections.unmodifiableList(new ArrayList<Model>(modelList));
        }
    }

    public static final class Part {
        private final Condition condition;
        private final List<Model> modelList;
        public Part(Condition condition,List<Model> modelList) {
            this.condition=condition;
            this.modelList=Collections.unmodifiableList(new ArrayList<Model>(modelList));
        }
    }

    public static final class Model {
        private final ResourceLocation resourceLocation;
        private final int rotateX;
        private final int rotateY;
        boolean lock;
        int index;
        public Model(ResourceLocation resourceLocation,int rotateX,int rotateY,boolean lock,int index) {
            this.resourceLocation=resourceLocation;
            this.rotateX=rotateX;
            this.rotateY=rotateY;
            this.lock=lock;
            this.index=index;
        }

        public ResourceLocation getModel() {
            return resourceLocation;
        }

        public int getRotateX() {
            return rotateX;
        }

        public int getRotateY() {
            return rotateY;
        }

        public boolean isLock() {
            return lock;
        }

        public int getIndex() {
            return index;
        }
    }

    public interface Condition {
        Condition TRUE=new Condition() {
            @Override
            public boolean matches(Map<String, String> stringStringMap) {
                return true;
            }

            @Override
            public int specificity() {
                return 0;
            }
        };
        boolean matches(Map<String,String> stringStringMap);
        int specificity();
    }

    public static final class KeyValueCondition implements Condition {
        private final String string;
        private final Set<String> stringSet;
        public KeyValueCondition(String string,Set<String> stringSet) {
            this.string=string;
            this.stringSet=Collections.unmodifiableSet(new LinkedHashSet<String>(stringSet));
        }

        @Override
        public boolean matches(Map<String,String> stringStringMap) {
            String stringMap=stringStringMap.get(string);
            return stringMap != null && stringSet.contains(stringMap);
        }

        @Override
        public int specificity() {
            return 1;
        }
    }

    public static final class AndCondition implements Condition {
        private final List<Condition> conditionList;
        public AndCondition(List<Condition> conditionList) {
            this.conditionList=Collections.unmodifiableList(new ArrayList<Condition>(conditionList));
        }

        @Override
        public boolean matches(Map<String,String> stringStringMap) {
            for (Condition condition : conditionList) {
                if(!condition.matches(stringStringMap)) return false;
            }
            return true;
        }

        @Override
        public int specificity() {
            int index=0;
            for(Condition condition : conditionList) {
                index += condition.specificity();
            }
            return index;
        }
    }


    public static final class OrCondition implements Condition {
        private final List<Condition> conditionList;
        public OrCondition(List<Condition> conditionList) {
            this.conditionList=Collections.unmodifiableList(new ArrayList<Condition>(conditionList));
        }

        @Override
        public boolean matches(Map<String,String> stringStringMap) {
            for (Condition condition : conditionList) {
                if(condition.matches(stringStringMap)) return true;
            }
            return false;
        }

        @Override
        public int specificity() {
            int index=0;
            for (Condition condition : conditionList) {
                index=Math.max(index,condition.specificity());
            }
            return index;
        }
    }
}
