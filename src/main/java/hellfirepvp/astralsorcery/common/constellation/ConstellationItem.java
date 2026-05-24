package hellfirepvp.astralsorcery.common.constellation;

import javax.annotation.Nullable;
import net.minecraft.world.item.ItemStack;

public interface ConstellationItem
{
    @Nullable
    IWeakConstellation getAttunedConstellation(final ItemStack p0);
    
    boolean setAttunedConstellation(final ItemStack p0, @Nullable final IWeakConstellation p1);
    
    @Nullable
    IMinorConstellation getTraitConstellation(final ItemStack p0);
    
    boolean setTraitConstellation(final ItemStack p0, @Nullable final IMinorConstellation p1);
}
