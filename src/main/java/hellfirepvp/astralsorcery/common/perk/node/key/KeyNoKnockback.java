package hellfirepvp.astralsorcery.common.perk.node.key;

import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.world.level.entity.Entity;
import net.minecraft.world.level.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.node.KeyPerk;

public class KeyNoKnockback extends KeyPerk
{
    public KeyNoKnockback(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    public void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener((Consumer)this::onKnockback);
    }
    
    private void onKnockback(final LivingKnockBackEvent event) {
        final LivingEntity attacked = event.getEntityLiving();
        if (attacked instanceof Player) {
            final Player player = (Player)attacked;
            final LogicalSide side = this.getSide((Entity)player);
            final PlayerProgress prog = ResearchHelper.getProgress(player, side);
            if (prog.getPerkData().hasPerkEffect(this)) {
                event.setCanceled(true);
            }
        }
    }
}
