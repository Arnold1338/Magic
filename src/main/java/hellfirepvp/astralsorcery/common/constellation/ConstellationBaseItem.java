package hellfirepvp.astralsorcery.common.constellation;

import javax.annotation.Nullable;
import net.minecraft.world.item.ItemStack;

public interface ConstellationBaseItem
{
    @Nullable
    IConstellation getConstellation(final ItemStack p0);
    
    boolean setConstellation(final ItemStack p0, @Nullable final IConstellation p1);
}
