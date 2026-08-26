package net.chaolux.delightlegacy.common.utility;

import net.chaolux.delightlegacy.DelightLegacy;
import net.chaolux.delightlegacy.common.item.ConsumableItem;
import net.chaolux.delightlegacy.legacy.common.item.LegacyFoodProperties;
import net.chaolux.delightlegacy.legacy.common.item.LegacyItem;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TextUtils {
    public static final String PLACEABLE;
    public static final String PLACEABLE_SNEAKING;
    public static final String DEBUG_ITEM;

    public TextUtils() {
    }

    public static String getTranslation(String key, Object... args) {
        return translatable(DelightLegacy.MOD_ID + "." + key, args);
    }

    public static String getTextWithType(String translationType, String translationKey, Object... args) {
        return translatable(translationType + "." + DelightLegacy.MOD_ID + "." + translationKey, args);
    }

    public static String block(String key, Object... args) {
        return getTextWithType("block", key, args);
    }

    public static String item(String key, Object... args) {
        return getTextWithType("item", key, args);
    }

    public static String advancement(String key, Object... args) {
        return getTextWithType("advancements", key, args);
    }

    public static String container(String key, Object... args) {
        return getTextWithType("container", key, args);
    }

    public static String NEI(String key, Object... args) {
        return getTextWithType("jei", key, args);
    }

    public static String tooltip(String key, Object... args) {
        return getTextWithType("tooltip", key, args);
    }

    public static String subtitleKey(String key, Object... args) {
        return getTextWithType("subtitles", key, args);
    }

    public static void addFoodEffectTooltip(ItemStack stack, List<String> lores, float durationFactor) {
        LegacyItem legacyItem=(LegacyItem) stack.getItem();
        LegacyFoodProperties legacyFoodProperties=legacyItem.getLegacyFoodProperties();
        if(legacyFoodProperties == null) return;
        List<Pair> attributeList=new ArrayList<Pair>();
        for(LegacyFoodProperties.Effect effect : legacyFoodProperties.getEffectList()) {
            PotionEffect potionEffect=effect.potionEffect();
            if(potionEffect == null) continue;
            int value=potionEffect.getPotionID();
            if(value < 0 || value >= Potion.potionTypes.length) continue;
            Potion potion=Potion.potionTypes[value];
            if(potion == null) continue;
            String string= StatCollector.translateToLocal(potionEffect.getEffectName()).trim();
            if(potionEffect.getAmplifier() > 0) string += " " + StatCollector.translateToLocal("potion.potency." + potionEffect.getAmplifier()).trim();
            if(potionEffect.getDuration() > 20) {
                PotionEffect potionEffects=duration(potionEffect,durationFactor);
                string += " (" + Potion.getDurationString(potionEffects) + ")";
            }
            lores.add((potion.isBadEffect() ? EnumChatFormatting.RED : EnumChatFormatting.BLUE) + string);
            Map map=potion.func_111186_k();
            if(map == null || map.isEmpty()) continue;
            for(Object object : map.entrySet()) {
                Map.Entry entry=(Map.Entry) object;
                if(!(entry.getKey() instanceof IAttribute) || !(entry.getValue() instanceof AttributeModifier)) continue;
                IAttribute iAttribute=(IAttribute) entry.getKey();
                AttributeModifier attributeModifier=(AttributeModifier) entry.getValue();
                AttributeModifier modifier=new AttributeModifier(attributeModifier.getName(),potion.func_111183_a(potionEffect.getAmplifier(),attributeModifier),attributeModifier.getOperation());
                attributeList.add(new Pair(iAttribute,modifier));
            }
        }
        if(attributeList.isEmpty()) return;
        lores.add("");
        lores.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("potion.effects.whenDrank"));
        for (Pair pair : attributeList) {
            AttributeModifier attributeModifier=pair.attributeModifier;
            double valueModifier=attributeModifier.getAmount();
            double value;
            if(attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2) {
                value=attributeModifier.getAmount();
            } else {
                value=attributeModifier.getAmount() * 100;
            }
            String string=StatCollector.translateToLocal("attribute.name." + pair.iAttribute.getAttributeUnlocalizedName());
            if(valueModifier > 0) {
                lores.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + attributeModifier.getOperation(),ItemStack.field_111284_a.format(value),string));
            } else if(valueModifier < 0) {
                value *= -1;
                lores.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + attributeModifier.getOperation(),ItemStack.field_111284_a.format(value),string));
            }
        }
    }

    private static PotionEffect duration(PotionEffect potionEffect,float durationFactor) {
        if(durationFactor == 1.0f) return potionEffect;
        int duration=Math.round(potionEffect.getDuration() * durationFactor);
        return new PotionEffect(potionEffect.getPotionID(),duration,potionEffect.getAmplifier(),potionEffect.getIsAmbient());
    }

    private static String translatable(String string,Object... objects) {
        if(objects == null || objects.length == 0) return StatCollector.translateToLocal(string);
        return StatCollector.translateToLocalFormatted(string,objects);
    }

    private static final class Pair {
        private final IAttribute iAttribute;
        private final AttributeModifier attributeModifier;
        private Pair(IAttribute iAttribute,AttributeModifier attributeModifier) {
            this.iAttribute=iAttribute;
            this.attributeModifier=attributeModifier;
        }
    }


    static {
        PLACEABLE = EnumChatFormatting.DARK_GRAY + "" + EnumChatFormatting.ITALIC + tooltip("placeable");
        PLACEABLE_SNEAKING = EnumChatFormatting.DARK_GRAY + "" + EnumChatFormatting.ITALIC + tooltip("placeable_sneaking");
        DEBUG_ITEM = EnumChatFormatting.RED + tooltip("debug_item");

    }
}
