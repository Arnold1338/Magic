package hellfirepvp.astralsorcery.common.base.patreon.types.provider;

import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.base.patreon.FlareColor;
import java.util.List;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.base.patreon.types.TypeCrystalFootprints;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffectProvider;

public class ProviderCrystalFootprints implements PatreonEffectProvider<TypeCrystalFootprints>
{
    @Override
    public TypeCrystalFootprints buildEffect(final UUID playerUUID, final List<String> effectParameters) throws Exception {
        final UUID uniqueId = UUID.fromString(effectParameters.get(0));
        FlareColor fc = null;
        if (!"null".equals(effectParameters.get(1))) {
            fc = FlareColor.valueOf(effectParameters.get(1));
        }
        final Color color = new Color(Integer.parseInt(effectParameters.get(2)));
        return new TypeCrystalFootprints(uniqueId, fc, playerUUID, color);
    }
}
