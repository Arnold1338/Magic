package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.constellation.mantle.MantleEffect;
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
import hellfirepvp.astralsorcery.common.lib.MantleEffectsAS;
import hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectAevitas;

public class RegistryMantleEffects
{
    private RegistryMantleEffects() {
    }
    
    public static void init() {
        MantleEffectsAS.AEVITAS = register(new MantleEffectAevitas());
        MantleEffectsAS.ARMARA = register(new MantleEffectArmara());
        MantleEffectsAS.BOOTES = register(new MantleEffectBootes());
        MantleEffectsAS.DISCIDIA = register(new MantleEffectDiscidia());
        MantleEffectsAS.EVORSIO = register(new MantleEffectEvorsio());
        MantleEffectsAS.FORNAX = register(new MantleEffectFornax());
        MantleEffectsAS.HOROLOGIUM = register(new MantleEffectHorologium());
        MantleEffectsAS.LUCERNA = register(new MantleEffectLucerna());
        MantleEffectsAS.MINERALIS = register(new MantleEffectMineralis());
        MantleEffectsAS.OCTANS = register(new MantleEffectOctans());
        MantleEffectsAS.PELOTRIO = register(new MantleEffectPelotrio());
        MantleEffectsAS.VICIO = register(new MantleEffectVicio());
    }
    
    private static <T extends MantleEffect> T register(final T effect) {
        AstralSorcery.getProxy().getRegistryPrimer().register(effect);
        return effect;
    }
}
