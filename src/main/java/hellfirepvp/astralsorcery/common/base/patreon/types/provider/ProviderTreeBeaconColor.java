package hellfirepvp.astralsorcery.common.base.patreon.types.provider;

import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.base.patreon.FlareColor;
import java.util.List;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.base.patreon.types.TypeTreeBeaconColor;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffectProvider;

public class ProviderTreeBeaconColor implements PatreonEffectProvider<TypeTreeBeaconColor>
{
    @Override
    public TypeTreeBeaconColor buildEffect(final UUID playerUUID, final List<String> effectParameters) throws Exception {
        final UUID effectUUID = UUID.fromString(effectParameters.get(0));
        FlareColor fc = null;
        if (!"null".equals(effectParameters.get(1))) {
            fc = FlareColor.valueOf(effectParameters.get(1));
        }
        final int color = Integer.parseInt(effectParameters.get(2));
        return new TypeTreeBeaconColor(effectUUID, fc, new Color(color));
    }
}
