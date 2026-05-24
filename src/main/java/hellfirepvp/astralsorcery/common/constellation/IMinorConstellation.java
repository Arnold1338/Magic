package hellfirepvp.astralsorcery.common.constellation;

import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.base.MoonPhase;
import java.util.List;

public interface IMinorConstellation extends IConstellation
{
    List<MoonPhase> getShowupMoonPhases(final long p0);
    
    default Component getInfoTraitEffect() {
        return (Component)new Component(this.getTranslationKey() + ".trait");
    }
    
    default void affectConstellationEffect(final ConstellationEffectProperties properties) {
    }
}
