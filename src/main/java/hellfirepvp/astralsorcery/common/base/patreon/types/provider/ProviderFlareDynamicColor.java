package hellfirepvp.astralsorcery.common.base.patreon.types.provider;

import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import java.util.List;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.base.patreon.types.TypeFlareColor;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffectProvider;

public class ProviderFlareDynamicColor implements PatreonEffectProvider<TypeFlareColor>
{
    @Override
    public TypeFlareColor buildEffect(final UUID playerUUID, final List<String> effectParameters) throws Exception {
        final UUID uniqueId = UUID.fromString(effectParameters.get(0));
        return new TypeFlareColor(uniqueId, () -> Color.getHSBColor(ClientScheduler.getClientTick() % 360L / 360.0f, 1.0f, 1.0f));
    }
}
