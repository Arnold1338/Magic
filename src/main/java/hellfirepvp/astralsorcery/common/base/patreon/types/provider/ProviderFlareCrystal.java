package hellfirepvp.astralsorcery.common.base.patreon.types.provider;

import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;
import hellfirepvp.astralsorcery.client.resource.query.TextureQuery;
import hellfirepvp.astralsorcery.client.resource.AssetLoader;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.base.patreon.FlareColor;
import java.util.List;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.base.patreon.types.TypeFlareCrystal;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffectProvider;

public class ProviderFlareCrystal implements PatreonEffectProvider<TypeFlareCrystal>
{
    @Override
    public TypeFlareCrystal buildEffect(final UUID playerUUID, final List<String> effectParameters) throws Exception {
        final UUID uniqueId = UUID.fromString(effectParameters.get(0));
        FlareColor fc = null;
        if (!"null".equals(effectParameters.get(1))) {
            fc = FlareColor.valueOf(effectParameters.get(1));
        }
        final int colorTheme = Integer.parseInt(effectParameters.get(2));
        String modelTexture = effectParameters.get(3);
        if (modelTexture.equalsIgnoreCase("crystal_big_magenta")) {
            modelTexture = "crystal_magenta";
        }
        return new TypeFlareCrystal(uniqueId, fc, new Color(colorTheme), new TextureQuery(AssetLoader.TextureLocation.MODEL, new String[] { modelTexture }));
    }
}
