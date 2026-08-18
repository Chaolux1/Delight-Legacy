package net.chaolux.delightlegacy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.item.crafting.CraftingManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = DelightLegacy.MOD_ID, name = DelightLegacy.NAME, version = Tags.VERSION)
public class DelightLegacy {

    public static final String MOD_ID="delightlegacy";
    public static final String NAME="Delight Legacy";
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void init(FMLInitializationEvent ev) {

    }
}
