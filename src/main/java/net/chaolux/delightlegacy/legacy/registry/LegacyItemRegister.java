package net.chaolux.delightlegacy.legacy.registry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Supplier;

public class LegacyItemRegister {
    private final String namespace;
    private final CreativeTabs creativeTabs;
    private final LinkedHashSet<LegacyRegistryObject<? extends Item>> linkedHashSet=new LinkedHashSet<LegacyRegistryObject<? extends Item>>();
    private boolean registered;
    public LegacyItemRegister(String namespace,CreativeTabs creativeTabs) {
        this.namespace=namespace;
        this.creativeTabs=creativeTabs;
    }

    public <T extends Item> LegacyRegistryObject<T> register(String string, Supplier<? extends T> supplier) {
        return add(string,supplier,true);
    }

    public <T extends Item> LegacyRegistryObject<T> registryObject(String string,Supplier<? extends T> supplier) {
        return add(string,supplier,false);
    }

    private <T extends Item> LegacyRegistryObject<T> add(String string,Supplier<? extends T> supplier,boolean value) {
        if(registered) throw new IllegalStateException("Item have already been registered");
        LegacyRegistryObject<T> legacyRegistryObject=new LegacyRegistryObject<T>(namespace,string,supplier,value);
        linkedHashSet.add(legacyRegistryObject);
        return legacyRegistryObject;
    }

    public void setRegistered() {
        if(registered) return;
        for (LegacyRegistryObject<? extends Item> legacyRegistryObject : linkedHashSet) legacyRegistryObject.register(creativeTabs);
        registered=true;
    }

    public Set<LegacyRegistryObject<? extends Item>> getObject() {
        return Collections.unmodifiableSet(linkedHashSet);
    }
}
