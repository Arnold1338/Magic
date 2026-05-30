package hellfirepvp.astralsorcery.common.perk.type;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ArrowEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;

public class AttributeTypeCritMultiplier extends PerkAttributeType
{
    public AttributeTypeCritMultiplier() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_INC_CRIT_MULTIPLIER, true);
    }
    
    @Override
    protected void attachListeners(final IEventBus eventBus) {
        super.attachListeners(eventBus);
        eventBus.addListener(EventPriority.LOWEST, (Consumer)this::onArrowCrit);
        eventBus.addListener(EventPriority.LOWEST, (Consumer)this::onHitCrit);
    }
    
    private void onArrowCrit(final EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof ArrowEntity) {
            final ArrowEntity arrow = (ArrowEntity)event.getEntity();
            if (!arrow.func_70241_g()) {

            }
            final Entity shooter = arrow.func_234616_v_();
            if (shooter instanceof Player) {
                final Player player = (Player)shooter;
                final LogicalSide side = this.getSide((Entity)player);
                if (!this.hasTypeApplied(player, side)) {

                }
                float dmgMod = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, ResearchHelper.getProgress(player, side), this, 1.0f);
                dmgMod = AttributeEvent.postProcessModded(player, this, dmgMod);
                arrow.func_70239_b(arrow.func_70242_d() * dmgMod);
            }
        }
    }
    
    private void onHitCrit(final CriticalHitEvent event) {
        if (!event.isVanillaCritical() && event.getResult() != Event.Result.ALLOW) {

        }
        final Player player = event.getPlayer();
        final LogicalSide side = this.getSide((Entity)player);
        if (!this.hasTypeApplied(player, side)) {

        }
        float dmgMod = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, ResearchHelper.getProgress(player, side), this, 1.0f);
        dmgMod = AttributeEvent.postProcessModded(player, this, dmgMod);
        event.setDamageModifier(event.getDamageModifier() * dmgMod);
    }
}
