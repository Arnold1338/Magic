package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.astralsorcery.common.perk.type.ModifierType;
import hellfirepvp.astralsorcery.common.perk.node.focus.KeyGelu;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.lib.PerkConvertersAS;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.perk.source.ModifierSource;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.PerkConverter;
import hellfirepvp.astralsorcery.AstralSorcery;

public class RegistryPerkConverters
{
    private RegistryPerkConverters() {
    }
    
    public static void init() {
        PerkConvertersAS.IDENTITY = register(new PerkConverter(AstralSorcery.key("identity")) {
            @Nonnull
            @Override
            public PerkAttributeModifier convertModifier(final Player player, final PlayerProgress progress, final PerkAttributeModifier modifier, @Nullable final ModifierSource owningSource) {
                return modifier;
            }
        });
        PerkConvertersAS.FOCUS_ALCARA = register(new PerkConverter(AstralSorcery.key("focus_alcara")) {
            @Nonnull
            @Override
            public PerkAttributeModifier convertModifier(final Player player, final PlayerProgress progress, final PerkAttributeModifier modifier, @Nullable final ModifierSource owningSource) {
                if (modifier.getAttributeType().equals(PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EXP)) {
                    return modifier.convertModifier(PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT, modifier.getMode(), modifier.getValue(player, progress));
                }
                return modifier;
            }
        });
        PerkConvertersAS.FOCUS_GELU = register(new PerkConverter(AstralSorcery.key("focus_gelu")) {
            @Nonnull
            @Override
            public PerkAttributeModifier convertModifier(final Player player, final PlayerProgress progress, final PerkAttributeModifier modifier, @Nullable final ModifierSource owningSource) {
                if (modifier.getAttributeType().equals(PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT) && owningSource != null && !(owningSource instanceof KeyGelu)) {
                    return modifier.convertModifier(PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT, ModifierType.STACKING_MULTIPLY, 1.0f);
                }
                return modifier;
            }
        });
    }
    
    private static <T extends PerkConverter> T register(final T converter) {
        AstralSorcery.getProxy().getRegistryPrimer().register(converter);
        return converter;
    }
}
