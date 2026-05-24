package hellfirepvp.astralsorcery.common.util;

import net.minecraft.util.WeightedRandom;

public class WRItemObject<T> extends WeightedRandom.Item
{
    private final T object;
    
    public WRItemObject(final int itemWeightIn, final T value) {
        super(itemWeightIn);
        this.object = value;
    }
    
    public T getValue() {
        return this.object;
    }
}
