package hellfirepvp.astralsorcery.common.item.useables;

import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import hellfirepvp.astralsorcery.common.constellation.IMajorConstellation;

public class ItemShiftingStarVicio extends ItemShiftingStar
{
    @Nullable
    @Override
    public IMajorConstellation getBaseConstellation() {
        return ConstellationsAS.vicio;
    }
}
