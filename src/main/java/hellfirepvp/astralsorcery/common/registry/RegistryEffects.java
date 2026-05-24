package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraftforge.common.MinecraftForge;
import hellfirepvp.astralsorcery.common.util.NameUtil;
import hellfirepvp.astralsorcery.common.effect.EffectCustomTexture;
import hellfirepvp.astralsorcery.common.effect.EffectDropModifier;
import hellfirepvp.astralsorcery.common.effect.EffectCheatDeath;
import hellfirepvp.astralsorcery.common.lib.EffectsAS;
import hellfirepvp.astralsorcery.common.effect.EffectBleed;

public class RegistryEffects
{
    private RegistryEffects() {
    }
    
    public static void init() {
        EffectsAS.EFFECT_BLEED = register(new EffectBleed());
        EffectsAS.EFFECT_CHEAT_DEATH = register(new EffectCheatDeath());
        EffectsAS.EFFECT_DROP_MODIFIER = register(new EffectDropModifier());
    }
    
    private static <T extends EffectCustomTexture> T register(final T effect) {
        effect.setRegistryName(NameUtil.fromClass(effect, "Effect"));
        effect.attachEventListeners(MinecraftForge.EVENT_BUS);
        AstralSorcery.getProxy().getRegistryPrimer().register(effect);
        return effect;
    }
}
