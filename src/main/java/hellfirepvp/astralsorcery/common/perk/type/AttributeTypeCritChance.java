package hellfirepvp.astralsorcery.common.perk.type;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import net.minecraft.world.level.entity.Entity;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.world.entity.projectile.ArrowEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.perk.modifier.AttributeModifierCritChance;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;

public class AttributeTypeCritChance extends PerkAttributeType
{
    public AttributeTypeCritChance() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_INC_CRIT_CHANCE);
    }
    
    @Override
    protected void attachListeners(final IEventBus eventBus) {
        super.attachListeners(eventBus);
        eventBus.addListener(EventPriority.HIGH, (Consumer)this::onArrowCrit);
        eventBus.addListener(EventPriority.LOW, (Consumer)this::onHitCrit);
    }
    
    @Nonnull
    @Override
    public PerkAttributeModifier createModifier(final float modifier, final ModifierType mode) {
        return new AttributeModifierCritChance(this, mode, modifier);
    }
    
    private void onArrowCrit(final EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof ArrowEntity) {
            final ArrowEntity arrow = (ArrowEntity)event.getEntity();
            final Entity shooter = arrow.func_234616_v_();
            if (shooter instanceof Player) {
                final Player player = (Player)shooter;
                final LogicalSide side = this.getSide((Entity)player);
                if (!this.hasTypeApplied(player, side)) {
                    return;
                }
                float critChance = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, ResearchHelper.getProgress(player, side), this, 0.0f);
                critChance = AttributeEvent.postProcessModded(player, this, critChance);
                critChance /= 100.0f;
                if (critChance >= AttributeTypeCritChance.rand.nextFloat()) {
                    arrow.func_70243_d(true);
                }
            }
        }
    }
    
    private void onHitCrit(final CriticalHitEvent event) {
        if (event.isVanillaCritical() || event.getResult() == Event.Result.ALLOW) {
            return;
        }
        final Player player = event.getPlayer();
        final LogicalSide side = this.getSide((Entity)player);
        if (!this.hasTypeApplied(player, side)) {
            return;
        }
        float critChance = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, ResearchHelper.getProgress(player, side), this, 0.0f);
        critChance = AttributeEvent.postProcessModded(player, this, critChance);
        critChance /= 100.0f;
        if (critChance >= AttributeTypeCritChance.rand.nextFloat()) {
            event.setResult(Event.Result.ALLOW);
        }
    }
}
