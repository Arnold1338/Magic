package hellfirepvp.astralsorcery.common.registry;

import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.lib.PerkCustomModifiersAS;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import hellfirepvp.astralsorcery.common.perk.type.ModifierType;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.AstralSorcery;

public class RegistryPerkCustomModifiers
{
    private RegistryPerkCustomModifiers() {
    }
    
    public static void init() {
        PerkCustomModifiersAS.FOCUS_GELU = register(new PerkAttributeModifier(AstralSorcery.key("focus_gelu"), PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT, ModifierType.ADDED_MULTIPLY, 0.02f) {
            @Override
            protected void initModifier() {
                super.initModifier();
                this.setAbsolute();
            }
            
            @Override
            public float getValue(final Player player, final PlayerProgress progress) {
                return this.getRawValue() * progress.getPerkData().getEffectGrantingPerks().size();
            }
            
            @Override
            public boolean hasDisplayString() {
                return false;
            }
        });
        PerkCustomModifiersAS.FOCUS_ULTERIA = register(new PerkAttributeModifier(AstralSorcery.key("focus_ulteria"), PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EXP, ModifierType.STACKING_MULTIPLY, 1.05f) {
            @Override
            protected void initModifier() {
                super.initModifier();
                this.setAbsolute();
            }
            
            @Override
            public float getValue(final Player player, final PlayerProgress progress) {
                final LogicalSide side = player.func_130014_f_().func_201670_d() ? LogicalSide.CLIENT : LogicalSide.SERVER;
                return 1.0f + 0.05f * progress.getPerkData().getAvailablePerkPoints(player, side);
            }
            
            @Override
            public boolean hasDisplayString() {
                return false;
            }
        });
        PerkCustomModifiersAS.FOCUS_VORUX = register(new PerkAttributeModifier(AstralSorcery.key("focus_vorux"), PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT, ModifierType.ADDED_MULTIPLY, 0.01f) {
            @Override
            protected void initModifier() {
                super.initModifier();
                this.setAbsolute();
            }
            
            @Override
            public float getValue(final Player player, final PlayerProgress progress) {
                return this.getRawValue() * progress.getPerkData().getEffectGrantingPerks().size();
            }
            
            @Override
            public boolean hasDisplayString() {
                return false;
            }
        });
    }
    
    private static <T extends PerkAttributeModifier> T register(final T modifier) {
        AstralSorcery.getProxy().getRegistryPrimer().register(modifier);
        return modifier;
    }
}
