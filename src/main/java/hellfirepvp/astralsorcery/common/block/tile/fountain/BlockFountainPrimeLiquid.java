package hellfirepvp.astralsorcery.common.block.tile.fountain;

import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.crafting.nojson.FountainEffectRegistry;
import hellfirepvp.astralsorcery.common.crafting.nojson.fountain.FountainEffect;

public class BlockFountainPrimeLiquid extends BlockFountainPrime
{
    @Nonnull
    @Override
    public FountainEffect<?> provideEffect() {
        return FountainEffectRegistry.EFFECT_LIQUID;
    }
}
