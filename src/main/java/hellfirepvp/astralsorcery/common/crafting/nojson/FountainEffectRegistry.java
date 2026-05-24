package hellfirepvp.astralsorcery.common.crafting.nojson;

import java.util.HashMap;
import hellfirepvp.astralsorcery.common.crafting.nojson.fountain.FountainEffectVortex;
import hellfirepvp.astralsorcery.common.crafting.nojson.fountain.FountainEffectLiquid;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import hellfirepvp.astralsorcery.common.crafting.nojson.fountain.FountainEffect;

public class FountainEffectRegistry
{
    public static final FountainEffect<?> EFFECT_LIQUID;
    public static final FountainEffect<?> EFFECT_VORTEX;
    private static final Map<ResourceLocation, FountainEffect<?>> fountainEffectRegistry;
    
    private FountainEffectRegistry() {
    }
    
    public static void register(final FountainEffect<?> effect) {
        FountainEffectRegistry.fountainEffectRegistry.put(effect.getId(), effect);
    }
    
    @Nullable
    public static FountainEffect<?> getEffect(final ResourceLocation key) {
        return FountainEffectRegistry.fountainEffectRegistry.get(key);
    }
    
    static {
        EFFECT_LIQUID = new FountainEffectLiquid();
        EFFECT_VORTEX = new FountainEffectVortex();
        fountainEffectRegistry = new HashMap<ResourceLocation, FountainEffect<?>>();
        register(FountainEffectRegistry.EFFECT_LIQUID);
        register(FountainEffectRegistry.EFFECT_VORTEX);
    }
}
