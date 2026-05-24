package hellfirepvp.astralsorcery.common.perk.type;

import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.level.entity.player.Player;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import net.minecraft.world.level.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;

public class AttributeTypeBreakSpeed extends PerkAttributeType
{
    public static boolean evaluateBreakSpeedWithoutPerks;
    
    public AttributeTypeBreakSpeed() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_INC_HARVEST_SPEED);
    }
    
    @Override
    protected void attachListeners(final IEventBus eventBus) {
        super.attachListeners(eventBus);
        eventBus.addListener(EventPriority.LOW, (Consumer)this::onBreakSpeed);
    }
    
    private void onBreakSpeed(final PlayerEvent.BreakSpeed event) {
        if (AttributeTypeBreakSpeed.evaluateBreakSpeedWithoutPerks) {
            return;
        }
        final Player player = event.getPlayer();
        final LogicalSide side = this.getSide((Entity)player);
        if (!this.hasTypeApplied(player, side)) {
            return;
        }
        float speed = PerkAttributeHelper.getOrCreateMap(player, side).modifyValue(player, ResearchHelper.getProgress(player, side), this, event.getNewSpeed());
        speed = AttributeEvent.postProcessModded(player, this, speed);
        event.setNewSpeed(speed);
    }
    
    static {
        AttributeTypeBreakSpeed.evaluateBreakSpeedWithoutPerks = false;
    }
}
