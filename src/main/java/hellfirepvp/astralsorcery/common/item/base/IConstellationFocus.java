package hellfirepvp.astralsorcery.common.item.base;

import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import net.minecraft.world.level.item.ItemStack;

public interface IConstellationFocus
{
    @Nullable
    IConstellation getFocusConstellation(final ItemStack p0);
}
