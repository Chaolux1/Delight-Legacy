package net.chaolux.delightlegacy.legacy.assets.blockstates;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

import java.util.Map;

public interface LegacyBlockStateMapper {
    Map<String,String> getBlockState(IBlockAccess iBlockAccess, int x, int y, int z, Block block,int data);
    Map<String,String> getItemState(Block block,int data);
}
