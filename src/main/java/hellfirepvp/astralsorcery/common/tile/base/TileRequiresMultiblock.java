package hellfirepvp.astralsorcery.common.tile.base;

import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;

public interface TileRequiresMultiblock
{
    @Nullable
    StructureType getRequiredStructureType();
}
