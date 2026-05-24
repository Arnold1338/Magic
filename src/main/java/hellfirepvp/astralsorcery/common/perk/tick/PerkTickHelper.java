package hellfirepvp.astralsorcery.common.perk.tick;

import java.util.EnumSet;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.data.research.PlayerPerkData;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class PerkTickHelper implements ITickHandler
{
    public static final PerkTickHelper INSTANCE;
    
    private PerkTickHelper() {
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        final Player ticked = (Player)context[0];
        final LogicalSide side = (LogicalSide)context[1];
        final PlayerProgress prog = ResearchHelper.getProgress(ticked, side);
        if (prog.isValid()) {
            final PlayerPerkData perkData = prog.getPerkData();
            for (final AbstractPerk perk : perkData.getEffectGrantingPerks()) {
                if (perk instanceof PlayerTickPerk) {
                    ((PlayerTickPerk)perk).onPlayerTick(ticked, side);
                }
            }
        }
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.PLAYER);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "PlayerPerkHandler";
    }
    
    static {
        INSTANCE = new PerkTickHelper();
    }
}
