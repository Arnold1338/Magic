package hellfirepvp.astralsorcery.common.block.base;

import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.Level;

public interface BlockStarlightRecipient
{
    void receiveStarlight(final Level p0, final Random p1, final BlockPos p2, final IWeakConstellation p3, final double p4);
}
