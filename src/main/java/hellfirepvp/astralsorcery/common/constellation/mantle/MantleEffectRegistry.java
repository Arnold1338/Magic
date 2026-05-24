package hellfirepvp.astralsorcery.common.constellation.mantle;

import hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectVicio;
import hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectPelotrio;
import hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectOctans;
import hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectMineralis;
import hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectLucerna;
import hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectHorologium;
import hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectFornax;
import hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectEvorsio;
import hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectDiscidia;
import hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectBootes;
import hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectArmara;
import hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectAevitas;
import hellfirepvp.astralsorcery.common.data.config.ServerConfig;

public class MantleEffectRegistry
{
    public static void addConfigEntries(final ServerConfig config) {
        config.addConfigEntry(MantleEffectAevitas.CONFIG);
        config.addConfigEntry(MantleEffectArmara.CONFIG);
        config.addConfigEntry(MantleEffectBootes.CONFIG);
        config.addConfigEntry(MantleEffectDiscidia.CONFIG);
        config.addConfigEntry(MantleEffectEvorsio.CONFIG);
        config.addConfigEntry(MantleEffectFornax.CONFIG);
        config.addConfigEntry(MantleEffectHorologium.CONFIG);
        config.addConfigEntry(MantleEffectLucerna.CONFIG);
        config.addConfigEntry(MantleEffectMineralis.CONFIG);
        config.addConfigEntry(MantleEffectOctans.CONFIG);
        config.addConfigEntry(MantleEffectPelotrio.CONFIG);
        config.addConfigEntry(MantleEffectVicio.CONFIG);
    }
}
