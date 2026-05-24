package hellfirepvp.astralsorcery.common.perk.type;

import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import hellfirepvp.astralsorcery.common.perk.source.ModifierSource;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.level.entity.player.Player;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;

public class AttributeTypeChargeMaximum extends PerkAttributeType
{
    public AttributeTypeChargeMaximum() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_ALIGNMENT_CHARGE_MAXIMUM);
    }
    
    @Override
    protected void attachListeners(final IEventBus eventBus) {
        super.attachListeners(eventBus);
        eventBus.addListener((Consumer)this::onAttributePostProcess);
    }
    
    @Override
    public void onApply(final Player player, final LogicalSide side, final ModifierSource source) {
        super.onApply(player, side, source);
        AlignmentChargeHandler.INSTANCE.updateMaximum(player, side);
    }
    
    @Override
    public void onRemove(final Player player, final LogicalSide side, final boolean removedCompletely, final ModifierSource source) {
        super.onRemove(player, side, removedCompletely, source);
        AlignmentChargeHandler.INSTANCE.updateMaximum(player, side);
    }
    
    private void onAttributePostProcess(final AttributeEvent.PostProcessModded processEvent) {
        if (processEvent.getType() instanceof AttributeTypeChargeMaximum && processEvent.getValue() < 0.0) {
            processEvent.setValue(0.0);
        }
    }
}
