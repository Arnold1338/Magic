package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.astralsorcery.AstralSorcery;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProvider;
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
import hellfirepvp.astralsorcery.common.lib.ConstellationEffectsAS;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffect;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;
import java.util.function.Function;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.constellation.effect.aoe.CEffectAevitas;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;

public class RegistryConstellationEffects
{
    private RegistryConstellationEffects() {
    }
    
    public static void init() {
        ConstellationEffectsAS.AEVITAS = register(makeProvider(ConstellationsAS.aevitas, CEffectAevitas::new));
        ConstellationEffectsAS.ARMARA = register(makeProvider(ConstellationsAS.armara, CEffectArmara::new));
        ConstellationEffectsAS.BOOTES = register(makeProvider(ConstellationsAS.bootes, CEffectBootes::new));
        ConstellationEffectsAS.DISCIDIA = register(makeProvider(ConstellationsAS.discidia, CEffectDiscidia::new));
        ConstellationEffectsAS.EVORSIO = register(makeProvider(ConstellationsAS.evorsio, CEffectEvorsio::new));
        ConstellationEffectsAS.FORNAX = register(makeProvider(ConstellationsAS.fornax, CEffectFornax::new));
        ConstellationEffectsAS.HOROLOGIUM = register(makeProvider(ConstellationsAS.horologium, CEffectHorologium::new));
        ConstellationEffectsAS.LUCERNA = register(makeProvider(ConstellationsAS.lucerna, CEffectLucerna::new));
        ConstellationEffectsAS.MINERALIS = register(makeProvider(ConstellationsAS.mineralis, CEffectMineralis::new));
        ConstellationEffectsAS.OCTANS = register(makeProvider(ConstellationsAS.octans, CEffectOctans::new));
        ConstellationEffectsAS.PELOTRIO = register(makeProvider(ConstellationsAS.pelotrio, CEffectPelotrio::new));
        ConstellationEffectsAS.VICIO = register(makeProvider(ConstellationsAS.vicio, CEffectVicio::new));
    }
    
    private static ConstellationEffectProvider makeProvider(final IWeakConstellation cst, final Function<ILocatable, ? extends ConstellationEffect> effectProvider) {
        return new ConstellationEffectProvider(cst) {
            @Override
            public ConstellationEffect createEffect(@Nullable final ILocatable origin) {
                return effectProvider.apply(origin);
            }
        };
    }
    
    private static <T extends ConstellationEffectProvider> T register(final T effectProvider) {
        AstralSorcery.getProxy().getRegistryPrimer().register(effectProvider);
        return effectProvider;
    }
}
