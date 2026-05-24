package hellfirepvp.astralsorcery.common.constellation.effect;

import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;
import hellfirepvp.astralsorcery.common.constellation.effect.aoe.CEffectVicio;
import hellfirepvp.astralsorcery.common.constellation.effect.aoe.CEffectPelotrio;
import hellfirepvp.astralsorcery.common.constellation.effect.aoe.CEffectOctans;
import hellfirepvp.astralsorcery.common.constellation.effect.aoe.CEffectMineralis;
import hellfirepvp.astralsorcery.common.constellation.effect.aoe.CEffectLucerna;
import hellfirepvp.astralsorcery.common.constellation.effect.aoe.CEffectHorologium;
import hellfirepvp.astralsorcery.common.constellation.effect.aoe.CEffectFornax;
import hellfirepvp.astralsorcery.common.constellation.effect.aoe.CEffectEvorsio;
import hellfirepvp.astralsorcery.common.constellation.effect.aoe.CEffectDiscidia;
import hellfirepvp.astralsorcery.common.constellation.effect.aoe.CEffectBootes;
import hellfirepvp.astralsorcery.common.constellation.effect.aoe.CEffectArmara;
import hellfirepvp.astralsorcery.common.constellation.effect.aoe.CEffectAevitas;
import hellfirepvp.astralsorcery.common.data.config.ServerConfig;

public class ConstellationEffectRegistry
{
    public static final String ENTITY_TAG_LUCERNA_SKIP_ENTITY = "skip.spawn.deny";
    
    public static void addConfigEntries(final ServerConfig config) {
        config.addConfigEntry(CEffectAevitas.CONFIG);
        config.addConfigEntry(CEffectArmara.CONFIG);
        config.addConfigEntry(CEffectBootes.CONFIG);
        config.addConfigEntry(CEffectDiscidia.CONFIG);
        config.addConfigEntry(CEffectEvorsio.CONFIG);
        config.addConfigEntry(CEffectFornax.CONFIG);
        config.addConfigEntry(CEffectHorologium.CONFIG);
        config.addConfigEntry(CEffectLucerna.CONFIG);
        config.addConfigEntry(CEffectMineralis.CONFIG);
        config.addConfigEntry(CEffectOctans.CONFIG);
        config.addConfigEntry(CEffectPelotrio.CONFIG);
        config.addConfigEntry(CEffectVicio.CONFIG);
    }
    
    @Nullable
    public static ConstellationEffect createInstance(@Nullable final ILocatable origin, final IWeakConstellation constellation) {
        final ConstellationEffectProvider effect = constellation.getConstellationEffect();
        if (effect != null) {
            return effect.createEffect(origin);
        }
        return null;
    }
}
