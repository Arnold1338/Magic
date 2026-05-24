package hellfirepvp.astralsorcery.common.crafting.nojson.fountain;

import net.minecraft.nbt.CompoundTag;
import java.util.List;

public class VortexContext extends FountainEffect.EffectContext
{
    public Object fountainSprite;
    public Object facingVortexPlane;
    public List<Object> ctrlEffectNoise;
    
    public VortexContext() {
        this.ctrlEffectNoise = null;
    }
    
    @Override
    public void readFromNBT(final CompoundTag compound) {
    }
    
    @Override
    public void writeToNBT(final CompoundTag compound) {
    }
}
