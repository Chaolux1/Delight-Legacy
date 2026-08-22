package net.chaolux.delightlegacy.registry;

import net.chaolux.delightlegacy.legacy.assets.ResourceLocations;
import net.chaolux.delightlegacy.legacy.assets.blockstates.LegacyDataStateMapper;
import net.chaolux.delightlegacy.legacy.assets.models.LegacyModelRegistry;

public class ModClientModels {
    public static void register() {
        LegacyModelRegistry.registerItem(ModItems.TEST, ResourceLocations.resourceLocations("item/test"));
    }
}
