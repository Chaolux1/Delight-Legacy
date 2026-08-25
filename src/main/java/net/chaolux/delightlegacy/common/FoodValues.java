package net.chaolux.delightlegacy.common;

import com.google.common.collect.ImmutableMap;
import net.chaolux.delightlegacy.legacy.common.item.LegacyFoodProperties;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.Map;

public class FoodValues {
    public static final int BRIEF_DURATION = 600;
    public static final int SHORT_DURATION = 1200;
    public static final int MEDIUM_DURATION = 3600;
    public static final int LONG_DURATION = 6000;
    public static final LegacyFoodProperties CABBAGE = (new LegacyFoodProperties.Builder()).nutrition(2).saturationMod(0.4F).build();
    public static final LegacyFoodProperties TOMATO = (new LegacyFoodProperties.Builder()).nutrition(1).saturationMod(0.3F).build();
    public static final LegacyFoodProperties ONION = (new LegacyFoodProperties.Builder()).nutrition(2).saturationMod(0.4F).build();
    public static final LegacyFoodProperties APPLE_CIDER = (new LegacyFoodProperties.Builder()).alwaysEat().effect(() -> new PotionEffect(Potion.field_76444_x.id, 1200, 0), 1.0F).build();
    public static final LegacyFoodProperties FRIED_EGG = (new LegacyFoodProperties.Builder()).nutrition(4).saturationMod(0.4F).build();
    public static final LegacyFoodProperties TOMATO_SAUCE = (new LegacyFoodProperties.Builder()).nutrition(4).saturationMod(0.4F).build();
    public static final LegacyFoodProperties WHEAT_DOUGH = (new LegacyFoodProperties.Builder()).nutrition(2).saturationMod(0.3F).effect(() -> new PotionEffect(Potion.hunger.id, 600, 0), 0.3F).build();
    public static final LegacyFoodProperties RAW_PASTA = (new LegacyFoodProperties.Builder()).nutrition(2).saturationMod(0.3F).effect(() -> new PotionEffect(Potion.hunger.id, 600, 0), 0.3F).build();
    public static final LegacyFoodProperties PIE_CRUST = (new LegacyFoodProperties.Builder()).nutrition(2).saturationMod(0.2F).build();
    public static final LegacyFoodProperties PUMPKIN_SLICE = (new LegacyFoodProperties.Builder()).nutrition(3).saturationMod(0.3F).build();
    public static final LegacyFoodProperties CABBAGE_LEAF = (new LegacyFoodProperties.Builder()).nutrition(1).saturationMod(0.4F).fast().build();
    public static final LegacyFoodProperties MINCED_BEEF = (new LegacyFoodProperties.Builder()).nutrition(2).saturationMod(0.3F).meat().fast().build();
    public static final LegacyFoodProperties BEEF_PATTY = (new LegacyFoodProperties.Builder()).nutrition(4).saturationMod(0.8F).meat().fast().build();
    public static final LegacyFoodProperties CHICKEN_CUTS = (new LegacyFoodProperties.Builder()).nutrition(1).saturationMod(0.3F).effect(() -> new PotionEffect(Potion.hunger.id, 600, 0), 0.3F).meat().fast().build();
    public static final LegacyFoodProperties COOKED_CHICKEN_CUTS = (new LegacyFoodProperties.Builder()).nutrition(3).saturationMod(0.6F).meat().fast().build();
    public static final LegacyFoodProperties BACON = (new LegacyFoodProperties.Builder()).nutrition(2).saturationMod(0.3F).meat().fast().build();
    public static final LegacyFoodProperties COOKED_BACON = (new LegacyFoodProperties.Builder()).nutrition(4).saturationMod(0.8F).meat().fast().build();
    public static final LegacyFoodProperties COD_SLICE = (new LegacyFoodProperties.Builder()).nutrition(1).saturationMod(0.1F).fast().build();
    public static final LegacyFoodProperties COOKED_COD_SLICE = (new LegacyFoodProperties.Builder()).nutrition(3).saturationMod(0.5F).fast().build();
    public static final LegacyFoodProperties SALMON_SLICE = (new LegacyFoodProperties.Builder()).nutrition(1).saturationMod(0.1F).fast().build();
    public static final LegacyFoodProperties COOKED_SALMON_SLICE = (new LegacyFoodProperties.Builder()).nutrition(3).saturationMod(0.8F).fast().build();
    public static final LegacyFoodProperties MUTTON_CHOP = (new LegacyFoodProperties.Builder()).nutrition(1).saturationMod(0.3F).meat().fast().build();
    public static final LegacyFoodProperties COOKED_MUTTON_CHOP = (new LegacyFoodProperties.Builder()).nutrition(3).saturationMod(0.8F).meat().fast().build();
    public static final LegacyFoodProperties HAM = (new LegacyFoodProperties.Builder()).nutrition(5).saturationMod(0.3F).meat().build();
    public static final LegacyFoodProperties SMOKED_HAM = (new LegacyFoodProperties.Builder()).nutrition(10).saturationMod(0.8F).meat().build();
    public static final LegacyFoodProperties POPSICLE = (new LegacyFoodProperties.Builder()).nutrition(3).saturationMod(0.2F).fast().alwaysEat().build();
    public static final LegacyFoodProperties COOKIES = (new LegacyFoodProperties.Builder()).nutrition(2).saturationMod(0.1F).fast().build();
    public static final LegacyFoodProperties CAKE_SLICE = (new LegacyFoodProperties.Builder()).nutrition(2).saturationMod(0.1F).fast().effect(() -> new PotionEffect(Potion.moveSpeed.id, 400, 0, false), 1.0F).build();
    public static final LegacyFoodProperties PIE_SLICE = (new LegacyFoodProperties.Builder()).nutrition(3).saturationMod(0.3F).fast().effect(() -> new PotionEffect(Potion.moveSpeed.id, 600, 0, false), 1.0F).build();
    public static final LegacyFoodProperties FRUIT_SALAD = (new LegacyFoodProperties.Builder()).nutrition(6).saturationMod(0.6F).effect(() -> new PotionEffect(Potion.regeneration.id, 100, 0), 1.0F).build();
    public static final LegacyFoodProperties GLOW_BERRY_CUSTARD = (new LegacyFoodProperties.Builder()).nutrition(7).saturationMod(0.6F).alwaysEat().build();
    public static final LegacyFoodProperties MIXED_SALAD = (new LegacyFoodProperties.Builder()).nutrition(6).saturationMod(0.6F).effect(() -> new PotionEffect(Potion.regeneration.id, 100, 0), 1.0F).build();
    public static final LegacyFoodProperties NETHER_SALAD = (new LegacyFoodProperties.Builder()).nutrition(5).saturationMod(0.4F).effect(() -> new PotionEffect(Potion.confusion.id, 240, 0), 0.3F).build();
    public static final LegacyFoodProperties BARBECUE_STICK = (new LegacyFoodProperties.Builder()).nutrition(8).saturationMod(0.9F).build();
    public static final LegacyFoodProperties EGG_SANDWICH = (new LegacyFoodProperties.Builder()).nutrition(8).saturationMod(0.8F).build();
    public static final LegacyFoodProperties CHICKEN_SANDWICH = (new LegacyFoodProperties.Builder()).nutrition(10).saturationMod(0.8F).build();
    public static final LegacyFoodProperties HAMBURGER = (new LegacyFoodProperties.Builder()).nutrition(11).saturationMod(0.8F).build();
    public static final LegacyFoodProperties BACON_SANDWICH = (new LegacyFoodProperties.Builder()).nutrition(10).saturationMod(0.8F).build();
    public static final LegacyFoodProperties MUTTON_WRAP = (new LegacyFoodProperties.Builder()).nutrition(10).saturationMod(0.8F).build();
    public static final LegacyFoodProperties DUMPLINGS = (new LegacyFoodProperties.Builder()).nutrition(8).saturationMod(0.8F).build();
    public static final LegacyFoodProperties STUFFED_POTATO = (new LegacyFoodProperties.Builder()).nutrition(10).saturationMod(0.7F).build();
    public static final LegacyFoodProperties CABBAGE_ROLLS = (new LegacyFoodProperties.Builder()).nutrition(5).saturationMod(0.5F).build();
    public static final LegacyFoodProperties SALMON_ROLL = (new LegacyFoodProperties.Builder()).nutrition(7).saturationMod(0.6F).build();
    public static final LegacyFoodProperties COD_ROLL = (new LegacyFoodProperties.Builder()).nutrition(7).saturationMod(0.6F).build();
    public static final LegacyFoodProperties KELP_ROLL = (new LegacyFoodProperties.Builder()).nutrition(12).saturationMod(0.6F).build();
    public static final LegacyFoodProperties KELP_ROLL_SLICE = (new LegacyFoodProperties.Builder()).nutrition(6).saturationMod(0.5F).fast().build();
    public static final LegacyFoodProperties COOKED_RICE = (new LegacyFoodProperties.Builder()).nutrition(6).saturationMod(0.4F).effect(() -> nourishment(600), 1.0F).build();
    public static final LegacyFoodProperties BONE_BROTH = (new LegacyFoodProperties.Builder()).nutrition(8).saturationMod(0.7F).effect(() -> nourishment(1200), 1.0F).build();
    public static final LegacyFoodProperties BEEF_STEW = (new LegacyFoodProperties.Builder()).nutrition(12).saturationMod(0.8F).effect(() -> nourishment(3600), 1.0F).build();
    public static final LegacyFoodProperties VEGETABLE_SOUP = (new LegacyFoodProperties.Builder()).nutrition(12).saturationMod(0.8F).effect(() -> nourishment(3600), 1.0F).build();
    public static final LegacyFoodProperties FISH_STEW = (new LegacyFoodProperties.Builder()).nutrition(12).saturationMod(0.8F).effect(() -> nourishment(3600), 1.0F).build();
    public static final LegacyFoodProperties ONION_SOUP = (new LegacyFoodProperties.Builder()).nutrition(12).saturationMod(0.8F).effect(() -> nourishment(3600), 1.0F).build();
    public static final LegacyFoodProperties CHICKEN_SOUP = (new LegacyFoodProperties.Builder()).nutrition(12).saturationMod(0.8F).effect(() -> nourishment(3600), 1.0F).build();
    public static final LegacyFoodProperties FRIED_RICE = (new LegacyFoodProperties.Builder()).nutrition(12).saturationMod(0.8F).effect(() -> nourishment(3600), 1.0F).build();
    public static final LegacyFoodProperties PUMPKIN_SOUP = (new LegacyFoodProperties.Builder()).nutrition(14).saturationMod(0.75F).effect(() -> nourishment(6000), 1.0F).build();
    public static final LegacyFoodProperties BAKED_COD_STEW = (new LegacyFoodProperties.Builder()).nutrition(14).saturationMod(0.75F).effect(() -> nourishment(6000), 1.0F).build();
    public static final LegacyFoodProperties NOODLE_SOUP = (new LegacyFoodProperties.Builder()).nutrition(14).saturationMod(0.75F).effect(() -> nourishment(6000), 1.0F).build();
    public static final LegacyFoodProperties BACON_AND_EGGS = (new LegacyFoodProperties.Builder()).nutrition(10).saturationMod(0.6F).effect(() -> nourishment(1200), 1.0F).build();
    public static final LegacyFoodProperties RATATOUILLE = (new LegacyFoodProperties.Builder()).nutrition(10).saturationMod(0.6F).effect(() -> nourishment(1200), 1.0F).build();
    public static final LegacyFoodProperties STEAK_AND_POTATOES = (new LegacyFoodProperties.Builder()).nutrition(12).saturationMod(0.8F).effect(() -> nourishment(3600), 1.0F).build();
    public static final LegacyFoodProperties PASTA_WITH_MEATBALLS = (new LegacyFoodProperties.Builder()).nutrition(12).saturationMod(0.8F).effect(() -> nourishment(3600), 1.0F).build();
    public static final LegacyFoodProperties PASTA_WITH_MUTTON_CHOP = (new LegacyFoodProperties.Builder()).nutrition(12).saturationMod(0.8F).effect(() -> nourishment(3600), 1.0F).build();
    public static final LegacyFoodProperties MUSHROOM_RICE = (new LegacyFoodProperties.Builder()).nutrition(12).saturationMod(0.8F).effect(() -> nourishment(3600), 1.0F).build();
    public static final LegacyFoodProperties ROASTED_MUTTON_CHOPS = (new LegacyFoodProperties.Builder()).nutrition(14).saturationMod(0.75F).effect(() -> nourishment(6000), 1.0F).build();
    public static final LegacyFoodProperties VEGETABLE_NOODLES = (new LegacyFoodProperties.Builder()).nutrition(14).saturationMod(0.75F).effect(() -> nourishment(6000), 1.0F).build();
    public static final LegacyFoodProperties SQUID_INK_PASTA = (new LegacyFoodProperties.Builder()).nutrition(14).saturationMod(0.75F).effect(() -> nourishment(6000), 1.0F).build();
    public static final LegacyFoodProperties GRILLED_SALMON = (new LegacyFoodProperties.Builder()).nutrition(14).saturationMod(0.75F).effect(() -> nourishment(3600), 1.0F).build();
    public static final LegacyFoodProperties ROAST_CHICKEN = (new LegacyFoodProperties.Builder()).nutrition(14).saturationMod(0.75F).effect(() -> nourishment(6000), 1.0F).build();
    public static final LegacyFoodProperties STUFFED_PUMPKIN = (new LegacyFoodProperties.Builder()).nutrition(14).saturationMod(0.75F).effect(() -> nourishment(6000), 1.0F).build();
    public static final LegacyFoodProperties HONEY_GLAZED_HAM = (new LegacyFoodProperties.Builder()).nutrition(14).saturationMod(0.75F).effect(() -> nourishment(6000), 1.0F).build();
    public static final LegacyFoodProperties SHEPHERDS_PIE = (new LegacyFoodProperties.Builder()).nutrition(14).saturationMod(0.75F).effect(() -> nourishment(6000), 1.0F).build();
    public static final LegacyFoodProperties GLEAMING_SALAD = (new LegacyFoodProperties.Builder()).nutrition(14).saturationMod(0.75F).effect(() -> nourishment(6000), 1.0F).build();
    public static final LegacyFoodProperties DOG_FOOD = (new LegacyFoodProperties.Builder()).nutrition(4).saturationMod(0.2F).meat().build();
    public static final Map<Item, LegacyFoodProperties> VANILLA_SOUP_EFFECTS;

    public FoodValues() {
    }

    public static PotionEffect nourishment(int duration) {
        return null;
//        return new PotionEffect((MobEffect)ModEffects.NOURISHMENT.get(), duration, 0, false, false);
    }

    static {
        VANILLA_SOUP_EFFECTS = new ImmutableMap.Builder<Item,LegacyFoodProperties>().put(Items.mushroom_stew, (new LegacyFoodProperties.Builder()).effect(() -> nourishment(3600), 1.0F).build()).build();
    }
}
