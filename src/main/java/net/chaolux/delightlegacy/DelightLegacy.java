package net.chaolux.delightlegacy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.chaolux.delightlegacy.legacy.assets.LegacyResources;
import net.chaolux.delightlegacy.legacy.data.recipe.LegacyRecipeManager;
import net.chaolux.delightlegacy.registry.ModItems;
import net.chaolux.delightlegacy.registry.ModRecipeSerializers;
import net.chaolux.delightlegacy.registry.ModRecipeTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = DelightLegacy.MOD_ID, name = DelightLegacy.NAME, version = Tags.VERSION)
public class DelightLegacy {

    public static final String MOD_ID="delightlegacy";
    public static final String NAME="Delight Legacy";
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent fmlPreInitializationEvent) {
        LegacyResources.initialize(DelightLegacy.class);
        ModItems.register();
        ModRecipeTypes.resister();
        ModRecipeSerializers.register();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent ev) {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent fmlPostInitializationEvent) {
        LegacyRecipeManager.getInstance().reload();
    }
}
