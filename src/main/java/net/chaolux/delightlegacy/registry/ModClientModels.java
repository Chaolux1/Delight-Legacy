package net.chaolux.delightlegacy.registry;

import net.chaolux.delightlegacy.legacy.assets.ResourceLocations;
import net.chaolux.delightlegacy.legacy.assets.blockstates.LegacyDataStateMapper;
import net.chaolux.delightlegacy.legacy.assets.models.LegacyModelRegistry;
import net.chaolux.delightlegacy.legacy.registry.LegacyRegistryObject;
import net.minecraft.item.Item;

public class ModClientModels {
    public static void register() {
        for (LegacyRegistryObject<? extends Item> legacyRegistryObject : ModItems.ITEMS.getObject()) {
            LegacyModelRegistry.registerItem(legacyRegistryObject.getValue(),legacyRegistryObject.getModel());
        }
    }
}
