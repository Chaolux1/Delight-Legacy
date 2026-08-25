/*
Copyright (c) Forge Development LLC and contributors
SPDX-License-Identifier: LGPL-2.1-only
Modified by Chaolux, 2026
 */
package net.chaolux.delightlegacy.legacy.registry;

import cpw.mods.fml.common.registry.GameRegistry;
import net.chaolux.delightlegacy.legacy.assets.ResourceLocations;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

public class LegacyRegistryObject<T extends Item> {
    private final String namespace;
    private final String string;
    private final Supplier<? extends T> supplier;
    private final boolean tab;
    private String model;
    private T value;
    LegacyRegistryObject(String namespace,String string,Supplier<? extends T> supplier,boolean tab) {
        this.namespace=namespace;
        this.string=string;
        this.supplier=supplier;
        this.tab=tab;
        this.model="item/" + string;
    }

    void register(CreativeTabs creativeTabs) {
        if(value != null) return;
        value=supplier.get();
        if(value == null) throw new IllegalStateException("Item supplier return empty for " + namespace + ":" + string);
        value.setUnlocalizedName(namespace + "." + string);
        value.setTextureName("minecraft:stick");
        if(tab) value.setCreativeTab(creativeTabs);
        GameRegistry.registerItem(value,string);
    }

    public T getValue() {
        if(value == null) throw new IllegalStateException("Item " + namespace + ":" + string +" has not been register");
        return value;
    }

    public boolean isTab() {
        return tab;
    }

    public String getString() {
        return string;
    }

    public String getNamespace() {
        return namespace;
    }

    public ResourceLocation getLocation() {
        return ResourceLocations.locations(namespace,string);
    }

    public ResourceLocation getModel() {
        return ResourceLocations.locations(namespace,model);
    }

    public String getTranslationKey() {
        return "item." + namespace + "." + string + ".name";
    }

    public LegacyRegistryObject<T> legacyRegistryObject(String string) {
        if(string == null || string.isEmpty()) throw new IllegalArgumentException("Model cannot be empty");
        this.model=string;
        return this;
    }
}
