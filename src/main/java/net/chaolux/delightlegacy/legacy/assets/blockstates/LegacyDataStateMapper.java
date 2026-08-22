package net.chaolux.delightlegacy.legacy.assets.blockstates;

import javafx.util.Builder;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class LegacyDataStateMapper implements LegacyBlockStateMapper {
    private final Map<Integer,Map<String,String>> blockstates;
    public LegacyDataStateMapper(Map<Integer,Map<String,String>> blockstates) {
        Map<Integer,Map<String,String>> integerMapMap=new LinkedHashMap<Integer,Map<String,String>>();
        for(Map.Entry<Integer,Map<String,String>> entry : blockstates.entrySet()) {
            integerMapMap.put(entry.getKey(), Collections.unmodifiableMap(new LinkedHashMap<String,String>(entry.getValue())));
        }
        this.blockstates=Collections.unmodifiableMap(integerMapMap);
    }

    @Override
    public Map<String,String> getBlockState(IBlockAccess iBlockAccess, int x, int y, int z, Block block,int data) {
        return state(data);
    }

    @Override
    public Map<String,String> getItemState(Block block,int data) {
        return state(data);
    }

    private Map<String,String> state(int data) {
        Map<String,String> state=blockstates.get(data);
        return state == null ? Collections.<String,String>emptyMap() : state;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static LegacyDataStateMapper legacyDataStateMapper(String string,int min,int max) {
        Builder builder=builder();
        for(int index=min;index <= max;index++) {
            builder.put(index,string,String.valueOf(index));
        }
        return builder.build();
    }

    public static final class Builder {
        private final Map<Integer,Map<String,String>> blockstates=new LinkedHashMap<Integer,Map<String,String>>();
        public Builder put(int data,String string,String value) {
            Map<String,String> state=blockstates.get(data);
            if(state == null) {
                state=new LinkedHashMap<String,String>();
                blockstates.put(data,state);
            }
            state.put(string,value);
            return this;
        }

        public Builder variant(int data,String string) {
            if(string == null || string.trim().isEmpty()) {
                blockstates.put(data,new LinkedHashMap<String,String>());
                return this;
            }
            String[] strings=string.split(",");
            for(String state : strings) {
                String[] states=state.split("=",2);
                if(states.length != 2) throw new IllegalArgumentException("Invalid blockstate state: " + state);
                put(data,states[0].trim(),states[1].trim());
            }
            return this;
        }

        public LegacyDataStateMapper build() {
            return new LegacyDataStateMapper(blockstates);
        }
    }
}
