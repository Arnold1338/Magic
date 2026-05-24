package hellfirepvp.astralsorcery.common.network.play.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraft.world.level.entity.player.Player;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.observerlib.common.data.WorldCacheDomain;
import hellfirepvp.astralsorcery.common.lib.DataAS;
import hellfirepvp.astralsorcery.common.data.world.GatewayCache;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.entity.Entity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.tile.TileCelestialGateway;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.Level;
import net.minecraft.resources.ResourceKey;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktRequestTeleport extends ASPacket<PktRequestTeleport>
{
    private RegistryKey<World> dim;
    private BlockPos pos;
    
    public PktRequestTeleport() {
    }
    
    public PktRequestTeleport(final RegistryKey<World> dim, final BlockPos pos) {
        this.dim = dim;
        this.pos = pos;
    }
    
    @Nonnull
    @Override
    public Encoder<PktRequestTeleport> encoder() {
        return (packet, buffer) -> {
            ByteBufUtils.writeVanillaRegistryEntry(buffer, packet.dim);
            ByteBufUtils.writePos(buffer, packet.pos);
        };
    }
    
    @Nonnull
    @Override
    public Decoder<PktRequestTeleport> decoder() {
        return (Decoder<PktRequestTeleport>)(buffer -> {
            final PktRequestTeleport pkt = new PktRequestTeleport();
            pkt.dim = ByteBufUtils.readVanillaRegistryEntry(buffer);
            pkt.pos = ByteBufUtils.readPos(buffer);
            return pkt;
        });
    }
    
    @Nonnull
    @Override
    public Handler<PktRequestTeleport> handler() {
        return (packet, context, side) -> context.enqueueWork(() -> {
            final Player player = (Player)context.getSender();
            final TileCelestialGateway gate = MiscUtils.getTileAt((IBlockReader)player.field_70170_p, Vector3.atEntityCorner((Entity)player).toBlockPos(), TileCelestialGateway.class, false);
            if (gate != null && gate.hasMultiblock() && gate.doesSeeSky()) {
                final MinecraftServer server = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
                if (server != null) {
                    final World to = (World)server.func_71218_a((RegistryKey)packet.dim);
                    if (to != null) {
                        final GatewayCache.GatewayNode node = ((GatewayCache)DataAS.DOMAIN_AS.getData(to, (WorldCacheDomain.SaveKey)DataAS.KEY_GATEWAY_CACHE)).getGatewayNode(packet.pos);
                        if (node != null && node.hasAccess(player)) {
                            AstralSorcery.getProxy().scheduleDelayed(() -> {
                                final Player playerEntity = MiscUtils.transferEntityTo(player, (RegistryKey<World>)to.dimension(), packet.pos);
                            });
                        }
                    }
                }
            }
        });
    }
}
