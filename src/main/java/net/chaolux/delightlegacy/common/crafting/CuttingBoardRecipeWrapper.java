package net.chaolux.delightlegacy.common.crafting;

import net.minecraft.item.ItemStack;

public class CuttingBoardRecipeWrapper {
    private final ItemStack input;
    private final ItemStack tool;
    public CuttingBoardRecipeWrapper(ItemStack input,ItemStack tool) {
        this.input=input;
        this.tool=tool;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getTool() {
        return tool;
    }
}
