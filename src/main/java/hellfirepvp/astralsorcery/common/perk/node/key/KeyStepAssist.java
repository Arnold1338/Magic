package hellfirepvp.astralsorcery.common.perk.node.key;

import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.play.server.PktSyncStepAssist;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.server.level.ServerPlayer;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.perk.PerkCooldownHelper;
import net.minecraft.world.level.entity.player.Player;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.CooldownPerk;
import hellfirepvp.astralsorcery.common.perk.tick.PlayerTickPerk;
import hellfirepvp.astralsorcery.common.perk.node.KeyPerk;

public class KeyStepAssist extends KeyPerk implements PlayerTickPerk, CooldownPerk
{
    public KeyStepAssist(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    public void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener(EventPriority.LOWEST, (Consumer)this::onTeleport);
    }
    
    @Override
    public void onPlayerTick(final Player player, final LogicalSide side) {
        if (side.isServer()) {
            final float currentHeight = player.field_70138_W;
            if (!PerkCooldownHelper.isCooldownActiveForPlayer(player, this)) {
                player.field_70138_W += 0.5f;
            }
            else if (player.field_70138_W < 1.1f) {
                player.field_70138_W = 1.1f;
            }
            PerkCooldownHelper.forceSetCooldownForPlayer(player, this, 20);
            if (currentHeight != player.field_70138_W && player instanceof ServerPlayer && MiscUtils.isConnectionEstablished((ServerPlayer)player)) {
                final PktSyncStepAssist sync = new PktSyncStepAssist(player.field_70138_W);
                PacketChannel.CHANNEL.sendToPlayer(player, sync);
            }
        }
    }
    
    @Override
    public void onCooldownTimeout(final Player player) {
        player.field_70138_W -= 0.5f;
        if (player.field_70138_W < 0.6f) {
            player.field_70138_W = 0.6f;
        }
        if (player instanceof ServerPlayer && MiscUtils.isConnectionEstablished((ServerPlayer)player)) {
            final PktSyncStepAssist sync = new PktSyncStepAssist(player.field_70138_W);
            PacketChannel.CHANNEL.sendToPlayer(player, sync);
        }
    }
    
    private void onTeleport(final EntityTravelToDimensionEvent event) {
        if (!event.getEntity().func_130014_f_().func_201670_d() && event.getEntity() instanceof Player) {
            PerkCooldownHelper.removeAllCooldowns((Player)event.getEntity(), LogicalSide.SERVER);
        }
    }
}
