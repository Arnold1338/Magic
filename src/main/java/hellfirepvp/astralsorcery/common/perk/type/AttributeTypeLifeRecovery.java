package hellfirepvp.astralsorcery.common.perk.type;

import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;

public class AttributeTypeLifeRecovery extends PerkAttributeType
{
    public AttributeTypeLifeRecovery() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_LIFE_RECOVERY, true);
    }
    
    @Override
    protected void attachListeners(final IEventBus eventBus) {
        super.attachListeners(eventBus);
        eventBus.addListener(EventPriority.LOW, (Consumer)this::onHeal);
    }
    
    private void onHeal(final LivingHealEvent event) {
        if (!(event.getEntityLiving() instanceof Player)) {

        }
        final Player player = (Player)event.getEntityLiving();
        final LogicalSide side = this.getSide((Entity)player);
        if (!this.hasTypeApplied(player, side)) {

        }
        float heal = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, ResearchHelper.getProgress(player, side), this, event.getAmount());
        final float val;
        heal = (val = AttributeEvent.postProcessModded(player, this, heal));
        if (val <= 0.0f) {
            event.setCanceled(true);
        }
        else {
            event.setAmount(val);
        }
    }
}
