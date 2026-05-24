package hellfirepvp.astralsorcery.common.constellation;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.constellation.mantle.MantleEffect;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProvider;

public interface IWeakConstellation extends IConstellation
{
    @Nullable
    default ConstellationEffectProvider getConstellationEffect() {
        return (ConstellationEffectProvider)RegistriesAS.REGISTRY_CONSTELLATION_EFFECT.getValue(this.getRegistryName());
    }
    
    @Nullable
    default MantleEffect getMantleEffect() {
        return (MantleEffect)RegistriesAS.REGISTRY_MANTLE_EFFECT.getValue(this.getRegistryName());
    }
    
    default Component getInfoRitualEffect() {
        return (Component)new Component(this.getTranslationKey() + ".ritual");
    }
    
    default Component getInfoCorruptedRitualEffect() {
        return (Component)new Component(this.getTranslationKey() + ".corruption");
    }
    
    default Component getInfoMantleEffect() {
        return (Component)new Component(this.getTranslationKey() + ".mantle");
    }
}
