/*
Copyright (c) Forge Development LLC and contributors
SPDX-License-Identifier: LGPL-2.1-only
Modified by Chaolux, 2026
 */
package net.chaolux.delightlegacy.legacy.common.item;

import net.minecraft.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class LegacyFoodProperties {
    private final int nutrition;
    private final float saturationModifier;
    private final boolean meat;
    private final boolean fast;
    private final boolean alwaysEat;
    private final List<Effect> effectList;
    private LegacyFoodProperties(Builder builder) {
        this.nutrition=builder.nutrition;
        this.saturationModifier=builder.saturationModifier;
        this.meat=builder.meat;
        this.fast=builder.fast;
        this.alwaysEat=builder.alwaysEat;
        this.effectList= Collections.unmodifiableList(new ArrayList<Effect>(builder.effectList));
    }

    public int getNutrition() {
        return nutrition;
    }

    public float getSaturationModifier() {
        return saturationModifier;
    }

    public boolean isMeat() {
        return meat;
    }

    public boolean isFast() {
        return fast;
    }

    public boolean isAlwaysEat() {
        return alwaysEat;
    }

    public List<Effect> getEffectList() {
        return effectList;
    }

    public static final class Effect {
        private final Supplier<PotionEffect> potionEffectSupplier;
        private final float value;
        private Effect(Supplier<PotionEffect> potionEffectSupplier,float value) {
            this.potionEffectSupplier=potionEffectSupplier;
            this.value=value;
        }

        public PotionEffect potionEffect() {
            return potionEffectSupplier.get();
        }

        public float getValue() {
            return value;
        }
    }

    public static final class Builder {
        private int nutrition;
        private float saturationModifier;
        private boolean meat;
        private boolean fast;
        private boolean alwaysEat;
        private final List<Effect> effectList=new ArrayList<Effect>();
        public Builder nutrition(int nutrition) {
            this.nutrition=nutrition;
            return this;
        }

        public Builder saturationMod(float saturationModifier) {
            this.saturationModifier=saturationModifier;
            return this;
        }

        public Builder meat() {
            this.meat=true;
            return this;
        }

        public Builder fast() {
            this.fast=true;
            return this;
        }

        public Builder alwaysEat() {
            this.alwaysEat=true;
            return this;
        }

        public Builder effect(Supplier<PotionEffect> potionEffectSupplier,float value) {
            effectList.add(new Effect(potionEffectSupplier,value));
            return this;
        }

        public LegacyFoodProperties build() {
            return new LegacyFoodProperties(this);
        }
    }
}
