package hellfirepvp.astralsorcery.common.network.play.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.util.PlayerReference;
import net.minecraft.world.entity.player.Player;
import net.minecraft.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileCelestialGateway;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import hellfirepvp.astralsorcery.common.network.base.ASPacket;

public class PktRevokeGatewayAccess extends ASPacket<PktRevokeGatewayAccess>
{
    private RegistryKey<Level> dim;
    private BlockPos pos;
    private UUID revokeUUID;
    
    public PktRevokeGatewayAccess() {
        this.dim = null;
        this.pos = BlockPos.field_177992_a;
        this.revokeUUID = null;
    }
    
    public PktRevokeGatewayAccess(final RegistryKey<Level> dim, final BlockPos pos, final UUID revokeUUID) {
        this.dim = null;
        this.pos = BlockPos.field_177992_a;
        this.revokeUUID = null;
        this.dim = dim;
        this.pos = pos;
        this.revokeUUID = revokeUUID;
    }
    
    @Nonnull
    @Override
    public Encoder<PktRevokeGatewayAccess> encoder() {
        return (packet, buffer) -> {
            ByteBufUtils.writeVanillaRegistryEntry(buffer, packet.dim);
            ByteBufUtils.writePos(buffer, packet.pos);
            ByteBufUtils.writeUUID(buffer, packet.revokeUUID);
        };
    }
    
    @Nonnull
    @Override
    public Decoder<PktRevokeGatewayAccess> decoder() {
        return (Decoder<PktRevokeGatewayAccess>)(buffer -> new PktRevokeGatewayAccess(ByteBufUtils.readVanillaRegistryEntry(buffer), ByteBufUtils.readPos(buffer), ByteBufUtils.readUUID(buffer)));
    }
    
    @Nonnull
    @Override
    public Handler<PktRevokeGatewayAccess> handler() {
        return (packet, context, side) -> {
            if (side.isServer()) {
                final Player sender = (Player)context.getSender();
                if (sender != null) {
                    final MinecraftServer srv = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
                    final Level world = (Level)srv.getLevel((RegistryKey)packet.dim);
                    final TileCelestialGateway gateway = MiscUtils.getTileAt((IBlockReader)world, packet.pos, TileCelestialGateway.class, false);
                    if (gateway != null && gateway.isLocked() && gateway.getOwner() != null && gateway.getOwner().isPlayer(sender)) {
                        final BlockPos testPos = Vector3.atEntityCorner((Entity)sender).toBlockPos();
                        final TileCelestialGateway playerGateway = MiscUtils.getTileAt((IBlockReader)world, testPos, TileCelestialGateway.class, false);
                        if (gateway.equals(playerGateway)) {
                            final PlayerReference removedPlayer = gateway.removeAllowedUser(packet.revokeUUID);
                            if (removedPlayer != null) {
                                final PktPlayEffect pkt = new PktPlayEffect(PktPlayEffect.Type.GATEWAY_REVOKE_EFFECT).addData(buffer -> ByteBufUtils.writePos(buffer, gateway.getBlockState()));
                                PacketChannel.CHANNEL.sendToPlayer(sender, pkt);
                                new Component("astralsorcery.misc.link.gateway.unlink", new Object[] { removedPlayer.getPlayerName() });
                                final Component translationTextComponent;
                                final Component accessGrantedMessage = (Component)translationTextComponent.toString()ChatFormatting.GREEN);
                                sender.func_145747_a(accessGrantedMessage, Util.NIL_UUID);
                            }
                        }
                    }
                }
            }
        };
    }
}
