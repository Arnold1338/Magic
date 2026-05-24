package hellfirepvp.astralsorcery.common.base.patreon.types;

import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.base.patreon.entity.PatreonFlareDynamicColor;
import hellfirepvp.astralsorcery.common.base.patreon.entity.PatreonPartialEntity;
import hellfirepvp.astralsorcery.common.base.patreon.FlareColor;
import java.util.UUID;
import java.awt.Color;
import java.util.function.Supplier;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;

public class TypeFlareColor extends PatreonEffect
{
    private final Supplier<Color> colorProvider;
    
    public TypeFlareColor(final UUID uniqueId, final Supplier<Color> colorProvider) {
        super(uniqueId, null);
        this.colorProvider = colorProvider;
    }
    
    public Supplier<Color> getColorProvider() {
        return this.colorProvider;
    }
    
    @Override
    public boolean hasPartialEntity() {
        return true;
    }
    
    @Nullable
    @Override
    public PatreonPartialEntity createEntity(final UUID playerUUID) {
        return new PatreonFlareDynamicColor(this.getEffectUUID(), playerUUID);
    }
}
