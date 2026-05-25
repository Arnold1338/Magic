package hellfirepvp.astralsorcery.common.constellation.effect;

import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface ConstellationEffectStatus
{
    boolean runStatusEffect(final Level p0, final BlockPos p1, final int p2, final ConstellationEffectProperties p3, @Nullable final IMinorConstellation p4);
}
