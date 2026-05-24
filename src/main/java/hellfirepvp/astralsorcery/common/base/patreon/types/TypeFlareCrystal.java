package hellfirepvp.astralsorcery.common.base.patreon.types;

import hellfirepvp.astralsorcery.common.base.patreon.entity.PatreonCrystalFlare;
import hellfirepvp.astralsorcery.common.base.patreon.entity.PatreonPartialEntity;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.base.patreon.FlareColor;
import java.util.UUID;
import hellfirepvp.astralsorcery.client.resource.query.TextureQuery;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;

public class TypeFlareCrystal extends PatreonEffect
{
    private final Color colorTheme;
    private final TextureQuery crystalTexture;
    
    public TypeFlareCrystal(final UUID effectUUID, @Nullable final FlareColor flareColor, final Color colorTheme, final TextureQuery crystalTexture) {
        super(effectUUID, flareColor);
        this.colorTheme = colorTheme;
        this.crystalTexture = crystalTexture;
    }
    
    @Nullable
    @Override
    public PatreonPartialEntity createEntity(final UUID playerUUID) {
        return new PatreonCrystalFlare(this.getEffectUUID(), playerUUID).setQueryTexture(this.crystalTexture).setColorTheme(this.colorTheme);
    }
}
