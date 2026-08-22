/*
Copyright (c) Forge Development LLC and contributors
SPDX-License-Identifier: LGPL-2.1-only
Modified by Chaolux, 2026
 */
package net.chaolux.delightlegacy.legacy.assets.models;

import javafx.scene.chart.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.Face;

import javax.xml.bind.Element;
import javax.xml.crypto.dsig.Transform;
import java.util.*;

public class LegacyModelDefinition {
    private final ResourceLocation id;
    private final ResourceLocation location;
    private final Boolean occlusion;
    private String GUI;
    private final Map<String,String> texture;
    private final List<Element> elementList;
    private final boolean define;
    private final Map<String, Transform> stringTransformMap;
    private final List<Override> overrideList;
    private final boolean generated;
    private final boolean handheld;
    private final String string;
    public LegacyModelDefinition(ResourceLocation id,ResourceLocation location,Boolean occlusion,String gui,Map<String,String> texture,List<Element> elementList,boolean define,Map<String, Transform> stringTransformMap,List<Override> overrideList,boolean generated,boolean handheld,String string) {
        this.id =id;
        this.location=location;
        this.occlusion=occlusion;
        this.GUI =gui;
        this.texture = Collections.unmodifiableMap(new LinkedHashMap<String,String>(texture));
        this.elementList=Collections.unmodifiableList(new ArrayList<Element>(elementList));
        this.define=define;
        this.stringTransformMap=Collections.unmodifiableMap(new LinkedHashMap<String,Transform>(stringTransformMap));
        this.overrideList=Collections.unmodifiableList(new ArrayList<Override>(overrideList));
        this.generated=generated;
        this.handheld=handheld;
        this.string=string;
    }

    public ResourceLocation getId() {
        return id;
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public Boolean getOcclusion() {
        return occlusion;
    }

    public String getGUI() {
        return GUI;
    }

    public Map<String,String> getTexture() {
        return texture;
    }

    public List<Element> getElement() {
        return elementList;
    }

    public boolean isDefine() {
        return define;
    }

    public Map<String,Transform> getTransform() {
        return stringTransformMap;
    }

    public List<Override> getOverride() {
        return overrideList;
    }

    public boolean isGenerated() {
        return generated;
    }

    public boolean isHandheld() {
        return handheld;
    }

    public String getString() {
        return string;
    }

    public static final class Element {
        private final float[] from;
        private final float[] to;
        private final Rotation rotation;
        private final boolean shade;
        private final Map<LegacyDirection,Face> legacyDirectionFaceMap;
        public Element(float[] from, float[] to, Rotation rotation, boolean shade, Map<LegacyDirection, Face> legacyDirectionFaceMap) {
            this.from=copy(from);
            this.to=copy(to);
            this.rotation=rotation;
            this.shade=shade;
            this.legacyDirectionFaceMap=Collections.unmodifiableMap(new EnumMap<LegacyDirection,Face>(legacyDirectionFaceMap));
        }

        public float[] getFrom() {
            return copy(from);
        }

        public float[] getTo() {
            return copy(to);
        }

        public Rotation getRotation() {
            return rotation;
        }

        public boolean isShade() {
            return shade;
        }

        public Map<LegacyDirection,Face> getFace() {
            return legacyDirectionFaceMap;
        }
    }

    public static final class Face {
        private final float[] uv;
        private final String string;
        private final LegacyDirection legacyDirection;
        private final int rotate;
        private final int tint;
        public Face(float[] uv,String string,LegacyDirection legacyDirection,int rotate,int tint) {
            this.uv=uv == null ? null : copyUV(uv);
            this.string=string;
            this.legacyDirection=legacyDirection;
            this.rotate=rotate;
            this.tint=tint;
        }

        public float[] getUV() {
            return uv == null ? null : copyUV(uv);
        }

        public String getString() {
            return string;
        }

        public LegacyDirection getLegacyDirection() {
            return legacyDirection;
        }

        public int getRotate() {
            return rotate;
        }

        public int getTint() {
            return tint;
        }
    }

    public static final class Rotation {
        private final float[] rotate;
        private final Axis axis;
        private final float angle;
        private final boolean scale;
        public Rotation(float[] rotate,Axis axis,float angle,boolean scale) {
            this.rotate=copy(rotate);
            this.axis=axis;
            this.angle=angle;
            this.scale=scale;
        }

        public float[] getRotate() {
            return copy(rotate);
        }

        public Axis getAxis() {
            return axis;
        }

        public float getAngle() {
            return angle;
        }

        public boolean isScale() {
            return scale;
        }
    }

    public static final class Transform {
        public static final Transform TRANSFORM=new Transform(new float[] {0.0f,0.0f,0.0f},new float[] {0.0f,0.0f,0.0f},new float[] {1.0f,1.0f,1.0f});
        private final float[] rotate;
        private final float[] transform;
        private final float[] scale;
        public Transform(float[] rotate,float[] transform,float[] scale) {
            this.rotate=copy(rotate);
            this.transform=copy(transform);
            this.scale=copy(scale);
        }

        public float[] getRotate() {
            return copy(rotate);
        }

        public float[] getTransform() {
            return copy(transform);
        }

        public float[] getScale() {
            return copy(scale);
        }
    }

    public static final class Override {
        private final Map<ResourceLocation,Float> predicate;
        private final ResourceLocation overrides;
        public Override(Map<ResourceLocation,Float> predicate,ResourceLocation overrides) {
            this.predicate=Collections.unmodifiableMap(new LinkedHashMap<ResourceLocation,Float>(predicate));
            this.overrides=overrides;
        }

        public Map<ResourceLocation,Float> getPredicate() {
            return predicate;
        }

        public ResourceLocation getOverrides() {
            return overrides;
        }
    }

    public enum Axis {
        x,y,z;
        public static Axis name(String string) {
            if(string == null) throw new IllegalArgumentException("Object rotation axis cannot be null");
            return valueOf(string.toLowerCase());
        }
    }

    private static float[] copy(float[] value) {
        return new float[] {
            value[0],value[1],value[2]
        };
    }

    private static float[] copyUV(float[] value) {
        return new float[] {
            value[0],value[1],value[2],value[3]
        };
    }
}
