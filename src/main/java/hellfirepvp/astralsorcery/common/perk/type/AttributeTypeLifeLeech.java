package hellfirepvp.astralsorcery.common.perk.type;

import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.level.damagesource.DamageSource;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import net.minecraft.world.level.entity.Entity;
import net.minecraft.world.level.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.perk.modifier.AttributeModifierLifeLeech;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;

public class AttributeTypeLifeLeech extends PerkAttributeType
{
    public AttributeTypeLifeLeech() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_ATTACK_LIFE_LEECH);
    }
    
    @Override
    protected void attachListeners(final IEventBus eventBus) {
        super.attachListeners(eventBus);
        eventBus.addListener(EventPriority.LOWEST, (Consumer)this::onLeech);
    }
    
    @Nonnull
    @Override
    public PerkAttributeModifier createModifier(final float modifier, final ModifierType mode) {
        return new AttributeModifierLifeLeech(this, mode, modifier);
    }
    
    private void onLeech(final LivingDamageEvent event) {
        final DamageSource source = event.getSource();
        if (source.func_76346_g() != null && source.func_76346_g() instanceof Player) {
            final Player player = (Player)source.func_76346_g();
            final LogicalSide side = this.getSide((Entity)player);
            if (side.isServer() && this.hasTypeApplied(player, side)) {
                float leechPerc = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, ResearchHelper.getProgress(player, side), this, 0.0f);
                leechPerc /= 100.0f;
                leechPerc = AttributeEvent.postProcessModded(player, this, leechPerc);
                if (leechPerc > 0.0f) {
                    final float leech = event.getAmount() * leechPerc;
                    if (leech > 0.0f) {
                        player.func_70691_i(leech);
                    }
                }
            }
        }
    }
}
