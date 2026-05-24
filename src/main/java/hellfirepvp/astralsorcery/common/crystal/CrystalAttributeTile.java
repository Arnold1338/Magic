package hellfirepvp.astralsorcery.common.crystal;

import javax.annotation.Nullable;

public interface CrystalAttributeTile
{
    @Nullable
    CrystalAttributes getAttributes();
    
    void setAttributes(@Nullable final CrystalAttributes p0);
    
    default CrystalAttributes getMissingAttributes() {
        return CrystalAttributes.Builder.newBuilder(false).build();
    }
}
