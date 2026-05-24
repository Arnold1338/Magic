package hellfirepvp.astralsorcery.common.item.useables;

import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import hellfirepvp.astralsorcery.common.constellation.IMajorConstellation;

public class ItemShiftingStarArmara extends ItemShiftingStar
{
    @Nullable
    @Override
    public IMajorConstellation getBaseConstellation() {
        return ConstellationsAS.armara;
    }
}
