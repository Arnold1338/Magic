package hellfirepvp.astralsorcery.common.perk.node.focus;

import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.entity.player.Player;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.world.level.entity.Entity;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import net.minecraft.resources.ResourceLocation;

public class KeyVorux extends FocusPerk
{
    public KeyVorux(final ResourceLocation name, final float x, final float y) {
        super(name, ConstellationsAS.vorux, x, y);
    }
    
    public void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener((Consumer)this::onExpGain);
    }
    
    public void onExpGain(final AttributeEvent.PostProcessModded ev) {
        if (ev.getType().equals(PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EXP)) {
            final Player player = ev.getPlayer();
            final LogicalSide side = this.getSide((Entity)player);
            final PlayerProgress prog = ResearchHelper.getProgress(player, side);
            if (prog.getPerkData().hasPerkEffect(this)) {
                ev.setValue(0.0);
            }
        }
    }
}
