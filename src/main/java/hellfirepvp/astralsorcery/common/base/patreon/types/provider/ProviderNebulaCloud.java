package hellfirepvp.astralsorcery.common.base.patreon.types.provider;

import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;
import hellfirepvp.astralsorcery.common.base.patreon.FlareColor;
import java.util.List;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.base.patreon.types.TypeNebulaCloud;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffectProvider;

public class ProviderNebulaCloud implements PatreonEffectProvider<TypeNebulaCloud>
{
    @Override
    public TypeNebulaCloud buildEffect(final UUID playerUUID, final List<String> effectParameters) throws Exception {
        final UUID effectUUID = UUID.fromString(effectParameters.get(0));
        FlareColor fc = null;
        if (!"null".equals(effectParameters.get(1))) {
            fc = FlareColor.valueOf(effectParameters.get(1));
        }
        return new TypeNebulaCloud(effectUUID, fc, playerUUID);
    }
}
